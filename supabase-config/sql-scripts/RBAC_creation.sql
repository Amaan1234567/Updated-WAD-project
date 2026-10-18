-- ============================================================
-- PLANT.IO - RBAC SETUP
-- ============================================================

-- -----------------------------------------------
-- 1. ENUMS
-- -----------------------------------------------

CREATE TYPE users_svc.app_role AS ENUM ('ADMIN', 'USER', 'CSR', 'CUSTOM');

CREATE TYPE users_svc.app_permission AS ENUM (
    -- Product permissions
    'products.create',
    'products.update',
    'products.delete',

    -- Order permissions
    'orders.view_own',
    'orders.update_own',
    'orders.view_all',
    'orders.update_status',
    'orders.cancel_any',
    'orders.delete',

    -- User permissions
    'users.view_all',
    'users.update_any',
    'users.delete_any'
);

-- -----------------------------------------------
-- 2. TABLES
-- -----------------------------------------------

CREATE TABLE users_svc.role_permissions (
    role        users_svc.app_role       NOT NULL,
    permission  users_svc.app_permission NOT NULL,
    PRIMARY KEY (role, permission)
);

create table users_svc.user_roles (
  user_id uuid not null,
  role users_svc.app_role not null default 'USER'::users_svc.app_role,
  constraint user_roles_pkey primary key (user_id),
  constraint user_roles_user_id_fkey foreign KEY (user_id) references auth.users (id) on update CASCADE on delete CASCADE
) TABLESPACE pg_default;


CREATE TABLE users_svc.user_permissions (
    user_id    UUID                        REFERENCES auth.users(id) ON DELETE CASCADE,
    permission users_svc.app_permission     NOT NULL,
    PRIMARY KEY (user_id, permission)
);

-- -----------------------------------------------
-- 3. SEED ROLE PERMISSIONS
-- -----------------------------------------------

-- ADMIN gets everything
INSERT INTO users_svc.role_permissions (role, permission) VALUES
    ('ADMIN', 'products.create'),
    ('ADMIN', 'products.update'),
    ('ADMIN', 'products.delete'),
    ('ADMIN', 'orders.view_own'),
    ('ADMIN', 'orders.update_own'),
    ('ADMIN', 'orders.view_all'),
    ('ADMIN', 'orders.update_status'),
    ('ADMIN', 'orders.cancel_any'),
    ('ADMIN', 'orders.delete'),
    ('ADMIN', 'users.view_all'),
    ('ADMIN', 'users.update_any'),
    ('ADMIN', 'users.delete_any');

-- CSR can manage orders and view users, no product write access
INSERT INTO users_svc.role_permissions (role, permission) VALUES
    ('CSR', 'orders.view_own'),
    ('CSR', 'orders.update_own'),
    ('CSR', 'orders.view_all'),
    ('CSR', 'orders.update_status'),
    ('CSR', 'orders.cancel_any'),
    ('CSR', 'users.view_all'),
    ('CSR', 'products.create'),
    ('CSR', 'products.update');

-- USER can only manage their own orders
INSERT INTO users_svc.role_permissions (role, permission) VALUES
    ('USER', 'orders.view_own'),
    ('USER', 'orders.update_own');

-- CUSTOM gets no default permissions
-- assigned entirely via users_svc.user_permissions per user

-- -----------------------------------------------
-- 4. AUTHORIZE FUNCTION
-- -----------------------------------------------

CREATE OR REPLACE FUNCTION users_svc.authorize(
    requested_permission users_svc.app_permission
)
RETURNS BOOLEAN AS $$
DECLARE
    has_permission INT;
BEGIN
    SELECT COUNT(*) INTO has_permission
    FROM (
        -- Check via named role
        SELECT 1
        FROM users_svc.user_roles ur
        JOIN users_svc.role_permissions rp ON rp.role = ur.role
        WHERE ur.user_id = auth.uid()
          AND rp.permission = requested_permission

        UNION

        -- Check via direct user permission override
        SELECT 1
        FROM users_svc.user_permissions
        WHERE user_id = auth.uid()
          AND permission = requested_permission
    ) combined;

    RETURN has_permission > 0;
END;
$$ LANGUAGE plpgsql STABLE SECURITY DEFINER SET search_path = '';

-- -----------------------------------------------
-- 5. CUSTOM ACCESS TOKEN HOOK
--    Injects highest role into JWT
-- -----------------------------------------------

CREATE OR REPLACE FUNCTION users_svc.custom_access_token_hook(event JSONB)
RETURNS JSONB LANGUAGE plpgsql VOLATILE
SECURITY DEFINER
SET search_path = users_svc, pg_catalog
AS $$
DECLARE
    claims    JSONB;
    user_role users_svc.app_role;
    target_user_id UUID;
    email_id text;
BEGIN
-- RAISE EXCEPTION 'The incoming event payload is: %', event;
    target_user_id := (event->>'user_id')::UUID;
    email_id := (event->'claims'->>'email');
    
    -- Upsert the default role if the user doesn't have one yet
    INSERT INTO users_svc.users (auth_id,email, user_type)
    VALUES (target_user_id, email_id, 'USER')
    ON CONFLICT DO NOTHING;

    -- Fetch highest role
    SELECT user_type INTO user_role
    FROM users_svc.users
    WHERE auth_id = target_user_id
    ORDER BY CASE user_type
        WHEN 'ADMIN'  THEN 1
        WHEN 'CSR'    THEN 2
        WHEN 'USER'   THEN 3
        WHEN 'CUSTOM' THEN 4
    END
    LIMIT 1;

    claims := event->'claims';
    claims := jsonb_set(claims, '{user_role}',
        COALESCE(to_jsonb(user_role::TEXT), '"USER"')
    );
    event := jsonb_set(event, '{claims}', claims);
    RETURN event;
