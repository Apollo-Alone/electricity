package com.rs.electricity.bean;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

/**
 * @Author wuxw
 * @Date 2020/5/28 18:41
 * @Version 1.0
 * add by wuxw 2020/5/28
 **/
public class ResultVo implements Serializable {

    public static final int CODE_OK = 0; // 成功

    public static final int CODE_GET_ELECTRICITY_ERROR = 1200; // 获取失败

    public static final String MSG_ERROR = "获取失败";// 获取失败

    public static final String MSG_OK = "成功"; // 成功

    //状态码
    private int code;

    //错误提示
    private String msg;

    //数据对象
    private Object data;

    public ResultVo() {
    }

    public ResultVo(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ResultVo(Object data) {
        this.code = CODE_OK;
        this.msg = MSG_OK;
        this.data = data;
    }

    public ResultVo(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return JSONObject.toJSONString(this, SerializerFeature.DisableCircularReferenceDetect, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 创建ResponseEntity对象
     *
     * @param resultVo 数据对象
     * @return
     */
    public static ResponseEntity<String> createResponseEntity(ResultVo resultVo) {
        return new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
    }

    /**
     * 成功通用回复
     *
     * @return
     */
    public static ResponseEntity<String> success() {
        ResultVo resultVo = new ResultVo(CODE_OK, MSG_OK);
        return new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
    }

    /**
     * 页面跳转
     *
     * @param url
     * @return
     */
    public static ResponseEntity<String> redirectPage(String url) {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.LOCATION, url);
        ResponseEntity<String> responseEntity = new ResponseEntity<String>("", headers, HttpStatus.FOUND);
        return responseEntity;
    }

    /**
     * 创建ResponseEntity对象
     *
     * @param code 状态嘛
     * @param msg  返回信息
     * @param data 数据对象
     * @return
     */
    public static ResponseEntity<String> createResponseEntity(int code, String msg, Object data) {
        ResultVo resultVo = new ResultVo(code, msg, data);
        return new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
    }

    /**
     * 创建ResponseEntity对象
     *
     * @param code 状态嘛
     * @param msg  返回信息
     * @return
     */
    public static ResponseEntity<String> createResponseEntity(int code, String msg) {
        ResultVo resultVo = new ResultVo(code, msg);
        return new ResponseEntity<String>(resultVo.toString(), HttpStatus.OK);
    }

}
