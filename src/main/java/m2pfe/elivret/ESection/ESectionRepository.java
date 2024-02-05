package m2pfe.elivret.ESection;

import m2pfe.elivret.EUser.EUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Component(value="SectionRepository")
@Transactional
public interface ESectionRepository extends JpaRepository<ESection, Integer> {
    @Query("SELECT s FROM ESection s " +
            "JOIN s.livret l " +
            "WHERE l.student = :user OR l.tutor = :user OR l.master = :user OR l.responsable = :user")
    List<ESection> findByUser(@Param("user") EUser user);
}
