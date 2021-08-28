package com.app.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.app.dao.IUserDAO;
import com.app.dto.UsersDTO;
import com.app.pojos.Users;

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
  
  @Autowired
  private IUserDAO userDAO;
  
  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Users user = userDAO.findByUsername(username);
    System.out.println("In loadUserByUsername : " + user);
    if (user == null) {
      throw new UsernameNotFoundException(username + " not found.");      
    }
    return new User(user.getUsername(), user.getPassword(), new ArrayList<>());
  }

  public Users save (UsersDTO user) {
    Users newUser = new Users();
    newUser.setUsername(user.getUsername());
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));
    return userDAO.save(newUser);
  }
}
