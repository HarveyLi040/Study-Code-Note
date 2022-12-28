USE mp;

-- 创建测试表
-- ----------------------------
-- Table structure for tb_user
-- ----------------------------
DROP TABLE IF EXISTS `tb_user`;
CREATE TABLE `tb_user` (
                           `id` BIGINT(20) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '主键',
                           `user_name` VARCHAR(20) NOT NULL COMMENT '用户名',
                           `password` VARCHAR(20) NOT NULL COMMENT '密码',
                           `name` VARCHAR(30) DEFAULT NULL COMMENT '姓名',
                           `age` INT(11) DEFAULT NULL COMMENT '年龄',
                           `email` VARCHAR(50) DEFAULT NULL COMMENT '邮箱',
                           PRIMARY KEY (`id`)
) ENGINE = INNODB AUTO_INCREMENT = 1 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = COMPACT;
-- 插入测试数据
INSERT INTO `tb_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES
    ('1', 'zhangsan', '123456', '张三', '18', 'test1@itcast.cn');
INSERT INTO `tb_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES
    ('2', 'lisi', '123456', '李四', '20', 'test2@itcast.cn');
INSERT INTO `tb_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES
    ('3', 'wangwu', '123456', '王五', '28', 'test3@itcast.cn');
INSERT INTO `tb_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES
    ('4', 'zhaoliu', '123456', '赵六', '21', 'test4@itcast.cn');
INSERT INTO `tb_user` (`id`, `user_name`, `password`, `name`, `age`, `email`) VALUES
    ('5', 'sunqi', '123456', '孙七', '24', 'test5@itcast.cn');