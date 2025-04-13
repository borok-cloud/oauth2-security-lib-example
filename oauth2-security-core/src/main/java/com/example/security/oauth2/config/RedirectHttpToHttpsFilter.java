package com.example.security.oauth2.config;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RedirectHttpToHttpsFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // Initialization logic if needed
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Check if the request is HTTP and if the URL is not already HTTPS
        if ("http".equalsIgnoreCase(httpRequest.getScheme())) {
            String redirectUrl = "https://" + httpRequest.getServerName()
                    + (httpRequest.getServerPort() == 80 ? "" : ":" + httpRequest.getServerPort())
                    + httpRequest.getRequestURI();
            if (httpRequest.getQueryString() != null) {
                redirectUrl += "?" + httpRequest.getQueryString();
            }
            httpResponse.sendRedirect(redirectUrl);  // Redirect to HTTPS
        } else {
            chain.doFilter(request, response);  // Continue with the chain if already HTTPS
        }
    }

    @Override
    public void destroy() {
        // Clean up if needed
    }
}
