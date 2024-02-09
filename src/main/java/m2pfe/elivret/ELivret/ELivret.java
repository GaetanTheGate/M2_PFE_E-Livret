package m2pfe.elivret.ELivret;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import m2pfe.elivret.ESection.ESection;
import m2pfe.elivret.EUser.EUser;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import java.util.List;

/**
 * <p>
 * Entity used to represent a livret in the application.
 * </p>
 * <p>
 * An ELivret <b>primary key</b> is its <b>id</b>.
 * </p>
 * 
 * @author Gaëtan PUPET
 * @version 1.1
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ELivret {
    /**
     * Primary key of the ELivret.
     * Used to identify an ELivret.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Name of the livret.
     */
    @NotEmpty
    @Basic
    private String name;

    /**
     * Student linked to the livret.
     */
    @OneToOne(cascade = CascadeType.PERSIST)
    @JsonBackReference
    private EUser student;

    /**
     * Tutor of the student in the livret.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    @JsonBackReference
    private EUser tutor;

    /**
     * Master of the student in the livret.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    @JsonBackReference
    private EUser master;

    /**
     * The responsable of the student.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(referencedColumnName = "id")
    @JsonBackReference
    private EUser responsable;

    /**
     * The sections which compose a livret.
     */
    @OneToMany(mappedBy = "livret")
    @JsonManagedReference
    private List<ESection> sections;

    /**
     * <p>
     * Return the user linked to the role passed in parameter.
     * <p>
     * 
     * @param role The role of the user you are trying to get.
     * @return The user linked to the role.
     */
    public EUser getUserFromRole(UserRole role) {
        switch (role) {
            case STUDENT:
                return this.student;
            case TUTOR:
                return this.tutor;
            case MASTER:
                return this.master;
            case RESPONSABLE:
                return this.responsable;
        }

        return null;
    }

    /**
     * List the different role the user can have in a livret.
     */
    public static enum UserRole {
        STUDENT, TUTOR, MASTER, RESPONSABLE
    }
}
