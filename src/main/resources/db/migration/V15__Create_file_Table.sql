create table files (
    id bigserial not null,
    name varchar(255),
    uuid varchar(2024),
    download varchar(2024),
    path varchar(2024),
    size int8,
    format varchar(255),
    comment_id int8,
    task_id int8,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
)