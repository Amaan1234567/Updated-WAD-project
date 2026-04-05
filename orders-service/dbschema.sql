drop table if exists orders_svc.orders cascade;
drop table if exists orders_svc.order_items cascade;
drop sequence if exists orders_svc.id_gen cascade;
drop type if exists orders_svc.order_status_type;

create sequence orders_svc.id_gen start with 10 increment by 10;

create type orders_svc.order_status_type AS ENUM ('PENDING_PAYMENT', 'CONFIRMED' ,'SHIPPED','DELIVERED','CANCELLED');

CREATE TABLE orders_svc.orders (
    order_id BIGSERIAL PRIMARY KEY,
    customer_id BIGINT NOT NULL,  -- References user_svc.users
    order_date TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    total_amount DECIMAL(10,2) NOT NULL,
    order_status orders_svc.order_status_type not null default 'PENDING_PAYMENT'::orders_svc.order_status_type,
    delivery_address TEXT NOT NULL,
    payment_details jsonb NOT NULL,
    delivery_date TIMESTAMPTZ 
);


CREATE TABLE orders_svc.order_items (
    order_id BIGINT REFERENCES orders_svc.orders(order_id),
    product_id BIGINT NOT NULL,  -- References product_svc.products
    quantity INT NOT NULL,
    price_per_unit DECIMAL(10,2) NOT NULL,
    subtotal DECIMAL(10,2) NOT NULL,
    PRIMARY KEY (order_id, product_id),
    CONSTRAINT check_subtotal 
    CHECK (subtotal = price_per_unit * quantity);
);