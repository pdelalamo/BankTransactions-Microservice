How to Test the Application

1)Launch the application: To launch the application there are several ways: (IF YOU WANT TO DIRECTLY RUN THE JAR YOU'LL NEED JAVA 17 INSTALLED)

*Execute the jar in the target/demo-0.0.1-SNAPSHOT.jar folder (NEED TO HAVE JAVA 17 ON YOUR MACHINE)
*From eclipse: 
     You can clone the repository via http/https or download the project and import it to the IDE (in my case eclipse and it would be file, import, existing maven projects into workspace). Right click on the root folder of the project and run as springboot app or if it does not appear, right click, run as, run configurations and in goals we write: spring-boot:run, in base directory put the location where the project is located, in this case in the workspace, you can put or clicking directly on workspace or writing ${workspace_loc:/demo}. If you want to test only the unit tests, open the test folder and right click on the only class and run as JUnitTest.

2) Test the endpoints: 

I have included the postman collection with the main requests indicated in the PDF. To import them in postman open the program and click on import and select the file. If you do not use postman nothing happens but you will have to create the requests, I have included them in the postman collection to save time when testing. With the createTransaction you can include new transactions in the system. With the nonExistentTransaction you can try to request a transaction that does not exist. With the transactionStatus you can check all the other cases. To do this simply create transactions that match what you want to test (from today, before today, after today...).
