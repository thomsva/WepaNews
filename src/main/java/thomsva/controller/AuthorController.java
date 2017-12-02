package thomsva.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import thomsva.domain.Author;
import thomsva.repository.AuthorRepository;

@Controller
public class AuthorController {
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @PostMapping("/authors")
    public String addAuthor(@RequestParam String name){
        Author author=new Author();
        author.setName(name);
        authorRepository.save(author);
        return "redirect:/authors"; 
    }
    
    @GetMapping("/authors")
    public String getAuthors(Model model){
        List<Author> authors=authorRepository.findAll();
        model.addAttribute(authors);
        return "authors";
    }
    
    @GetMapping("/test")
    @ResponseBody
    public String showHW(){
        return "It works";
    }
    
    @GetMapping("/")
    @ResponseBody
    public String home() {
        return "Hello World!";
    }
    
    
    
}
