drop schema gateway if exists;
create schema gateway;

-- ----------------------------
-- gateway
-- ----------------------------
DROP TABLE IF EXISTS `gateway_api_group`;

CREATE TABLE `gateway_api_group`
(
  `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name`         varchar(255) DEFAULT NULL COMMENT 'api名称',
  `describe`     varchar(500) DEFAULT NULL COMMENT 'api描述',
  `backend_host` varchar(255) DEFAULT NULL COMMENT '目标地址',
  `backend_port` varchar(255) DEFAULT NULL COMMENT '目标端口',
  `backend_path` varchar(255) DEFAULT NULL COMMENT '目标url前缀',
  `gmt_create`   datetime     DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime     DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`)
) COMMENT='api';


INSERT INTO `gateway_api_group` (`id`, `name`, `describe`, `backend_host`, `backend_port`, `backend_path`, `gmt_create`,
                                 `gmt_modified`)
VALUES
(1, '服务化', '服务化标准分组', '', '', '', '2018-02-03 03:52:00', '2018-02-03 03:52:00'),
(2, '聚合支付', '聚合支付系列API', 'cashierbe.dev.bkjk.cn', '80', 'cashierbe', '2018-06-07 18:05:26', '2018-06-07 18:14:03');


DROP TABLE IF EXISTS `gateway_api`;

CREATE TABLE `gateway_api`
(
  `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name`         varchar(255) DEFAULT NULL COMMENT 'api名称',
  `describe`     varchar(500) DEFAULT NULL COMMENT 'api描述',
  `group_id`     bigint(20) unsigned NOT NULL COMMENT '分组Id',
  `url`          varchar(255) DEFAULT NULL COMMENT '请求路径',
  `http_method`  varchar(255) DEFAULT NULL COMMENT '提交方式',
  `path`         varchar(255) DEFAULT NULL COMMENT '后端请求路径',
  `routes`       tinyint(1) DEFAULT NULL COMMENT '是否RPC请求',
  `gmt_create`   datetime     DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime     DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_url` (`url`),
  KEY            `fk_group` (`group_id`),
) COMMENT='api';

INSERT INTO `gateway_api` (`id`, `name`, `describe`, `group_id`, `url`, `http_method`, `path`, `routes`, `gmt_create`,
                           `gmt_modified`)
VALUES
(1, '查询报表', '无参数查询案例', 1, '/oa/data/monitor/query_monitoridl_data.json', 'POST', '', 1, '2018-05-17 11:59:11', '2018-05-17 11:59:11'),
(2, '查询报表数据', '有参数查询案例', 1, '/oa/data/monitor/query_chart_data.json', 'POST', '', 1, '2018-05-18 03:13:17', '2018-05-18 03:13:17');


DROP TABLE IF EXISTS `gateway_api_rpc`;

CREATE TABLE `gateway_api_rpc`
(
  `id`                   bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `api_id`               bigint(20) unsigned NOT NULL COMMENT 'apiId',
  `service_name`         varchar(255) DEFAULT NULL COMMENT '服务名',
  `method_name`          varchar(100) DEFAULT NULL COMMENT '方法名',
  `service_group`        varchar(100) DEFAULT NULL COMMENT '服务组名',
  `service_version`      varchar(100) DEFAULT NULL COMMENT '服务版本',
  `proto_context`        text COMMENT 'proto内容',
  `dubbo_param_template` text COMMENT 'dubbo请求参数类型',
  `gmt_create`           datetime     DEFAULT NULL COMMENT '创建时间',
  `gmt_modified`         datetime     DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_service` (`service_name`,`method_name`,`service_group`,`service_version`,`api_id`),
  KEY                    `fk_rpc_api` (`api_id`),
) COMMENT='rpc服务映射表';
INSERT INTO `gateway_api_rpc` (`id`, `api_id`, `service_name`, `method_name`, `service_group`, `service_version`, `proto_context`,
                               `dubbo_param_template`, `gmt_create`, `gmt_modified`)
VALUES
(1, 1, 'com.fenqile.data.monitor.service.MonitorIdlService', 'queryMonitorIdlData', 'gateway', '1.0.0',
 NULL ,'', '2018-05-17 11:59:11', '2018-05-17 11:59:11'),
(2, 2, 'com.fenqile.data.monitor.service.MonitorIdlService', 'queryMonitorChartDemo', 'gateway', '1.0.0', NULL,
 '[{"type":"com.fenqile.data.monitor.req.BaseQuery","expression":"${jsonStr}"}]','2018-05-18 03:13:17', '2018-05-18 03:13:17');


DROP TABLE IF EXISTS `gateway_api_springcloud`;

CREATE TABLE `gateway_api_springcloud`
(
  `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `instance_id`  varchar(100) DEFAULT NULL COMMENT '服务ID',
  `gmt_create`   datetime     DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime     DEFAULT NULL COMMENT '修改时间',
  `api_id`       bigint(20) unsigned NOT NULL COMMENT 'apiId',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_instance_api` (`instance_id`,`api_id`),
  KEY            `fk_sc_api` (`api_id`),
) COMMENT='SpringCloud服务映射表';

INSERT INTO `gateway_api_springcloud` (`id`, `instance_id`, `gmt_create`, `gmt_modified`, `api_id`)
VALUES
  (1, 'tesla', '2018-05-18 09:07:31', '2018-05-18 09:07:31', 3);


DROP TABLE IF EXISTS `gateway_filter`;

CREATE TABLE `gateway_filter`
(
  `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `name`         varchar(255)  DEFAULT NULL COMMENT '组件名称',
  `describe`     varchar(500)  DEFAULT NULL COMMENT '组件描述',
  `in_or_out`    varchar(10)   DEFAULT 'in' COMMENT '入口还是出口',
  `filter_type`  varchar(100)  DEFAULT NULL,
  `rule`         varchar(5000) DEFAULT NULL,
  `api_id`       bigint(20) DEFAULT NULL COMMENT 'apiId',
  `group_id`     bigint(20) DEFAULT NULL COMMENT 'groupId',
  `gmt_create`   datetime      DEFAULT NULL COMMENT '创建时间',
  `gmt_modified` datetime      DEFAULT NULL COMMENT '修改时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_filter_api` (`filter_type`,`api_id`),
  UNIQUE KEY `unique_instance_group` (`filter_type`,`group_id`)
) COMMENT='过滤规则表';

