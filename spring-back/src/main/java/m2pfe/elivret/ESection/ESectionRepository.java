package m2pfe.elivret.ESection;

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
 * Repository (DAO) for the ESection entity.
 * </p>
 * <p>
 * It is used to access and manipulate the ESections in the database, set in the
 * configuration.
 * </p>
 * 
 * @see ESection
 * @see JpaRepository
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@Repository
@Component(value = "SectionRepository")
@Transactional
public interface ESectionRepository extends JpaRepository<ESection, Integer> {
    @Query("SELECT s FROM ESection s " +
            "JOIN s.livret l " +
            "WHERE l.student = :user OR l.tutor = :user OR l.master = :user OR l.responsable = :user")
    List<ESection> findByUser(@Param("user") EUser user);

    // TODO : plus jolie d'une manière si possible
    // Remplacer la fin de la query par une méthode
    @Query("SELECT DISTINCT(s) FROM ELivret l JOIN l.sections s ON s.livret = l JOIN s.questions q ON q.section = s JOIN q.answers a ON a.question = q WHERE a.value IS NULL AND ( (s.owner = 'STUDENT' AND l.student = :user) OR (s.owner = 'TUTOR' AND l.tutor = :user) OR (s.owner = 'MASTER' AND l.master = :user) OR (s.owner = 'RESPONSABLE' AND l.responsable = :user) )")
    Optional<List<ESection>> findAllSectionsUserHasToComplete(@Param("user") EUser user);

}
