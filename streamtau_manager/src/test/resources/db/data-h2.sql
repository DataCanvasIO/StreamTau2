insert into project select * from csvread('classpath:/db/data/project.csv');

insert into user_project select * from csvread('classpath:/db/data/user_project.csv');

insert into asset select * from csvread('classpath:/db/data/asset.csv');

insert into project_asset select * from csvread('classpath:/db/data/project_asset.csv');

insert into job select * from csvread('classpath:/db/data/job.csv');
