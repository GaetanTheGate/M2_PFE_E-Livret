package m2pfe.elivret.EAnswer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

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
    
}
