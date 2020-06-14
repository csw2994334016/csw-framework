package com.three.common.vo;

import com.three.common.enums.ResultCodeEnum;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JsonData<T> {

    private int code;

    private String msg;

    private T data;

    public JsonData() {
    }

    public JsonData(T data) {
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.msg = ResultCodeEnum.SUCCESS.getMessage();
        this.data = data;
    }

    public JsonData<T> success() {
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.msg = ResultCodeEnum.SUCCESS.getMessage();
        return this;
    }

    public JsonData<T> success(String msg) {
        this.code = ResultCodeEnum.SUCCESS.getCode();
        this.msg = msg;
        return this;
    }

    public JsonData<T> error() {
        this.code = ResultCodeEnum.FAIL.getCode();
        this.msg = ResultCodeEnum.FAIL.getMessage();
        return this;
    }

    public JsonData<T> error(String msg) {
        this.code = ResultCodeEnum.FAIL.getCode();
        this.msg = msg;
        return this;
    }

    public JsonData<T> setMsg(String msg) {
        this.msg = msg;
        return this;
    }

    public JsonData<T> setResultCode(ResultCodeEnum resultCodeEnum) {
        this.code = resultCodeEnum.getCode();
        this.msg = resultCodeEnum.getMessage();
        return this;
    }

    public JsonData<T> setCodeAndMsg(int code, String msg) {
        this.code = code;
        this.msg = msg;
        return this;
    }
}
