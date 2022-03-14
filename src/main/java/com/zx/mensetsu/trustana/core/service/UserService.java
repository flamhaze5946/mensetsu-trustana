package com.zx.mensetsu.trustana.core.service;

import com.zx.mensetsu.trustana.core.model.UserDO;

public interface UserService
{
    UserDO queryUserByTempId(String tempId);

    UserDO queryUser(Integer userId);

    UserDO queryUserByName(String name);

    void saveUser(UserDO userDO);

    String generateTempId(Integer userId);
}
