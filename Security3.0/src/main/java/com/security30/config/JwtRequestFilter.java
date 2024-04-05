package com.security30.config;

import com.security30.model.UserProperty;
import com.security30.repository.UserPropertyRepository;
import com.security30.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private JwtService jwtService;
    private UserPropertyRepository propertyRepository;

    public JwtRequestFilter(JwtService jwtService, UserPropertyRepository propertyRepository) {
        this.jwtService = jwtService;
        this.propertyRepository = propertyRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String tokenHeader = request.getHeader("Authorization");
        if (tokenHeader!=null && tokenHeader.startsWith("Bearer ")) {
            String token = tokenHeader.substring(8,tokenHeader.length()-1);
            String userName = jwtService.extractUserName(token);
            Optional<UserProperty> OpUser = propertyRepository.findByUsername(userName);
            if(OpUser.isPresent()) {
                UserProperty userProperty = OpUser.get();
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken
                        (userProperty, null, Collections.singleton(new SimpleGrantedAuthority(userProperty.getUserRole())));
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }
}
