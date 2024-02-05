package m2pfe.elivret.EQuestion.QuestionType;

import java.util.List;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import m2pfe.elivret.EAnswer.EAnswer;
import m2pfe.elivret.EQuestion.AbstractEQuestion;

/**
 * <p>
 * Entity used to represent a question with no answer.
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
public class NoChoiceQuestion extends AbstractEQuestion {
    @Override
    public List<EAnswer> getAnswers() {
        return List.of();
    }
}
