-- ============================================================
-- PLANT.IO - USERS_SVC SCHEMA SETUP
-- ============================================================

CREATE SCHEMA IF NOT EXISTS users_svc;

-- -----------------------------------------------
-- ENUMS
-- -----------------------------------------------

CREATE TYPE users_svc.app_role AS ENUM ('admin', 'user', 'csr', 'custom');

CREATE TYPE users_svc.app_permission AS ENUM (
    'products.create',
    'products.update',
    'products.delete',
    'orders.view_own',
    'orders.update_own',
    'orders.view_all',
    'orders.update_status',
    'orders.cancel_any',
    'orders.delete',
    'orders.create_order',
    'orders.get_order_by_order_id',
    'orders.get_order_items_by_order_id',
    'orders.get_user_orders_by_user_id',
    'orders.update_address',
    'users.view_all',
    'users.update_any',
    'users.delete_any'
);

-- -----------------------------------------------
-- TABLES
-- -----------------------------------------------

CREATE TABLE users_svc.users (
    user_id    TEXT PRIMARY KEY,
    auth_id    UUID NOT NULL UNIQUE,
    email      VARCHAR(255) UNIQUE NOT NULL,
    password   VARCHAR(255),
    full_name  VARCHAR(255),
    phone      NUMERIC(15,0),
    address    TEXT,
    city       VARCHAR(200),
    state      VARCHAR(100),
    country    VARCHAR(100),
    pincode    VARCHAR(10),
    user_type  users_svc.app_role DEFAULT 'user'::users_svc.app_role,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users_svc.role_permissions (
    role        users_svc.app_role       NOT NULL,
    permission  users_svc.app_permission NOT NULL,
    PRIMARY KEY (role, permission)
);

CREATE TABLE users_svc.user_permissions (
    user_id    UUID                        REFERENCES auth.users(id) ON DELETE CASCADE,
    permission users_svc.app_permission     NOT NULL,
    PRIMARY KEY (user_id, permission)
);

-- -----------------------------------------------
-- SEED ROLE PERMISSIONS
-- -----------------------------------------------

-- ADMIN gets everything
INSERT INTO users_svc.role_permissions (role, permission) VALUES
    ('admin', 'products.create'),
    ('admin', 'products.update'),
    ('admin', 'products.delete'),
    ('admin', 'orders.view_own'),
    ('admin', 'orders.update_own'),
    ('admin', 'orders.view_all'),
    ('admin', 'orders.update_status'),
    ('admin', 'orders.cancel_any'),
    ('admin', 'orders.delete'),
    ('admin', 'orders.create_order'),
    ('admin', 'orders.get_order_by_order_id'),
    ('admin', 'orders.get_order_items_by_order_id'),
    ('admin', 'orders.get_user_orders_by_user_id'),
    ('admin', 'orders.update_address'),
    ('admin', 'users.view_all'),
    ('admin', 'users.update_any'),
    ('admin', 'users.delete_any');

-- CSR can manage orders and view users
INSERT INTO users_svc.role_permissions (role, permission) VALUES
    ('csr', 'orders.view_own'),
    ('csr', 'orders.update_own'),
    ('csr', 'orders.view_all'),
    ('csr', 'orders.update_status'),
    ('csr', 'orders.cancel_any'),
    ('csr', 'orders.create_order'),
    ('csr', 'orders.get_order_by_order_id'),
    ('csr', 'orders.get_order_items_by_order_id'),
    ('csr', 'orders.get_user_orders_by_user_id'),
    ('csr', 'orders.update_address'),
    ('csr', 'users.view_all'),
    ('csr', 'products.create'),
    ('csr', 'products.update');

-- USER can only manage their own orders
INSERT INTO users_svc.role_permissions (role, permission) VALUES
    ('user', 'orders.view_own'),
    ('user', 'orders.update_own'),
    ('user', 'orders.create_order'),
    ('user', 'orders.get_order_by_order_id'),
    ('user', 'orders.get_order_items_by_order_id'),
    ('user', 'orders.get_user_orders_by_user_id'),
    ('user', 'orders.update_address');

-- -----------------------------------------------
-- FUNCTIONS
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
        SELECT 1
        FROM users_svc.users u
        JOIN users_svc.role_permissions rp ON rp.role = u.user_type::users_svc.app_role
        WHERE u.auth_id = auth.uid()
          AND rp.permission = requested_permission

        UNION

        SELECT 1
        FROM users_svc.user_permissions
        WHERE user_id = auth.uid()
          AND permission = requested_permission
    ) combined;

    RETURN has_permission > 0;
END;
$$ LANGUAGE plpgsql STABLE SECURITY DEFINER SET search_path = '';

CREATE OR REPLACE FUNCTION users_svc.custom_access_token_hook(event JSONB)
RETURNS JSONB LANGUAGE plpgsql VOLATILE AS $$
DECLARE
    claims    JSONB;
    user_role TEXT;
    target_user_id UUID;
BEGIN
    target_user_id := (event->>'user_id')::UUID;

    SELECT user_type INTO user_role
    FROM users_svc.users
    WHERE auth_id = target_user_id;

    IF user_role IS NULL THEN
        user_role := 'user';
    END IF;

    claims := event->'claims';
    claims := jsonb_set(claims, '{user_role}', to_jsonb(user_role));
    event := jsonb_set(event, '{claims}', claims);
    RETURN event;
END;
$$;

-- -----------------------------------------------
-- GRANTS
-- -----------------------------------------------

GRANT USAGE ON SCHEMA users_svc TO supabase_auth_admin;
GRANT SELECT ON users_svc.users TO supabase_auth_admin;
GRANT EXECUTE ON FUNCTION users_svc.custom_access_token_hook TO supabase_auth_admin;
REVOKE EXECUTE ON FUNCTION users_svc.custom_access_token_hook FROM authenticated, anon, public;

GRANT USAGE ON SCHEMA users_svc TO authenticated;
GRANT SELECT ON users_svc.users TO authenticated;
GRANT SELECT ON users_svc.role_permissions TO authenticated;
GRANT SELECT ON users_svc.user_permissions TO authenticated;
GRANT EXECUTE ON FUNCTION users_svc.authorize(users_svc.app_permission) TO authenticated;

-- -----------------------------------------------
-- RLS
-- -----------------------------------------------

ALTER TABLE users_svc.role_permissions  ENABLE ROW LEVEL SECURITY;
ALTER TABLE users_svc.user_permissions  ENABLE ROW LEVEL SECURITY;
