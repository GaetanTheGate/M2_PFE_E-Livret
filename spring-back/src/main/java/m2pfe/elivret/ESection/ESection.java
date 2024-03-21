package m2pfe.elivret.ESection;

import lombok.*;
import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.ELivret.ELivret.UserRole;
import m2pfe.elivret.EQuestion.EQuestion;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import javax.persistence.*;
import java.util.List;

/**
 * <p>
 * Entity used to represent a section from a ELivret in the application.
 * </p>
 * <p>
 * An ESection <b>primary key</b> is its <b>id</b>.
 * </p>
 * 
 * @see ELivret
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ESection {
    /**
     * Primary key of the ELivret.
     * Used to identify an ELivret.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Basic
    private Integer location;

    @PostLoad
    protected void onCreate(){
        if(this.location == null){
            this.location = this.id;
        }
    }
    /**
     * The owner of the section.
     */
    @Basic
    @Enumerated(EnumType.STRING)
    private UserRole owner;

    /**
     * The visibility of the section
     */
    @Basic
    private Boolean visibility;

    /**
     * The title of the section
     */
    @Basic
    private String title;

    /**
     * The livret where this section is from.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "livret_id", referencedColumnName = "id")
    @JsonBackReference
    private ELivret livret;

    /**
     * The question which compose the section.
     */
    @OneToMany(mappedBy = "section")
    @JsonManagedReference
    private List<EQuestion> questions;
}
