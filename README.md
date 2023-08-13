-What does this microservice do:
  I have built a microservice that will handle bank transactions. I have created the
following endpoints: 
  *Create transaction
    This endpoint will receive the transaction information and store it into the system.
    It is IMPORTANT to note that a transaction that leaves the total account balance bellow 0 is not allowed.
        reference (optional): The transaction unique reference number in our system. If not present, the system
    will generate one.
    account_iban (mandatory): The IBAN number of the account where the transaction has happened.
    date (optional): Date when the transaction took place
    amount (mandatory): If positive the transaction is a credit (add money) to the account. If negative it is a
    debit (deduct money from the account)
    fee (optional): Fee that will be deducted from the amount, regardless on the amount being positive or
    negative.
    description (optional): The description of the transaction
  *Search transactions
    This endpoint searches for transactions and should be able to:
    Filter by account_iban
    Sort by amount (ascending/descending) 
  *Transaction status
    This endpoint, based on the payload and some business rules, will return the status and additional information
    for a specific transaction.
    Payload:
    {
    "reference":"12345A",
    "channel":"CLIENT"
    }
    reference (mandatory): The transaction reference number
    channel (optional): The type of the channel that is asking for the status. It can be any of these values:
    CLIENT, ATM, INTERNAL

-How to Test the Application:
  1)In the directory that the DockerFile.txt is, run the next command to build de docker image: docker build .
  2)after the image is created, run the next command: docker run -d --name nameyouwanttogivethecontainer -p portofyourmachinetoruntheapp:8086 nameofthegeneratedimage
  3)docker ps to check that the container is up and running


  *Test the endpoints: 

  I have included the postman collection with the main requests. To import them in postman open the program and click on import and select the file. If you do not use postman nothing happens but you will have to create the requests, I have included them in the postman collection to save time when testing. With the createTransaction you can include new transactions in the system. With the nonExistentTransaction you can try to request a transaction that does not exist. With the transactionStatus you can check all the other cases. To do this simply create transactions that match what you want to test (from today, before today, after today...).
