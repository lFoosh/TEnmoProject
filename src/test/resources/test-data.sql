BEGIN TRANSACTION;
DROP TABLE IF EXISTS tenmo_user, account, transfers;
DROP SEQUENCE IF EXISTS seq_user_id, seq_account_id, seq_transfer_id;
-- Sequence to start user_id values at 1001 instead of 1
CREATE SEQUENCE seq_user_id
  INCREMENT BY 1
  START WITH 1001
  NO MAXVALUE;
CREATE TABLE tenmo_user (
	user_id int NOT NULL DEFAULT nextval('seq_user_id'),
	username varchar(50) NOT NULL,
	password_hash varchar(200) NOT NULL,
	CONSTRAINT PK_tenmo_user PRIMARY KEY (user_id),
	CONSTRAINT UQ_username UNIQUE (username)
);
-- Sequence to start account_id values at 2001 instead of 1
-- Note: Use similar sequences with unique starting values for additional tables
CREATE SEQUENCE seq_account_id
  INCREMENT BY 1
  START WITH 2001
  NO MAXVALUE;
CREATE TABLE account (
	account_id int NOT NULL DEFAULT nextval('seq_account_id'),
	user_id int NOT NULL,
	balance numeric(13, 2) NOT NULL,
	CONSTRAINT PK_account PRIMARY KEY (account_id),
	CONSTRAINT FK_account_tenmo_user FOREIGN KEY (user_id) REFERENCES tenmo_user (user_id)
);
CREATE SEQUENCE seq_transfer_id
  INCREMENT BY 1
  START WITH 3001
  NO MAXVALUE;
CREATE TABLE transfers (
	transfer_id int NOT NULL DEFAULT nextval('seq_transfer_id'),
	sender_id int NOT NULL,
	receiver_id int NOT NULL,
	amount numeric(6, 2) NOT NULL,
	transfer_status varchar (15) NOT NULL,
	CONSTRAINT PK_transfers PRIMARY KEY (transfer_id),
	CONSTRAINT FK_transfers_sender_id FOREIGN KEY (sender_id) REFERENCES account (account_id),
	CONSTRAINT FK_transfers_receiver_id FOREIGN KEY (receiver_id) REFERENCES account (account_id)
);
INSERT INTO tenmo_user (username, password_hash)
VALUES ('bob', '$2a$10$G/MIQ7pUYupiVi72DxqHquxl73zfd7ZLNBoB2G6zUb.W16imI2.W2'),
       ('user', '$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy')
	   RETURNING user_id, username;

INSERT INTO account (user_id, balance)
VALUES
    (currval('seq_user_id'), 1000.00),
    (currval('seq_user_id'), 1000.00);

UPDATE account
SET user_id = 1001
WHERE account_id = 2001;
INSERT INTO transfers (transfer_id, sender_id, receiver_id, amount, transfer_status)
VALUES
    (nextval('seq_transfer_id'), 2001, 2002, 100.00, 'Approved');
COMMIT;