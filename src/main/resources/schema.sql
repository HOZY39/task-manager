CREATE TABLE IF NOT EXISTS subjects (
    name VARCHAR(50) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS sections (
    name VARCHAR(50) PRIMARY KEY,
    subject VARCHAR(50) REFERENCES subjects(name)
);

CREATE TABLE IF NOT EXISTS roles (
    name VARCHAR(20) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS users (
    username VARCHAR(50) PRIMARY KEY,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) REFERENCES roles(name)
);

CREATE TABLE IF NOT EXISTS tasks (
    id SERIAL PRIMARY KEY,
    subject VARCHAR(50) REFERENCES subjects(name),
    section VARCHAR(50) REFERENCES sections(name),
    description TEXT,
    creator_username VARCHAR(50) REFERENCES users(username),
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS solutions (
    id SERIAL PRIMARY KEY,
    task_id INTEGER REFERENCES tasks(id),
    solution TEXT,
    creator_username VARCHAR(50) REFERENCES users(username),
    date_added TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

