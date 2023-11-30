package m2pfe.elivret.EUser;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * Entity used to represent an user in the application.
 * </p>
 * <p>
 * An EUser <b>primary key</b> is its <b>id</b>.
 * </p>
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EUser {
    /**
     * Primary key of the EUser.
     * Used to identify an EUser.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * The email of the EUser.
     * It is used to log in the application.
     */
    @Basic
    @NotBlank
    private String email; // TODO: rendre unique

    /**
     * The password of the EUser.
     * It is ised to log in the application.
     */
    @Basic
    @NotBlank
    private String password;
}
