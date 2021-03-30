/**
  创建数据库
 */
CREATE DATABASE IF NOT EXISTS `cache_demo`
    DEFAULT CHARACTER SET utf8mb4
    DEFAULT COLLATE utf8mb4_general_ci;

/**
  创建 employee 表
 */
DROP TABLE IF EXISTS `employee`;
create table employee
(
    id        int auto_increment comment '自增主键ID',
    name      varchar(50)  not null comment '员工姓名',
    emp_no    varchar(50)  not null comment '员工编号',
    email     varchar(100) null comment '员工邮箱',
    phone     varchar(20)  null comment '员工联系电话',
    create_at datetime     not null comment '创建时间',
    update_at datetime     not null comment '更新时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `id_UNIQUE` (`id`)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8mb4 comment '员工表';




