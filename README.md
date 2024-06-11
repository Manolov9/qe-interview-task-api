**Instructions**

Fork this repository and work on the forked repo. When you have completed the task push the changes to your fork and share it with us.

Using the resources in https://jsonplaceholder.typicode.com/ you will need to:

1. Create a test user + associated entities for all resource types
2. Let's pretend the backend system has an intermittent DB problem and occasionally it fails to persist an entity, while other times the entity is persisted twice. Validate the integrity of your user and associated resources from point 1
3. Implement a simple performance test measuring how long it takes to create 100 new users plus one ToDo for each

For a basic idea of what we are trying to achieve take a look at the "ToDoTests" class.

**Tech Stack**

Java 17 or Higher

Groovy 4.0.14

Gradle 7.4.1

RestAssured 5.4.0
