// Category (id, name, newsItems)
package thomsva.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category extends AbstractPersistable<Long> {
    
    private String name; 
    
    @ManyToMany(mappedBy = "categories")
    private List<NewsItem> newsItems;
    
    
    
}
