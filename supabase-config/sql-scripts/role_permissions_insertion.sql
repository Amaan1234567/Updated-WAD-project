insert into public.role_permissions (role, permission)
values
  ('admin', 'products.remove'),
  ('admin', 'products.read'),
  ('admin', 'products.update'),
  ('admin', 'products.add'),
  ('csr', 'products.read'),
  ('csr', 'products.update'),
  ('csr', 'products.add'),
  ('user', 'products.read');