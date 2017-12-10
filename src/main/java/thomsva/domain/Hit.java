package thomsva.domain;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@NoArgsConstructor
@Entity
public class Hit extends AbstractPersistable<Long> {

    @ManyToOne
    NewsItem newsItem;

    private LocalDateTime dateTime;

}
