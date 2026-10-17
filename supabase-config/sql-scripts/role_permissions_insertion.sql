insert into public.role_permissions (role, permission)
values
  ('ADMIN', 'products.remove'),
  ('ADMIN', 'products.read'),
  ('ADMIN', 'products.update'),
  ('ADMIN', 'products.add'),
  ('CSR', 'products.read'),
  ('CSR', 'products.update'),
  ('CSR', 'products.add'),
  ('USER', 'products.read');