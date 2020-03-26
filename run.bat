call mvn install -DskipTests=true
call cd target
call java -jar kibana-0.0.1-SNAPSHOT.jar --spring.main.web-application-type=NONE
call cd ..