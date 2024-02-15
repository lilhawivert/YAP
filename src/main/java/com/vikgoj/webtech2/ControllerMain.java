package com.vikgoj.webtech2;

import java.time.LocalTime;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.ModelAndView;

import com.vikgoj.webtech2.Entities.Authority;
import com.vikgoj.webtech2.Entities.IncomingMessage;
import com.vikgoj.webtech2.Entities.LikeResponse;
import com.vikgoj.webtech2.Entities.OutgoingMessage;
import com.vikgoj.webtech2.Entities.User;
import com.vikgoj.webtech2.Entities.Video;

import reactor.core.publisher.Mono;

@Controller
public class ControllerMain {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private VideoRepository videoRepository;

    @Autowired
    private AuthoritiesRepository authoritiesRepository;

    @Value("${oauth.clientId}")
    private String clientId;

    @Value("${oauth.apiKey}")
    private String apiKey;

    @Value("${oauth.clientSecret}")
    private String clientSecret;

    @Value("${oauth.redirectURI}")
    private String redirectUri;

    @Value("${oauth.scope}")
    private String scope;

    private UserDetails getLoggedInUserDetails() {
        return (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @GetMapping("/api")
    @ResponseBody
    public String getApi() {
        return clientId;
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

    private LikeResponse getLikeResponse(String videoId) {
        String url = "https://www.googleapis.com/youtube/v3/videos/getRating?id=" + videoId;

        WebClient.Builder builder = WebClient.builder();
        

        String token = "Bearer " + getUser().getoAuth();
        LikeResponse response = builder.build().get().uri(url)
        .header("Authorization", token)
        .retrieve().bodyToMono(LikeResponse.class).block(); 
        

        return response;
    }

    private void likeVideo(String videoId) {
        String url = "https://www.googleapis.com/youtube/v3/videos/rate?id=" + videoId + "&rating=like";
        WebClient.Builder builder = WebClient.builder();
        String token = "Bearer " + getUser().getoAuth();
        LikeResponse response = builder.build().post().uri(url).header("Authorization", token).retrieve().bodyToMono(LikeResponse.class).block();
    }

    private void reducePointsAndUpdate(Video video) {
        User uploader = userRepository.findByUsername(video.getUsername());
        uploader.setPoints(uploader.getPoints()-video.getPoints());
        userRepository.save(uploader);
        updateVideosFromUploader(uploader);
    }

    private void updateVideosFromUploader(User uploader) {
        List<Video> videos = videoRepository.findAllByUsername(uploader.getUsername());
        for (Video video : videos) {
            if(video.getPoints() > uploader.getPoints()) {
                videoRepository.deleteById(video.getUrl());
            }
        }
    }

    private void givePointsToLiker(User user, Video video) {
        getUser().setPoints(getUser().getPoints() + video.getPoints());
        userRepository.save(getUser());
    }

    private boolean isLikeable(User user, Video video) {
        if(!user.isOauthValid()) return false;
        LikeResponse response = getLikeResponse(video.getUrl());
        return !response.getItems()[0].getRating().equals("like") && video != null && !video.getUsername().equals(user.getUsername());
    }

    @GetMapping("/like/{videoId}")
    public String videoLiked(@PathVariable String videoId) {
        Video video = videoRepository.findByUrl(videoId);
        if(!isLikeable(getUser(), video)) return "redirect:/";
        likeVideo(videoId);
        reducePointsAndUpdate(video);
        givePointsToLiker(getUser(), video);
        return "redirect:/";
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

    @GetMapping("/new")
    public String getNewVideo(Model model) {
        model.addAttribute("user", getUser());
        model.addAttribute("video", new Video());
        return "newvideo";
    }

    private boolean videoIsValid(String url) {
        WebClient.Builder builder = WebClient.builder();
        String REQUEST_URL = "https://youtube.googleapis.com/youtube/v3/videos?part=snippet&id=" + url + "&key=" + apiKey;
        YoutubeResponse apiResponse = builder.build().get().uri(REQUEST_URL).retrieve().bodyToMono(YoutubeResponse.class).block();
        return apiResponse.getPageInfo().get("totalResults") != 0;
    }

    @PostMapping("/new")
    public String postNewVideo(@ModelAttribute Video video, Model model) {
        video.setUsername(getUser().getUsername());
        if(video.getPoints() <= 0 || !videoIsValid(video.getUrl())) return "redirect:/new";
        videoRepository.save(video);
        model.addAttribute("user", getUser());
        return "redirect:/";
    }

    @GetMapping("/free") //shh
    public String getFreePoints(Model model) {
        User user = userRepository.findByUsername(getUser().getUsername());
        user.setPoints(user.getPoints() + 1000);
        userRepository.save(user);
        model.addAttribute("user", user);
        return "redirect:/";
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

    @GetMapping("/oauth")
    @ResponseBody
    public ModelAndView getOauth() {
        final String REQUEST_URL = "https://accounts.google.com/o/oauth2/v2/auth?scope="+scope+"&access_type=offline&include_granted_scopes=true&response_type=code&state=state_parameter_passthrough_value&redirect_uri="+redirectUri+"&client_id="+clientId;
        return new ModelAndView("redirect:" + REQUEST_URL);
    }

    @GetMapping("/login/oauth2/code/google")
    public String getAuth(@RequestParam String code, Model model) {
        final String REQUEST_URL = "https://oauth2.googleapis.com/token?code=" + code
                + "&redirect_uri=" + redirectUri + "&client_id="+clientId+"&client_secret=" + clientSecret + "&scope=&grant_type=authorization_code";
        WebClient.Builder builder = WebClient.builder();
        OAuthResponse oAuthResponse = builder.build().post().uri(REQUEST_URL).retrieve().bodyToMono(OAuthResponse.class)
                .block();
        User user = getUser();
        user.setoAuth(oAuthResponse.getAccess_token());
        user.setOauthTime(java.sql.Time.valueOf(LocalTime.now()));
        userRepository.save(user);
        model.addAttribute("oauthSuccess", true);
        return "redirect:/";
    }

}
