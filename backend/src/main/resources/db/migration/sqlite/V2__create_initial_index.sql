CREATE INDEX idx_inventory_stock_prod_wh ON inventory_stock (product_id, warehouse_id);
CREATE INDEX idx_batch_stock_batch_wh ON batch_stock (batch_id, warehouse_id);
CREATE INDEX idx_role_permissions_role_perm ON role_permissions (role_id, permission_id);