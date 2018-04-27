package com.hc.sys;

import java.util.HashMap;
import java.util.Map;

/**
 * 向客户端返回结果
 * @author 陈顺华
 *
 */
public class R {
    //是否处理成功
    boolean success;
    //数据
    Object data;
    //错误信息
    Map<String, String> errors;

    private R(){
    }
    
    /**
     * 成功，输入需要返回的结果
     * @param data 输出的结果
     * @return R
     */
    public static R success(Object data){
        R rs = new R();
        rs.success = true;
        rs.data = data;
        rs.errors = null;
        return rs;
    }
    
    /**
     * 失败，输入一系列错误消息，如果参数只有1个则在errors中增加一个叫message的消息
     * 如果是多个参数则按消息名，消息内容；消息名，消息内容增加到errors
     * @param msg
     * @return
     */
    public static R failure(String ... msg){
        R rs = new R();
        rs.success = false;
        rs.data = null;
        rs.errors = new HashMap<String, String>();
        if(msg.length==1){
            rs.errors.put("message", msg[0]);
        }
        else{
            for(int i=1; i<msg.length; i+=2){
                rs.errors.put(String.valueOf(msg[i-1]), msg[i]);
            }
        }
        return rs;
    }
    
    /**
     * 失败，输入错误消息的Map
     * @param errors
     * @return
     */
    public static R failure(Map<String, String> errors){
    	R rs = new R();
        rs.success = false;
        rs.data = null;
        rs.errors = errors;
        return rs;
    }

    public boolean isSuccess() {
        return success;
    }
    
    public void setData(Object data){
    	this.data = data;
    }

    public Object getData() {
        return data;
    }

    public Map<String, String> getErrors() {
        return errors;
    }
}
