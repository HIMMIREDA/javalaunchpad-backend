package com.javalaunchpad.web;



import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  @GetMapping("/me")
  public ResponseEntity<?> me(){
    return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication());
  }
}
