Delivery Service

This is a quick and basic implementation of a delivery service.

You can create users, restaurants, meals, and orders. You can also edit them and delete users, restaurants, and meals. In addition, you can get statistics for a restaurant.

The entity change history is preserved, and a soft delete is used. Separate entities are used for storage (_Entity), business logic (_State), and sending to users (_Value). 
Change history stored in json as column in same entity. OrderValue contains metadata that contains information about user, restaurant, and meals at creation/last edition and their current states.
Orders have a small lifecycle: DRAFT (created, edited) -> PROCESSING (approved by user, sent to the restaurant, etc.) -> DELIVERED (delivered to the user). 

Statistics contains information about numbers of draft, processing, and delivered orders, statistics of delivered orders (popular meals, most expensive meals, etc.) 

Validations:

* User: Ensure existence, not deleted, and a unique username. 
* Restaurant: Ensure existence and not deleted. 
* Meal: Ensure existence, not deleted, a price, and unique addons. 
* Order: Ensure existence, correct statuses and actual information about user/restaurant/meals. An update is required to refresh the info.

Application requires Java 17, works with MySql launched on localhost (default port, db, username and password - delivery, settings can be changed via application.properties, migrations can be found in V1__creation.sql). Swagger can be accessed at /swagger-ui/index.html 