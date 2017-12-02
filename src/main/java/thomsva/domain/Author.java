
package thomsva.domain;

//(id, name, newsitems, password, admin)

import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Author {
    private Long id;
    private String name; 
    
    @ManyToMany(mappedBy = "authors")
    private List<NewsItem> newsItems;
    
    private String password;
    private Boolean admin;
    
   
    
}
