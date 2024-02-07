package m2pfe.elivret.EQuestion;

import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import m2pfe.elivret.EAnswer.EAnswer;
import m2pfe.elivret.ESection.ESection;

/**
 * <p>
 * Abtract entity used to represent a question from an ESection in the application.
 * </p>
 * <p>
 * An EQuestion <b>primary key</b> is its <b>id</b>.
 * </p>
 * <p>
 * Each abstraction of this entity will define their type of questions and their EAnswsers.
 * </p>
 * 
 * @see ESection
 * @see EAnswers
 * 
 * @author Gaëtan PUPET
 * @version 2.0
 */
@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EQuestion {
    /**
     * Primary key of the EQuestion.
     * Used to identify an EQuestion.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * Type of the question.
     */
    @Basic
    private QuestionType type;

    /**
     * Title of the question.
     */
    @Basic
    @Size(max = 500)
    private String title;

    /**
     * ESection where the question is located.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    @JsonBackReference
    private ESection section;

    /**
     * The answers to the question.
     */
    @OneToMany(mappedBy = "question", fetch = FetchType.EAGER)
    @JsonManagedReference
    private List<EAnswer> answers;

    /**
     * Define the type of question that can exist.
     */
    public static enum QuestionType {
        LABEL, TEXT, CHECKBOX, RADIO
    }
}
