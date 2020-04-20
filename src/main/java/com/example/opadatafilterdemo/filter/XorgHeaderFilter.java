package com.example.opadatafilterdemo.filter;

import com.example.opadatafilterdemo.exceptions.ApiError;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * For demonstration purpose, an X-ORG-HEADER is required
 * to demo the opa.partial-request.user-attribute-to-http-header-map config
 * from application.yml.
 */
@Order(1)
public class XorgHeaderFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(XorgHeaderFilter.class);

    private static final String X_ORG_HEADER = "X-ORG-HEADER";

    @Override
    public void init(FilterConfig filterConfig) {
        //do nothing
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        String xOrgHeader = httpServletRequest.getHeader(X_ORG_HEADER);
        if(xOrgHeader == null) {
            LOGGER.error("'X-XSRF-Header' is required!");
            ApiError apiError = new ApiError(400, "Bad Request", "X-ORG-HEADER is required");
            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpServletResponse.setContentType(MediaType.APPLICATION_JSON_VALUE);
            PrintWriter printWriter = httpServletResponse.getWriter();
            printWriter.write(new ObjectMapper().writeValueAsString(apiError));
            printWriter.flush();
            return;
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        //do nothing
    }
}
