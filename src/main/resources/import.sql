INSERT INTO authority (id, authority) VALUES (1, 'PRODUCT_MANAGER');
INSERT INTO authority (id, authority) VALUES (2, 'DEVELOPER');
INSERT INTO authority (id, authority) VALUES (3, 'USER');

INSERT INTO user (id, email, password, enabled, expired, account_expired, credentials_expired, locked) VALUES (1, 'fletchersarip@emaildomain.com', '$2y$11$rPf4cvOMC7jBSlKAypMmhOOXocdapZe/WwZtIZO/cS.4CI/z49/v2', TRUE, FALSE, FALSE, FALSE, FALSE);
INSERT INTO user (id, email, password, enabled, expired, account_expired, credentials_expired, locked) VALUES (2, 'jack@emaildomain.com', '$2y$11$rPf4cvOMC7jBSlKAypMmhOOXocdapZe/WwZtIZO/cS.4CI/z49/v2', TRUE, FALSE, FALSE, FALSE, FALSE);
INSERT INTO user (id, email, password, enabled, expired, account_expired, credentials_expired, locked) VALUES (3, 'daniel@emaildomain.com', '$2y$11$rPf4cvOMC7jBSlKAypMmhOOXocdapZe/WwZtIZO/cS.4CI/z49/v2', TRUE, FALSE, FALSE, FALSE, FALSE);
INSERT INTO user (id, email, password, enabled, expired, account_expired, credentials_expired, locked) VALUES (4, 'ben@emaildomain.com', '$2y$11$rPf4cvOMC7jBSlKAypMmhOOXocdapZe/WwZtIZO/cS.4CI/z49/v2', TRUE, FALSE, FALSE, FALSE, FALSE);

INSERT INTO user_authority (user_id, authority_id) VALUES (1, 1);
INSERT INTO user_authority (user_id, authority_id) VALUES (1, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (1, 3);

INSERT INTO user_authority (user_id, authority_id) VALUES (2, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (2, 3);

INSERT INTO user_authority (user_id, authority_id) VALUES (3, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (3, 3);

INSERT INTO user_authority (user_id, authority_id) VALUES (4, 2);
INSERT INTO user_authority (user_id, authority_id) VALUES (4, 3);

INSERT INTO feature (id, name) VALUES (1, 'feature-1');
INSERT INTO feature (id, name) VALUES (2, 'feature-2');
INSERT INTO feature (id, name) VALUES (3, 'feature-3');
INSERT INTO feature (id, name) VALUES (4, 'feature-4');