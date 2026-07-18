CREATE TABLE refresh_tokens
(
    id          TEXT PRIMARY KEY,
    employee_id TEXT        NOT NULL,
    token       TEXT UNIQUE NOT NULL,
    expires_at  TIMESTAMP   NOT NULL,
    is_revoked  BOOLEAN   DEFAULT 0,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE TABLE password_history
(
    id            TEXT PRIMARY KEY,
    employee_id   TEXT NOT NULL,
    password_hash TEXT NOT NULL,
    created_at    TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (employee_id) REFERENCES employees (id) ON DELETE CASCADE ON UPDATE NO ACTION
);

CREATE INDEX idx_refresh_tokens_employee ON refresh_tokens (employee_id);
CREATE INDEX idx_refresh_tokens_token ON refresh_tokens (token);

CREATE INDEX idx_password_history_employee ON password_history (employee_id);

