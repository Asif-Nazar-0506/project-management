CREATE TABLE tasks (
                       id BIGINT PRIMARY KEY AUTO_INCREMENT,
                       title VARCHAR(255) NOT NULL,
                       description VARCHAR(2000),
                       status VARCHAR(50) NOT NULL,
                       priority VARCHAR(50) NOT NULL,
                       due_date DATE,
                       project_id BIGINT NOT NULL,
                       created_at TIMESTAMP NOT NULL,
                       updated_at TIMESTAMP NOT NULL,
                       FOREIGN KEY (project_id) REFERENCES projects(id)
);