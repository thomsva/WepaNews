
package thomsva.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import thomsva.repository.AuthorRepository;
import thomsva.repository.NewsItemRepository;
import thomsva.service.AuthenticationService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import thomsva.domain.NewsItem;

@Controller
public class NewsItemController {
    
    @Autowired
    private NewsItemRepository newsItemRepository;
    
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthenticationService authenticationService;
    
    
    @GetMapping("/")
    public String showFrontpage(Model model){
        return "index";
    }
    
    @GetMapping("/newsitem")
    public String showNewsItems(Model model){
        
        model.addAttribute("newsItems", newsItemRepository.findAll());
        return "newsitem";
    }
    
    @GetMapping(path = "/newsitem/{id}/picture", produces = "image/jpg")
    @ResponseBody
    public byte[] get(@PathVariable Long id) {
        return newsItemRepository.getOne(id).getPicture();
    }
    
    @PostMapping("/newsitem")
    public String addNewsItem (
            @RequestParam String heading,
            @RequestParam("picture") MultipartFile picture, 
            @RequestParam String lede,
            @RequestParam String text) throws IOException {
        NewsItem newsItem=new NewsItem();
        newsItem.setHeading(heading);
        newsItem.setPicture(picture.getBytes());
        newsItem.setLede(lede);
        newsItem.setText(text);
        newsItem.setApproved(false);
        newsItem.setDateTime(LocalDateTime.now());
        
        newsItemRepository.save(newsItem);
        return "redirect:/newsitem";
        
    }
    
    
}
