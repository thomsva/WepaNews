// Category (id, name, newsItems)
package thomsva.domain;

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {
    
    @Id
    private Long id;
    private String name; 
    
    @ManyToMany(mappedBy = "categories")
    private List<NewsItem> newsItems;
    
    
    
}
