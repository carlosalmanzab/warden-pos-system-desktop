
CREATE TABLE categories (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL
);

CREATE TABLE warehouses (
                            id INTEGER PRIMARY KEY AUTOINCREMENT,
                            name TEXT NOT NULL,
                            location TEXT
);

CREATE TABLE suppliers (
                           id TEXT PRIMARY KEY, --  TEXT --> UUID
                           supplier_code TEXT UNIQUE NOT NULL,
                           company_name TEXT NOT NULL,
                           contact_name TEXT,
                           phone TEXT,
                           email TEXT,
                           tax_identifier TEXT
);

CREATE TABLE roles (
                       id INTEGER PRIMARY KEY AUTOINCREMENT,
                       name TEXT NOT NULL UNIQUE
);

CREATE TABLE permissions (
                             id INTEGER PRIMARY KEY AUTOINCREMENT,
                             name TEXT NOT NULL UNIQUE
);

CREATE TABLE customers (
                           id TEXT PRIMARY KEY, -- TEXT --> UUID
                           customer_number TEXT UNIQUE NOT NULL,
                           name TEXT NOT NULL,
                           tax_identifier TEXT,
                           phone TEXT,
                           email TEXT,
                           credit_limit DECIMAL(10, 2) DEFAULT 0.00
);

CREATE TABLE expense_categories (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                                    name TEXT NOT NULL,
                                    description TEXT
);

