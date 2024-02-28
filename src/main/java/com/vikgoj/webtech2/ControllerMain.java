package com.vikgoj.webtech2;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.vikgoj.webtech2.Entities.User;
import com.vikgoj.webtech2.Entities.Yap;
import com.vikgoj.webtech2.Exceptions.LoginException;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ControllerMain {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private YapRepository yapRepository;
    
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


    @PostMapping("/login")
    public ResponseEntity postLogin(@RequestBody User user) throws LoginException {
        Optional<User> userFromRepo = userRepository.findById(user.getUsername());
        if(userFromRepo.isPresent() && userFromRepo.get().getPassword().equals(user.getPassword())) return new ResponseEntity<>(HttpStatus.OK);
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
    }

    @PostMapping("/register")
    public ResponseEntity postRegister(@RequestBody User user) {
        if(!userRepository.existsByUsername(user.getUsername())) {
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/yap")
    public ResponseEntity postYap(@RequestBody Yap yap) {
        this.yapRepository.save(yap);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    

}
