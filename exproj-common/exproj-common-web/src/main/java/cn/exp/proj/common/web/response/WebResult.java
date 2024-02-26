package cn.exp.proj.common.web.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WebResult<T> {
    private int code;

    private String message;

    private T data;

    public static WebResult suceess() {
        return suceess(null);
    }

    public static <T> WebResult suceess(T data) {
        return new WebResult(200, "success", data);
    }

    public static WebResult failed(int code, String message) {
        return new WebResult(code, message, null);
    }
}
