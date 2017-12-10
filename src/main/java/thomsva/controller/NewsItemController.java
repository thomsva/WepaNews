package thomsva.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import thomsva.repository.AuthorRepository;
import thomsva.repository.NewsItemRepository;
import thomsva.service.AuthenticationService;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import thomsva.domain.Author;
import thomsva.domain.Category;
import thomsva.domain.NewsItem;
import thomsva.repository.CategoryRepository;

@Controller
public class NewsItemController {

    @Autowired
    private NewsItemRepository newsItemRepository;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private AuthenticationService authenticationService;

    //Front page
    @GetMapping("/")
    public String showFrontpage(Model model) {
        Pageable pageableNewTop5 = PageRequest.of(0, 5, Sort.Direction.DESC, "dateTime");
        Pageable pageableNewTop25 = PageRequest.of(0, 20, Sort.Direction.DESC, "dateTime");
        Pageable pageableHitsTop25 = PageRequest.of(0, 20, Sort.Direction.DESC, "hits");

        model.addAttribute("newsItemsNewTop5", newsItemRepository.findByApproved(true, pageableNewTop5));
        model.addAttribute("newsItemsNewTop25", newsItemRepository.findByApproved(true, pageableNewTop25));
        model.addAttribute("newsItemsHitsTop25", newsItemRepository.findByApproved(true, pageableHitsTop25));
        return "index";
    }

    //Show newsitem
    @GetMapping("/{id}")
    public String showNewsItem(Model model, @PathVariable Long id) {
        Pageable pageableNewTop5 = PageRequest.of(0, 5, Sort.Direction.DESC, "dateTime");
        Pageable pageableNewTop25 = PageRequest.of(0, 20, Sort.Direction.DESC, "dateTime");
        Pageable pageableHitsTop25 = PageRequest.of(0, 20, Sort.Direction.DESC, "hits");
        NewsItem selectedNewsItem=newsItemRepository.getOne(id);
        selectedNewsItem.incrementHits();
        newsItemRepository.save(selectedNewsItem);
        

        model.addAttribute("newsItemsNewTop5", newsItemRepository.findByApproved(true, pageableNewTop5));
        model.addAttribute("newsItemsNewTop25", newsItemRepository.findByApproved(true, pageableNewTop25));
        model.addAttribute("newsItemsHitsTop25", newsItemRepository.findByApproved(true, pageableHitsTop25));
        model.addAttribute("selectedNewsItem", selectedNewsItem);
        return "index";
    }

    @GetMapping("/newsitem")
    public String showNewsItems(Model model) {
        if (authenticationService.authorSignedIn() != null) {
            ArrayList<Long> selectedAuthors = new ArrayList<>();
            selectedAuthors.add(authenticationService.authorSignedIn().getId());
            selectedAuthors.add(3L);
            model.addAttribute("newsItems", newsItemRepository.findAll());
            model.addAttribute("authors", authorRepository.findAll());
            model.addAttribute("categories", categoryRepository.findAll());
            model.addAttribute("authorSignedIn", authenticationService.authorSignedIn());
            return "newsitem";
        } else {
            model.addAttribute("error", "Kirjaudu ensin sisään.");
            return "redirect:/author/login";
        }
    }

    @Transactional
    @GetMapping(path = "/newsitem/{id}/picture", produces = "image/jpg")
    @ResponseBody
    public byte[] getPicture(@PathVariable Long id) {
        return newsItemRepository.getOne(id).getPicture();
    }

    @PostMapping("/newsitem")
    public String addNewsItem(
            @RequestParam String heading,
            @RequestParam("picture") MultipartFile picture,
            @RequestParam String lede,
            @RequestParam String text) throws IOException {
        NewsItem newsItem = new NewsItem();
        newsItem.setHeading(heading);
        newsItem.setPicture(picture.getBytes());
        newsItem.setLede(lede);
        newsItem.setText(text);
        Author a = authenticationService.authorSignedIn();
        if (a != null) {
            newsItem.getAuthors().add(a);
        }
        newsItem.setApproved(false);
        newsItem.setDateTime(LocalDateTime.now());

        newsItemRepository.save(newsItem);
        return "redirect:/newsitem";

    }