INSERT INTO `gateway_filter` (`id`, `name`, `describe`, `in_or_out`, `filter_type`, `rule`, `api_id`, `group_id`,
                              `gmt_create`, `gmt_modified`)
VALUES
(1, 'Cookie黑名单', '标准的Cookie黑名单', 'IN', 'BlackCookieHttpRequestFilter',
 '\\.\\./\n\\:\\$\n\\$\\{\nselect.+(from|limit)\n(?:(union(.*?)select))\nhaving|rongjitest\nsleep\\((\\s*)(\\d*)(\\s*)\\)\nbenchmark\\((.*)\\,(.*)\\)\nbase64_decode\\(\n(?:from\\W+information_schema\\W)\n(?:(?:current_)user|database|schema|connection_id)\\s*\\(\n(?:etc\\/\\W*passwd)\ninto(\\s+)+(?:dump|out)file\\s*\ngroup\\s+by.+\\(\nxwork.methodaccessor\n(?:define|eval|file_get_contents|include|require|require_once|shell_exec|phpinfo|system|passthru|preg_\\w+|execute|echo|print|print_r|var_dump|(fp)open|alert|showmodaldialog)\\(\nxwork\\.methodaccessor\n(gopher|doc|php|glob|file|phar|zlib|ftp|ldap|dict|ogg|data)\\:\\/\njava\\.lang\n\\$_(get|post|cookie|files|session|env|phplib|globals|server)\\[\n',
 NULL, NULL, '2018-05-18 10:48:00', '2018-05-18 10:48:00'),
(2, 'URL参数黑名单', '标准的URL参数黑名单', 'IN', 'URLParamHttpRequestFilter',
 '\\.\\./\n\\:\\$\n\\$\\{\nselect.+(from|limit)\n(?:(union(.*?)select))\nhaving|rongjitest\nsleep\\((\\s*)(\\d*)(\\s*)\\)\nbenchmark\\((.*)\\,(.*)\\)\nbase64_decode\\(\n(?:from\\W+information_schema\\W)\n(?:(?:current_)user|database|schema|connection_id)\\s*\\(\n(?:etc\\/\\W*passwd)\ninto(\\s+)+(?:dump|out)file\\s*\ngroup\\s+by.+\\(\nxwork.methodaccessor\n(?:define|eval|file_get_contents|include|require|require_once|shell_exec|phpinfo|system|passthru|preg_\\w+|execute|echo|print|print_r|var_dump|(fp)open|alert|showmodaldialog)\\(\nxwork\\.MethodAccessor\n(gopher|doc|php|glob|file|phar|zlib|ftp|ldap|dict|ogg|data)\\:\\/\njava\\.lang\n\\$_(get|post|cookie|files|session|env|phplib|globals|server)\\[\n\\<(iframe|script|body|img|layer|div|meta|style|base|object|input)\n(onmouseover|onerror|onload)\\=\n',
 NULL, NULL, '2018-05-18 10:48:00', '2018-05-18 10:48:00'),
(3, 'URL黑名单', '标准的URL黑名单', 'IN', 'BlackURLHttpRequestFilter',
 '\\.(svn|git|htaccess|bash_history)\n\\.(bak|inc|old|mdb|sql|backup|java|class)$\n(vhost|bbs|host|wwwroot|www|site|root|hytop|flashfxp).*\\.rar\n(phpmyadmin|jmx-console|jmxinvokerservlet)\njava\\.lang\n/(attachments|upimg|images|css|uploadfiles|html|uploads|templets|static|template|data|inc|forumdata|upload|includes|cache|avatar)/(\\\\w+).(php|jsp)\n',
 NULL, NULL, '2018-05-18 10:48:00', '2018-05-18 10:48:00');


DROP TABLE IF EXISTS `gateway_user_filter`;

CREATE TABLE `gateway_user_filter`
(
  `id`           bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `filter_class` varchar(250) NOT NULL,
  `rule`         varchar(5000) DEFAULT NULL,
  `filter_id`    int(11) NOT NULL,
  `gmt_create`   datetime      DEFAULT NULL,
  `gmt_modified` datetime      DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `unique_instance` (`filter_id`,`filter_class`),
  KEY            `fk_user_filter` (`filter_id`)
) COMMENT='用户自定义规则表';


