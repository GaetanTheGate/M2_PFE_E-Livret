package m2pfe.elivret.ELivret;

import m2pfe.elivret.EUser.EUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * <p>
 * Repository (DAO) for the ELivret entity.
 * </p>
 * <p>
 * It is used to access and manipulate the ELivrets in the database, set in the configuration.
 * </p>
 * 
 * @see ELivret
 * @see JpaRepository
 * 
 * @author Gaëtan PUPET
 * @version 1.1
 */
@Repository
@Component(value="LivretRepository")
@Transactional
public interface ELivretRepository  extends JpaRepository<ELivret, Integer> {
    // TODO : Liste de Optionnal de liste de ELivret partout

    List<ELivret> findByStudent(EUser student);

    List<ELivret> findByTutor(EUser tutor);

    List<ELivret> findByMaster(EUser master);

    List<ELivret> findByResponsable(EUser responsable);

    @Query("SELECT DISTINCT(l) FROM ELivret l WHERE l.student = :user OR l.tutor = :user OR l.master = :user OR l.responsable = :user")
    List<ELivret> findByUser(@Param("user") EUser user);

    // TODO : plus jolie d'une manière si possible
        // Remplacer la fin de la query par une méthode
    @Query("SELECT DISTINCT(l) FROM ELivret l JOIN l.sections s ON s.livret = l JOIN s.questions q ON q.section = s JOIN q.answers a ON a.question = q WHERE a.value IS NULL AND ( (s.owner = 'STUDENT' AND l.student = :user) OR (s.owner = 'TUTOR' AND l.tutor = :user) OR (s.owner = 'MASTER' AND l.master = :user) OR (s.owner = 'RESPONSABLE' AND l.responsable = :user) )")
    Optional<List<ELivret>> findAllLivretsUserHasToComplete(@Param("user") EUser user); 
}