END;
$$;

-- Grant hook permissions
GRANT USAGE ON SCHEMA users_svc TO supabase_auth_admin;
GRANT EXECUTE ON FUNCTION users_svc.custom_access_token_hook TO supabase_auth_admin;
REVOKE EXECUTE ON FUNCTION users_svc.custom_access_token_hook FROM authenticated, anon, public;
GRANT ALL ON TABLE user_svc.user_roles TO supabase_auth_admin;
GRANT ALL ON TABLE user_svc.user_permissions TO supabase_auth_admin;
GRANT ALL ON TABLE users_svc.user_roles TO supabase_auth_admin;
GRANT ALL ON TABLE users_svc.users TO supabase_auth_admin;
REVOKE ALL ON TABLE users_svc.user_roles FROM authenticated, anon, public;

-- Grant authenticated role permission to execute authorize
GRANT EXECUTE ON FUNCTION users_svc.authorize(users_svc.app_permission) TO authenticated;

-- Also grant usage on the schema so authenticated can see it
GRANT USAGE ON SCHEMA users_svc TO authenticated;

-- And read access to the tables the function queries
GRANT SELECT ON users_svc.user_roles TO authenticated;
GRANT SELECT ON users_svc.role_permissions TO authenticated;
GRANT SELECT ON users_svc.user_permissions TO authenticated;

-- -----------------------------------------------
-- 6. RLS POLICIES
-- -----------------------------------------------

-- Enable RLS on all tables
ALTER TABLE users_svc.role_permissions  ENABLE ROW LEVEL SECURITY;
ALTER TABLE users_svc.user_roles        ENABLE ROW LEVEL SECURITY;
ALTER TABLE users_svc.user_permissions  ENABLE ROW LEVEL SECURITY;
ALTER TABLE products_svc.products       ENABLE ROW LEVEL SECURITY;
ALTER TABLE orders_svc.orders           ENABLE ROW LEVEL SECURITY;
ALTER TABLE orders_svc.order_items      ENABLE ROW LEVEL SECURITY;

-- Allow auth admin to insert into user_roles
CREATE POLICY "Allow auth admin to insert user roles"
    ON user_svc.user_roles AS PERMISSIVE FOR INSERT
    TO supabase_auth_admin
    WITH CHECK (true);

-- Allow auth hook to read user roles
CREATE POLICY "Allow auth admin to read user roles"
    ON users_svc.user_roles AS PERMISSIVE FOR SELECT
    TO supabase_auth_admin USING (true);

-- Products: public read, restricted write
CREATE POLICY "Products are publicly viewable"
    ON products_svc.products FOR SELECT
    TO anon, authenticated
    USING (true);

CREATE POLICY "Authorized product create"
    ON products_svc.products FOR INSERT TO authenticated
    WITH CHECK ((SELECT users_svc.authorize('products.create')));

CREATE POLICY "Authorized product update"
    ON products_svc.products FOR UPDATE TO authenticated
    USING ((SELECT users_svc.authorize('products.update')));

CREATE POLICY "Authorized product delete"
    ON products_svc.products FOR DELETE TO authenticated
    USING ((SELECT users_svc.authorize('products.delete')));

-- Orders: own rows for users, all rows for admin/csr
CREATE POLICY "Orders select policy"
    ON orders_svc.orders FOR SELECT TO authenticated
    USING (
        (SELECT users_svc.authorize('orders.view_all'))
        OR (
            (SELECT users_svc.authorize('orders.view_own'))
            AND user_id = (
                SELECT user_id FROM users_svc.users WHERE auth_id = auth.uid()
            )
        )
    );

CREATE POLICY "Orders update policy"
    ON orders_svc.orders FOR UPDATE TO authenticated
    USING (
        (SELECT users_svc.authorize('orders.update_status'))
        OR (
            (SELECT users_svc.authorize('orders.update_own'))
            AND user_id = (
                SELECT user_id FROM users_svc.users WHERE auth_id = auth.uid()
            )
        )
    );

CREATE POLICY "Authorized order cancel"
    ON orders_svc.orders FOR UPDATE TO authenticated
    USING ((SELECT users_svc.authorize('orders.cancel_any')))
    WITH CHECK (order_status = 'cancelled');

CREATE POLICY "Authorized order delete"
    ON orders_svc.orders FOR DELETE TO authenticated
    USING ((SELECT users_svc.authorize('orders.delete')));

-- Order items follow the same access as their parent order
CREATE POLICY "Order items select policy"
    ON orders_svc.order_items FOR SELECT TO authenticated
    USING (
        (SELECT users_svc.authorize('orders.view_all'))
        OR (
            (SELECT users_svc.authorize('orders.view_own'))
            AND order_id IN (
                SELECT order_id FROM orders_svc.orders
                WHERE user_id = (
                    SELECT user_id FROM users_svc.users WHERE auth_id = auth.uid()
                )
            )
        )
    );