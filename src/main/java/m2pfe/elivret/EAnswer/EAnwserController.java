package m2pfe.elivret.EAnswer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * <p>
 * Rest controller for the EAnswer entity.
 * </p>
 * <p>
 * API that manages the request from the path "/api/answers".
 * </p>
 * 
 * @see EAnswer
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@RestController
@RequestMapping("/api/answers")
public class EAnwserController {
    /*
     * Repository fir the EAnswer entities.
     */
    @Autowired
    private EAnswerRepository ar;

    // TODO: COMMENTS.
    @GetMapping("/getAll")
    public List<EAnswer> getAllAnswer() {
        return ar.findAll();
    }
    
}
