package com.itheima.stock.mapper;

import com.itheima.stock.pojo.SysRole;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Entity com.itheima.stock.pojo.SysRole
 */
@Mapper
public interface SysRoleMapper {

    int deleteByPrimaryKey(Long id);

    int insert(SysRole record);

    int insertSelective(SysRole record);

    SysRole selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SysRole record);

    int updateByPrimaryKey(SysRole record);

}




