package m2pfe.elivret.EModel;

import java.util.List;

import lombok.Data;

import m2pfe.elivret.EQuestion.EQuestion;

/**
 * <p>
 * DTO of an EQuestion used to represent its model.
 * </p>
 * 
 * @see EQuestion
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@Data
public class EQuestionModel {
    private String type;
    private String title;

    private List<EAnswerModel> answers;
}
