CREATE TABLE IF NOT EXISTS tickets (
    id INTEGER PRIMARY KEY AUTOINCREMENT,
    subject VARCHAR(255) NOT NULL,
    action VARCHAR(255) NOT NULL,
    details TEXT,
    location VARCHAR(255),
    status VARCHAR(50) NOT NULL,
    cancellation_reason VARCHAR(255),
    creator_id INTEGER NOT NULL,
    recipient_id INTEGER NOT NULL,
    responsible_id INTEGER,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (creator_id) REFERENCES users(id),
    FOREIGN KEY (recipient_id) REFERENCES users(id),
    FOREIGN KEY (responsible_id) REFERENCES users(id)
);

CREATE TABLE IF NOT EXISTS tickets_observers (
    ticket_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    PRIMARY KEY (ticket_id, user_id),
    FOREIGN KEY (ticket_id) REFERENCES tickets(id),
    FOREIGN KEY (user_id) REFERENCES users(id)
);