CREATE INDEX idx_boards_owner ON boards (owner_id);
CREATE INDEX idx_tasks_board ON tasks (board_id);
CREATE INDEX idx_comments_task ON comments (task_id);

-- Seed demo data so the platform is usable on first boot.
INSERT INTO users (username, display_name)
VALUES ('alice', 'Alice Founder'),
       ('bob', 'Bob Builder');

INSERT INTO boards (name, description, owner_id)
VALUES ('Product Launch', 'Tasks to ship v1', 1),
       ('Personal', 'My personal todo board', 2);

INSERT INTO tasks (title, description, status, priority, board_id, assignee_id)
VALUES ('Design landing page', 'Create the hero section and pricing layout', 'TODO', 2, 1, 1),
       ('Set up CI pipeline', 'Configure GitHub Actions for build and test', 'IN_PROGRESS', 3, 1, 2),
       ('Write API docs', 'Document the REST and GraphQL endpoints', 'TODO', 1, 1, 1),
       ('Buy groceries', 'Milk, eggs, bread', 'TODO', 1, 2, 2);

INSERT INTO comments (body, author_id, task_id)
VALUES ('Lets use a dark theme for the hero.', 2, 1),
       ('CI is half done, runners are green.', 2, 2);
