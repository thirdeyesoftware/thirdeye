To Compile:

Make sure QovImpl.dll is in java.library.path (<java_home>/lib is a good place.).

To Compile Server:

no special requirements

To Compile Sample Client:

make sure RMI server classes are in classpath

To Run:

server example:

start java -classpath %CLASSPATH%;.\classes;. telesync.qov.rmi.CompareEngine

client example:

java -classpath %CLASSPATH%;.\classes;. telesync.qov.rmi.client.CompareClient c:\source.wav c:\degraded.wav localhost

