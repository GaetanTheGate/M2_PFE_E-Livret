package m2pfe.elivret.ESection;

import lombok.*;
import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.EQuestion.AbstractEQuestion;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

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

    @Basic
    private UserRole owner;

    @Basic
    private Boolean visibility;

    @Basic
    private String title;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livret_id", referencedColumnName = "id")
    @JsonBackReference
    private ELivret livret;

    // @ElementCollection
    // private List<String> listQuestion;
    // @ElementCollection
    // private List<String> listReponse;

    @OneToMany(mappedBy = "section")
    @JsonManagedReference
    private List<AbstractEQuestion> questions;
}
