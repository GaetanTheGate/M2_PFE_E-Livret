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
            public Integer id;
            public String proposition;    
        }

        /**
         * The DTO used to change the value of an anwser.
         */
        @Data
        @NoArgsConstructor
        public static class Value {
            public Integer id;
            public String value;    
        }
    }

    /**
     * The DTOs the user will get from the application.
     */
    public static class Out {

    }
    
}
