package com.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.app.pojos.JWTRequest;
import com.app.pojos.JWTResponse;
import com.app.service.JWTUserDetailService;
import com.app.utils.JWTUtils;

/**
 * Expose a POST API /authenticate using the JwtAuthenticationController.
 * The POST API gets username and password in the body - Using Spring Authentication Manager we authenticate the username and password.
 * If the credentials are valid, a JWT token is created using the JWTTokenUtil and provided to the client. 
 * @author Anshuman Gupta
 *
 */

@RestController
public class AuthenticateController {
  
  @Autowired
  private AuthenticationManager authManager;
  
  @Autowired
  private JWTUtils jwtUtil;
  
  @Autowired
  private JWTUserDetailService userDetailsService;
  
  @PostMapping("/authenticate")
  public ResponseEntity<?> createAuthToken(@RequestBody JWTRequest authRequest) throws Exception {
    authenticate(authRequest.getUsername(), authRequest.getPassword());
    
    final UserDetails user = userDetailsService.loadUserByUsername(authRequest.getUsername());
    
    final String token = jwtUtil.generateToken(user);

    return ResponseEntity.ok(new JWTResponse(token));
  }
  
  private void authenticate(String username, String password) throws Exception {
    try {
      authManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
    } catch (DisabledException e) {
      throw new Exception("USER_DISABLED", e);
    } catch (BadCredentialsException e) {
      throw new Exception("INVALID_CREDENTIALS", e);
    }
  }
}
