package com.zx.mensetsu.trustana.web.controller;

import com.zx.mensetsu.trustana.core.model.UserDO;
import com.zx.mensetsu.trustana.core.service.UserService;
import com.zx.mensetsu.trustana.web.convertor.UserConvertor;
import com.zx.mensetsu.trustana.web.model.UserFO;
import com.zx.mensetsu.trustana.web.model.request.UserSetRequest;
import com.zx.mensetsu.trustana.web.model.response.BaseResponse;
import com.zx.mensetsu.trustana.web.model.response.UserCreateResponse;
import com.zx.mensetsu.trustana.web.model.response.UserQueryResponse;
import com.zx.mensetsu.trustana.web.model.response.UserShortLinkGenerateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("user")
public class UserController
{
    @Autowired
    private UserService userService;

    @Autowired
    private UserConvertor userConvertor;

    @GetMapping("t/{shortLink}")
    public UserQueryResponse queryUser(@PathVariable("shortLink") String shortLink)
    {
        UserQueryResponse response = new UserQueryResponse();
        response.call(() ->
        {
            UserDO userDO = userService.queryUserByTempId(shortLink);
            UserFO userFO = userConvertor.convertToUpper(userDO);
            response.setUser(userFO);
        });
        return response;
    }

    @GetMapping("self")
    public UserQueryResponse queryUser(Principal principal)
    {
        UserQueryResponse response = new UserQueryResponse();
        response.call(() ->
        {
            UserDO userDO = userService.queryUserByName(principal.getName());
            UserFO userFO = userConvertor.convertToUpper(userDO);
            response.setUser(userFO);
        });
        return response;
    }

    @PostMapping("createUser")
    public UserCreateResponse createUser(@RequestBody UserSetRequest request)
    {
        // not supported in mock mode
        throw new UnsupportedOperationException();
    }

    @PostMapping("setUser")
    public BaseResponse setUser(@RequestBody UserSetRequest request)
    {
        BaseResponse response = new BaseResponse();
        response.call(() ->
        {
            UserFO userFO = new UserFO();
            userFO.setId(request.getId());
            userFO.setName(request.getName());
            userFO.setSex(request.getSex());
            userFO.setJob(request.getJob());
            userFO.setAvatar(request.getAvatar());
            userFO.setProfile(request.getProfile());

            UserDO userDO = userConvertor.convertToLower(userFO);
            userService.saveUser(userDO);
        });

        return response;
    }

    @GetMapping("shortLink")
    public UserShortLinkGenerateResponse generateShortLink(Principal principal)
    {
        UserShortLinkGenerateResponse response = new UserShortLinkGenerateResponse();
        response.call(() ->
        {
            UserDO userDO = userService.queryUserByName(principal.getName());
            String shortLink = userService.generateTempId(userDO.getId());
            response.setShortLink(shortLink);
        });

        return response;
    }
}
