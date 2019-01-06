/*
Navicat MySQL Data Transfer

Source Server         : ssm
Source Server Version : 50519
Source Host           : localhost:3306
Source Database       : contentsales

Target Server Type    : MYSQL
Target Server Version : 50519
File Encoding         : 65001

Date: 2019-01-06 21:45:18
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `account`
-- ----------------------------
DROP TABLE IF EXISTS `account`;
CREATE TABLE `account` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `contentId` int(11) DEFAULT NULL COMMENT '内容Id',
  `title` varchar(255) DEFAULT NULL COMMENT '标题',
  `image` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `price` int(20) DEFAULT NULL COMMENT '购买时的价格（单位：分）',
  `num` int(11) DEFAULT NULL COMMENT '购买时的数量',
  `time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '购买时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of account
-- ----------------------------
INSERT INTO `account` VALUES ('8', '1', 'SICP', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1138464224,1601768176&fm=26&gp=0.jpg', '9988', '2', '2019-01-06 21:43:12');

-- ----------------------------
-- Table structure for `content`
-- ----------------------------
DROP TABLE IF EXISTS `content`;
CREATE TABLE `content` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键Id',
  `title` varchar(150) DEFAULT NULL COMMENT '标题',
  `summary` varchar(200) DEFAULT NULL COMMENT '摘要',
  `image` varchar(255) DEFAULT NULL COMMENT '图片地址',
  `price` bigint(20) DEFAULT NULL COMMENT '单价(单位：分)',
  `text` varchar(1500) DEFAULT NULL COMMENT '内容',
  `num` int(11) DEFAULT '0' COMMENT '卖出数量',
  `valid` int(2) DEFAULT '1' COMMENT '是否有效（1：有效，0：无效）',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of content
-- ----------------------------
INSERT INTO `content` VALUES ('1', 'SICP', '测试书籍', 'https://ss0.bdstatic.com/70cFvHSh_Q1YnxGkpoWK1HF6hhy/it/u=1138464224,1601768176&fm=26&gp=0.jpg', '12088', '这是一本非常非常好的书籍', '2', '1');
INSERT INTO `content` VALUES ('2', '标题', '摘要', 'D:/contentsales/src/main/resources/images/Penguins.jpg', '2932', '正文', '0', '0');
INSERT INTO `content` VALUES ('3', '黑猪', '非常有营养的黑猪', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546530313216&di=eb3280be3ce8bbb5f0e79551baf0bc20&imgtype=0&src=http%3A%2F%2Fpic.90sjimg.com%2Fdesign%2F00%2F79%2F50%2F59%2F58db72685ef1f.png', '99999', '非常可爱讨人喜欢的黑猪', '0', '1');
INSERT INTO `content` VALUES ('4', 'Effective Java', 'Java程序员入门书籍', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546782333961&di=77769c3c0e6e61d4c5c30fa07873d35a&imgtype=0&src=http%3A%2F%2Fimages.cnblogs.com%2Fcnblogs_com%2F0xcafebabe%2F885796%2Fo_s3479802.jpg', '9999', '这是一本非常适合Java初级程序员的入门书籍。', '0', '1');
INSERT INTO `content` VALUES ('5', 'Java编程思想', 'Java程序员的必备清单', 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546782533031&di=05bce000a32cabc6f0c5987977502a77&imgtype=0&src=http%3A%2F%2Fimg.alicdn.com%2Fimgextra%2Fi4%2F2265273009%2FTB2GwNvcXXXXXaPXpXXXXXXXXXX_%2521%25212265273009.jpg_728x728.jpg', '19999', '让你更加清楚的了解什么是面向对象。', '0', '1');
