package com.vikgoj.webtech2;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.vikgoj.webtech2.Entities.Comment;
import com.vikgoj.webtech2.Entities.CommentLike;
import com.vikgoj.webtech2.Entities.Like;
import com.vikgoj.webtech2.Entities.User;
import com.vikgoj.webtech2.Entities.Yap;
import com.vikgoj.webtech2.Exceptions.LoginException;
import com.vikgoj.webtech2.Repositories.CommentLikeRepository;
import com.vikgoj.webtech2.Repositories.CommentRepository;
import com.vikgoj.webtech2.Repositories.LikeRepository;
import com.vikgoj.webtech2.Repositories.UserRepository;
import com.vikgoj.webtech2.Repositories.YapRepository;

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

    @Autowired
    private LikeRepository likeRepository;
    
    @Autowired
    private CommentLikeRepository commentLikeRepository;
    
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
    public String postYap(@RequestBody Yap yap) {
        Yap saved = yapRepository.save(yap);
        return String.valueOf(saved.getId());
    }

    @GetMapping("/yap")
    public List<Yap> getYaps() {
        List<Yap> yaps = yapRepository.findAll();
        Collections.reverse(yaps);
        return yaps.subList(0, Math.min(10, yaps.size()));
    }
    
    @PostMapping("/yap/{id}")
    public Yap getYap(@PathVariable String id, @RequestBody String username) {
        Yap yap = yapRepository.findById(Long.parseLong(id)).get();
        yap.setLiked(likeRepository.existsByUserThatLikedAndYapId(username, Long.parseLong(id)));
        yap.getComments().stream().filter(comment -> commentLikeRepository.existsByUserThatLikedAndCommentId(username, comment.getId())).forEach(comment -> {
            comment.setLiked(true);
        });
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

    @PostMapping("/yap/{id}/like")
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
    public ResponseEntity deleteComment(@PathVariable String id, @PathVariable String commentId) {
        Yap yap = yapRepository.findById(Long.parseLong(id)).get();
        Comment comment = commentRepository.findByYapAndId(yap, Long.parseLong(commentId));
        comment.setMessage("[deleted]");
        comment.setDeleted(true);
        commentRepository.save(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    
    

}
