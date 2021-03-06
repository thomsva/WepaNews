
package thomsva.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import thomsva.domain.Author;


public interface AuthorRepository extends JpaRepository<Author, Long> {
    
    Author findByName(String name);
    List<Author> findBychiefEditor (Boolean chiefEditor);
}

