package thomsva.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thomsva.domain.Author;
import thomsva.repository.AuthorRepository;
import thomsva.service.AuthenticationService;

@Controller
public class AuthorController {




    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/author/login")
    public String loginAuthor(
            RedirectAttributes redirectAttributes,
            @RequestParam String name,
            @RequestParam String password
    ) {
        if (authenticationService.login(name, password)) {
            redirectAttributes.addFlashAttribute("message", "Tervetuloa takaisin, " + authenticationService.nameSignedIn());
            return "redirect:/author";
        } else {
            redirectAttributes.addFlashAttribute("error", "väärää salalasananssd");

            return "redirect:/author/login";
        }
    }

    @PostMapping("/author/signup")
    public String addAuthor(
            RedirectAttributes redirectAttributes,
            @RequestParam String name,
            @RequestParam String password,
            @RequestParam String verifyPassword) {
        Author author = new Author();
        author.setName(name);
        author.setPassword(password);
        author.setVerifyPassword(verifyPassword);

        boolean valid = true;
        if (!author.getPassword().equals(author.getVerifyPassword())) {
            valid = false;
            redirectAttributes.addFlashAttribute("error", "Salasanat eivät täsmää.");
        }
        if (valid) {
            redirectAttributes.addFlashAttribute("message", "Käyttäjäksi rekisteröinti onnistui");
            authorRepository.save(author);
            authenticationService.login(author.getName(), author.getPassword());
            return "redirect:/author";
        } else {
            return "signup";
        }

    }

    @GetMapping("/author")
    public String getAuthors(Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("nameSignedIn", authenticationService.nameSignedIn());
        return "authors";
    }

    @GetMapping("/author/signup")
    public String showSignup(Model model) {
        return "signup";
    }

    @GetMapping("/author/login")
    public String showLogin(Model model) {
        return "login";
    }

    @GetMapping("/author/logout")
    public String logoutAuthor(Model model) {
        authenticationService.logout();
        model.addAttribute("message", "Olet kirjautunut ulos.");
        return "login";
    }

}
