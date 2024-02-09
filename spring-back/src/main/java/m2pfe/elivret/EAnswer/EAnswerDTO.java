package m2pfe.elivret.EAnswer;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * <p>
 * A list of DTO to be mapped with an EAnswer.
 * <p>
 * </p>
 * Used to filter the informations of a given or taken EAnwser.
 * </p>
 * 
 * @see EAnswer
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
public class EAnswerDTO {
    /**
     * The DTOs the user will give to the application.
     */
    public static class In {
        /**
         * The DTO used to change the proposition of an anwser.
         */
        @Data
        @NoArgsConstructor
        public static class Proposition {
            private Integer id;
            private String proposition;
        }

        /**
         * The DTO used to change the value of an anwser.
         */
        @Data
        @NoArgsConstructor
        public static class Value {
            private Integer id;
            private String value;
        }
    }

    /**
     * The DTOs the user will get from the application.
     */
    public static class Out {
        /**
         * The DTO used to give all the public information about an answer.
         */
        @Data
        @NoArgsConstructor
        public static class AllPublic {
            private Integer id;
            private String proposition;
            private String value;
        }
    }

}
