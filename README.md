# ABCFitness

Steps to start the app
1. git clone
2. cd to the ABCFitness directory
3. execute the following commands
     a. mvn clean install
     (watch all the test cases)
     b. docker-compose up

Steps to test the app using postman
1. cd to the ABCFitness directory
2. import the postman collection - ABC.postman_collection.json
3. test


Note:
The development uses hard coded db username and password in application.properties. We will have to use some secret management service to store these credentials.
