CREATE SCHEMA IF NOT EXISTS users_svc;


CREATE TABLE users_svc.users (
    user_id text PRIMARY KEY,
    auth_id UUID NOT NULL UNIQUE,  -- Links to Supabase auth.users
    email VARCHAR(255) UNIQUE NOT NULL,
    full_name VARCHAR(255),
    phone numeric(15,0),
    address TEXT,
    city VARCHAR(100),
    pincode VARCHAR(10),
    user_type users_svc.app_role DEFAULT 'user'::users_svc.app_role,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);