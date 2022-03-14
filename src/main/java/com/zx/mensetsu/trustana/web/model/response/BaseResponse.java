package com.zx.mensetsu.trustana.web.model.response;

import com.zx.mensetsu.trustana.common.exceptions.BizException;

public class BaseResponse
{
    public static final Integer SUCCESS_CODE = 200;
    public static final Integer UNKNOWN_BIZ_ERROR_CODE = 998;
    public static final Integer UNKNOWN_ERROR_CODE = 999;

    public static final String SUCCESS_MESSAGE = "";

    private Integer code;

    private String message;

    public void call(Action action)
    {
        try
        {
            action.invoke();
        }
        catch (BizException e)
        {
            this.setCode(UNKNOWN_BIZ_ERROR_CODE);
            this.setMessage(e.getMessage());
        }
        catch (Exception e)
        {
            this.setCode(UNKNOWN_ERROR_CODE);
            this.setMessage(e.getMessage());
        }

        this.setCode(SUCCESS_CODE);
        this.setMessage(SUCCESS_MESSAGE);
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @FunctionalInterface
    public interface Action
    {
        void invoke();
    }
}
