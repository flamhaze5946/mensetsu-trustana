package com.zx.mensetsu.trustana.web.convertor;

import com.zx.mensetsu.trustana.core.model.UserDO;
import com.zx.mensetsu.trustana.web.model.UserFO;
import org.springframework.stereotype.Component;

@Component
public class UserConvertor implements ModelConvertor<UserDO, UserFO>
{
    @Override
    public UserDO convertToLower(UserFO upper)
    {
        if (upper == null)
        {
            return null;
        }
        UserDO lower = new UserDO();
        lower.setId(upper.getId());
        lower.setName(upper.getName());
        lower.setSex(upper.getSex());
        lower.setJob(upper.getJob());
        lower.setAvatar(upper.getAvatar());
        lower.setProfile(upper.getProfile());

        return lower;
    }

    @Override
    public UserFO convertToUpper(UserDO lower)
    {
        if (lower == null)
        {
            return null;
        }
        UserFO upper = new UserFO();
        upper.setId(lower.getId());
        upper.setName(lower.getName());
        upper.setSex(lower.getSex());
        upper.setJob(lower.getJob());
        upper.setAvatar(lower.getAvatar());
        upper.setProfile(lower.getProfile());

        return upper;
    }
}
