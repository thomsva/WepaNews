
package thomsva.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Data
@AllArgsConstructor
@Entity
public class NewsItem extends AbstractPersistable<Long>{
    
    private String heading;
    @Lob
    private byte[] picture;
    private String lede;
    private String text;
    
    private LocalDateTime dateTime;
    
    @ManyToMany
    private List<Category> categories;
    
    @ManyToMany
    private List<Author> authors;
    
    private boolean approved;
    
    @ManyToOne
    private Author approvedBy;
    
    private Long hits;
    
    public NewsItem(){
        this.categories = new ArrayList<>();
        this.authors = new ArrayList<>();
    }
      
}
