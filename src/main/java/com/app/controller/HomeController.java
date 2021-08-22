package com.app.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

  @RequestMapping({ "/greet" })
  public String greeting() {
    return "Spring Boot REST Controller";
  }
  
  @GetMapping({ "/list" })
  public List<String> getList() {
    List<String> names = new ArrayList<>();
    names.add("Java");
    names.add("JavaScript");
    names.add("Python");
    names.add("React");
    names.add("Redux");
    return names;
  }
  
  @PostMapping({ "/map" })
  public Map<String, String> getMap() {
    Map<String, String> names = new HashMap<>();
    names.put("Alex", "Java");
    names.put("James", "JavaScript");
    names.put("Lucy", "Python");
    names.put("Dora", "React");
    names.put("Ammy", "Redux");
    return names;
  }
}
