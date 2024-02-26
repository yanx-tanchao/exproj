package cn.exp.proj.common.web.aop;

import cn.exp.proj.common.web.aop.exception.WebException;
import cn.exp.proj.common.web.response.WebResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    /**
     * @param ex Web Exception。 业务异常信息
     * @return WebResult
     * @date 2022/8/4 08:06
     */
    @ExceptionHandler(value = WebException.class)
    public WebResult bizExceptionHandler(WebException ex) {
        print(ex);
        return WebResult.failed(ex.getCode(), ex.getMessage());
    }

    /**
     * 自定义验证异常
     */
    @ExceptionHandler(BindException.class)
    public WebResult bindException(BindException e) {
        print(e);
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return WebResult.failed(500, message);
    }

    @ExceptionHandler(value = Exception.class)
    public WebResult<Object> unknownException(Exception ex) {
        print(ex);
        return WebResult.failed(500, "");
    }

    private void print(Exception e) {
        log.error("运行异常.", e);
    }
}
