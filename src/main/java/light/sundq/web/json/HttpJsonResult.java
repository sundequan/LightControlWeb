package light.sundq.web.json;

import java.util.List;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * 
 * @Title: LeeJSONResult.java
 * @Package com.lee.utils
 * @Description: 自定义响应数据结构
 * 				这个类是提供给门户，ios，安卓，微信商城用的
 * 				门户接受此类数据后需要使用本类的方法转换成对于的数据类型格式（类，或者list）
 * 				其他自行处理
 * 				200：表示成功
 * 				500：表示错误，错误信息在msg字段中
 * 				501：bean验证错误，不管多少个错误都以map形式返回
 * 				502：拦截器拦截到用户token出错
 * 				555：异常抛出信息
 * Copyright: Copyright (c) 2016
 * Company:Nathan.Lee.Salvatore
 * 
 * @author leechenxiang
 * @date 2016年4月22日 下午8:33:36
 * @version V1.0
 */
public class HttpJsonResult {

    // 定义jackson对象
    private static final ObjectMapper MAPPER = new ObjectMapper();

    // 响应业务状态
    private Integer code;

    // 响应消息
    private String msg;

    // 响应中的数据
    private Object data;
    

    public static HttpJsonResult build(Integer status, String msg, Object data) {
        return new HttpJsonResult(status, msg, data);
    }

    public static HttpJsonResult ok(Object data) {
        return new HttpJsonResult(data);
    }

    public static HttpJsonResult ok() {
        return new HttpJsonResult(null);
    }
    
    public static HttpJsonResult errorMsg(String msg) {
        return new HttpJsonResult(500, msg, null);
    }
    
    public static HttpJsonResult errorMap(Object data) {
        return new HttpJsonResult(501, "error", data);
    }
    
    public static HttpJsonResult errorTokenMsg(String msg) {
        return new HttpJsonResult(502, msg, null);
    }
    
    public static HttpJsonResult errorException(String msg) {
        return new HttpJsonResult(555, msg, null);
    }

    public HttpJsonResult() {

    }


    public HttpJsonResult(Integer status, String msg, Object data) {
        this.code = status;
        this.msg = msg;
        this.data = data;
    }

    public HttpJsonResult(Object data) {
        this.code = 200;
        this.msg = "OK";
        this.data = data;
    }

    public Integer getStatus() {
        return code;
    }

    public void setStatus(Integer status) {
        this.code = status;
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


    public static HttpJsonResult formatToPojo(String jsonData, Class<?> clazz) {
        try {
            if (clazz == null) {
                return MAPPER.readValue(jsonData, HttpJsonResult.class);
            }
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (clazz != null) {
                if (data.isObject()) {
                    obj = MAPPER.readValue(data.traverse(), clazz);
                } else if (data.isTextual()) {
                    obj = MAPPER.readValue(data.asText(), clazz);
                }
            }
            return build(jsonNode.get("code").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }

    public static HttpJsonResult format(String json) {
        try {
            return MAPPER.readValue(json, HttpJsonResult.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static HttpJsonResult formatToList(String jsonData, Class<?> clazz) {
        try {
            JsonNode jsonNode = MAPPER.readTree(jsonData);
            JsonNode data = jsonNode.get("data");
            Object obj = null;
            if (data.isArray() && data.size() > 0) {
                obj = MAPPER.readValue(data.traverse(),
                        MAPPER.getTypeFactory().constructCollectionType(List.class, clazz));
            }
            return build(jsonNode.get("code").intValue(), jsonNode.get("msg").asText(), obj);
        } catch (Exception e) {
            return null;
        }
    }
}
