package m2pfe.elivret.EQuestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * Repository (DAO) for the AbstractEQuestion entity.
 * </p>
 * <p>
 * It is used to access and manipulate the AbstractEQuestions in the database, set in the configuration.
 * </p>
 * 
 * @see AbstractEQuestion
 * @see JpaRepository
 * 
 * @author Gaëtan PUPET
 * @version 1.1
 */
@Repository
@Component(value="QuestionRepository")
@Transactional
public interface EQestionRepository extends JpaRepository<AbstractEQuestion, Integer> {
    
}