CREATE TABLE products (
                          id TEXT PRIMARY KEY,
                          sku TEXT UNIQUE NOT NULL,
                          barcode TEXT UNIQUE,
                          name TEXT NOT NULL,
                          category_id INTEGER,
                          is_active BOOLEAN DEFAULT 1,
                          FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE role_permissions (
                                  role_id INTEGER,
                                  permission_id INTEGER,
                                  PRIMARY KEY (role_id, permission_id),
                                  FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                  FOREIGN KEY (permission_id) REFERENCES permissions(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE employees (
                           id TEXT PRIMARY KEY,
                           username TEXT UNIQUE NOT NULL,
                           password_hash TEXT NOT NULL,
                           first_name TEXT NOT NULL,
                           last_name TEXT NOT NULL,
                           email TEXT,
                           role_id INTEGER,
                           is_active BOOLEAN DEFAULT 1,
                           FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE inventory_stock (
                                 product_id TEXT,
                                 warehouse_id INTEGER,
                                 quantity DECIMAL(10, 2) DEFAULT 0.00,
                                 PRIMARY KEY (product_id, warehouse_id),
                                 FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                 FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE product_batches (
                                 id TEXT PRIMARY KEY,
                                 product_id TEXT NOT NULL,
                                 batch_number TEXT NOT NULL,
                                 supplier_id TEXT,
                                 manufacturing_date DATE,
                                 expiry_date DATE,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                 FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE expiration_alerts (
                                   id INTEGER PRIMARY KEY AUTOINCREMENT,
                                   product_id TEXT NOT NULL,
                                   days_before_alert INTEGER NOT NULL,
                                   action_required TEXT,
                                   FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE sales (
                       id TEXT PRIMARY KEY,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       total_amount DECIMAL(10, 2) NOT NULL,
                       employee_id TEXT NOT NULL,
                       FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE purchase_orders (
                                 id TEXT PRIMARY KEY,
                                 po_number TEXT UNIQUE NOT NULL,
                                 supplier_id TEXT NOT NULL,
                                 warehouse_id INTEGER NOT NULL,
                                 status TEXT NOT NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                 FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE expenses (
                          id TEXT PRIMARY KEY,
                          expense_category_id INTEGER NOT NULL,
                          warehouse_id INTEGER NOT NULL,
                          employee_id TEXT NOT NULL,
                          supplier_id TEXT,
                          amount DECIMAL(10, 2) NOT NULL,
                          tax_amount DECIMAL(10, 2) DEFAULT 0.00,
                          payment_method TEXT NOT NULL,
                          status TEXT NOT NULL,
                          reference_number TEXT,
                          attachment_url TEXT,
                          expense_date DATE NOT NULL,
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          FOREIGN KEY (expense_category_id) REFERENCES expense_categories(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                          FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                          FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                          FOREIGN KEY (supplier_id) REFERENCES suppliers(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE audit_logs (
                            id INTEGER PRIMARY KEY AUTOINCREMENT, -- SQLite assigns a ROWID alias to the BIGINT PRIMARY KEY using INTEGER
                            employee_id TEXT NOT NULL,
                            action TEXT NOT NULL,
                            table_name TEXT NOT NULL,
                            record_id TEXT NOT NULL,
                            old_values TEXT, -- JSON stored as plain text
                            new_values TEXT, -- JSON stored as plain text
                            ip_address TEXT,
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                            FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE batch_stock (
                             batch_id TEXT,
                             warehouse_id INTEGER,
                             quantity DECIMAL(10, 2) DEFAULT 0.00,
                             PRIMARY KEY (batch_id, warehouse_id),
                             FOREIGN KEY (batch_id) REFERENCES product_batches(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                             FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE sale_items (
                            id TEXT PRIMARY KEY,
                            sale_id TEXT NOT NULL,
                            product_id TEXT NOT NULL,
                            batch_id TEXT,
                            quantity DECIMAL(10, 2) NOT NULL,
                            unit_price DECIMAL(10, 2) NOT NULL,
                            subtotal DECIMAL(10, 2) NOT NULL,
                            FOREIGN KEY (sale_id) REFERENCES sales(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                            FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                            FOREIGN KEY (batch_id) REFERENCES product_batches(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE invoices (
                          id TEXT PRIMARY KEY,
                          sale_id TEXT UNIQUE NOT NULL, -- UNIQUE guarantees a one-to-one (1:1) relationship
                          customer_id TEXT NOT NULL,
                          invoice_serial TEXT UNIQUE NOT NULL,
                          subtotal DECIMAL(10, 2) NOT NULL,
                          tax_amount DECIMAL(10, 2) NOT NULL,
                          discount_amount DECIMAL(10, 2) DEFAULT 0.00,
                          grand_total DECIMAL(10, 2) NOT NULL,
                          status TEXT NOT NULL,
                          FOREIGN KEY (sale_id) REFERENCES sales(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                          FOREIGN KEY (customer_id) REFERENCES customers(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE purchase_items (
                                id TEXT PRIMARY KEY,
                                purchase_order_id TEXT NOT NULL,
                                product_id TEXT NOT NULL,
                                batch_id TEXT,
                                quantity_ordered DECIMAL(10, 2) NOT NULL,
                                quantity_received DECIMAL(10, 2) DEFAULT 0.00,
                                unit_cost DECIMAL(10, 2) NOT NULL,
                                FOREIGN KEY (purchase_order_id) REFERENCES purchase_orders(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                FOREIGN KEY (batch_id) REFERENCES product_batches(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE stock_movements (
                                 id TEXT PRIMARY KEY,
                                 product_id TEXT NOT NULL,
                                 warehouse_id INTEGER NOT NULL,
                                 batch_id TEXT,
                                 movement_type TEXT NOT NULL,
                                 source_type TEXT NOT NULL,
                                 source_id TEXT NOT NULL,
                                 quantity DECIMAL(10, 2) NOT NULL,
                                 created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                 FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                 FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                 FOREIGN KEY (batch_id) REFERENCES product_batches(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE petty_cash_ledger (
                                   id TEXT PRIMARY KEY,
                                   warehouse_id INTEGER NOT NULL,
                                   employee_id TEXT NOT NULL,
                                   expense_id TEXT,
                                   amount DECIMAL(10, 2) NOT NULL,
                                   type TEXT NOT NULL,
                                   reason TEXT NOT NULL,
                                   created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   FOREIGN KEY (warehouse_id) REFERENCES warehouses(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                   FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE NO ACTION ON UPDATE NO ACTION,
                                   FOREIGN KEY (expense_id) REFERENCES expenses(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE invoice_payments (
                                  id TEXT PRIMARY KEY,
                                  invoice_id TEXT NOT NULL,
                                  payment_method TEXT NOT NULL,
                                  amount DECIMAL(10, 2) NOT NULL,
                                  processed_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                  FOREIGN KEY (invoice_id) REFERENCES invoices(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);

CREATE TABLE kardex (
                        id TEXT PRIMARY KEY,
                        movement_id TEXT UNIQUE NOT NULL, -- UNIQUE guarantees a one-to-one (1:1) relationship
                        input_qty DECIMAL(10, 2) DEFAULT 0.00,
                        input_unit_cost DECIMAL(10, 2) DEFAULT 0.00,
                        input_total_cost DECIMAL(10, 2) DEFAULT 0.00,
                        output_qty DECIMAL(10, 2) DEFAULT 0.00,
                        output_unit_cost DECIMAL(10, 2) DEFAULT 0.00,
                        output_total_cost DECIMAL(10, 2) DEFAULT 0.00,
                        balance_qty DECIMAL(10, 2) NOT NULL,
                        balance_unit_cost DECIMAL(10, 2) NOT NULL,
                        balance_total_cost DECIMAL(10, 2) NOT NULL,
                        created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                        FOREIGN KEY (movement_id) REFERENCES stock_movements(id) ON DELETE NO ACTION ON UPDATE NO ACTION
);