create table task (
    id bigserial not null,
    title varchar(255),
    description varchar(2024),
    delete_status boolean default false,
    task_status_id int8,
    author_id int8,
    responsible_id int8,
    due_date timestamp,
    created_at timestamp,
    updated_at timestamp,
    primary key (id)
)
