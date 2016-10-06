# JMX client on JShell

## Pre-Requirements

* JDK 9 b139 or later

## How to use

```
$ jshell --add-exports jdk.jconsole/sun.tools.jconsole jmxclient.jsh
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
$ jshell --add-exports jdk.jconsole/sun.tools.jconsole jmxclient.jsh
JMXClient 0.1.0
Copyright (C) 2016  Yasumasa Suenaga
|  Welcome to JShell -- Version 9-internal
|  For an introduction type: /help intro

jmxclient> JMXClient.listLocalVMs()
23497: jdk.jshell.execution.RemoteExecutionControl 38649
23468: jdk.jshell/jdk.internal.jshell.tool.JShellTool --add-exports jdk.jconsole/sun.tools.jconsole jmxclient.jsh
jmxclient> JMXClient client = JMXClient.getJMXClient(23497)
jmxclient> client.listMBeans()
java.lang:name=Metaspace,type=MemoryPool:
  Information on the management interface of the MBean
java.lang:name=Eden Space,type=MemoryPool:
  Information on the management interface of the MBean
java.lang:name=CodeHeap 'profiled nmethods',type=MemoryPool:
  Information on the management interface of the MBean
java.lang:name=Survivor Space,type=MemoryPool:
  Information on the management interface of the MBean
java.lang:name=Copy,type=GarbageCollector:
  Information on the management interface of the MBean
JMImplementation:type=MBeanServerDelegate:
  Represents  the MBean server from the management point of view.
java.lang:type=Runtime:
  Information on the management interface of the MBean
java.lang:type=Threading:
  Information on the management interface of the MBean
java.lang:type=OperatingSystem:
  Information on the management interface of the MBean
java.lang:name=MarkSweepCompact,type=GarbageCollector:
  Information on the management interface of the MBean
java.nio:name=direct,type=BufferPool:
  Information on the management interface of the MBean
java.lang:type=Compilation:
  Information on the management interface of the MBean
java.lang:name=CodeHeap 'non-nmethods',type=MemoryPool:
  Information on the management interface of the MBean
java.lang:name=Tenured Gen,type=MemoryPool:
  Information on the management interface of the MBean
java.lang:name=CodeCacheManager,type=MemoryManager:
  Information on the management interface of the MBean
java.lang:name=Compressed Class Space,type=MemoryPool:
  Information on the management interface of the MBean
java.lang:type=Memory:
  Information on the management interface of the MBean
java.nio:name=mapped,type=BufferPool:
  Information on the management interface of the MBean
java.util.logging:type=Logging:
  Information on the management interface of the MBean
java.lang:type=ClassLoading:
  Information on the management interface of the MBean
java.lang:name=Metaspace Manager,type=MemoryManager:
  Information on the management interface of the MBean
com.sun.management:type=DiagnosticCommand:
  Diagnostic Commands
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
 def new generation   total 14848K, used 8249K [0x00000000d1e00000, 0x00000000d2e10000, 0x00000000e1400000)
  eden space 13248K,  62% used [0x00000000d1e00000, 0x00000000d260e550, 0x00000000d2af0000)
  from space 1600K,   0% used [0x00000000d2af0000, 0x00000000d2af0000, 0x00000000d2c80000)
  to   space 1600K,   0% used [0x00000000d2c80000, 0x00000000d2c80000, 0x00000000d2e10000)
 tenured generation   total 32768K, used 2773K [0x00000000e1400000, 0x00000000e3400000, 0x0000000100000000)
   the space 32768K,   8% used [0x00000000e1400000, 0x00000000e16b5640, 0x00000000e16b5800, 0x00000000e3400000)
 Metaspace       used 15114K, capacity 16732K, committed 16896K, reserved 1064960K
  class space    used 1658K, capacity 1963K, committed 2048K, reserved 1048576K

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

## License

GNU General Public License, version 2
