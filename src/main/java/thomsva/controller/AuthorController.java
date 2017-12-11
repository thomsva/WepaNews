package thomsva.controller;

import java.util.List;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    @GetMapping("/author")
    public String getAuthors(Model model) {
        model.addAttribute("authors", authorRepository.findAll());
        model.addAttribute("authorSignedIn", authenticationService.authorSignedIn());
        return "authors";
    }

    @GetMapping("/author/signup")
    public String showSignup(Model model) {
        return "signup";
    }

    @PostMapping("/author/signup")
    public String addAuthor(
            RedirectAttributes redirectAttributes,
            Model model,
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
            model.addAttribute("error", "Salasanat eivät täsmää.");
        }
        if (valid) {
            redirectAttributes.addFlashAttribute("message",
                    "Terveruloa uusi kirjoittaja " + author.getName()
                            + ". Järjestelmä kirjasi sinut sisään.");
            authorRepository.save(author);
            authenticationService.login(author.getName(), author.getPassword());
            
            return "redirect:/author";
        } else {
            return "signup";
        }
    }

    @GetMapping("/author/login")
    public String showLogin(Model model) {
        return "login";
    }

    @PostMapping("/author/login")
    public String loginAuthor(
            RedirectAttributes redirectAttributes,
            @RequestParam String name,
            @RequestParam String password) {
        if (authenticationService.login(name, password)) {
            redirectAttributes.addFlashAttribute("message", "Tervetuloa takaisin " + authenticationService.nameSignedIn() + ".");
            return "redirect:/author";
        } else {
            redirectAttributes.addFlashAttribute("error", "Virheellinen nimi tai salalasana.");
            return "redirect:/author/login";
        }
    }

    @GetMapping("/author/logout")
    public String logoutAuthor(Model model) {
        authenticationService.logout();
        model.addAttribute("message", "Olet kirjautunut ulos.");
        return "login";
    }

    @DeleteMapping("/author/{id}")
    public String removeAuthor(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Author author = authorRepository.getOne(id);
        //The last ChiefEditor can't be deleted
        //Only ChiefEditors can delete authors. 
        //Only authors without NewsItems can be deleted. 
        if (author.getChiefEditor()&&authorRepository.findBychiefEditor(true).size() <= 1) {
            redirectAttributes.addFlashAttribute("error", "Päätoimittajia on oltava vähintään 1kpl.");
        } else {
            if (!authenticationService.authorSignedIn().getChiefEditor()) {
                redirectAttributes.addFlashAttribute("error", "Vain päätoimittajat voivat poistaa kirjoittajia.");
            } else {
                if (author.getNewsItems().size()>0) {
                    redirectAttributes.addFlashAttribute("error", "Kirjoittajaa ei voi poistaa koska kirjoittajalla on uutisia.");
                } else {
                    authorRepository.deleteById(id);
                    redirectAttributes.addFlashAttribute("message", "Kirjoittajan poisto onnistui.");
                }
            }
        }
        return "redirect:/author";
    }

    @GetMapping("/author/{id}/password")
    public String showPasswordForm(Model model, @PathVariable Long id) {
        Author author = authorRepository.getOne(id);
        model.addAttribute(author);
        if (Objects.equals(authenticationService.authorSignedIn().getId(), id)) {
            return "password";
        } else {
            model.addAttribute("error", "Ei oikeuksia toisen kirjoittajan tietoihin.");
            return "author";
        }
    }

    @PostMapping("/author/{id}/password")
    public String updatePassword(
            RedirectAttributes redirectAttributes,
            @PathVariable Long id,
            @RequestParam String password,
            @RequestParam String verifyPassword) {
        Author author = authorRepository.getOne(id);
        if (Objects.equals(authenticationService.authorSignedIn().getId(), id)) {
            if (!author.getPassword().equals(author.getVerifyPassword())) {
                redirectAttributes.addFlashAttribute("error", "Salasanat eivät täsmää.");
                return "redirect:/author/{" + id + "}/password";
            } else {
                author.setPassword(password);
                author.setVerifyPassword(verifyPassword);
                authorRepository.save(author);
                redirectAttributes.addAttribute("message", "Salasana vaihdettu.");
            }
        } else {
            redirectAttributes.addAttribute("error", "Ei oikeuksia toisen kirjoittajan salsanaan.");
        }
        return "redirect:/author";
    }

    @PostMapping("/author/{id}/promote")
    public String promoteAuthor(
            RedirectAttributes redirectAttributes,
            @PathVariable Long id) {
        Author author = authorRepository.getOne(id);
        if (!authenticationService.authorSignedIn().getChiefEditor()) {
            redirectAttributes.addFlashAttribute("error", "Ei oikeuksia.");
        } else {
            author.setChiefEditor(true);
            authorRepository.save(author);
            redirectAttributes.addFlashAttribute("message", "Kirjoittajan " + author.getName() + " oikeudet päivitetty.");
        }
        return "redirect:/author";
    }

    @PostMapping("/author/{id}/demote")
    public String demoteAuthor(
            RedirectAttributes redirectAttributes,
            @PathVariable Long id) {
        Author author = authorRepository.getOne(id);
        if (authorRepository.findBychiefEditor(true).size() <= 1) {
            redirectAttributes.addFlashAttribute("error", "Päätoimittajia on oltava vähintään 1kpl.");
        } else {
            if (!authenticationService.authorSignedIn().getChiefEditor()) {
                redirectAttributes.addFlashAttribute("error", "Ei oikeuksia.");
            } else {
                author.setChiefEditor(false);
                authorRepository.save(author);
                redirectAttributes.addFlashAttribute("message", "Kirjoittajan " + author.getName() + " oikeudet päivitetty.");
            }
        }
        return "redirect:/author";
    }

}
