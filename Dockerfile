FROM openjdk:17.0.1-slim
COPY build/libs/retailer-0.0.1-SNAPSHOT.jar /app/retailer-0.0.1-SNAPSHOT.jar

CMD ["java","-jar","/app/retailer-0.0.1-SNAPSHOT.jar"]

EXPOSE 8080-8080