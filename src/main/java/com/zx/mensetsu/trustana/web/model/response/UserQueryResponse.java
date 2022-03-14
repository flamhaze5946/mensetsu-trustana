package com.zx.mensetsu.trustana.web.model.response;

import com.zx.mensetsu.trustana.web.model.UserFO;

public class UserQueryResponse extends BaseResponse
{
    private UserFO user;

    public UserFO getUser() {
        return user;
    }

    public void setUser(UserFO user) {
        this.user = user;
    }
}
