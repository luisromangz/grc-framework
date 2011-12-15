
package com.greenriver.commons.web.security.filters;

import java.io.IOException;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author luisro
 */
public class CustomHeaderFormAuthenticationFilter extends UsernamePasswordAuthenticationFilter{
    
    private Map<String,String> headers ;
    
    @Override
    protected void successfulAuthentication(
            HttpServletRequest request, HttpServletResponse response,
            Authentication authResult) throws IOException, ServletException {
        super.successfulAuthentication(request, response, authResult);
        
        for(Map.Entry<String,String> entry : headers.entrySet()) {
            response.addHeader(entry.getKey(), entry.getValue());
        }
    }
    
    

    /**
     * @return the headers
     */
    public Map<String,String> getHeaders() {
        return headers;
    }

    /**
     * @param headers the headers to set
     */
    public void setHeaders(Map<String,String> headers) {
        this.headers = headers;
    }
    
}
