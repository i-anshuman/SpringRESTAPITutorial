package com.app.filters;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.app.service.JWTUserDetailService;
import com.app.utils.JWTUtils;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class JWTRequestFilter extends OncePerRequestFilter {
  
  @Autowired
  private JWTUserDetailService jwtUserDetailsService;
  
  @Autowired
  private JWTUtils jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    
    final String reqTokenHeader = request.getHeader("Authorization");
    String username = null;
    String password = null;
    String jwtToken = null;
    
    if (reqTokenHeader != null && reqTokenHeader.startsWith("Bearer ")) {
      jwtToken = reqTokenHeader.substring(7);
      try {
        username = jwtUtil.getUserNameFromToken(jwtToken);
      } catch (IllegalArgumentException e) {
        System.out.println("Unable to get JWT Token");
      } catch (ExpiredJwtException e) {
        System.out.println("JWT Token has expired");
      }
    }
    else {
      logger.warn("JWT Token doesn't begin with Bearer String.");
    }
    
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      UserDetails user = this.jwtUserDetailsService.loadUserByUsername(username);
      if (jwtUtil.validateToken(jwtToken, user)) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            user, null, user.getAuthorities());
        usernamePasswordAuthenticationToken
            .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }
    
    filterChain.doFilter(request, response);
  }

}