    @Transactional
    @DeleteMapping("/newsitem/{newsId}/author/{authorId}")
    public String removeAuthorFromNewsItem(
            RedirectAttributes redirectAttributes,
            @PathVariable Long newsId,
            @PathVariable Long authorId) {

        NewsItem n = newsItemRepository.getOne(newsId);
        List<Author> a = n.getAuthors();
        for (int i = 0; i < a.size(); i++) {
            if (Objects.equals(a.get(i).getId(), authorId)) {
                newsItemRepository.getOne(newsId).getAuthors().remove(i);
            }
        }
        newsItemRepository.save(n);
        redirectAttributes.addFlashAttribute("message", "Kirjoittajan poisto artikkelista onnistui!");
        redirectAttributes.addFlashAttribute("authorSignedIn", authenticationService.authorSignedIn());

        return "redirect:/newsitem";
    }

    @Transactional
    @PostMapping("/newsitem/{newsId}/author")
    public String addAuthorToNewsItem(
            RedirectAttributes redirectAttributes,
            @PathVariable Long newsId,
            @RequestParam Long authorId) {
        NewsItem n = newsItemRepository.getOne(newsId);
        n.getAuthors().add(authorRepository.getOne(authorId));
        newsItemRepository.save(n);
        redirectAttributes.addFlashAttribute("message", "Kirjoittaja lisättiin.");
        redirectAttributes.addFlashAttribute("authorSignedIn", authenticationService.authorSignedIn());
        return "redirect:/newsitem/";

    }

    @Transactional
    @DeleteMapping("/newsitem/{newsId}/category/{categoryId}")
    public String removeCategoryFromNewsItem(
            RedirectAttributes redirectAttributes,
            @PathVariable Long newsId,
            @PathVariable Long categoryId) {

        NewsItem n = newsItemRepository.getOne(newsId);
        List<Category> c = n.getCategories();
        if (c.size() == 1) {
            redirectAttributes.addFlashAttribute("error", "Ainutta kirjoittajaa ei voi poistaa.");
        } else {
            for (int i = 0; i < c.size(); i++) {
                if (Objects.equals(c.get(i).getId(), categoryId)) {
                    newsItemRepository.getOne(newsId).getCategories().remove(i);
                    break;
                }
            }
            newsItemRepository.save(n);
            redirectAttributes.addFlashAttribute("message", "Kategorian poisto artikkelista onnistui!");
        }
        redirectAttributes.addFlashAttribute("authorSignedIn", authenticationService.authorSignedIn());

        return "redirect:/newsitem";
    }

    @Transactional
    @PostMapping("/newsitem/{newsId}/category")
    public String addCategoryToNewsItem(
            RedirectAttributes redirectAttributes,
            @PathVariable Long newsId,
            @RequestParam Long categoryId) {
        NewsItem n = newsItemRepository.getOne(newsId);
        n.getCategories().add(categoryRepository.getOne(categoryId));
        newsItemRepository.save(n);
        redirectAttributes.addFlashAttribute("message", "Kategoria lisättiin.");
        redirectAttributes.addFlashAttribute("authorSignedIn", authenticationService.authorSignedIn());
        return "redirect:/newsitem/";

    }

    @DeleteMapping("/newsitem/{id}")
    public String removeNewsItem(
            RedirectAttributes redirectAttributes,
            @PathVariable Long id) {
        newsItemRepository.deleteById(id);
        redirectAttributes.addFlashAttribute("message", "Uutinen poistettiin.");
        redirectAttributes.addFlashAttribute("authorSignedIn", authenticationService.authorSignedIn());
        return "redirect:/newsitem/";

    }

    @PostMapping("/newsitem/{id}/approve")
    public String toggleNewsItemApproval(
            RedirectAttributes redirectAttributes,
            @PathVariable Long id) {
        NewsItem n = newsItemRepository.getOne(id);
        n.setApproved(!n.isApproved());
        newsItemRepository.save(n);
        if (n.isApproved()) {
            n.setApprovedBy(authenticationService.authorSignedIn());
            redirectAttributes.addFlashAttribute("message", "Uutinen hyväksyttiin ja on nyt julkaistu.");
        } else {
            redirectAttributes.addFlashAttribute("message", "Uutisen hyväksyntä peruttiin.");
        }
        redirectAttributes.addFlashAttribute("authorSignedIn", authenticationService.authorSignedIn());
        return "redirect:/newsitem/";

    }

}
