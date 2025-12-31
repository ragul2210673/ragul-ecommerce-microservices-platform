-- Create audit log table first
CREATE TABLE INVENTORY_LOG (
    LOG_ID NUMBER GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    PRODUCT_NAME VARCHAR2(255) NOT NULL,
    CHANGE_QTY NUMBER NOT NULL,
    CHANGE_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    REASON VARCHAR2(100)
);

-- Trigger: Log inventory changes
CREATE OR REPLACE TRIGGER trg_log_inventory_change
AFTER UPDATE OF stock_quantity ON PRODUCTS
FOR EACH ROW
BEGIN
    INSERT INTO INVENTORY_LOG (product_name, change_qty, reason)
    VALUES (
        :NEW.name,
        :NEW.stock_quantity - :OLD.stock_quantity,
        'STOCK_UPDATE'
    );
END;
/

-- Trigger: Prevent negative stock
CREATE OR REPLACE TRIGGER trg_check_stock_level
BEFORE UPDATE OF stock_quantity ON PRODUCTS
FOR EACH ROW
BEGIN
    IF :NEW.stock_quantity < 0 THEN
        RAISE_APPLICATION_ERROR(-20003, 
            'Insufficient stock for ' || :OLD.name || 
            '. Available: ' || :OLD.stock_quantity);
    END IF;
END;
/

-- Trigger: Auto-update timestamp
CREATE OR REPLACE TRIGGER trg_update_product_timestamp
BEFORE UPDATE ON PRODUCTS
FOR EACH ROW
BEGIN
    :NEW.updated_at := CURRENT_TIMESTAMP;
END;
/