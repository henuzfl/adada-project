DROP TABLE IF EXISTS event_messages;
CREATE TABLE event_messages (
    id bigint(20) UNSIGNED NOT NULL AUTO_INCREMENT,
    body text NOT NULL,
    event_aggregate_id varchar(255) NOT NULL,
    event_aggregate_type varchar(255) NOT NULL,
    event_class_name varchar(255) NOT NULL,
    msg_id varchar(255) NOT NULL,
    published tinyint(1) UNSIGNED NOT NULL DEFAULT 0,
    PRIMARY KEY (id)
);
