package com.ruanyouzhi.estate.estate.Mapper;

import com.ruanyouzhi.estate.estate.Model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url) values (#{name},#{accountID},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    void insert(User user);
    @Select("select * from user where token=#{token}")
    User findByToken(@Param("token") String token);//如果是类，可以自动注入，如果不是，需要@param一下
    @Select("select * from user where id=#{id}")
    User findById(@Param("id") int id);
    @Select("select count(1) from question")
    Integer count();
}
