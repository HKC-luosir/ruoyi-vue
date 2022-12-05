

DELETE FROM `sys_menu` WHERE menu_id BETWEEN 2016 AND 2021;
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2016, '学生历史状态', 1081, 1, 'his', 'student/his/index', NULL, 1, 0, 'C', '0', '0', 'student:his:list', 'row', 'admin', '2022-12-05 06:45:54', 'admin', '2022-12-05 06:47:12', '学生历史状态菜单');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2017, '学生历史状态查询', 2016, 1, '#', '', NULL, 1, 0, 'F', '0', '0', 'student:his:query', '#', 'admin', '2022-12-05 06:45:54', '', NULL, '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2018, '学生历史状态新增', 2016, 2, '#', '', NULL, 1, 0, 'F', '0', '0', 'student:his:add', '#', 'admin', '2022-12-05 06:45:54', '', NULL, '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2019, '学生历史状态修改', 2016, 3, '#', '', NULL, 1, 0, 'F', '0', '0', 'student:his:edit', '#', 'admin', '2022-12-05 06:45:54', '', NULL, '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2020, '学生历史状态删除', 2016, 4, '#', '', NULL, 1, 0, 'F', '0', '0', 'student:his:remove', '#', 'admin', '2022-12-05 06:45:54', '', NULL, '');
INSERT INTO `sys_menu`(`menu_id`, `menu_name`, `parent_id`, `order_num`, `path`, `component`, `query`, `is_frame`, `is_cache`, `menu_type`, `visible`, `status`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES (2021, '学生历史状态导出', 2016, 5, '#', '', NULL, 1, 0, 'F', '0', '0', 'student:his:export', '#', 'admin', '2022-12-05 06:45:54', '', NULL, '');

