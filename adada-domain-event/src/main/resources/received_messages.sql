DROP TABLE IF EXISTS received_messages;
CREATE TABLE received_messages(
    consumer_id varchar(255) NOT NULL,
    msg_id varchar(255) NOT NULL,
    create_time datetime(0) NOT NULL,
    PRIMARY KEY (consumer_id, msg_id)
);
