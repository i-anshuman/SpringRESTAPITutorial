package com.app.pojos;

/**
 * This is class is required for creating a response containing the JWT to be returned to the user.
 * @author Anshuman Gupta
 *
 */
public class JWTResponse {
  private final String token;

  public JWTResponse(String token) {
    super();
    this.token = token;
  }

  public String getToken() {
    return token;
  }
}
