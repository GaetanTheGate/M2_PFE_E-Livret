package m2pfe.elivret.EQuestion.QuestionType;

import java.util.List;

import javax.persistence.Entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import m2pfe.elivret.EAnswer.EAnswer;
import m2pfe.elivret.EQuestion.AbstractEQuestion;

@Entity
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
public class LabelQuestion extends AbstractEQuestion {@Override
    public List<EAnswer> getAnswers() {
        return List.of();
    }
}
