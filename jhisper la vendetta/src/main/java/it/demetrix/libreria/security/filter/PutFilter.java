package it.demetrix.libreria.security.filter;

import it.demetrix.libreria.security.AuthoritiesConstants;
import it.demetrix.libreria.security.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
@WebFilter(filterName = "PutFilter")
@Component
public class PutFilter implements Filter {
    private static final Logger log = LoggerFactory.getLogger(PutFilter.class);
    String encoding;
    public void init(FilterConfig config) throws ServletException {
        encoding = config.getInitParameter("requestEncoding");

        if (encoding == null) {
            encoding = "UTF-8";
        }
    }
    @Override
    public void doFilter(ServletRequest request,
                         ServletResponse response,
                         FilterChain chain)
                        throws ServletException, IOException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String method = httpRequest.getMethod();
        String url = httpRequest.getRequestURI();
        HttpServletResponse httpResponse =  (HttpServletResponse) response;
        if ( method.equalsIgnoreCase(HttpMethod.PUT.toString())){
            if(SecurityUtils.isCurrentUserInRole(AuthoritiesConstants.EDITOR)){
                url= url+"/Editor";
                httpRequest.getRequestDispatcher(url).forward(httpRequest, httpResponse);
            }else{
                httpRequest.getRequestDispatcher(url).forward(httpRequest, httpResponse);
            }
        }else{
            httpRequest.getRequestDispatcher(url).forward(httpRequest, httpResponse);
        }

        chain.doFilter(httpRequest, httpResponse);
    }

    public void destroy() {
    }
}
