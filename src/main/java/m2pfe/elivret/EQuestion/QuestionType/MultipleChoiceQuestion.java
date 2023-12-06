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
import m2pfe.elivret.EQuestion.AbstractEQuestion;

/**
 * <p>
 * Entity used to represent a question with multiple answers.
 * </p>
 * 
 * @see AbstractEQuestion
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
public class MultipleChoiceQuestion extends AbstractEQuestion {
    // @ElementCollection
    // private List<String> values;

    /**
     * The answers to the question.
     */
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<EAnswer> answers;

    @Override
    public List<EAnswer> getAnswers() {
        return answers;
    }
}
