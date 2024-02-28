package com.vikgoj.webtech2;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import com.vikgoj.webtech2.Entities.Authority;
import com.vikgoj.webtech2.Entities.User;
import com.vikgoj.webtech2.Entities.Video;

@Controller
public class ControllerMain {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;
    private UserDetails getLoggedInUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/")
    public String getHomepage(Model model) {
        model.addAttribute("user", getUser());
        model.addAttribute("videos", getVideosFromDB());
        return "homepage";
    }

    private Map<String, Integer> getVideosFromDB() { 
        Map<String, Integer> videos = new HashMap<String, Integer>();
        for (Video video : videoRepository.findAll()) {
            if(!video.getUsername().equals(getUser().getUsername())) videos.put(video.getUrl(), video.getPoints());
        }
        
        return videos;

    }

    @GetMapping("/getuser")
    @ResponseBody
    public User getUser() {
        return userRepository.findByUsername(getLoggedInUserDetails().getUsername());
    }

    @GetMapping("/register")
    public String getRegister(Model model) {
        if (!model.containsAttribute("user"))
            model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String postRegister(@ModelAttribute User user, Model model, BCryptPasswordEncoder encoder) {
        user.setPassword(encoder.encode(user.getPassword()));
        user.setEnabled(true);
        user.setPoints(0);
        if (!userRepository.existsByUsername(user.getUsername())) {
            userRepository.save(user);
            Authority authority = new Authority(user.getUsername(), "ROLE_USER");
            System.out.println(authority.getAuthority());
            authoritiesRepository.save(authority);
        } else {
            model.addAttribute("error", true);
            return "register";
        }

        model.addAttribute("success", true);
        return "redirect:/login";
    }

    @GetMapping("/room")
    public String createRoom(Model model) {
        String id = UUID.randomUUID().toString();
        model.addAttribute("roomId", id);
        model.addAttribute("username", getUser().getUsername());
        return "room"; 
    }

    @PostMapping("/room")
    public String joinRoom(@RequestBody String id, Model model) {
        model.addAttribute("roomId", id.substring(3, id.length()));
        model.addAttribute("username", getUser().getUsername());
        return "room"; 
    }

    @GetMapping("/login")
    public String getLogin(Model model) {
        if (!model.containsAttribute("user"))
            model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String postLogin(@ModelAttribute User user, Model model, BCryptPasswordEncoder encoder,
            OAuthResponse oAuthResponse) {
        if (!userRepository.existsByUsername(user.getUsername()) || !encoder.matches(user.getPassword(),
                userRepository.findByUsername(user.getUsername()).getPassword())) {
            model.addAttribute("error", true);
            return "login";
        }
        user = userRepository.findByUsername(user.getUsername());
        model.addAttribute("user", user);
        if (user.getoAuth() == null) {
            return "redirect:/oauth?username=" + user.getUsername();
        }

        return "redirect:/";
    }

}
