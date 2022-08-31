package com.mybatisplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mybatisplus.entity.Admin;
import com.mybatisplus.mapper.AdminMapper;
import com.mybatisplus.service.IAdminService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author wxt
 * @since 2022-08-03
 */
@Service
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {
    @Autowired
    private AdminMapper adminMapper;

    @Override
    public Admin get_Admin_permission(String account) {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("account", account);
        Admin admin = adminMapper.selectOne( adminQueryWrapper);
        return admin;
    }

    @Override
    public String set_Admin_permission(Admin admin) {
        int insert = baseMapper.insert(admin);
        String re = "";
        if (insert == 0) {
            re = "设置管理员权限失败";
        } else {
            re = "设置管理员权限成功";
        }
        return re;
    }
}

