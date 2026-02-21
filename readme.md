backend services list

1. products service 
2. orders service
3. users service
4. bestsellers service
5. recommendation service
6. reviews service
7. sellers service
8. resource upload and download service (to handle objects in s3)


### products service

| column_name | type | purpose |
|---|---|---|
| product_id | varchar | primary key id |
| product_name | varchar | name of product |
| product_description | varchar | description of product |
| product_wiki_link | varchar | wikipedia link of plant or seed etc |
| product_resource_links | varchar | resource links to pictures and videos
| price | real | price in rupees |
| discount | real | % discount |
| product_type | ENUM type('plant','seed','supplies','tools') | ENUM for storing which category of product it is 

APIs:

- `GET /products/id` : return product details based on id
- `GET /products/search` : takes in all details like product_name, price range, how much discount it should have and its enum type and returns all the products that match the category.
- `POST /products/add` : adds the provided product in json payload to DB
- `PUT /products/update` : updates the product details based on provided id and details to be updated, blank details for columns mean no update in info.
- `DELETE /products/remove/id` : removes product with provided id

### orders service

| column_name | type | purpose |
|---|---|---|
| order_id | varchar | primary key id |
| customer_id | varchar | foreign key referencing users table |
| order_date | datetime | timestamp when order was created |
| total_amount | real | total price of order in rupees |
| order_status | ENUM type('pending','confirmed','shipped','delivered','cancelled') | current status of the order |
| delivery_address | varchar | address where order should be delivered |
| payment_method | varchar | payment method used (credit card, debit card, UPI, etc) |

APIs:

- `GET /orders/user/{user_id}` : return all orders for a specific user
- `GET /orders/{order_id}` : return order details based on id
- `POST /orders/create` : creates a new order with provided order details and product items in json payload
- `PUT /orders/{order_id}/update` : updates order status based on provided id and new status
- `DELETE /orders/{order_id}/cancel` : cancels the order with provided id

### users service

| column_name | type | purpose |
|---|---|---|
| user_id | varchar | primary key id |
| email | varchar | email address (unique) |
| full_name | varchar | user's full name |
| phone | varchar | contact phone number |
| address | varchar | residential address |
| city | varchar | city of residence |
| pincode | varchar | postal code |
| user_type | ENUM type('customer','admin','staff') | type of user account |
| created_at | datetime | account creation timestamp |

APIs:

- `GET /users/{user_id}` : return user profile details based on id
- `POST /users/register` : creates a new user account with provided credentials and profile info in json payload
- `PUT /users/{user_id}/update` : updates user profile details based on provided id, blank fields mean no update
- `DELETE /users/{user_id}/delete` : deletes user account with provided id

### bestsellers service

| column_name | type | purpose |
|---|---|---|
| bestseller_id | varchar | primary key id |
| product_id | varchar | foreign key referencing products table |
| sales_count | int | number of units sold |
| revenue | real | total revenue generated in rupees |
| rating | real | average customer rating (0-5) |
| last_updated | datetime | timestamp of last metrics update |

APIs:

- `GET /bestsellers` : return list of all bestselling products with their metrics
- `GET /bestsellers/top/{limit}` : return top N bestselling products sorted by sales count
- `GET /bestsellers/category/{type}` : return bestselling products filtered by product type (plant, seed, supplies, tools)
- `POST /bestsellers/update` : updates bestseller metrics for a product based on id and new metrics in json payload
- `GET /bestsellers/trending` : return trending products based on recent sales velocity

### recommendation service

| column_name | type | purpose |
|---|---|---|
| recommendation_id | varchar | primary key id |
| user_id | varchar | foreign key referencing users table |
| product_id | varchar | foreign key referencing products table |
| score | real | recommendation score (0-1) indicating relevance |
| reason | ENUM type('popular','trending','similar','frequently_bought_together','personalized') | reason for recommendation |
| created_at | datetime | timestamp when recommendation was generated |

APIs:

- `GET /recommendations/user/{user_id}` : return personalized product recommendations for a specific user
- `GET /recommendations/product/{product_id}` : return products frequently bought together with this product
- `POST /recommendations/generate` : generates and stores recommendations for a user based on browsing/purchase history
- `PUT /recommendations/{recommendation_id}/update` : updates recommendation score for a specific recommendation
- `DELETE /recommendations/user/{user_id}/clear` : clears old recommendations for a user
