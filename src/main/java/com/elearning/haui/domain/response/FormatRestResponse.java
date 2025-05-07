package com.elearning.haui.domain.response;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice
public class FormatRestResponse implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(
            Object body,
            MethodParameter returnType,
            MediaType selectedContentType,
            Class selectedConverterType,
            ServerHttpRequest request,
            ServerHttpResponse response) {

        // Không wrap các loại raw như byte[] hay String
        if (body instanceof RestResponse || body instanceof byte[] || body instanceof String) {
            return body;
        }

        // Lấy status code an toàn (mặc định 200)
        int status = 200;
        if (response instanceof org.springframework.http.server.ServletServerHttpResponse servletResponse) {
            status = servletResponse.getServletResponse().getStatus();
        }

        RestResponse<Object> res = new RestResponse<>();
        res.setStatusCode(status);

        if (status >= 400) {
            // Với lỗi, giữ nguyên body (để exception handler xử lý)
            return body;
        } else {
            res.setData(body);
            res.setMessage("CALL API SUCCESS");
            return res;
        }
    }
}
