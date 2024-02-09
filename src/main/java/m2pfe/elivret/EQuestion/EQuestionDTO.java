package m2pfe.elivret.EQuestion;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;
import m2pfe.elivret.EAnswer.EAnswerDTO;
import m2pfe.elivret.EQuestion.EQuestion.QuestionType;

/**
 * <p>
 * A list of DTO to be mapped with an EQuestion.
 * <p>
 * </p>
 * Used to filter the informations of a given or taken EQuestion.
 * </p>
 * 
 * @see EQuestion
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
public class EQuestionDTO {
    /**
     * The DTOs the user will give to the application.
     */
    public static class In {

    }

    /**
     * The DTOs the user will get from the application.
     */
    public static class Out {
        /**
         * The DTO used to give all the public information about a question.
         */
        @Data
        @NoArgsConstructor
        public static class AllPublic {
            private Integer id;
            private QuestionType type;
            private String title;
            private List<EAnswerDTO.Out.AllPublic> answers;
        }
    }

}
