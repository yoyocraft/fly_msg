package com.juzi.flymsg.mapper;

import com.juzi.flymsg.model.entity.UserInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author codejuzi
* @description 针对表【userInfo(用户信息表)】的数据库操作Mapper
* @createDate 2023-04-02 20:25:45
* @Entity com.juzi.flymsg.model.entity.UserInfo
*/
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {

}




