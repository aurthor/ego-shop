package com.aurthor.service.impl;

import com.aurthor.constant.AuthConstant;
import com.aurthor.domain.SysUserDetail;
import com.aurthor.mapper.SysUserDetailMapper;
import com.aurthor.mapper.UserDetailMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

/**
 * @author: Aurthor King
 * @Version: v1.0
 * @Description:
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
    @Autowired
    private UserDetailMapper userDetailMapper;

    @Autowired
    private SysUserDetailMapper sysUserDetailMapper;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String loginType = request.getHeader(AuthConstant.LOGIN_TYPE);
        if (StringUtils.isEmpty(loginType)) {
            return null;
        }
        switch (loginType) {
            case AuthConstant.USER_TYPE:
                //如果是前台用户 直接插数据库

                break;
            case AuthConstant.SYS_USER_TYPE:
                //如果是后台用户，要查询权限
                SysUserDetail sysUserDetail = sysUserDetailMapper.selectOne(new LambdaQueryWrapper<SysUserDetail>()
                        .eq(SysUserDetail::getUsername, username)
                        .eq(SysUserDetail::getStatus, 1)
                );
                if (!ObjectUtils.isEmpty(sysUserDetail)) {
                    //查询权限
                    List<String> authorization = getUserPermission(sysUserDetail.getUserId());
                    if (!ObjectUtils.isEmpty(authorization)) {
                        sysUserDetail.setPermissions(authorization);
                    }
                }
                return sysUserDetail;
            default:
        }
        return null;
    }

    /**
     * 查询权限的方法
     *
     * @param userId
     * @return
     */
    private List<String> getUserPermission(Long userId) {
        HashSet<String> actualPermissions = new HashSet<>();
        //得到权限
        List<String> permission = sysUserDetailMapper.getPermissionByUserId(userId);
        if (ObjectUtils.isEmpty(permission)) {
            return Collections.emptyList();
        }
        //处理逗号分隔
        permission.forEach(p -> {
            String[] complexPermission = p.split(",");
            for (String s : complexPermission) {
                actualPermissions.add(s);
            }
        });
        return new ArrayList<>(actualPermissions);
    }
}
