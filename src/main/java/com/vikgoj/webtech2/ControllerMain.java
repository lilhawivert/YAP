package com.vikgoj.webtech2;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vikgoj.webtech2.Entities.Comment;
import com.vikgoj.webtech2.Entities.User;
import com.vikgoj.webtech2.Entities.Yap;
import com.vikgoj.webtech2.Exceptions.LoginException;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class ControllerMain {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private YapRepository yapRepository;

    @Autowired
    private CommentRepository commentRepository;
    
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
        yapRepository.save(yap);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/yap")
    public List<Yap> getYaps() {
        List<Yap> yaps = yapRepository.findAll();
        Collections.reverse(yaps);
        return yaps.subList(0, 10);
    }
    
    @GetMapping("/yap/{id}")
    public Yap getYap(@PathVariable String id) {
        Yap yap = yapRepository.findById(Long.parseLong(id)).get();
        return yap;
    }

    @PostMapping("/yap/{id}/comment")
    public ResponseEntity postComment(@PathVariable String id, @RequestBody Comment comment) {
        Yap yap = yapRepository.findById(Long.parseLong(id)).get();
        yap.getComments().add(comment);
        commentRepository.save(comment);
        yapRepository.save(yap);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/yap/{id}/comment")
    public List<Comment> getYapComments(@PathVariable String id) {
        return commentRepository.findAllByYap(yapRepository.findById(Long.parseLong(id)).get());
    }
    

}
