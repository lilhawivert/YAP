package com.vikgoj.webtech2;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.vikgoj.webtech2.Entities.TestUser;
import com.vikgoj.webtech2.Entities.User;
import com.vikgoj.webtech2.Entities.Video;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ControllerMain {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    

    @GetMapping("/register")
    public String getRegister(Model model) {
        if (!model.containsAttribute("user"))
            model.addAttribute("user", new User());
        return "register";
    }

    

    @GetMapping("/room")
    public String createRoom(Model model) {
        String id = UUID.randomUUID().toString();
        model.addAttribute("roomId", id);
        // model.addAttribute("username", getUser().getUsername());
        return "room"; 
    }

    @PostMapping("/room")
    public String joinRoom(@RequestBody String id, Model model) {
        model.addAttribute("roomId", id.substring(3, id.length()));
        // model.addAttribute("username", getUser().getUsername());
        return "room"; 
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        if (!model.containsAttribute("user"))
            model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public boolean postLogin(@RequestBody TestUser user) {
        System.out.println("HERE! " + user.getUsername() + " " + user.getPassword());
        return false;
    }

}
