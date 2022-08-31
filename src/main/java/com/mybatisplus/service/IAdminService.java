package com.mybatisplus.service;

import com.mybatisplus.entity.Admin;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author wxt
 * @since 2022-08-03
 */
public interface IAdminService extends IService<Admin> {
        public Admin get_Admin_permission(String account);
        public String set_Admin_permission(Admin admin);
}
