create table comment (
    id bigserial not null,
    text varchar(255),
    user_id int8 not null,
    task_id int8,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
)