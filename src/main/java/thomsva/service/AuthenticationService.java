
package thomsva.service;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import thomsva.domain.Author;
import thomsva.repository.AuthorRepository;

@Service
public class AuthenticationService {
    
    @Autowired
    private HttpSession session;
    
    @Autowired
    private AuthorRepository authorRepository;
    
    @PostConstruct
    public void postConstruct() {
        if(authorRepository.findBychiefEditor(true)==null){
            Author author=new Author();
            author.setName("Admin");
            author.setPassword("123");
            author.setVerifyPassword("123");
            author.setChiefEditor(true);
            authorRepository.save(author);
        }
    }
    
    public String nameSignedIn(){
        return (String) session.getAttribute("nameSignedIn");
    }
    
    public Author authorSignedIn(){
        return authorRepository.findByName((String) session.getAttribute("nameSignedIn"));
    }
    
    public boolean login(String name, String password){
        Author author=authorRepository.findByName(name);
        if (author.getPassword().equals(password)){
            session.setAttribute("nameSignedIn", name);
            return true;
        }
        return false;        
    }
    
    public void logout(){
        this.session.invalidate();
    }
    
    

    
    
}
