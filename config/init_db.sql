create table resume
(
    uuid      varchar(36) primary key,
    full_name text not null
);

create table contact
(
    id          serial,
    resume_uuid varchar(36) not null references resume (uuid) on delete cascade,
    type        text        not null,
    value       text        not null
);

create unique index contact_uuid_type_index on contact (resume_uuid, type);