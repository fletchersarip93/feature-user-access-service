INSERT INTO authority (id, authority) VALUES (1, 'PRODUCT_MANAGER');
INSERT INTO authority (id, authority) VALUES (2, 'USER');
INSERT INTO authority (id, authority) VALUES (3, 'DEVELOPER');

INSERT INTO user (id, email, password, enabled, expired, account_expired, credentials_expired, locked) VALUES (1, 'fletchersarip@gmail.com', '$2y$11$WBktA7QiZhtmmLJ.Y2BJDu1mfCX4CB4.6IiMozbGyoCv60cdNJLiu', TRUE, FALSE, FALSE, FALSE, FALSE);
INSERT INTO user (id, email, password, enabled, expired, account_expired, credentials_expired, locked) VALUES (2, 'jack@gmail.com', '$2y$11$WBktA7QiZhtmmLJ.Y2BJDu1mfCX4CB4.6IiMozbGyoCv60cdNJLiu', TRUE, FALSE, FALSE, FALSE, FALSE);
INSERT INTO user (id, email, password, enabled, expired, account_expired, credentials_expired, locked) VALUES (3, 'daniel@gmail.com', '$2y$11$WBktA7QiZhtmmLJ.Y2BJDu1mfCX4CB4.6IiMozbGyoCv60cdNJLiu', TRUE, FALSE, FALSE, FALSE, FALSE);
INSERT INTO user (id, email, password, enabled, expired, account_expired, credentials_expired, locked) VALUES (4, 'ben@gmail.com', '$2y$11$WBktA7QiZhtmmLJ.Y2BJDu1mfCX4CB4.6IiMozbGyoCv60cdNJLiu', TRUE, FALSE, FALSE, FALSE, FALSE);

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