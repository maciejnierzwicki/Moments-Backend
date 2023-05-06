CREATE TABLE IF NOT EXISTS Users (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	username TEXT NOT NULL,
	password TEXT NOT NULL,
	email TEXT,
	registration_date DATETIME NOT NULL,
	last_online_date DATETIME NOT NULL,
	profile_picture TEXT,
	verified BOOLEAN DEFAULT FALSE NOT NULL
);

CREATE TABLE IF NOT EXISTS Roles (
	id VARCHAR(45) PRIMARY KEY NOT NULL,
	displayname TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS Users_roles (
  user_id BIGINT NOT NULL,
  role_id VARCHAR(45) NOT NULL,
  FOREIGN KEY (role_id) REFERENCES Roles(id),
  FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Conversations (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	sender_id BIGINT NOT NULL,
	recipient_id BIGINT NOT NULL,
	sent_date DATETIME NOT NULL,
	message TEXT NOT NULL,
	FOREIGN KEY (sender_id) REFERENCES Users(id),
	FOREIGN KEY (recipient_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Posts (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	user_id BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	description TEXT,
	image_location TEXT,
	FOREIGN KEY (user_id) REFERENCES Users(id)
);


CREATE TABLE IF NOT EXISTS Likes (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	post_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	sent_date DATETIME NOT NULL,
	FOREIGN KEY (post_id) REFERENCES Posts(id),
	FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Comments (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	post_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	sent_date DATETIME NOT NULL,
	message TEXT NOT NULL,
	FOREIGN KEY (post_id) REFERENCES Posts(id),
	FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Posts_Likes (
  post_id BIGINT NOT NULL,
  like_id BIGINT NOT NULL,
  FOREIGN KEY (post_id) REFERENCES Posts(id),
  FOREIGN KEY (like_id) REFERENCES Likes(id)
);

CREATE TABLE IF NOT EXISTS Posts_Comments (
  post_id BIGINT NOT NULL,
  comment_id BIGINT NOT NULL,
  FOREIGN KEY (post_id) REFERENCES Posts(id),
  FOREIGN KEY (comment_id) REFERENCES Comments(id)
);

CREATE TABLE IF NOT EXISTS Tracking (
  id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  user_id BIGINT NOT NULL,
  followed_id BIGINT NOT NULL,
  sent_date DATETIME NOT NULL,
  CONSTRAINT IX_TR UNIQUE (user_id, followed_id),
  FOREIGN KEY (user_id) REFERENCES Users(id),
  FOREIGN KEY (followed_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Notifications (
  id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
  user_id BIGINT NOT NULL,
  sent_date DATETIME NOT NULL,
  read BOOLEAN NOT NULL,
  notification_type TEXT NOT NULL,
  post_id BIGINT,
  like_id BIGINT,
  comment_id BIGINT,
  tracking_id BIGINT,
  FOREIGN KEY (post_id) REFERENCES Posts(id),
  FOREIGN KEY (like_id) REFERENCES Likes(id),
  FOREIGN KEY (comment_id) REFERENCES Comments(id),
  FOREIGN KEY (tracking_id) REFERENCES Tracking(id)
);



CREATE TABLE IF NOT EXISTS RefreshTokens (
	id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	user_id BIGINT NOT NULL,
	token TEXT NOT NULL,
	expiry_date TIMESTAMP NOT NULL,
	FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS VerificationCodes (
	id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	code INT NOT NULL,
	user_id BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS PasswordResetCodes (
	id BIGINT PRIMARY KEY NOT NULL AUTO_INCREMENT,
	code INT NOT NULL,
	user_id BIGINT NOT NULL,
	creation_date DATETIME NOT NULL,
	FOREIGN KEY (user_id) REFERENCES Users(id)
);


/*
CREATE TABLE IF NOT EXISTS Likes (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	post_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	sent_date DATETIME NOT NULL,
	FOREIGN KEY (post_id) REFERENCES Posts(id),
	FOREIGN KEY (user_id) REFERENCES Users(id)
);

CREATE TABLE IF NOT EXISTS Comments (
	id BIGINT PRIMARY KEY AUTO_INCREMENT NOT NULL,
	post_id BIGINT NOT NULL,
	user_id BIGINT NOT NULL,
	sent_date DATETIME NOT NULL,
	message TEXT NOT NULL,
	FOREIGN KEY (post_id) REFERENCES Posts(id),
	FOREIGN KEY (user_id) REFERENCES Users(id)
);

*/