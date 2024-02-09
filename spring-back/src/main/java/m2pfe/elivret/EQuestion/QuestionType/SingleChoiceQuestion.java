package m2pfe.elivret.EQuestion.QuestionType;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import m2pfe.elivret.EAnswer.EAnswer;
import m2pfe.elivret.EQuestion.EQuestion;

/**
 * <p>
 * Entity used to represent a question with one answer.
 * </p>
 * 
 * @see EQuestion
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@Deprecated
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class SingleChoiceQuestion extends EQuestion {
    /**
     * The answer to the question.
     */
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER) // TODO : OneToMany FAUX, mais fais fonctionner les
                                                               // entités "MultipleChoiceQuestion". Remettre à OneToOne
    @JsonManagedReference
    private List<EAnswer> answer;

    @Override
    public List<EAnswer> getAnswers() {
        return answer;
    }
}
