package demo

import java.io.IOException

import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.FilterConfig
import javax.servlet.ServletException
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component

@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
class CorsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // do nothing
    }

    @Override
    public void doFilter(
            final ServletRequest req, 
            final ServletResponse res,
            final FilterChain chain) throws IOException, ServletException {

        
        try {
            HttpServletResponse response = (HttpServletResponse) res
            
            // quick and dirty - NOT FOR PRODUCTION!
            response.setHeader('Access-Control-Allow-Origin', '*')
            
            response.setHeader('Access-Control-Allow-Methods', 'POST, PUT, GET, OPTIONS, DELETE')
            response.setHeader('Access-Control-Allow-Headers', 'x-requested-with')
            response.setHeader('Access-Control-Max-Age', '3600')
        } finally {
            HttpServletRequest request = (HttpServletRequest) req
            if (request.getMethod() != 'OPTIONS') {
                chain.doFilter(req, res)
            }
        }
    }

    @Override
    public void destroy() {
        // do nothing
    }

}
