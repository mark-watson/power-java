# build and run

mvn clean compile package assembly:single
 
## service discovery:

java -cp target/iot-1.0-SNAPSHOT-jar-with-dependencies.jar com.markwatson.iot.CreateTestService

java -cp target/iot-1.0-SNAPSHOT-jar-with-dependencies.jar com.markwatson.iot.LookForTestService

## UDP experiments:

java -cp target/iot-1.0-SNAPSHOT-jar-with-dependencies.jar com.markwatson.iot.UDPServer

java -cp target/iot-1.0-SNAPSHOT-jar-with-dependencies.jar com.markwatson.iot.UDPClient



## Multicast experiments:

java -cp target/iot-1.0-SNAPSHOT-jar-with-dependencies.jar com.markwatson.iot.MulticastServer

java -cp target/iot-1.0-SNAPSHOT-jar-with-dependencies.jar com.markwatson.iot.MulticastClient



