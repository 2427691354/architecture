package com.lixing.util;

/**
 * @author cc
 * @date 2020/06/08
 **/

import lombok.Data;

@Data
public class JsonResult<T> {

    private String message;
    private T data;
    private Boolean state;


    /**
     * 错误，系统异常
     *
     * @return result
     */
    public static JsonResult renderError() {
        JsonResult result = new JsonResult();
        result.setState(Boolean.FALSE);
        return result;
    }

    /**
     * 错误数据（带消息）
     *
     * @param msg 需要返回的消息
     * @return result
     */
    public static JsonResult renderError(String msg) {
        JsonResult jsonResult = JsonResult.renderError();
        jsonResult.setMessage(msg);
        return jsonResult;
    }

    /**
     * 成功数据
     *
     * @return result
     */
    public static JsonResult renderSuccess() {
        JsonResult result = new JsonResult();
        result.setState(Boolean.TRUE);
        return result;
    }

    /**
     * 成功数据（带信息）
     *
     * @param msg 需要返回的信息
     * @return result
     */
    public static JsonResult renderSuccess(String msg) {
        JsonResult result = JsonResult.renderSuccess();
        result.setMessage(msg);
        return result;
    }

    /**
     * 成功数据（带数据）
     *
     * @param obj 需要返回的对象
     * @return result
     */
    public static JsonResult renderSuccess(Object obj) {
        JsonResult result = JsonResult.renderSuccess();
        result.setData(obj);
        return result;
    }

    /**
     * 失败数据
     *
     * @return result
     */
    public static JsonResult renderFail() {
        JsonResult result = new JsonResult();
        result.setState(Boolean.FALSE);
        return result;
    }

    /**
     * 失败数据（带消息）
     *
     * @param msg 需要返回的消息
     * @return result
     */
    public static JsonResult renderFail(String msg) {
        JsonResult result = JsonResult.renderFail();
        result.setMessage(msg);
        return result;
    }
}


