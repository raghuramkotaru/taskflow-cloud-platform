CREATE TABLE users (
    id           BIGSERIAL PRIMARY KEY,
    username     VARCHAR(255) NOT NULL UNIQUE,
    display_name VARCHAR(255) NOT NULL,
    created_at   TIMESTAMPTZ  NOT NULL DEFAULT now()
);

CREATE TABLE boards (
    id          BIGSERIAL PRIMARY KEY,
    name        VARCHAR(255)  NOT NULL,
    description VARCHAR(1000),
    owner_id    BIGINT        NOT NULL REFERENCES users (id),
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE TABLE tasks (
    id          BIGSERIAL PRIMARY KEY,
    title       VARCHAR(255)  NOT NULL,
    description VARCHAR(4000),
    status      VARCHAR(32)   NOT NULL DEFAULT 'TODO',
    priority    INT           NOT NULL DEFAULT 0,
    board_id    BIGINT        NOT NULL REFERENCES boards (id) ON DELETE CASCADE,
    assignee_id BIGINT        REFERENCES users (id),
    created_at  TIMESTAMPTZ   NOT NULL DEFAULT now()
);

CREATE TABLE comments (
    id         BIGSERIAL PRIMARY KEY,
    body       VARCHAR(4000) NOT NULL,
    author_id  BIGINT        NOT NULL REFERENCES users (id),
    task_id    BIGINT        NOT NULL REFERENCES tasks (id) ON DELETE CASCADE,
    created_at TIMESTAMPTZ   NOT NULL DEFAULT now()
);
