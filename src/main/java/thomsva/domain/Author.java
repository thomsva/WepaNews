
package thomsva.domain;

//(id, name, newsitems, password, admin)

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Author extends AbstractPersistable<Long>{
    
    private String name; 
    
    @ManyToMany(mappedBy = "authors")
    private List<NewsItem> newsItems;
    
    @OneToMany(mappedBy="approvedBy")
    private List<NewsItem> approvedNewsItems;
    
    private String password;
    private String verifyPassword;
    
    private Boolean chiefEditor=false;
    

    
}
