drop table if exists job;
drop table if exists project_asset;
drop table if exists asset;
drop table if exists user_project;
drop table if exists project;

create table project (
    project_id long primary key auto_increment,
    project_name char(255) not null,
    project_description varchar(512),
    project_type char(63) not null
    );

create table user_project (
    user_id char(63) not null,
    project_id long not null,
    user_project_id char(36) not null,
    primary key (user_id, project_id),
    foreign key (project_id) references project(project_id) on delete cascade
    );

create table asset (
    asset_id long primary key auto_increment,
    asset_name char(255) not null,
    asset_description varchar(512),
    asset_type char(63) not null,
    asset_category char(63) not null,
    script_format char(31) not null,
    script clob
    );

create table project_asset (
    project_id long not null,
    asset_id long not null,
    project_asset_id char(36) not null,
    primary key (project_id, asset_id),
    foreign key (project_id) references project(project_id) on delete cascade,
    foreign key (asset_id) references asset(asset_id) on delete cascade
    );

create table job (
    job_id long primary key auto_increment,
    job_name char(255) not null,
    project_id long not null,
    app_id char(36) not null,
    app_type char(63) not null,
    version int not null,
    job_definition clob,
    job_status char(15) not null,
    foreign key (project_id) references project(project_id) on delete cascade
    );
