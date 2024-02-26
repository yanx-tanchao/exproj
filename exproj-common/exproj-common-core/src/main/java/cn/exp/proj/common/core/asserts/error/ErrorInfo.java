package cn.exp.proj.common.core.asserts.error;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorInfo implements IError {
    private Integer code;

    private String message;

    public static ErrorInfo of(Integer code) {
        return of(code, null);
    }

    public static ErrorInfo of(Integer code, String message) {
        return new ErrorInfo(code, message);
    }
}
