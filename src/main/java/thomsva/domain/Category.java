// Category (id, name, newsItems)
package thomsva.domain;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.validation.constraints.NotEmpty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

@Data
@NoArgsConstructor
@Entity
public class Category extends AbstractPersistable<Long> {
    

    private String name;

    @ManyToMany(mappedBy = "categories", cascade = CascadeType.PERSIST)
    private List<NewsItem> newsItems = new ArrayList<>();

}
