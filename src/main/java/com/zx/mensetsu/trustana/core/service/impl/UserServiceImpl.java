package com.zx.mensetsu.trustana.core.service.impl;

import com.zx.mensetsu.trustana.core.model.UserDO;
import com.zx.mensetsu.trustana.core.model.UserTempIdDO;
import com.zx.mensetsu.trustana.core.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class UserServiceImpl implements UserService
{
    private final Map<Integer, UserDO> dbMock = new ConcurrentHashMap<>();

    private final Map<Integer, UserDO> cacheMock = new ConcurrentHashMap<>();

    private final Map<String, UserTempIdDO> tempDbMock = new ConcurrentHashMap<>();

    private final Map<String, UserTempIdDO> tempCacheMock = new ConcurrentHashMap<>();

    private final AtomicInteger mockUserIdGenerator = new AtomicInteger(0);

    private final AtomicInteger mockTempIdGenerator = new AtomicInteger(0);

    @Value("${user.temp.expire.days:14}")
    private Integer expireDays;

    @Autowired
    private UserService self;

    @Override
    public UserDO queryUserByTempId(String tempId)
    {
        UserTempIdDO userTempIdDO = tempCacheMock.computeIfAbsent(tempId, var -> tempDbMock.get(tempId));
        if (userTempIdDO == null)
        {
            return null;
        }
        if (userTempIdDO.getExpireDate().before(new Date()))
        {
            tempDbMock.remove(userTempIdDO.getTempId());
            tempCacheMock.remove(userTempIdDO.getTempId());
            return null;
        }
        return self.queryUser(userTempIdDO.getUserId());
    }

    @Override
    public UserDO queryUser(Integer userId)
    {
        return cacheMock.computeIfAbsent(userId, var -> dbMock.get(userId));
    }

    @Override
    public UserDO queryUserByName(String name)
    {
        for (UserDO userDO : dbMock.values())
        {
            if (StringUtils.equals(userDO.getName(), name))
            {
                return userDO;
            }
        }
        return null;
    }

    @Override
    public void saveUser(UserDO userDO)
    {
        dbMock.put(userDO.getId(), userDO);
        cacheMock.remove(userDO.getId());
    }

    @Override
    public String generateTempId(Integer userId)
    {
        String tempId;
        do
        {
            tempId = UUID.randomUUID().toString();
            tempId = tempId.substring(0, tempId.indexOf('-'));
        }
        while (tempDbMock.containsKey(tempId));
        UserTempIdDO userTempIdDO = new UserTempIdDO();
        userTempIdDO.setId(mockTempIdGenerator.incrementAndGet());
        userTempIdDO.setTempId(tempId);
        userTempIdDO.setUserId(userId);
        userTempIdDO.setExpireDate(Date.from(LocalDateTime.now()
                        .plusDays(expireDays)
                        .atZone(ZoneId.systemDefault()).toInstant()));
        tempDbMock.put(tempId, userTempIdDO);
        return tempId;
    }

    @PostConstruct
    private void initDbData()
    {
        UserDO tom = new UserDO();
        tom.setId(mockUserIdGenerator.incrementAndGet());
        tom.setName("Tom");
        tom.setAvatar(buildAvatarUrl(tom.getName()));
        tom.setJob("seller");
        tom.setSex(0);

        UserDO jerry = new UserDO();
        jerry.setId(mockUserIdGenerator.incrementAndGet());
        jerry.setName("Jerry");
        jerry.setAvatar(buildAvatarUrl(jerry.getName()));
        jerry.setJob("poster");
        jerry.setSex(1);

        UserDO takeshi = new UserDO();
        takeshi.setId(mockUserIdGenerator.incrementAndGet());
        takeshi.setName("Takashi");
        takeshi.setAvatar(buildAvatarUrl(takeshi.getName()));
        takeshi.setJob("manager");
        takeshi.setSex(0);

        self.saveUser(tom);
        self.saveUser(jerry);
        self.saveUser(takeshi);
    }

    private String buildAvatarUrl(String name)
    {
        return MessageFormat.format("https://image.s3.com/user/{0}", name);
    }
}
