CREATE TABLE projects (
                          id BIGINT PRIMARY KEY AUTO_INCREMENT,
                          name VARCHAR(255) NOT NULL,
                          description VARCHAR(1000),
                          user_id BIGINT NOT NULL,
                          created_at TIMESTAMP NOT NULL,
                          updated_at TIMESTAMP NOT NULL,
                          FOREIGN KEY (user_id) REFERENCES users(id)
);