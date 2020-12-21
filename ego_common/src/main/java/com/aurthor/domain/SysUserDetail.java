package com.aurthor.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.ObjectUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author: Aurthor King
 * @Version: v1.0
 * @Description: 后台用户
 */
@Data
@ApiModel(value="com-aurthor-domain-SysUserDetail")
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "sys_user")
public class SysUserDetail implements UserDetails, Serializable {

    @TableId(value = "user_id", type = IdType.AUTO)
    private Long userId;
    /**
     * 用户名
     */
    @TableField(value = "username")
    private String username;
    /**
     * 密码
     */
    @TableField(value = "password")
    private String password;


    /**
     * 状态  0：禁用   1：正常
     */
    @TableField(value = "status")
    private Integer status;

    @TableField(exist = false)
    List<String> permissions = new ArrayList<>(0);


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        if (!ObjectUtils.isEmpty(permissions.size())) {
            permissions.forEach(p -> {
                authorities.add(new SimpleGrantedAuthority(p));
            });
        }
        return authorities;
    }
    @Override
    public String getUsername() {
        return String.valueOf(userId);
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status == 1;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status == 1;
    }

    @Override
    public boolean isEnabled() {
        return this.status == 1;
    }
}
