
package thomsva.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import thomsva.domain.Category;
import thomsva.domain.NewsItem;


public interface NewsItemRepository extends JpaRepository<NewsItem, Long> {
    
    List<NewsItem> findByDateTime(LocalDateTime dateTime);
    List<NewsItem> findByApproved(Boolean approved);
    List<NewsItem> findByApproved(Boolean approved, Pageable pageable);
    List<NewsItem> findByPopular(Long popular);

}
