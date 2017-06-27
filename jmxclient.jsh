/*
 * jmxclient.jsh
 *
 * Copyright (C) 2016-2017 Yasumasa Suenaga
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA
 * 02110-1301, USA.
 */

import java.io.*;
import java.util.*;
import java.util.stream.*;
import java.lang.reflect.*;
import javax.management.*;

import sun.tools.jconsole.*;


class JMXClient implements AutoCloseable{

  private ProxyClient client;

  private JMXClient(ProxyClient client){
    this.client = client;
  }

  @Override
  public void close() throws Exception{
    Optional.ofNullable(client)
            .ifPresent(c -> c.disconnect());
  }

  public static void listLocalVMs(){
    LocalVirtualMachine.getAllVirtualMachines()
                       .forEach((k, v) -> System.out.println(k + ": " + v));
  }

  private static JMXClient connect(ProxyClient client, boolean requireSSL)
                        throws NoSuchMethodException, IllegalAccessException,
                                                      InvocationTargetException{
    Method connectMethod = ProxyClient.class.getDeclaredMethod(
                                                      "connect", boolean.class);
    connectMethod.setAccessible(true);
    connectMethod.invoke(client, requireSSL);

    return new JMXClient(client);
  }

  public static JMXClient getJMXClient(int pid)
                       throws IOException, NoSuchMethodException,
                              IllegalAccessException, InvocationTargetException{
    LocalVirtualMachine vm = LocalVirtualMachine.getLocalVirtualMachine(pid);
    if(vm == null){
      throw new RuntimeException(pid + " is not accessible!");
    }

    return connect(ProxyClient.getProxyClient(vm), false);
  }

  public static JMXClient getJMXClient(String url, String userName,
                                  String password, boolean requireSSL)
                                    throws IOException, NoSuchMethodException,
                              IllegalAccessException, InvocationTargetException{
    return connect(ProxyClient.getProxyClient(url, userName, password),
                                                                  requireSSL);
  }

  public static JMXClient getJMXClient(String hostName, int port,
                  String userName, String password, boolean requireSSL)
                                throws IOException, NoSuchMethodException,
                              IllegalAccessException, InvocationTargetException{
    return connect(ProxyClient.getProxyClient(hostName, port,
                                               userName, password), requireSSL);
  }

  public void listMBeans() throws IOException{
    client.getMBeans(null)
          .forEach((k, v) -> System.out.print(k + ":\n" +
                                             "  " + v.getDescription() + "\n"));
  }

  private void dumpAttribute(ObjectName objectName, MBeanAttributeInfo info){
    System.out.print("  ");

    if(info.isReadable()){
      try{
        System.out.println(
                  client.getAttributes(objectName, new String[]{info.getName()})
                        .stream()
                        .map(a -> a.toString())
                        .collect(Collectors.joining(", ")));
      }
      catch(IOException e){
        throw new UncheckedIOException(e);
      }
    }
    else{
      System.out.println(info.getName());
    }

  }

  private void dumpOperation(MBeanOperationInfo info){
    System.out.print("  " + info.getReturnType() +
                     " " + info.getName() +
                     " (");
    System.out.print(Arrays.stream(info.getSignature())
                                      .map(p -> p.getType() + " " + p.getName())
                                      .collect(Collectors.joining(", ")));
    System.out.println(")");
  }

  private void dumpNotification(MBeanNotificationInfo info){
    System.out.println("  " + info.getName());
    System.out.println("    " +
                       Arrays.stream(info.getNotifTypes())
                                         .collect(Collectors.joining(", ")));
  }

  public void dumpMBean(String name) throws IOException,
                                                MalformedObjectNameException{
    ObjectName objectName = new ObjectName(name);
    MBeanInfo mbeanInfo = client.getMBeans(null)
                                .get(objectName);
    if(mbeanInfo == null){
      throw new RuntimeException(objectName + " not found");
    }

    System.out.println(objectName.toString());
    System.out.println("  " + mbeanInfo.getDescription());

    System.out.println();
    System.out.println("Attributes:");
    Arrays.stream(mbeanInfo.getAttributes())
          .forEach(a -> dumpAttribute(objectName, a));

    System.out.println();
    System.out.println("Operations:");
    Arrays.stream(mbeanInfo.getOperations())
          .forEach(o -> dumpOperation(o));

    System.out.println();
    System.out.println("Notifications:");
    Arrays.stream(mbeanInfo.getNotifications())
          .forEach(n -> dumpNotification(n));
  }

  public Object invoke(String name, String operationName,
                            Object[] params, String[] signature)
               throws IOException, MalformedObjectNameException, MBeanException{
    return client.invoke(new ObjectName(name),
                                     operationName, params, signature);
  }

}


/*** JShell settings ***/
/set mode jmxclient -command
/set prompt jmxclient "jmxclient> " "        -> "

/*** Banner ***/
System.out.println("JMXClient 0.1.0");
System.out.println("Copyright (C) 2016-2017 Yasumasa Suenaga");
