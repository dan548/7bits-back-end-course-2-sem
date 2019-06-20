INSERT INTO users (username, password, enabled) VALUES ('anonymous', '$2a$10$lFRIscw5w9lrQfi108IcteybU8bCreFnU9UeEly2Dnqcbpoxgq4uy', TRUE);
INSERT INTO users (username, password, enabled) VALUES ('admin', '$2a$10$dqWkvL0aUAwyg.C46g/WyeUebQuADUkCfEZXH6lsMpYkbcnW5NHHa', TRUE);
INSERT INTO authorities (username, authority) VALUES ('anonymous', 'USER');
INSERT INTO authorities (username, authority) VALUES ('admin', 'USER');
INSERT INTO authorities (username, authority) VALUES ('admin', 'ADMIN');