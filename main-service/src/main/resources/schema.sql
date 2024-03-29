DROP SEQUENCE IF EXISTS category_sequence;
DROP SEQUENCE IF EXISTS user_sequence;
DROP SEQUENCE IF EXISTS event_sequence;
DROP SEQUENCE IF EXISTS location_sequence;
DROP SEQUENCE IF EXISTS request_sequence;
DROP SEQUENCE IF EXISTS compilation_sequence;
DROP SEQUENCE IF EXISTS comment_sequence;

DROP TABLE IF EXISTS category CASCADE;
DROP TABLE IF EXISTS users CASCADE;
DROP TABLE IF EXISTS events CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS request CASCADE;
DROP TABLE IF EXISTS compilation CASCADE;
DROP TABLE IF EXISTS compilation_event CASCADE;
DROP TABLE IF EXISTS comment CASCADE;

CREATE SEQUENCE IF NOT EXISTS category_sequence
    START WITH 1
    INCREMENT BY 1
    CACHE 25;

CREATE SEQUENCE IF NOT EXISTS user_sequence
    START WITH 1
    INCREMENT BY 1
    CACHE 25;

CREATE SEQUENCE IF NOT EXISTS event_sequence
    START WITH 1
    INCREMENT BY 1
    CACHE 25;

CREATE SEQUENCE IF NOT EXISTS location_sequence
    START WITH 1
    INCREMENT BY 1
    CACHE 25;

CREATE SEQUENCE IF NOT EXISTS request_sequence
    START WITH 1
    INCREMENT BY 1
    CACHE 25;

CREATE SEQUENCE IF NOT EXISTS compilation_sequence
    START WITH 1
    INCREMENT BY 1
    CACHE 25;

CREATE SEQUENCE IF NOT EXISTS comment_sequence
    START WITH 1
    INCREMENT BY 1
    CACHE 25;

CREATE TABLE IF NOT EXISTS users(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    CONSTRAINT UQ_USERS_EMAIL UNIQUE (email)
);

CREATE TABLE IF NOT EXISTS category(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    name VARCHAR(50) NOT NULL,
    CONSTRAINT UQ_CATEGORY_NAME UNIQUE (name)
);

CREATE TABLE IF NOT EXISTS location(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    lat FLOAT NOT NULL,
    lon FLOAT NOT NULL
);

CREATE TABLE IF NOT EXISTS events(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    annotation VARCHAR(2050) NOT NULL,
    category_id BIGINT NOT NULL,
    confirmed_req_num BIGINT,
    created_on VARCHAR(255),
    description VARCHAR(7050),
    event_date VARCHAR(255) NOT NULL,
    initiator_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    paid BOOLEAN,
    participant_lim BIGINT,
    published_on VARCHAR(255),
    req_mod_is_required BOOLEAN,
    state VARCHAR(255),
    title VARCHAR(255) NOT NULL,
    views BIGINT,
    CONSTRAINT fk_events_to_category FOREIGN KEY (category_id) REFERENCES category (id),
    CONSTRAINT fk_events_to_users FOREIGN KEY (initiator_id) REFERENCES users (id),
    CONSTRAINT fk_events_to_location FOREIGN KEY (location_id) REFERENCES location (id)
);

CREATE TABLE IF NOT EXISTS request(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    created_on VARCHAR(255),
    event_id BIGINT NOT NULL,
    requester_id BIGINT NOT NULL,
    status VARCHAR(255),
    CONSTRAINT fk_request_to_events FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_request_to_users FOREIGN KEY (requester_id) REFERENCES users (id)
);

CREATE TABLE IF NOT EXISTS compilation(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    pinned BOOLEAN,
    title VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS compilation_event(
    compilation_id BIGINT NOT NULL,
    event_id BIGINT NOT NULL,
    CONSTRAINT fk_comp_and_evnt_to_evnt FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_comp_and_evnt_to_comp FOREIGN KEY (compilation_id) REFERENCES compilation (id)
);

CREATE TABLE IF NOT EXISTS comment(
    id BIGINT GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY NOT NULL,
    text VARCHAR(5000) NOT NULL,
    event_id BIGINT NOT NULL,
    commenter_id BIGINT NOT NULL,
    posted_on VARCHAR(255) NOT NULL,
    CONSTRAINT fk_comments_to_events FOREIGN KEY (event_id) REFERENCES events (id),
    CONSTRAINT fk_comments_to_users FOREIGN KEY (commenter_id) REFERENCES users (id)
);
