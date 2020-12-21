package com.aurthor.mapper;

import com.aurthor.domain.SysUserDetail;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface SysUserDetailMapper extends BaseMapper<SysUserDetail> {

    /**
     * 根据用户ID查询权限信息
     * @param userId
     * @return
     */
    @Select(" select DISTINCT perms from sys_user_role t1 join sys_role_menu t2 on(t1.role_id=t2.role_id) join sys_menu t3 on(t2.menu_id=t3.menu_id) where t1.user_id = #{userId} and t3.type = 2 ")
    List<String> getPermissionByUserId(@Param("userId") Long userId);
}
