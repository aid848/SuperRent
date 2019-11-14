# SuperRent

How to run project:

//todo




Functionality todo list:

create tables for (if not initalized):
• Reservations 
• Rentals 
• Vehicles 
• VehicleTypes 
• Customers 
• Returns 


Transactions performed by a customer

• View the number of available vehicles for a specific car type, location, and time interval. 
The user should be able to provide any subset of {car type, location, time interval} to 
view the available vehicles. If the user provides no information, your application should 
automatically return a list of all vehicles (at that branch) sorted in some reasonable way 
for the user to peruse.  

 

The actual number of available vehicles should be displayed. After seeing the number of 
vehicles, there should be a way for the user to see the details of the available vehicles if 
the user desires to do so (e.g., if the user clicks on the number of available vehicles, a list 
with the vehicles’ details should be displayed).  

 

• Make a reservation. If a customer is new, add the customer’s details to the database. 
(You may choose to have a different interface for this).    

 

Upon successful completion, a confirmation number for the reservation should be 
shown along with the details entered during the reservation. Refer to the project 
description for details on what kind of information the user needs to provide when 
making a reservation. 

 

If the customer’s desired vehicle is not available, an appropriate error message should 
be shown. 




Transactions performed by a clerk 

• Renting a Vehicle: The system will display a receipt with the necessary details (e.g., 
confirmation number, date of reservation, type of car, location, how long the rental 
period lasts for, etc.) for the customer.  

 

Note: It is not necessary for a user to have made a reservation prior to renting a vehicle. 
If this is the case, then you will need to determine how to appropriately handle this 
situation. 

 

• Returning a Vehicle: Only a rented vehicle can be returned. Trying to return a vehicle 
that has not been rented should generate some type of error message for the clerk.   

 

When returning a vehicle, the system will display a receipt with the necessary details 
(e.g., reservation confirmation number, date of return, how the total was calculated 
etc.) for the customer.  

 

• Generate a report for: 
• Daily Rentals: This report contains information on all the vehicles rented out 
during the day. The entries are grouped by branch, and within each branch, the 
entries are grouped by vehicle category. The report also displays the number of 
vehicles rented per category (e.g., 5 sedan rentals, 2 SUV rentals, etc.), the 
number of rentals at each branch, and the total number of new rentals across 
the whole company 

 

• Daily Rentals for Branch: This is the same as the Daily Rental report but it is for 
one specified branch 

 

• Daily Returns: The report contains information on all the vehicles returned 
during the day. The entries are grouped by branch, and within each branch, the 
entries are grouped by vehicle category. The report also shows the number of 
vehicles returned per category, the revenue per category, subtotals for the 
number of vehicles and revenue per branch, and the grand totals for the day. 

 

• Daily Returns for Branch: This is the same as the Daily Returns report, but it is for 
one specified branch.  

User Interface  

 

Create an easy to use GUI that allows users to execute all the operations and transactions 
provided by the system. In other words, don’t worry about having the GUI change based on 
which user is accessing the system. In this case, any user accessing your system can use either 
the customer or clerk menu.  

 

Make sure your interface is designed in a way where all error messages appear in separate pop-
up boxes, or in a designated area of the main window, so that they don't interfere with other 
information.  

 

Your GUI should also enable the user to display all the rows, insert a tuple, and delete a tuple 
on any user defined table in the database.   

 

We are not expecting a complicated GUI; the bulk of your effort should be concentrated on 
implementing functionality, not the user interface. The goal of the project is to give you some 
experience with SQL, programming something that interacts with a database, and 
understanding how to structure your classes to work with GUI components. 
