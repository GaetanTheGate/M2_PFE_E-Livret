package m2pfe.elivret.ESection;



import lombok.*;
import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.EUser.EUser;
import org.springframework.data.util.Pair;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ESection {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    // @OneToOne(cascade = CascadeType.PERSIST)
    // @JsonBackReference
    // private EUser owner;

    private UserRole owner;

    @Basic
    private Boolean visibility;

    @Basic
    private String title;

    @ElementCollection
    private List<String> listQuestion;
    @ElementCollection
    private List<String> listReponse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livret_id", referencedColumnName = "id")
    @JsonBackReference
    private ELivret livret;
}
