# Retailer API
This is an application to count reward points for customers based on their transactions amounts

## Requirements

The fully fledged server uses the following:

* Java 17
* SpringBoot 3.x.x
* H2 database (embedded)

## Building and using the project
You will need:

*	Java JDK 17
*	Gradle 7+
*	Git
*	Docker (optional)
*	Postman (optional for testing)

Clone the project and use Gradle to build it (remember that your gradle should have Java 17 set up, if you don't want to change it globally on your machine, change it in intelliJ and use Gradle bar to execute command)
$ gradle build
Start application with your intelliJ or within terminal/gradle bar using command:
$ gradle bootRun
If you want to run tests - run it with intelliJ or within terminal/gradle bar using command:
$ gradle test


### Docker
You can run this application within Docker with few steps (go to /retailer directory where Dockerfile is):
1. Create image:
   $ docker build -t ${image.name}
2. Run container
   $ docker run ${image.name}
   Expose port for application is 8080 - make sure it is free or change it. To run container use:
   $ docker run ${image.name}
   It is known that on some machines you need to set up network to 'host' in order to be able to request your application container to do it use '--net=host' flag after run command
   $ docker run --net=host ${image.name}


### Tests
There are Unit Tests, Integration Tests (for Controllers) and Postman Collection in the application resources (that needs to be imported to your Postman in order to use it)


### Swagger
Open your browser at the following URL for Swagger UI:
http://localhost:8080/swagger-ui/index.html


### H2 console
Application uses H2 database with console enabled.
Open your browser at the following URL for H2 console:
http://localhost:8080/h2-console/


### Api Docs
You can view or download yaml api-docs with the following urls:
http://localhost:8080/api-docs
http://localhost:8080/api-docs.yaml


### Actuator
Open your browser at the following URL for Actuator information:
http://localhost:8080/actuator


### Assumptions for the task
1. Reward points for last month are counted from the first day of current month
2. Reward points for last three months are counted from the first day of month that was 2 months ago
3. There is default Zone Time used
4. There are some Java Docs (treat it more as comments) about what could be done in a different/better way
5. Customer to be unique must have unique login and email


### Improvements that can be done (no time for now)
1. Add e2e tests with testcontainers
2. Add verification if customer with unique values already exists, instead of catching DB errors
3. Move reward points calculation logic to database layer if customers would have thousands of transactions
4. Consider what Client side needs in responses and adjust them
5. Create interface for Controllers and put documentation Annotations there to increase readability of the code
6. Think about parameters in code and methods that can be set to final and do it
7. Test have builders based on random strings generators. There should be specified profile for tests with Database connected Beans. After every test database should be cleaned