package com.pblintern.web.Security.Authentication;

import com.pblintern.web.Security.Model.TokenSecurity;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class GoogleFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = getToken(request);
        try {
            if(token != null){
                SecurityContextHolder.getContext().setAuthentication(new TokenSecurity(token));
            }else{
                log.info("Can't get token");
            }
        }catch(Exception e){
            log.info("Filter error with {}" , e.getMessage());
        }
        filterChain.doFilter(request,response);
    }

    private String getToken(HttpServletRequest request){
        String header = request.getHeader("Authorization");
        if(header != null && header.startsWith("Bearer ")){
            return header.replace("Bearer " , "");
        }
        return null ;
    }
}
