package com.geekfoxer.gateway.ops.resp;

import com.alibaba.fastjson.JSONObject;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author pizhihui
 * @data 2018-10-14
 */
public class RespBean implements Serializable {

    private Integer code;
    private String message;
    private Object data;
    private Long total;

    public static RespBean pageSuccess(List data, long total) {
        return new RespBean(200, "成功", data, total);
    }

    public static RespBean pageSuccess(PageResult pageResult) {
        return new RespBean(200, "成功", pageResult.getData(), pageResult.getTotal());
    }

    public static RespBean ok() {
        return new RespBean(200, "成功", new EmptyVO());
    }

    public static RespBean okMsg(String msg) {
        return new RespBean(200, msg, new EmptyVO());
    }

    public static RespBean okData(Object oj) {
        return new RespBean(200, "成功", oj);
    }

    public static RespBean ok(String msg, Object data) {
        return new RespBean(200, msg, data);
    }

    public static RespBean error() {
        return new RespBean(500, "错误", new EmptyVO());
    }

    public static RespBean error(String msg) {
        return new RespBean(500, msg, new EmptyVO());
    }

    public static RespBean error(String msg, Object data) {
        return new RespBean(500, msg, data);
    }

    public static RespBean msg(ResultCode resultCode) {
        return new RespBean(resultCode, new EmptyVO());
    }

    public static RespBean msg(ResultCode resultCode, Object data) {
        return new RespBean(resultCode, data);
    }

    public void setResultCode(ResultCode resultCode) {
        this.code = resultCode.getCode();
        this.message = resultCode.getMsg();
    }

    public static RespBean list(Collection collection) {
        Map<String, Object> maps = new HashMap<>(1);
        maps.put("list", collection);
        return new RespBean(200, "成功", maps);
    }

    public RespBean(Integer code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public RespBean(ResultCode resultCode, Object data) {
        setResultCode(resultCode);
        this.data = data;
    }

    public RespBean(Integer code, String message, Object data, Long total) {
        this(code, message, data);
        this.total = total;
    }

    public Integer getCode() {
        return code;
    }

    public RespBean setCode(Integer code) {
        this.code = code;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public RespBean setMessage(String message) {
        this.message = message;
        return this;
    }

    public Object getData() {
        return data;
    }

    public RespBean setData(Object data) {
        this.data = data;
        return this;
    }

    public Long getTotal() {
        return total;
    }

    public RespBean setTotal(Long total) {
        this.total = total;
        return this;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this);
    }
}
