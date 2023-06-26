package com.example.watchex.controller.Admin;

import com.example.watchex.domain.User;
import com.example.watchex.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AuthController {
    AuthenticationManager authenticationManager;


    @Autowired
    UserService userService;

    @PostMapping("/admin/in")
    public String generateToken(@RequestBody User data) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(data.getEmail(), data.getPassword()));
            List<String> roleList = new ArrayList<>();
            String token = "1";

            Map<Object, Object> model = new HashMap<>();
            model.put("client", data.getEmail());
            model.put("token", token);
            model.put("success", true);
            return "redirect:/admin/users";
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username or password");
        }
    }
}