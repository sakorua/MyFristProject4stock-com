package com.itheima.stock.mapper;

import com.itheima.stock.pojo.SysUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.itheima.stock.pojo.SysUserRole
 */
@Mapper
public interface SysUserRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysUserRole record);

    int insertSelective(SysUserRole record);

    SysUserRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysUserRole record);

    int updateByPrimaryKey(SysUserRole record);

}




