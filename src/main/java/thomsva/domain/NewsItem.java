
package thomsva.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Data
@NoArgsConstructor
@Entity
public class NewsItem extends AbstractPersistable<Long>{
    
    private String heading;
    @Lob
    private byte[] picture;
    private String lede;
    private String text;
    
    private LocalDateTime dateTime;
    
    @ManyToMany (cascade = CascadeType.PERSIST)
    private List<Category> categories=new ArrayList<>();;
    
    @ManyToMany
    private List<Author> authors=new ArrayList<>();
    
    private boolean approved;
    
    @ManyToOne
    private Author approvedBy;
    
    @OneToMany(mappedBy="newsItem")
    private List<Hit> hits=new ArrayList<>();
    


}
