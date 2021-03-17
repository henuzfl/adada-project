
DROP TABLE IF EXISTS `t_dataset_dir`;
CREATE TABLE `t_dataset_dir`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255) NOT NULL,
  `parent` int(10) NULL DEFAULT NULL,
  `create_time` datetime(0) NOT NULL,
  `update_time` datetime(0) NOT NULL
);

DROP TABLE IF EXISTS `t_dataset_file`;
CREATE TABLE `t_dataset_file`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `name` varchar(255) NOT NULL,
  `dfs_path` varchar(1023) NULL,
  `parent` int(10) NULL DEFAULT NULL,
  `create_time` datetime(0) NOT NULL,
  `update_time` datetime(0) NOT NULL
);

DROP TABLE IF EXISTS `t_dataset_tree`;
CREATE TABLE `t_dataset_tree`  (
  `id` int(10) UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
  `root` int(10) UNSIGNED NOT NULL,
  `create_time` datetime(0) NOT NULL,
  `update_time` datetime(0) NOT NULL
);
