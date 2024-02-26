package cn.exp.proj.common.web.aop.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WebException extends RuntimeException {
    private int code;

    private String message;
}
