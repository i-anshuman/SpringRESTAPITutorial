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

/**
 * JWTRequestFilter extends OncePerRequestFilter. It will intercept every
 * incoming request and perform following tasks: 1. Check if request has the JWT
 * Token in Authorization Header or not.
 */

@Component
public class JWTRequestFilter extends OncePerRequestFilter {

  @Autowired
  private JWTUserDetailService jwtUserDetailsService;

  @Autowired
  private JWTUtils jwtUtil;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    // Extract Authorization Header
    final String reqTokenHeader = request.getHeader("Authorization");
    System.out.println("Header : " + reqTokenHeader);
    String username = null;
    String jwtToken = null;

    // If Authorization Header is present and has Bearer + JWT Token in it.
    if (reqTokenHeader != null && reqTokenHeader.startsWith("Bearer ")) {

      logger.info("JWT Token present in Bearer String.");

      // Extract JWT Token only, discard Bearer String.
      jwtToken = reqTokenHeader.substring(7);
      System.out.println("Token in filter : " + jwtToken);

      try {
        // Extract user name form the JWT Token
        username = jwtUtil.getUserNameFromToken(jwtToken);
        System.out.println("Username : " + username);
      } catch (IllegalArgumentException e) {
        System.out.println("Unable to get JWT Token");
      } catch (ExpiredJwtException e) {
        System.out.println("JWT Token has expired");
      }
    } else {
      logger.warn("JWT Token doesn't begin with Bearer String.");
    }

    /**
     * Currently Authenticated User is called principal. For getting details of
     * principal, we need to get SecurityContext. SecurityContext holds the
     * principal. SecurityContextHolder is the helper class having getContext()
     * method that return Security Context.
     * 
     * SecurityContext keeps the user details in Authentication object and
     * getAuthentication method returns the Authentication object.
     */

    // Once we have the token, validate it.
    if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      System.out.println("Username : " + username);
      UserDetails user = jwtUserDetailsService.loadUserByUsername(username);

      System.out.println("User : " + user);

      if (jwtUtil.validateToken(jwtToken, user)) {

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
            user, null, user.getAuthorities());
        usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        // After setting the Authentication in the context, we specify
        // that the current user is authenticated. So it passes the
        // Spring Security Configurations successfully.
        SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
      }
    }
    System.out.println("In Filter.");
    filterChain.doFilter(request, response);
  }

}
