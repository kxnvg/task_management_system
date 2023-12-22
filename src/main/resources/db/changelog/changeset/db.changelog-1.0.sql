CREATE TABLE users
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    email VARCHAR(128) UNIQUE NOT NULL,
    firstname VARCHAR(128) NOT NULL,
    lastname VARCHAR(128) NOT NULL,
    password VARCHAR(128) NOT NULL,
    role VARCHAR(64),
    created_at TIMESTAMPTZ DEFAULT current_timestamp,
    updated_at TIMESTAMPTZ DEFAULT current_timestamp
);

CREATE TABLE tasks
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    title VARCHAR(128) NOT NULL,
    content VARCHAR(4096),
    status VARCHAR(64) NOT NULL,
    priority VARCHAR(64),
    author_id BIGINT NOT NULL,
    executor_id BIGINT,
    created_at TIMESTAMPTZ DEFAULT current_timestamp,
    updated_at TIMESTAMPTZ DEFAULT current_timestamp,

    CONSTRAINT fk_task_author_id FOREIGN KEY (author_id) REFERENCES users (id),
    CONSTRAINT fk_task_executor_id FOREIGN KEY (executor_id) REFERENCES users (id)
);

CREATE TABLE comments
(
    id BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY UNIQUE,
    text VARCHAR(512) NOT NULL,
    user_id BIGINT NOT NULL,
    task_id BIGINT NOT NULL,
    created_at TIMESTAMPTZ DEFAULT current_timestamp,
    updated_at TIMESTAMPTZ DEFAULT current_timestamp,

    CONSTRAINT fk_user_id FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT fk_task_id FOREIGN KEY (task_id) REFERENCES tasks (id)
);