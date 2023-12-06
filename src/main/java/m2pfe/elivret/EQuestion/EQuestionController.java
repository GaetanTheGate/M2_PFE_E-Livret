package m2pfe.elivret.EQuestion;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.Authentification.EntityAccessAuthorization;

@RestController
@RequestMapping("/api/questions")
public class EQuestionController {
    @Autowired
    private EntityAccessAuthorization authorization;

    @Autowired
    private AuthentificationService service;

    @Autowired
    private EQestionRepository q_repo;

    private ModelMapper mapper = new ModelMapper();
    

    /// GetMapping

    @Deprecated
    @GetMapping("/getAll")
    public List<AbstractEQuestion> getSections() {
        return q_repo.findAll().stream().map(q -> mapper.map(q, AbstractEQuestion.class))
                .toList();
    }
}
