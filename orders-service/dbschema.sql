CREATE SCHEMA IF NOT EXISTS orders_svc;

create sequence orders_svc.id_gen start with 1000 increment by 10;

create type orders_svc.order_status_type AS ENUM ('PENDING', 'CONFIRMED' ,'SHIPPED','DELIVERED','CANCELLED');

CREATE TABLE orders_svc.orders (
    order_id BIGSERIAL PRIMARY KEY,
    user_id text NOT NULL,  -- References user_svc.users
    user_uuid uuid not null,
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    order_status VARCHAR(50) not null default 'PENDING'::orders_svc.order_status_type,
    delivery_address TEXT NOT NULL,
    payment_method VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders_svc.order_items (
    order_item_id BIGSERIAL PRIMARY KEY,
    order_id BIGINT REFERENCES orders_svc.orders(order_id),
    product_id BIGINT NOT NULL,  -- References product_svc.products
    quantity INT NOT NULL,
    price_per_unit DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL
);
