package m2pfe.elivret.ELivret;

import m2pfe.elivret.ESection.ESection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional
public interface ELivretRepository  extends JpaRepository<ELivret, Integer> {
    
}
