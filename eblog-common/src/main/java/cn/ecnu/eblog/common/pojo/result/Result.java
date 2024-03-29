package cn.ecnu.eblog.common.pojo.result;

import lombok.Data;

import java.io.Serializable;

@Data
public class Result<T> implements Serializable {
    private Integer code;  // 1代表成功，0代表失败
    private String msg;  // 错误信息
    private T data;  // 数据

    public static <T> Result<T> success(){
        Result<T> result = new Result<>();
        result.code = 1;
        result.msg = "";
        return result;
    }

    public static <T> Result<T> success(T object){
        Result<T> result = new Result<>();
        result.code = 1;
        result.msg = "";
        result.data = object;
        return result;
    }

    public static <T> Result<T> error(String msg) {
        Result<T> result = new Result<>();
        result.msg = msg;
        result.code = 0;
        return result;
    }
}
