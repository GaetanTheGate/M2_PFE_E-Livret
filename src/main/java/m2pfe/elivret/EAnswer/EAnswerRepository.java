package m2pfe.elivret.EAnswer;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import m2pfe.elivret.EUser.EUser;

/**
 * <p>
 * Repository (DAO) for the EAnswer entity.
 * </p>
 * <p>
 * It is used to access and manipulate the EAnswers in the database, set in the configuration.
 * </p>
 * 
 * @see EAnswer
 * @see JpaRepository
 * 
 * @author Gaëtan PUPET
 * @version 1.1
 */
@Repository
@Component(value="AnswerRepository")
@Transactional
public interface EAnswerRepository extends JpaRepository<EAnswer,Integer> {
    // TODO : plus jolie d'une manière si possible
        // Remplacer la fin de la query par une méthode
    @Query("SELECT DISTINCT(a) FROM ELivret l JOIN l.sections s ON s.livret = l JOIN s.questions q ON q.section = s JOIN q.answers a ON a.question = q WHERE a.value IS NULL AND ( (s.owner = 'STUDENT' AND l.student = :user) OR (s.owner = 'TUTOR' AND l.tutor = :user) OR (s.owner = 'MASTER' AND l.master = :user) OR (s.owner = 'RESPONSABLE' AND l.responsable = :user) )")
    Optional<List<EAnswer>> findAllAnswersUserHasToComplete(@Param("user") EUser user); 
}
