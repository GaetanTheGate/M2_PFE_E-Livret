package m2pfe.elivret.ELivret;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import m2pfe.elivret.ESection.ESection;
import m2pfe.elivret.EUser.EUser;


import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ELivret {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne(cascade = CascadeType.PERSIST)
    @JsonBackReference
    private EUser student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    @JsonBackReference
    private EUser tutor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    @JsonBackReference
    private EUser master;

    @OneToMany(mappedBy = "livret")
    @JsonManagedReference
    private List<ESection> sections;
}
