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
import thomsva.repository.HitRepository;

@Data
@NoArgsConstructor
@Entity
public class NewsItem extends AbstractPersistable<Long> {

    private String heading;
    @Lob
    private byte[] picture;
    private String lede;
    private String text;

    private LocalDateTime dateTime;

    @ManyToMany(cascade = CascadeType.PERSIST)
    private List<Category> categories = new ArrayList<>();
    ;
    
    @ManyToMany
    private List<Author> authors = new ArrayList<>();

    private boolean approved;

    @ManyToOne
    private Author approvedBy;

    @OneToMany(mappedBy = "newsItem")
    private List<Hit> hits = new ArrayList<>();

    private Long popular=0L;

    public void calculatePopular() {
        Long okHits=0L;
        for (Hit hit : this.hits) {
            if (hit.getDateTime().isAfter(LocalDateTime.now().minusDays(7))) {
                okHits++;
            }
        }
        this.popular = okHits;
    }
    
    //Returns false if NewsItem is not valid
    public boolean validate(){
        boolean valid=true;
        if(this.heading.length()>50){
            //Heading must be less than 50 characters
            valid=false;
        }
        if(this.lede.length()>100){
            //Lede must be less than 100 characters
            valid=false;
        }
        if(this.text.length()>200){
            //text must be less than 200 characters
            valid=false;
        }   
        return valid;
    }
    
    //Returns relevant error message if NewsItem is not valid
    public String validateMessage(){
        String msg="";
        if(this.heading.length()>50){
            //Heading must be less than 50 characters
            msg=msg+"Otsikko saa olla enintään 50 merkkiä pitkä. ";
        }
        if(this.lede.length()>100){
            //Lede must be less than 100 characters
            msg=msg+"Ingressi saa olla enintään 100 merkkiä pitkä. ";
        }
        if(this.text.length()>200){
            //text must be less than 200 characters
            msg=msg+"Teksti saa olla enintään 200 merkkiä pitkä. ";
        }   
        return msg;
    }
    

}
