package demo;

import java.io.IOException;
import java.util.Objects;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

public class CsrfHeaderFilter extends OncePerRequestFilter {

    private static final String XSRF_TOKEN = "XSRF-TOKEN";

    @Override
    protected void doFilterInternal(
            final HttpServletRequest request,
            final HttpServletResponse response, 
            final FilterChain filterChain) throws IOException, ServletException {
        
        final Object object = request.getAttribute(CsrfToken.class.getName());
        
        if (object instanceof CsrfToken) {
            final CsrfToken csrf = (CsrfToken) object;
            final String token = csrf.getToken();
            
            final Cookie cookie = WebUtils.getCookie(request, XSRF_TOKEN);
            
            if ( cookie == null || ! Objects.equals(token, cookie.getValue()) ) {
                final Cookie newCookie = new Cookie(XSRF_TOKEN, token);
                newCookie.setPath("/");
                response.addCookie(newCookie);
            }
        }

        filterChain.doFilter(request, response);
    }

}
