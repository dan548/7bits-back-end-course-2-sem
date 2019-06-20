ALTER TABLE users ADD COLUMN id VARCHAR(256);
ALTER TABLE authorities ADD COLUMN id VARCHAR(256);
UPDATE users SET id = '92e86a9e-d669-41ef-95a0-e5fb3ed60dcc' WHERE username = 'anonymous';
UPDATE users SET id = 'c3019bae-7c5f-4cfe-b492-c01c72ef2673' WHERE username = 'admin';
UPDATE authorities SET id = '92e86a9e-d669-41ef-95a0-e5fb3ed60dcc' WHERE username = 'anonymous';
UPDATE authorities SET id = 'c3019bae-7c5f-4cfe-b492-c01c72ef2673' WHERE username = 'admin';
ALTER TABLE authorities DROP CONSTRAINT authorities_pkey;
ALTER TABLE authorities DROP CONSTRAINT authorities_username_fkey;
ALTER TABLE authorities DROP COLUMN username;
ALTER TABLE users DROP CONSTRAINT users_pkey;
ALTER TABLE users ADD PRIMARY KEY (id);
ALTER TABLE authorities ADD FOREIGN KEY (id) REFERENCES users(id) ON DELETE CASCADE;
ALTER TABLE authorities ADD PRIMARY KEY (id, authority);