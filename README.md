# FoodDeliveryApp
This app consists of mainly 3 modules, Customer, Restaurent Owner, Delivery Boy. Each module has facility for authentication.

## Customer
- Edit personal details(name, address, contact, etc);
- Explore dishes from various restaurents
- Explore restaurents and their menu
- Add and remove items from cart
- Checkout facility to enter address or use current location
- Place order
- View status of order(when being prepared, picked up, and delivered)
- Get delivery personnel contact details
- Give rating to order
- View past orders with all details of each order(items ordered, detailed bill etc.)
## Restaurent
- Add items to the menu
- Edit any details of any menu item(name, dishtag, image, menu category, price etc.)
- View orders of customers
- Get delivery personnel details for an order
- Modify the restaurent details(name, location, image, cuisine, contact, etc)
## Delivery personnel
- View all unassigned orders
- Get assigned to an order
- Modify status of self assigned orders(pickedup, delivered etc.)
- View all self assigned orders and all details
- Modify personal details(name, contact etc);
## For Cloning repository purposes
- Clone the repository, now to use the app on your device, replace the firebase database reference URL with your own firebase reference URL in every java file of your cloned repo, the URL has been defined at top of every java class, you can get the above URL by creating a firebase account if you don't have one, add your project to the account. Refer to firebase documentation for more information about it, and then accordingly set the access rules to your database as per your choice. 
