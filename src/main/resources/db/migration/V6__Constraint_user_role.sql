-- alter table if exists user_role
--     add constraint user_role_user_fk
--         foreign key (user_id) references usr;
-- alter table if exists usr
--     add constraint usr_user_role_fk
--         foreign key (user_role_id) references user_role;

alter table if exists user_roles
    add constraint FK_user_roles_roles
        foreign key (role_id) references roles;

alter table if exists user_roles
    add constraint FK_user_roles_users
        foreign key (user_id) references users;