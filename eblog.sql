/*
 Navicat Premium Data Transfer

 Source Server         : 虚拟机mysql
 Source Server Type    : MySQL
 Source Server Version : 80027 (8.0.27)
 Source Host           : 192.168.126.131:3306
 Source Schema         : eblog

 Target Server Type    : MySQL
 Target Server Version : 80027 (8.0.27)
 File Encoding         : 65001

 Date: 09/04/2024 01:09:48
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for article
-- ----------------------------
DROP TABLE IF EXISTS `article`;
CREATE TABLE `article`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户id',
  `nickname` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称（冗余字段）',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户头像（冗余字段）',
  `title` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章标题',
  `summary` varchar(300) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '总结',
  `category_id` bigint NOT NULL COMMENT '分类id',
  `source` tinyint NOT NULL DEFAULT 1 COMMENT '来源，1为原创，0转载',
  `official_stat` tinyint NOT NULL DEFAULT 0 COMMENT '是否官方，1是，0否',
  `top_stat` tinyint NOT NULL DEFAULT 0 COMMENT '是否置顶，1是，0否',
  `status` tinyint NOT NULL DEFAULT 0 COMMENT '是否已发布，1是，0否，-1暂存',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 23 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci COMMENT = '头像（冗余字段）' ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of article
-- ----------------------------
INSERT INTO `article` VALUES (1, 1, '管理员', '', '这是第一篇测试文章', '', 1, 1, 0, 1, 1, 0, '2024-04-01 01:04:20', '2024-04-01 01:04:20');
INSERT INTO `article` VALUES (3, 4, '测试专员', 'http://dummyimage.com/100x100', '这是第二篇测试文章', '', 1, 1, 0, 1, 1, 0, '2024-04-01 01:05:07', '2024-04-01 01:05:07');
INSERT INTO `article` VALUES (5, 4, '测试专员', 'http://dummyimage.com/100x100', '这是第三篇测试文章', '', 1, 1, 0, 0, 1, 0, '2024-04-01 01:06:27', '2024-04-01 01:06:28');
INSERT INTO `article` VALUES (6, 4, '测试专员', 'http://dummyimage.com/100x100', '这是第四篇测试文章', '', 2, 1, 0, 0, 1, 0, '2024-04-01 01:09:27', '2024-04-01 01:09:28');
INSERT INTO `article` VALUES (7, 1, '管理员', '', '这是第五篇测试文章', '', 1, 1, 0, 0, 1, 0, '2024-04-01 01:16:27', '2024-04-01 01:16:28');
INSERT INTO `article` VALUES (8, 4, '测试专员', 'http://dummyimage.com/100x100', '这是一篇测试暂存的文章', '这是一篇测试暂存的文章', 1, 1, 0, 0, -1, 0, '2024-04-02 16:41:37', '2024-04-02 16:41:37');
INSERT INTO `article` VALUES (12, 4, '测试专员', 'http://dummyimage.com/100x100', '这是第二篇测试暂存的文章', '这是第二篇测试暂存的文章', 1, 1, 0, 0, -1, 0, '2024-04-02 16:52:29', '2024-04-02 16:52:29');
INSERT INTO `article` VALUES (15, 1, '管理员', '', '需习集产技联长', 'dolor', 3, 1, 1, 0, 1, 0, '2024-04-03 17:57:30', '2024-04-03 17:57:30');
INSERT INTO `article` VALUES (19, 1, '管理员', '', '农再主铁便', 'laborum elit enim eu', 1, 1, 1, 0, 1, 0, '2024-04-03 18:47:58', '2024-04-03 18:47:58');
INSERT INTO `article` VALUES (20, 4, '测试专员', 'http://dummyimage.com/100x100', '测试分布式事务', '测试分布式事务', 1, 1, 0, 0, 1, 0, '2024-04-05 23:42:45', '2024-04-05 23:42:45');
INSERT INTO `article` VALUES (22, 108, 'TomTom', 'http://dummyimage.com/100x100', '猫和老鼠', 'laboris cupidatat cillum est', 1, 0, 0, 0, 1, 0, '2024-04-06 16:23:24', '2024-04-06 16:23:24');

-- ----------------------------
-- Table structure for article_detail
-- ----------------------------
DROP TABLE IF EXISTS `article_detail`;
CREATE TABLE `article_detail`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `article_id` bigint NOT NULL COMMENT '文章id',
  `version` bigint NOT NULL DEFAULT 0 COMMENT '版本',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '文章内容',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `article_id`(`article_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 21 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of article_detail
-- ----------------------------
INSERT INTO `article_detail` VALUES (1, 1, 0, '这是第一篇文章的正文', 0, '2024-04-01 16:22:37', '2024-04-01 16:22:37');
INSERT INTO `article_detail` VALUES (2, 3, 0, '这是第二篇文章的正文', 0, '2024-04-01 16:28:15', '2024-04-01 16:28:15');
INSERT INTO `article_detail` VALUES (3, 5, 0, '这是第三篇文章的正文', 0, '2024-04-01 16:28:36', '2024-04-01 16:28:36');
INSERT INTO `article_detail` VALUES (4, 6, 0, '这是第四篇文章的正文', 0, '2024-04-01 16:28:55', '2024-04-01 16:28:55');
INSERT INTO `article_detail` VALUES (5, 7, 0, '这是第五篇文章的正文', 0, '2024-04-01 16:29:16', '2024-04-01 16:29:16');
INSERT INTO `article_detail` VALUES (6, 8, 0, 'in Ut magna quis Excepteur', 0, '2024-04-02 16:41:37', '2024-04-02 16:41:37');
INSERT INTO `article_detail` VALUES (10, 12, 0, 'qrt Ut dahte hfn Edspttre', 0, '2024-04-02 16:52:29', '2024-04-02 16:52:29');
INSERT INTO `article_detail` VALUES (13, 15, 0, 'do cillum ut aliqua incididunt', 0, '2024-04-03 17:57:30', '2024-04-03 17:57:30');
INSERT INTO `article_detail` VALUES (17, 19, 0, 'laboris dolor sunt sint', 0, '2024-04-03 18:47:58', '2024-04-03 18:47:58');
INSERT INTO `article_detail` VALUES (18, 20, 0, 'ex incididunt est ipsum dolor', 0, '2024-04-05 23:42:46', '2024-04-05 23:42:46');
INSERT INTO `article_detail` VALUES (20, 22, 11, 'et Ut culpa veniam ut', 0, '2024-04-06 16:23:24', '2024-04-06 16:23:24');

-- ----------------------------
-- Table structure for article_tag
-- ----------------------------
DROP TABLE IF EXISTS `article_tag`;
CREATE TABLE `article_tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `article_id` bigint NOT NULL COMMENT '文章id',
  `tag_id` bigint NOT NULL COMMENT '标签id',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  `tag_name` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT 'tag名称',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 65 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of article_tag
-- ----------------------------
INSERT INTO `article_tag` VALUES (1, 1, 1, 0, '2024-04-01 02:45:06', '2024-04-01 02:45:06', 'Java');
INSERT INTO `article_tag` VALUES (3, 1, 2, 0, '2024-04-01 02:45:23', '2024-04-01 02:45:23', 'Python');
INSERT INTO `article_tag` VALUES (4, 3, 1, 0, '2024-04-01 02:45:52', '2024-04-01 02:45:52', 'Java');
INSERT INTO `article_tag` VALUES (5, 5, 3, 0, '2024-04-01 02:46:05', '2024-04-01 02:46:05', 'C++');
INSERT INTO `article_tag` VALUES (6, 3, 3, 0, '2024-04-01 02:46:16', '2024-04-01 02:46:16', 'C++');
INSERT INTO `article_tag` VALUES (7, 6, 2, 0, '2024-04-01 02:46:40', '2024-04-01 02:46:40', 'Python');
INSERT INTO `article_tag` VALUES (8, 7, 1, 0, '2024-04-01 02:46:47', '2024-04-01 02:46:47', 'Java');
INSERT INTO `article_tag` VALUES (9, 8, 1, 0, '2024-04-02 16:41:38', '2024-04-02 16:41:38', 'Java');
INSERT INTO `article_tag` VALUES (10, 8, 3, 0, '2024-04-02 16:41:38', '2024-04-02 16:41:38', 'C++');
INSERT INTO `article_tag` VALUES (11, 8, 2, 0, '2024-04-02 16:41:38', '2024-04-02 16:41:38', 'Python');
INSERT INTO `article_tag` VALUES (12, 12, 1, 0, '2024-04-02 16:52:29', '2024-04-02 16:52:29', 'Java');
INSERT INTO `article_tag` VALUES (13, 12, 3, 0, '2024-04-02 16:52:29', '2024-04-02 16:52:29', 'C++');
INSERT INTO `article_tag` VALUES (14, 12, 2, 0, '2024-04-02 16:52:29', '2024-04-02 16:52:29', 'Python');
INSERT INTO `article_tag` VALUES (21, 15, 1, 0, '2024-04-03 17:57:30', '2024-04-03 17:57:30', 'Java');
INSERT INTO `article_tag` VALUES (22, 15, 2, 0, '2024-04-03 17:57:30', '2024-04-03 17:57:30', 'Python');
INSERT INTO `article_tag` VALUES (23, 15, 3, 0, '2024-04-03 17:57:30', '2024-04-03 17:57:30', 'C++');
INSERT INTO `article_tag` VALUES (24, 19, 1, 0, '2024-04-03 18:47:58', '2024-04-03 18:47:58', 'Java');
INSERT INTO `article_tag` VALUES (25, 19, 2, 0, '2024-04-03 18:47:58', '2024-04-03 18:47:58', 'Python');
INSERT INTO `article_tag` VALUES (26, 19, 3, 0, '2024-04-03 18:47:58', '2024-04-03 18:47:58', 'C++');
INSERT INTO `article_tag` VALUES (27, 20, 1, 0, '2024-04-05 23:42:46', '2024-04-05 23:42:46', 'Java');
INSERT INTO `article_tag` VALUES (62, 22, 2, 0, '2024-04-07 23:08:10', '2024-04-07 23:08:10', 'Python');
INSERT INTO `article_tag` VALUES (63, 22, 3, 0, '2024-04-07 23:08:10', '2024-04-07 23:08:10', 'C++');
INSERT INTO `article_tag` VALUES (64, 22, 1, 0, '2024-04-07 23:08:10', '2024-04-07 23:08:10', 'Java');

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '类名',
  `status` tinyint NULL DEFAULT 0 COMMENT '是否已发布，1是，0否',
  `sort` tinyint NOT NULL DEFAULT 0 COMMENT '排序',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `category_name`(`category_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 4 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES (1, '后端', 1, 0, 0, '2024-03-31 23:56:08', '2024-03-31 23:56:08');
INSERT INTO `category` VALUES (2, '前端', 1, 0, 0, '2024-03-31 23:56:36', '2024-03-31 23:56:36');
INSERT INTO `category` VALUES (3, '人工智能', 1, 0, 0, '2024-03-31 23:57:33', '2024-03-31 23:57:33');

-- ----------------------------
-- Table structure for comment
-- ----------------------------
DROP TABLE IF EXISTS `comment`;
CREATE TABLE `comment`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `article_id` bigint NOT NULL COMMENT '文章id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `nickname` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户昵称（冗余字段）',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '用户头像（冗余字段）',
  `root_comment_id` bigint NOT NULL DEFAULT 0 COMMENT '根评论id',
  `parent_comment_id` bigint NOT NULL DEFAULT 0 COMMENT '父评论id',
  `content` longtext CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '评论内容',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 52 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of comment
-- ----------------------------
INSERT INTO `comment` VALUES (1, 20, 4, '测试专员', 'http://dummyimage.com/100x100', 0, 0, '评论1', 0, '2024-04-05 23:45:45', '2024-04-05 23:45:45');
INSERT INTO `comment` VALUES (32, 20, 4, '测试专员', 'http://dummyimage.com/100x100', 0, 0, '评论22', 0, '2024-04-05 23:47:32', '2024-04-05 23:47:32');
INSERT INTO `comment` VALUES (33, 20, 4, '测试专员', 'http://dummyimage.com/100x100', 0, 0, '评论22', 0, '2024-04-05 23:47:33', '2024-04-05 23:47:33');
INSERT INTO `comment` VALUES (34, 20, 4, '测试专员', 'http://dummyimage.com/100x100', 1, 0, '评论22', 0, '2024-04-05 23:47:38', '2024-04-05 23:47:38');
INSERT INTO `comment` VALUES (35, 20, 4, '测试专员', 'http://dummyimage.com/100x100', 1, 34, '评论22', 0, '2024-04-05 23:48:01', '2024-04-05 23:48:01');
INSERT INTO `comment` VALUES (36, 22, 108, 'TomTom', 'http://dummyimage.com/100x100', 0, 0, 'in et', 0, '2024-04-06 16:24:59', '2024-04-06 16:24:59');
INSERT INTO `comment` VALUES (37, 22, 108, 'TomTom', 'http://dummyimage.com/100x100', 0, 0, 'in et', 0, '2024-04-06 16:25:00', '2024-04-06 16:25:00');
INSERT INTO `comment` VALUES (38, 22, 108, 'TomTom', 'http://dummyimage.com/100x100', 0, 0, 'in et', 0, '2024-04-06 16:25:01', '2024-04-06 16:25:01');
INSERT INTO `comment` VALUES (39, 22, 108, 'TomTom', 'http://dummyimage.com/100x100', 0, 0, 'in et', 0, '2024-04-06 16:25:01', '2024-04-06 16:25:01');
INSERT INTO `comment` VALUES (40, 22, 108, 'TomTom', 'http://dummyimage.com/100x100', 0, 0, 'gdfdfgdfhgfj sunt reprehenderit', 0, '2024-04-06 16:25:02', '2024-04-06 16:25:02');
INSERT INTO `comment` VALUES (41, 22, 108, 'TomTom', 'http://dummyimage.com/100x100', 40, 0, 'gdfdfgdfhgfj sunt reprehenderit', 0, '2024-04-06 16:25:30', '2024-04-06 16:25:30');
INSERT INTO `comment` VALUES (42, 22, 108, 'TomTom', 'http://dummyimage.com/100x100', 40, 0, 'agf', 0, '2024-04-06 16:25:30', '2024-04-06 16:25:30');
INSERT INTO `comment` VALUES (43, 22, 108, 'TomTom', 'http://dummyimage.com/100x100', 40, 0, 'agf', 0, '2024-04-06 16:25:31', '2024-04-06 16:25:31');
INSERT INTO `comment` VALUES (44, 22, 108, 'TomTom', 'http://dummyimage.com/100x100', 40, 41, 'gdfdfgdfhgfj sunt reprehenderit', 0, '2024-04-06 16:25:34', '2024-04-06 16:25:34');
INSERT INTO `comment` VALUES (45, 22, 108, 'TomTom', 'http://dummyimage.com/100x100', 40, 44, 'agf', 0, '2024-04-06 16:25:38', '2024-04-06 16:25:38');
INSERT INTO `comment` VALUES (46, 22, 108, 'TomTom', 'http://dummyimage.com/100x100', 40, 44, 'agf', 1, '2024-04-06 16:25:39', '2024-04-06 16:25:39');
INSERT INTO `comment` VALUES (47, 22, 108, 'TomTom', 'http://dummyimage.com/100x100', 0, 0, 'in aliqua et Excepteur', 0, '2024-04-07 19:19:05', '2024-04-07 19:19:05');
INSERT INTO `comment` VALUES (48, 22, 108, 'TomTom', 'http://dummyimage.com/100x100', 0, 0, 'in aliqua et Excepteur', 0, '2024-04-07 19:19:06', '2024-04-07 19:19:06');

-- ----------------------------
-- Table structure for like
-- ----------------------------
DROP TABLE IF EXISTS `like`;
CREATE TABLE `like`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL COMMENT '用户id',
  `article_id` bigint NOT NULL COMMENT '文章id',
  `status` tinyint NOT NULL DEFAULT 1 COMMENT '点赞状态，1为点赞，-1为取消点赞',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `like_pk`(`article_id` ASC, `user_id` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 8 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci COMMENT = '点赞表' ROW_FORMAT = Dynamic;

-- ----------------------------
-- Records of like
-- ----------------------------
INSERT INTO `like` VALUES (5, 1, 1, 1, '2024-04-08 08:40:50', '2024-04-08 08:40:50');
INSERT INTO `like` VALUES (7, 108, 22, 1, '2024-04-08 21:25:58', '2024-04-08 21:25:58');

-- ----------------------------
-- Table structure for tag
-- ----------------------------
DROP TABLE IF EXISTS `tag`;
CREATE TABLE `tag`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `tag_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '标签名',
  `status` tinyint NULL DEFAULT 0 COMMENT '是否已发布，1是，0否',
  `sort` tinyint NOT NULL DEFAULT 0 COMMENT '排序',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否已删除',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `tag_name`(`tag_name` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 5 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of tag
-- ----------------------------
INSERT INTO `tag` VALUES (1, 'Java', 1, 0, 0, '2024-04-01 00:26:29', '2024-04-01 00:26:29');
INSERT INTO `tag` VALUES (2, 'Python', 1, 0, 0, '2024-04-01 00:26:44', '2024-04-01 00:26:44');
INSERT INTO `tag` VALUES (3, 'C++', 1, 0, 0, '2024-04-01 00:27:03', '2024-04-01 00:27:03');
INSERT INTO `tag` VALUES (4, '算法', 0, 0, 0, '2024-04-01 00:27:17', '2024-04-01 00:27:17');

-- ----------------------------
-- Table structure for undo_log
-- ----------------------------
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE `undo_log`  (
  `branch_id` bigint NOT NULL COMMENT '分支事务ID',
  `xid` varchar(100) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '全局事务ID',
  `context` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '上下文',
  `rollback_info` longblob NOT NULL COMMENT '回滚信息',
  `log_status` int NOT NULL COMMENT '状态，0正常，1全局已完成',
  `log_created` datetime(6) NOT NULL COMMENT '创建时间',
  `log_modified` datetime(6) NOT NULL COMMENT '修改时间',
  UNIQUE INDEX `ux_undo_log`(`xid` ASC, `branch_id` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_general_ci COMMENT = 'AT transaction mode undo table' ROW_FORMAT = COMPACT;

-- ----------------------------
-- Records of undo_log
-- ----------------------------

-- ----------------------------
-- Table structure for user
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`  (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `username` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '用户名',
  `password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '密码',
  `create_time` datetime NOT NULL COMMENT '创建时间',
  `update_time` datetime NOT NULL COMMENT '更新时间',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '是否删除',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `username`(`username` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 114 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES (1, 'admin', 'e26affbfa9692165d5df6958975ad29d', '2024-03-30 15:10:11', '2024-03-30 15:10:51', 0);
INSERT INTO `user` VALUES (4, 'test', 'e26affbfa9692165d5df6958975ad29d', '2024-03-30 15:11:17', '2024-04-06 16:16:47', 0);
INSERT INTO `user` VALUES (5, 'test1', 'e26affbfa9692165d5df6958975ad29d', '2024-03-30 15:32:02', '2024-03-30 15:32:02', 0);
INSERT INTO `user` VALUES (6, 'test2', 'e26affbfa9692165d5df6958975ad29d', '2024-03-30 15:56:42', '2024-03-30 15:56:42', 0);
INSERT INTO `user` VALUES (7, 'zhangsan', 'e26affbfa9692165d5df6958975ad29d', '2024-03-30 16:37:47', '2024-03-30 16:37:47', 0);
INSERT INTO `user` VALUES (108, 'tom', 'e26affbfa9692165d5df6958975ad29d', '2024-04-06 16:16:09', '2024-04-06 16:16:09', 0);

-- ----------------------------
-- Table structure for user_info
-- ----------------------------
DROP TABLE IF EXISTS `user_info`;
CREATE TABLE `user_info`  (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `user_id` bigint NOT NULL COMMENT '用户id',
  `nickname` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL COMMENT '昵称',
  `avatar` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '头像',
  `position` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '所在地',
  `role` tinyint NOT NULL DEFAULT 0 COMMENT '1为管理员，0为普通用户',
  `profile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '' COMMENT '简介',
  `deleted` tinyint NOT NULL DEFAULT 0 COMMENT '0未删除，1已删除',
  `create_time` datetime NOT NULL,
  `update_time` datetime NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE INDEX `user_id`(`user_id` ASC) USING BTREE,
  UNIQUE INDEX `nickname`(`nickname` ASC) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 7 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of user_info
-- ----------------------------
INSERT INTO `user_info` VALUES (1, 1, '管理员', '', '', 1, '', 0, '2024-03-30 15:10:11', '2024-03-30 15:10:36');
INSERT INTO `user_info` VALUES (2, 4, '测试专员', 'http://dummyimage.com/100x100', 'occaecat consectetur', 0, 'eu est', 0, '2024-03-30 15:11:17', '2024-03-30 15:11:17');
INSERT INTO `user_info` VALUES (3, 5, '测试员1', '', '', 0, '', 0, '2024-03-30 15:32:02', '2024-03-30 15:32:02');
INSERT INTO `user_info` VALUES (4, 6, '测试员2', '', '', 0, '', 0, '2024-03-30 15:56:42', '2024-03-30 15:56:42');
INSERT INTO `user_info` VALUES (5, 7, '张三', '', '', 0, '', 0, '2024-03-30 16:37:47', '2024-03-30 16:37:47');
INSERT INTO `user_info` VALUES (6, 108, 'TomTom', 'http://dummyimage.com/100x100', '上海', 0, '我叫汤姆，我有一个朋友叫杰瑞。', 0, '2024-04-06 16:16:09', '2024-04-06 16:16:09');

-- ----------------------------
-- View structure for article_detail_view
-- ----------------------------
DROP VIEW IF EXISTS `article_detail_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `article_detail_view` AS select `article_detail`.`id` AS `id`,`article_detail`.`article_id` AS `article_id`,`article`.`user_id` AS `user_id`,`article`.`nickname` AS `nickname`,`article`.`avatar` AS `avatar`,`article`.`title` AS `title`,`article`.`summary` AS `summary`,`article`.`category_id` AS `category_id`,`category`.`category_name` AS `category_name`,`article`.`source` AS `source`,`article`.`official_stat` AS `official_stat`,`article`.`top_stat` AS `top_stat`,`article_detail`.`content` AS `content`,`article_detail`.`create_time` AS `create_time`,`article_detail`.`update_time` AS `update_time`,`article_detail`.`version` AS `version`,`article_detail`.`deleted` AS `deleted`,`article`.`status` AS `status` from ((`article_detail` join `article` on((`article_detail`.`article_id` = `article`.`id`))) join `category` on((`article`.`category_id` = `category`.`id`)));

-- ----------------------------
-- View structure for article_view
-- ----------------------------
DROP VIEW IF EXISTS `article_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `article_view` AS select `article`.`id` AS `id`,`article`.`user_id` AS `user_id`,`article`.`nickname` AS `nickname`,`article`.`title` AS `title`,`article`.`summary` AS `summary`,`article`.`category_id` AS `category_id`,`article`.`source` AS `source`,`article`.`official_stat` AS `official_stat`,`article`.`top_stat` AS `top_stat`,`article`.`status` AS `status`,`article`.`deleted` AS `deleted`,`article`.`create_time` AS `create_time`,`article`.`update_time` AS `update_time`,`category`.`category_name` AS `category_name` from (`article` left join `category` on((`article`.`category_id` = `category`.`id`)));

-- ----------------------------
-- View structure for second_comment_view
-- ----------------------------
DROP VIEW IF EXISTS `second_comment_view`;
CREATE ALGORITHM = UNDEFINED SQL SECURITY DEFINER VIEW `second_comment_view` AS select `t1`.`id` AS `id`,`t1`.`article_id` AS `article_id`,`t1`.`user_id` AS `user_id`,`t1`.`nickname` AS `nickname`,`t1`.`avatar` AS `avatar`,`t1`.`root_comment_id` AS `root_comment_id`,`t1`.`parent_comment_id` AS `parent_comment_id`,`t1`.`content` AS `content`,`t1`.`deleted` AS `deleted`,`t1`.`create_time` AS `create_time`,`t1`.`update_time` AS `update_time`,`t2`.`nickname` AS `target_nickname` from (`comment` `t1` left join `comment` `t2` on((`t1`.`parent_comment_id` = `t2`.`id`))) where (`t1`.`root_comment_id` > 0);

SET FOREIGN_KEY_CHECKS = 1;
