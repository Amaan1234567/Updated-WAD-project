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


-- Allow the role to see the schema
GRANT USAGE ON SCHEMA orders_svc TO authenticated;

-- Allow the role to select/insert/update rows in the table
GRANT SELECT, INSERT, UPDATE, DELETE ON orders_svc.orders TO authenticated;

-- If you have a join or foreign key to order_items, grant that too
GRANT SELECT, INSERT, UPDATE, DELETE ON orders_svc.order_items TO authenticated;

-- Grant permission on the sequence used by the 'orders' table
GRANT USAGE, SELECT ON SEQUENCE orders_svc.id_gen TO authenticated;

-- Create the role and user
CREATE ROLE orders_svc_role NOINHERIT;
CREATE USER orders_svc_app WITH PASSWORD 'your-strong-password-here' IN ROLE orders_svc_role;

-- Orders schema permissions
GRANT USAGE ON SCHEMA orders_svc TO orders_svc_app;
GRANT SELECT, INSERT, UPDATE, DELETE ON ALL TABLES IN SCHEMA orders_svc TO orders_svc_app;
GRANT USAGE, SELECT ON ALL SEQUENCES IN SCHEMA orders_svc TO orders_svc_app;

-- Users schema (read-only, needed for RLS policy lookups)
GRANT USAGE ON SCHEMA users_svc TO orders_svc_app;
GRANT SELECT ON users_svc.users TO orders_svc_app;

-- Ensure future tables also get permissions
ALTER DEFAULT PRIVILEGES IN SCHEMA orders_svc
    GRANT SELECT, INSERT, UPDATE, DELETE ON TABLES TO orders_svc_app;
ALTER DEFAULT PRIVILEGES IN SCHEMA orders_svc
    GRANT USAGE, SELECT ON SEQUENCES TO orders_svc_app;

-- RLS: make sure the policy applies to this role
ALTER POLICY "Enable users to view their own data only"
ON "orders_svc"."orders"
TO anon, authenticated, postgres, orders_svc_app;

-- Make RLS work even if connecting through pooler as superuser
ALTER TABLE orders_svc.orders FORCE ROW LEVEL SECURITY;
