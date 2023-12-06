package m2pfe.elivret.EQuestion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface EQestionRepository extends JpaRepository<AbstractEQuestion, Integer> {
    
}
