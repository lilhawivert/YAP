package com.vikgoj.webtech2;


import java.util.*;
import java.util.stream.Collectors;

import com.vikgoj.webtech2.Entities.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vikgoj.webtech2.Exceptions.LoginException;
import com.vikgoj.webtech2.Repositories.CommentLikeRepository;
import com.vikgoj.webtech2.Repositories.CommentRepository;
import com.vikgoj.webtech2.Repositories.DMRepository;
import com.vikgoj.webtech2.Repositories.FollowsRepository;
import com.vikgoj.webtech2.Repositories.LikeRepository;
import com.vikgoj.webtech2.Repositories.UserRepository;
import com.vikgoj.webtech2.Repositories.YapRepository;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@CrossOrigin(origins = "http://localhost:4200")
public class ControllerMain {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private YapRepository yapRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    
    @Autowired
    private FollowsRepository followsRepository;
    
    @Autowired
    private DMRepository dmRepository;

    @GetMapping("/health")
    public String getHealth() {
        return "OK";
    }

    @PostMapping("/login")
    @ResponseBody
    public User postLogin(@RequestBody User user) throws LoginException {
        if(!userRepository.existsByUsername(user.getUsername())) throw new LoginException(); 
        User userFromRepo = userRepository.findByUsername(user.getUsername());
        if(userFromRepo.getPassword().equals(helper.encodeString(user.getPassword()))) {
            user = userRepository.findByUsername(user.getUsername());
            return user;
        }
        else throw new LoginException(); 
    }

    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity postRegister(@RequestBody User user) {
        if(!userRepository.existsByUsername(user.getUsername())) {
            user.setPassword(helper.encodeString(user.getPassword()));
            followsRepository.save(new Follow(user.getUsername(), user.getUsername()));
            userRepository.save(user);
            return new ResponseEntity<>(HttpStatus.OK);
        }
        else return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @PostMapping("/yap")
    @ResponseBody
    public String postYap(@RequestBody Yap yap) {
        Yap saved = yapRepository.save(yap);
        return String.valueOf(saved.getId());
    }

    @PostMapping("/yaps/{maxYaps}")
    @ResponseBody
    public List<Yap> getYaps(@RequestBody String username,@PathVariable String maxYaps) {
        List<String> followedUsernames = followsRepository.findAllByUserWhoFollows(username).stream().map(Follow::getUserWhosFollowed).toList();
        List<Yap> yaps = new ArrayList<Yap>();
        followedUsernames.forEach(followedUsername -> {
            yaps.addAll(yapRepository.findAllByUsername(followedUsername));
        });
        yaps.addAll(yapRepository.findAllByUsername(username));
        Collections.reverse(yaps);
        List<Yap> ret = yaps.subList(0, Math.min(Integer.parseInt(maxYaps), yaps.size()));
        System.out.println("t1");
        return ret;
    }
    
    @PostMapping("/yap/{id}")
    @ResponseBody
    public Yap getYap(@PathVariable String id, @RequestBody String username) {
        Yap yap = yapRepository.findById(Long.parseLong(id)).get();
        yap.setLiked(likeRepository.existsByUserThatLikedAndYapId(username, Long.parseLong(id)));
        yap.getComments().stream().filter(comment -> commentLikeRepository.existsByUserThatLikedAndCommentId(username, comment.getId())).forEach(comment -> {
            comment.setLiked(true);
        });
        return yap;
    }

    @PostMapping("/yap/{id}/comment")
    @ResponseBody
    public Long postComment(@PathVariable String id, @RequestBody Comment comment) {
        Yap yap = yapRepository.findById(Long.parseLong(id)).get();
        yap.getComments().add(comment);
        Long ret = commentRepository.save(comment).getId();
        yapRepository.save(yap);
        return ret;
        // return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/yap/{id}/comment")
    @ResponseBody
    public List<Comment> getYapComments(@PathVariable String id) {
        return commentRepository.findAllByYap(yapRepository.findById(Long.parseLong(id)).get());
    }

    @PostMapping("/yap/{id}/like")
    @ResponseBody
    public ResponseEntity likeYap(@PathVariable String id, @RequestBody String username) {
        Yap yap = yapRepository.findById(Long.parseLong(id)).get();
        boolean liked = likeRepository.existsByUserThatLikedAndYapId(username, Long.parseLong(id));
        if(liked) { 
            yap.setLikes(yap.getLikes()-1);
            likeRepository.deleteByUserThatLikedAndYapId(username, yap.getId());
        }
        else if(!liked) { 
            yap.setLikes(yap.getLikes()+1);
            likeRepository.save(new Like(yap.getId(), username));
        }
        yapRepository.save(yap);
        return new ResponseEntity<>(HttpStatus.OK); 
    }

    @PostMapping("/yap/{id}/comment/{commentId}")
    @ResponseBody
    public ResponseEntity likeComment(@PathVariable String id, @PathVariable String commentId, @RequestBody String username) {
        Yap yap = yapRepository.findById(Long.parseLong(id)).get();
        Comment comment = commentRepository.findByYapAndId(yap, Long.parseLong(commentId));
        boolean liked = commentLikeRepository.existsByUserThatLikedAndCommentId(username, Long.parseLong(commentId));
        if(liked) { 
            comment.setLikes(comment.getLikes()-1);
            commentLikeRepository.deleteByUserThatLikedAndCommentId(username, Long.parseLong(commentId));
        }
        else if(!liked) { 
            comment.setLikes(comment.getLikes()+1);
            commentLikeRepository.save(new CommentLike(Long.parseLong(commentId), username));
        }
        // yapRepository.save(yap);
        return new ResponseEntity<>(HttpStatus.OK);
    }
    
    @DeleteMapping("/yap/{id}/comment/{commentId}")
    @ResponseBody
    public ResponseEntity deleteComment(@PathVariable String id, @PathVariable String commentId) {
        Yap yap = yapRepository.findById(Long.parseLong(id)).get();
        Comment comment = commentRepository.findByYapAndId(yap, Long.parseLong(commentId));
        comment.setMessage("[deleted]");
        comment.setDeleted(true);
        commentRepository.save(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/userExists/{userName}")
    @ResponseBody
    public Boolean getUserExists(@PathVariable String userName) {
        return userRepository.existsByUsername(userName);
    }

    @PostMapping("/changeProfilePicture/{userName}")
    @ResponseBody
    public ResponseEntity changeProfilePicture(@PathVariable String userName, @RequestBody String newPicture){
        User user = userRepository.findByUsername(userName);
        user.setProfilePic(newPicture);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/changeUserName/{userName}")
    @ResponseBody
    public ResponseEntity changeUserName(@PathVariable String userName, @RequestBody String newUsername){
        User user = userRepository.findByUsername(userName);
        user.setUsername(newUsername);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping("/changePassword/{userName}")
    @ResponseBody
    public ResponseEntity changePassword(@PathVariable String userName, @RequestBody String newPassword){
        User user = userRepository.findByUsername(userName);
        user.setPassword(helper.encodeString(newPassword));
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @DeleteMapping("/yap/{id}")
    @ResponseBody
    public ResponseEntity deleteYap(@PathVariable String id) {
        Yap yap = yapRepository.findById(Long.parseLong(id)).get();
        commentRepository.deleteAllByYap(yap);
        yapRepository.deleteById(Long.parseLong(id));
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/yaps/{username}")
    @ResponseBody
    public List<Yap> getYapsForUsername(@PathVariable String username) {
       return yapRepository.findAllByUsername(username);
    }

    @GetMapping("/{userWhosFollowed}/follow")
    @ResponseBody
    public Boolean getYapsForUsername(@PathVariable String userWhosFollowed, @RequestParam String userWhoFollows) {
       return followsRepository.existsByUserWhosFollowedAndUserWhoFollows(userWhosFollowed, userWhoFollows);
    }

    @PostMapping("/{userWhosFollowed}/follow")
    @ResponseBody
    public Boolean follow(@PathVariable String userWhosFollowed, @RequestBody String userWhoFollows) {
        if(followsRepository.existsByUserWhosFollowedAndUserWhoFollows(userWhosFollowed, userWhoFollows)) {
            followsRepository.deleteByUserWhosFollowedAndUserWhoFollows(userWhosFollowed, userWhoFollows);
            return false;
        }
        else {
            followsRepository.save(new Follow(userWhosFollowed, userWhoFollows));
            return true;
        }
    }

    @GetMapping("/getUser/{username}")
    @ResponseBody
    public User getUserForUsername(@PathVariable String username) {
        return userRepository.findByUsername(username);
    }

    @PostMapping("/getUsersOfYaps")
    @ResponseBody
    public User[] getUsersOfYaps(@RequestBody Yap[] yaps) {
        long startTime = System.currentTimeMillis();
        System.out.println("s1");
        User[] users = new User[yaps.length];
        for (int i = 0; i < users.length; i++) {
            users[i] = userRepository.findByUsername(yaps[i].getUsername());
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = (stopTime - startTime) ;
        System.out.println("s2");
        System.out.println("Vergangene Zeit: " + elapsedTime + " 2");
        return users;
    }

    @GetMapping("/getUserById/{userId}")
    @ResponseBody
    public User getUserById(@PathVariable String userId) {
        return userRepository.getUserById(Long.parseLong(userId));
    }

    @GetMapping("/getUsersByUsernamePartial/{username}")
    @ResponseBody
    public List<User> getUsersByUsernamePartial(@PathVariable String username) {
        return userRepository.findAllByUsernameContaining(username);
    }

    @GetMapping("/getUsersByUsernamePartial/")
    @ResponseBody
    public List<User> getAllUsersByUsername() {
        return userRepository.findAll();
    }

    @GetMapping("/getTrends")
    @ResponseBody
    public List<String> getTrends() {
        System.out.println("getting trends");
        Yap[] yaps = yapRepository.findAll().toArray(new Yap[0]);
        ArrayList<String> ret = new ArrayList<>();
        for (Yap yap : yaps) {
            ret.addAll(helper.extractHashtags(yap.getMessage()));
        }
        return ret;
    }

    @GetMapping("/getYapsOfTrend/{trend}")
    @ResponseBody
    public List<Yap> getYapsOfTrend(@PathVariable String trend) {
        System.out.println("getting trend yaps");
        return yapRepository.findAll()
                .stream()
                .filter(yap -> yap.getMessage().contains("#" + trend))
                .collect(Collectors.toList());
    }

    @PostMapping("/changeBgColor/{userName}")
    @ResponseBody
    public ResponseEntity changeBgColor(@PathVariable String userName, @RequestBody Long bgColor){
        User user = userRepository.findByUsername(userName);
        user.setBgColor(bgColor);
        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    // @MessageMapping("/greet")
    // @SendTo("/topic/greetings")
    // public static String greeting(String msg) {
    //     return msg;
    // }

    @PostMapping("/dm")
    @ResponseBody
    public void postDM(@RequestBody DM dm) {
        System.out.println("what");
        dmRepository.save(dm);
    }

    @GetMapping("/dm")
    @ResponseBody
    public List<DM> getDMS(@RequestParam String user1, @RequestParam String user2) {
         
        List<DM> firstHalf = dmRepository.findAllBySenderAndReceiver(user1, user2);
        List<DM> secondHalf = dmRepository.findAllBySenderAndReceiver(user2, user1);
        firstHalf.addAll(secondHalf);

        return firstHalf;
    }
    
    

}
