package com.app.service;

import java.util.ArrayList;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * JWTUserDetailsService implements the Spring Security UserDetailsService interface.
 * It overrides the loadUserByUsername for fetching user details from the database using the username.
 * The Spring Security Authentication Manager calls this method for getting the user details from the database
 * when authenticating the user details provided by the user. Here we are getting the user details from a hardcoded User List.
 * @author Anshuman Gupta
 *
 */

@Service
public class JWTUserDetailService implements UserDetailsService {

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    if (username.equals("Anshuman")) {
      return new User(username, "$2a$10$slYQmyNdGzTn7ZLBXBChFOC9f6kFjAqPhccnP6DxlWXx2lPk1C3G6", new ArrayList<>());
    }
    throw new UsernameNotFoundException(username + " not found.");
  }

}
