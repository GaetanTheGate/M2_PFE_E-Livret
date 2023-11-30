package m2pfe.elivret.EUser;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

/**
 * <p>
 * Repository (DAO) for the EUser entity.
 * </p>
 * <p>
 * It is used to access and manipulate the EUsers in the database, set in the configuration. 
 * </p>
 * 
 * @see EUser
 * @see JpaRepository
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@Repository
@Transactional
public interface EUserRepository extends JpaRepository<EUser, Integer> {
    
    /**
     * <p>
     * Fetch and return the user with the email passed in parameter.
     * </p>
     * 
     * @see EUser
     * 
     * @param email The email associated with the user you wish to find.
     * @return <ul>
     *  <li>The <i>EUser</i> found.</li>
     *  <li><i>null</i> if nothing was found</li>
     * </ul>
     */
    @Query("SELECT u FROM EUser u WHERE u.email = :email")
    public EUser findByEmail(String email); 
}
