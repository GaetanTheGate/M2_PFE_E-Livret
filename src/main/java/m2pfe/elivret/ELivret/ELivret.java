package m2pfe.elivret.ELivret;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import m2pfe.elivret.ESection.ESection;
import m2pfe.elivret.EUser.EUser;


import javax.persistence.*;
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
    private EUser student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tutor_id", referencedColumnName = "id")
    private EUser tutor;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", referencedColumnName = "id")
    private EUser master;

    @OneToMany(mappedBy = "livret")
    private List<ESection> sections;
}
