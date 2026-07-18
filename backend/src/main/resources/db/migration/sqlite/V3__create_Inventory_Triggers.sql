CREATE TRIGGER trg_audit_products_update
    AFTER UPDATE
    ON products
    FOR EACH ROW
BEGIN
    INSERT INTO audit_logs (employee_id,
                            action,
                            table_name,
                            record_id,
                            old_values,
                            new_values,
                            ip_address,
                            created_at)
    VALUES ('SYSTEM_TRIGGER',
            'UPDATE',
            'products',
            OLD.id,
            '{ "sku": "' || COALESCE(OLD.sku, '') || '", "name": "' || OLD.name || '", "is_active": ' ||
            OLD.is_active || ' }',
            '{ "sku": "' || COALESCE(NEW.sku, '') || '", "name": "' || NEW.name || '", "is_active": ' ||
            NEW.is_active || ' }',
            '127.0.0.1',
            CURRENT_TIMESTAMP);
END;

CREATE TRIGGER trg_sync_inventory_on_movement
    AFTER INSERT
    ON stock_movements
    FOR EACH ROW
BEGIN
    -- 1. Attempt to insert the record if it does not exist for that product and warehouse
    INSERT OR IGNORE INTO inventory_stock (product_id, warehouse_id, quantity)
    VALUES (NEW.product_id, NEW.warehouse_id, 0.00);

    -- 2. Update by adding or subtracting, depending on the type of transaction
    UPDATE inventory_stock
    SET quantity = quantity + (
        CASE
            WHEN NEW.movement_type IN ('INPUT', 'PURCHASE', 'ADJUSTMENT_IN') THEN NEW.quantity
            WHEN NEW.movement_type IN ('OUTPUT', 'SALE', 'ADJUSTMENT_OUT') THEN -NEW.quantity
            ELSE 0.00
            END
        )
    WHERE product_id = NEW.product_id
      AND warehouse_id = NEW.warehouse_id;
END;

CREATE TRIGGER trg_check_batch_expiration
    AFTER INSERT
    ON product_batches
    FOR EACH ROW
    WHEN NEW.expiry_date IS NOT NULL AND (julianday(NEW.expiry_date) - julianday('now')) <= 30
BEGIN
    INSERT INTO expiration_alerts (product_id, days_before_alert, action_required)
    VALUES (NEW.product_id,
            CAST(julianday(NEW.expiry_date) - julianday('now') AS INTEGER),
            'Lote ' || NEW.batch_number || ' próximo a vencer. Priorizar salida en cajas (FEFO).');
END;