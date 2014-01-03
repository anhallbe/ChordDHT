dht
===

Java implementation of a Distributed Hash Table. This project is part of the course ID2212 Network Programming with Java at KTH.


To compile (JDK 7):
    javac -d bin -sourcepath src src/dht/*.java
    javac -d bin -sourcepath src src/tests/*.java
    
To run the test application (tests/DistributedStorage):
    java tests.DistributedStorage [localname] | [localname, remotename, remotehost, remoteport]
    
    Using the single argument [localname] will create a small local network, which can be identified with the given name.
    
    Using the arguments [localname, remotename, remotehost, remoteport] will create a local node with the name localname,
    and connect to a network specified by remotehost and remoteport. Remotename must be registered in the remote host's
    RMI registry
    
A more detailed description of the program, its' interfaces and performance evaluation can be found in the javadoc or the written report.
