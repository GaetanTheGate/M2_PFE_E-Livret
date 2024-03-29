package m2pfe.elivret.EUser;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
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
@Builder
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
    @Column(unique = true)
    private String email;

    /**
     * The password of the EUser.
     * It is used to log in the application.
     */
    @Basic
    @NotBlank
    private String password;

    /**
     * The firstname of the EUser.
     */
    @Basic
    @NotBlank
    private String firstName;

    /**
     * The lastname of the EUser.
     */
    @Basic
    @NotBlank
    private String lastName;

    /**
     * The level of permission of the user.
     */
    @Basic
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Permission permission = Permission.USER;

    /**
     * The different level of permission a user can have in the application.
     */
    public static enum Permission {
        RESPONSABLE, USER
    }

}
