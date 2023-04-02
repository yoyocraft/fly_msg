package com.juzi.flymsg.mapper;

import com.juzi.flymsg.model.entity.UserLoginInfo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author codejuzi
* @description 针对表【userLoginInfo(用户登录信息表)】的数据库操作Mapper
* @createDate 2023-04-02 20:28:12
* @Entity com.juzi.flymsg.model.entity.UserLoginInfo
*/
@Mapper
public interface UserLoginInfoMapper extends BaseMapper<UserLoginInfo> {

}




