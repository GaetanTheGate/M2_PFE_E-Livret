package m2pfe.elivret.EModel;

import java.util.List;

import lombok.Data;

@Data
public class EQuestionModel {
    private String type;
    private String title;

    private List<EAnswerModel> answers;
}
