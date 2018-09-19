# JMX client on JShell

## Pre-Requirements

* JDK 9 b175 or later

## How to use

```
$ jshell --add-exports jdk.jconsole/sun.tools.jconsole --feedback jmxclient jmxclient.jsh
```

You need to get JMXClient instance and use it.

* You want to access local process:
  * ```JMXClient client = JMXClient.getJMXClient(pid)```
* You want to access remote process with JMX URL:
  * ```JMXClient client = JMXClient.getJMXClient(URL, UserName, Password, Require SSL)```
* You want to access remote process with hostname:
  * ```JMXClient client = JMXClient.getJMXClient(hostname, Port, UserName, Password, Require SSL)```

After your JMX operation(s), you have to close the connection:

```
client.close()
```

## Examples

```
$ jshell --add-exports jdk.jconsole/sun.tools.jconsole --feedback jmxclient jmxclient.jsh
JMXClient 0.1.0
Copyright (C) 2016-2017 Yasumasa Suenaga
|  Welcome to JShell -- Version 9
|  For an introduction type: /help intro
jmxclient> JMXClient.listLocalVMs()
12881: jdk.jshell/jdk.internal.jshell.tool.JShellToolProvider --add-exports jdk.jconsole/sun.tools.jconsole --feedback jmxclient jmxclient.jsh
12906: jdk.jshell.execution.RemoteExecutionControl 40933
jmxclient> JMXClient client = JMXClient.getJMXClient(12906)
jmxclient> client.listMBeans()
java.lang:name=Metaspace,type=MemoryPool:
  Information on the management interface of the MBean
java.lang:name=CodeHeap 'profiled nmethods',type=MemoryPool:
  Information on the management interface of the MBean
JMImplementation:type=MBeanServerDelegate:
  Represents  the MBean server from the management point of view.
java.lang:type=Runtime:
  Information on the management interface of the MBean
java.lang:type=Threading:
  Information on the management interface of the MBean
java.lang:type=OperatingSystem:
  Information on the management interface of the MBean
java.nio:name=direct,type=BufferPool:
  Information on the management interface of the MBean
java.lang:type=Compilation:
  Information on the management interface of the MBean
java.lang:name=CodeHeap 'non-nmethods',type=MemoryPool:
  Information on the management interface of the MBean
java.lang:name=G1 Young Generation,type=GarbageCollector:
  Information on the management interface of the MBean
java.lang:name=CodeCacheManager,type=MemoryManager:
  Information on the management interface of the MBean
java.lang:name=Compressed Class Space,type=MemoryPool:
  Information on the management interface of the MBean
java.lang:type=Memory:
  Information on the management interface of the MBean
java.lang:name=G1 Eden Space,type=MemoryPool:
  Information on the management interface of the MBean
java.lang:name=G1 Old Gen,type=MemoryPool:
  Information on the management interface of the MBean
java.nio:name=mapped,type=BufferPool:
  Information on the management interface of the MBean
java.util.logging:type=Logging:
  Information on the management interface of the MBean
java.lang:name=G1 Old Generation,type=GarbageCollector:
  Information on the management interface of the MBean
java.lang:type=ClassLoading:
  Information on the management interface of the MBean
java.lang:name=Metaspace Manager,type=MemoryManager:
  Information on the management interface of the MBean
com.sun.management:type=DiagnosticCommand:
  Diagnostic Commands
java.lang:name=G1 Survivor Space,type=MemoryPool:
  Information on the management interface of the MBean
java.lang:name=CodeHeap 'non-profiled nmethods',type=MemoryPool:
  Information on the management interface of the MBean
com.sun.management:type=HotSpotDiagnostic:
  Information on the management interface of the MBean
jmxclient> client.dumpMBean("com.sun.management:type=DiagnosticCommand")
com.sun.management:type=DiagnosticCommand
  Diagnostic Commands

Attributes:

Operations:
  java.lang.String compilerCodecache ()
  java.lang.String compilerCodelist ()
  java.lang.String compilerDirectivesAdd ([Ljava.lang.String; arguments)
  java.lang.String compilerDirectivesClear ()
  java.lang.String compilerDirectivesPrint ()
  java.lang.String compilerDirectivesRemove ()
  java.lang.String compilerQueue ()
  java.lang.String gcClassHistogram ([Ljava.lang.String; arguments)
  java.lang.String gcClassStats ([Ljava.lang.String; arguments)
  java.lang.String gcFinalizerInfo ()
  java.lang.String gcHeapInfo ()
  java.lang.String gcRun ()
  java.lang.String gcRunFinalization ()
  java.lang.String help ([Ljava.lang.String; arguments)
  java.lang.String jfrCheck ([Ljava.lang.String; arguments)
  java.lang.String jfrConfigure ([Ljava.lang.String; arguments)
  java.lang.String jfrDump ([Ljava.lang.String; arguments)
  java.lang.String jfrStart ([Ljava.lang.String; arguments)
  java.lang.String jfrStop ([Ljava.lang.String; arguments)
  java.lang.String jvmtiAgentLoad ([Ljava.lang.String; arguments)
  java.lang.String jvmtiDataDump ()
  java.lang.String threadPrint ([Ljava.lang.String; arguments)
  java.lang.String vmClassHierarchy ([Ljava.lang.String; arguments)
  java.lang.String vmClassloaderStats ()
  java.lang.String vmCommandLine ()
  java.lang.String vmDynlibs ()
  java.lang.String vmFlags ([Ljava.lang.String; arguments)
  java.lang.String vmInfo ()
  java.lang.String vmLog ([Ljava.lang.String; arguments)
  java.lang.String vmNativeMemory ([Ljava.lang.String; arguments)
  java.lang.String vmPrintTouchedMethods ()
  java.lang.String vmSetFlag ([Ljava.lang.String; arguments)
  java.lang.String vmStringtable ([Ljava.lang.String; arguments)
  java.lang.String vmSymboltable ([Ljava.lang.String; arguments)
  java.lang.String vmSystemProperties ()
  java.lang.String vmUptime ([Ljava.lang.String; arguments)
  java.lang.String vmVersion ()

Notifications:
  javax.management.Notification
    jmx.mbean.info.changed
jmxclient> String result = (String)client.invoke("com.sun.management:type=DiagnosticCommand", "gcHeapInfo", new Object[0], new String[0])
jmxclient> System.out.println(result)
 garbage-first heap   total 256000K, used 13252K [0x00000006c6e00000, 0x00000006c6f007d0, 0x00000007c0000000)
  region size 1024K, 11 young (11264K), 1 survivors (1024K)
 Metaspace       used 17419K, capacity 19140K, committed 19328K, reserved 1067008K
  class space    used 1824K, capacity 2111K, committed 2176K, reserved 1048576K

jmxclient> client.close()
```

## JMXClient reference

* ```close()```
  * Close JMX connection. You cannot use JMXClient instance after closing the connection.
* ```listLocalVMs()``` (static method)
  * List local JVM process.
* ```getJMXClient()``` (static method, overloaded method)
  * Open new JMX connection.
* ```listMBeans()```
  * List all MBeans on target.
* ```dumpMBean(name)```
  * Dump MBean information.
* ```invoke(name, operationName, params, signature)```
  * Invoke JMX method. It returns result of JMX method.
* `AttributeList getAttributes(String objectName, String... attrNames)`
  * Get [AttributeList](https://docs.oracle.com/javase/9/docs/api/javax/management/AttributeList.html) from MBean which is named `objectName` . You need to choose attributes what you want via `attrNames` .

## License

GNU General Public License, version 2
