# FoodDeliveryApp
This app consists of mainly 3 modules, Customer, Restaurent Owner, Delivery Boy

>Customer can explore different dishes, explore restaurents, see their menu, add items to cart, can set the number of any items in cart, place the order, get all the details about bill, and can review the status of the order(i.e. when order is being prepared, picked up, and delivered) and also details of delivery boy assigned to that order
-------------------------------------------------------------------------------------------------------------------------------------------------------------------
>Restaurent Owner can set the details about his restaurent(name, address, locality, image, etc.) and can add the dishes into the menu of his restaurent, while having able to change the details about the dishes later on.It can view orders and all the details of it.
--------------------------------------------------------------------------------------------------------------------------------------------------------------------
>Delivery Boy can view all the unassigned orders, view all details of order, get self assigned to any order, and after getting assigned can update status of order(picked up, delivered etc.).It can view his all self assigned orders in seperate section.

All above modules can set their details(name, address in case of customer and restaurent, contact, image in case of restaurent module,etc.).
## For Cloning repository purposes
- Clone the repository, now to use the app on your device, replace the firebase database reference URL with your own firebase reference URL in every java file of your cloned repo, the URL has been defined at top of every java class, you can get the above URL by creating a firebase account if you don't have one, add your project to the account. Refer to firebase documentation for more information about it, and then accordingly set the access rules to your database as per your choice. 
