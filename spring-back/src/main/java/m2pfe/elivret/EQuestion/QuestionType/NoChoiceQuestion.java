package m2pfe.elivret.EQuestion.QuestionType;

import java.util.List;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import m2pfe.elivret.EAnswer.EAnswer;
import m2pfe.elivret.EQuestion.EQuestion;

/// Is deprecated

/**
 * <p>
 * Entity used to represent a question with no answer.
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
public class NoChoiceQuestion extends EQuestion {
    @Override
    public List<EAnswer> getAnswers() {
        return List.of();
    }
}
