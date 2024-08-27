FROM openjdk:17

EXPOSE 8080

ADD build/libs/chatting-0.0.1-SNAPSHOT-plan.jar app.jar

ENTRYPOINT ["java","-jar","/app.jar"]