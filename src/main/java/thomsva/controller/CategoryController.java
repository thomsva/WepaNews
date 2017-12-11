
package thomsva.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thomsva.domain.Category;
import thomsva.repository.CategoryRepository;
import thomsva.service.AuthenticationService;

@Controller
public class CategoryController {
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    @Autowired
    private AuthenticationService authenticationService;
    
    @GetMapping("/category")
    public String showCategories(Model model) {
        model.addAttribute("category", new Category());
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("authorSignedIn", authenticationService.authorSignedIn());
        return "category";
    }
    
    @PostMapping("/category")
    public String addCategory(
            RedirectAttributes redirectAttributes,
            @ModelAttribute Category category){
        categoryRepository.save(category);
        redirectAttributes.addFlashAttribute("message", "Tallennettiin "+category.getId()+".");
        return "redirect:/category";
    }
    
    @GetMapping("/category/{id}")
    public String showCategories(Model model, @PathVariable Long id) {
        model.addAttribute("category", categoryRepository.getOne(id));
        model.addAttribute("categories", categoryRepository.findAll());
        model.addAttribute("authorSignedIn", authenticationService.authorSignedIn());
        return "category";
    }
    
    @DeleteMapping("/category/{id}")
    public String deleteCategory(
            RedirectAttributes redirectAttributes,
            @PathVariable Long id){
        categoryRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Kategoria poistettu");
        return "redirect:/category";
    }
    

    
}
