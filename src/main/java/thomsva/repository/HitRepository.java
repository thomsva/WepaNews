package thomsva.repository;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import thomsva.domain.Hit;
import thomsva.domain.NewsItem;
import org.springframework.data.jpa.repository.Query;

public interface HitRepository extends JpaRepository<Hit, Long> {

    List<Hit> findByDateTimeAfter(LocalDateTime datetime);


}
            

