package com.app.dao;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.app.pojos.Users;

@Repository
public interface IUserDAO extends CrudRepository<Users, Integer> {
  Users findByUsername(String username);
}
