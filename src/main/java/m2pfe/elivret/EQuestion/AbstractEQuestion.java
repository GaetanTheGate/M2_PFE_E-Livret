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

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.Data;
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
 * @version 1.0
 */
@Entity
@Data
public abstract class AbstractEQuestion {
    /**
     * Primary key of the AbstractEQuestion.
     * Used to identify an AbstractEQuestion.
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
    private String title;

    /**
     * ESection where the question is located.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    @JsonBackReference
    private ESection section;

    /**
     * <p>
     * Get the list of all EAnswers the questions has defined.
     * </p>
     * 
     * @return The list of EAnswers
     */
    abstract public List<EAnswer> getAnswers();

    /**
     * Define the type of question that can exist.
     */
    public static enum QuestionType {
        LABEL, TEXT, CHECKBOX, RATIOBUTTON
    }
}
