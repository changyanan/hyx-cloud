package org.xuenan.hyx.exception;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author changyanan1
 * @version 1.0.0
 * @Description 异常接口
 * @date 2019年09月02日 16:45:00
 */
@Controller
public class ExceptionController {
    @GetMapping("/exception/500")
    public ModelAndView serverError(HttpServletRequest request, HttpServletResponse response) {
        Exception ex = (Exception) request.getAttribute("javax.servlet.error.exception");
        return GlobalApiExceptionHandler.outException(request, response, ex);
    }
}
