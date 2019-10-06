package com.ruanyouzhi.estate.estate.Mapper;

import com.ruanyouzhi.estate.estate.Model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserMapper {
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified) values (#{name},#{accountID},#{token},#{gmtCreate},#{gmtModified})")
    void insert(User user);
}
