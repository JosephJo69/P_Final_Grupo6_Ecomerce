CREATE TABLE IF NOT EXISTS nodes (
    id VARCHAR(50) PRIMARY KEY,
    name VARCHAR(200) NOT NULL,
    description VARCHAR(500),
    parent_id VARCHAR(50),
    FOREIGN KEY (parent_id) REFERENCES nodes(id)
);
