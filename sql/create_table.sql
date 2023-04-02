-- 创建数据库 fly_msg
create database fly_msg;

-- 使用数据库
use fly_msg;

-- 创建用户信息表

create table userInfo
(
    id           bigint auto_increment comment 'id'
        primary key,
    userAccount  varchar(256)                       not null comment '账号',
    userPassword varchar(512)                       not null comment '密码',
    userName     varchar(256)                       null comment '用户昵称',
    userAvatar   varchar(1024)                      null comment '用户头像',
    userProfile  varchar(512)                       null comment '用户简介',
    userRole     int      default 0                 not null comment '用户角色：0 - user, 1 - admin',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除 0 - 否， 1 - 是'
)
    comment '用户信息表' collate = utf8mb4_unicode_ci;


-- 用户登录信息表
create table userLoginInfo
(
    id           bigint auto_increment comment 'id'
        primary key,
    userId       bigint                             null comment '用户id',
    userAccount  varchar(256)                       not null comment '账号',
    userPassword varchar(512)                       not null comment '密码',
    createTime   datetime default CURRENT_TIMESTAMP not null comment '创建时间',
    updateTime   datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',
    isDelete     tinyint  default 0                 not null comment '是否删除 0 - 否， 1 - 是'
)
    comment '用户登录信息表' collate = utf8mb4_unicode_ci;