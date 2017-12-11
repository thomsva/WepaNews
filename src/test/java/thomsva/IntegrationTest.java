/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package thomsva;

import java.time.LocalDateTime;
import javax.transaction.Transactional;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import thomsva.domain.Author;
import thomsva.domain.Category;
import thomsva.domain.Hit;
import thomsva.domain.NewsItem;
import thomsva.repository.HitRepository;
import thomsva.repository.NewsItemRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class IntegrationTest {

    @Autowired
    private NewsItemRepository newsItemRepository;

    @Autowired
    private HitRepository hitRepository;

    @Test
    public void testCanAddCategoryToNewsItem() {
        NewsItem newsItem = new NewsItem();
        Category category = new Category();
        newsItem.getCategories().add(category);
        assertEquals(1, newsItem.getCategories().size());

    }

    @Test
    public void testCanAddAuthorToNewsItem() {
        NewsItem newsItem = new NewsItem();
        Author author = new Author();
        newsItem.getAuthors().add(author);
        assertEquals(1, newsItem.getAuthors().size());
   

    }

}
