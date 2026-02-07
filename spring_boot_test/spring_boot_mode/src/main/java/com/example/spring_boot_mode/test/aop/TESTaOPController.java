package com.example.spring_boot_mode.test.aop;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/testAOP")
public class TESTaOPController {
    @Autowired
    UserService userService;
    @GetMapping("/getUserById")
    public boolean getUserById(@RequestParam("id") Long id) {
        return userService.getUserById(id);
    }
}
