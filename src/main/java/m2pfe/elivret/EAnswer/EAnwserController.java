package m2pfe.elivret.EAnswer;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
@RequestMapping("/api/answers")
public class EAnwserController {
    @Autowired
    private EAnswerRepository ar;

    @GetMapping("/getAll")
    public List<EAnswer> getAllAnswer() {
        return ar.findAll();
    }
    
}
