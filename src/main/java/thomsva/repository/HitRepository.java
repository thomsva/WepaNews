
package thomsva.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import thomsva.domain.Hit;
import thomsva.domain.NewsItem;


public interface HitRepository extends JpaRepository<Hit, Long>{
    
    List<Hit> findByDateTimeAfter(LocalDateTime datetime);
    List<Hit> findByNewsItemAndDateTimeAfter(NewsItem newsItem, LocalDateTime datetime);
    
}
