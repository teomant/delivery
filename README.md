Simple delivery service.

You can create, edit, delete users, restaurants, meals and orders. 

Orders have small lifecycle, DRAFT(order created) -> PROCESSING(user approved order, restaurant cook it, etc) -> DELIVERED(order delivered to user)

Order stores not just references to restaurant/meal/user, but also state of an order at the moment when it was created or updated.
This allows us to properly process cases when restaurant/user/meal changes, we can show changes to user and make sure that he knows what he approves. 
Order can't change status to PROCESSING if something in saved state is outdated. Order can change status to DELIVERED only from PROCESSING status. 

This also allows us to collect statistics right, if restaurant/users/meals changed/deleted, we still, for example, can collect correct prices as they were when order was delivered.

