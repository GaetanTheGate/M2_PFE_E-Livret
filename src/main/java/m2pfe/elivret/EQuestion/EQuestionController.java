package m2pfe.elivret.EQuestion;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import m2pfe.elivret.Authentification.AuthentificationService;
import m2pfe.elivret.Authentification.EntityAccessAuthorization;
import m2pfe.elivret.EAnswer.EAnswer;

/**
 * <p>
 * Rest controller for the AbstractEQuestion entity.
 * </p>
 * <p>
 * API that manages the request from the path "/api/questions".
 * </p>
 * 
 * @see AbstractEQuestion
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@RestController
@RequestMapping("/api/questions")
public class EQuestionController {
    /**
     * The service used to check the authenticated user's rights.
     */
    @Autowired
    private EntityAccessAuthorization authorization;

    /**
     * The service to authenticate the user.
     */
    @Autowired
    private AuthentificationService service;

    /**
     * Respository for the EQuestions.
     */
    @Autowired
    private EQestionRepository q_repo;

    /**
     * Mapper for mapping an object to another.
     */
    private ModelMapper mapper = new ModelMapper();
    

    /// GetMapping

    // TODO: Comments.
    @Deprecated
    @GetMapping("/getAll")
    public List<AbstractEQuestion> getSections() {
        return q_repo.findAll().stream().map(q -> mapper.map(q, AbstractEQuestion.class))
                .toList();
    }
}
