
create table `base_sqlset` (
	`id` varchar (255),
	`sqlType` varchar (255),
	`dataSource` varchar (255),
	`statement` blob 
); 
insert into `base_sqlset` (`id`, `sqlType`, `dataSource`, `statement`) values('listNameByCode','select','dataSource','select name from department where code like \'%${code}%\'');
insert into `base_sqlset` (`id`, `sqlType`, `dataSource`, `statement`) values('override','select','dataSource','select * from department where code = :code');

create table `department` (
	`id` bigint (20),
	`code` varchar (255),
	`name` varchar (255)
); 
insert into `department` (`id`, `code`, `name`) values('1','D0001','开发部');
insert into `department` (`id`, `code`, `name`) values('2','D0002','行政部');
