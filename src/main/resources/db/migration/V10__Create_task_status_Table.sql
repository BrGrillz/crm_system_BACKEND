create table task_status (
    id bigserial not null,
    name varchar(255),
    status varchar(255),
    primary key (id)
)