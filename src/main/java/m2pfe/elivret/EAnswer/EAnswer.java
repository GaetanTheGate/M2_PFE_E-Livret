package m2pfe.elivret.EAnswer;

import javax.persistence.Basic;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.springframework.lang.NonNull;

import com.fasterxml.jackson.annotation.JsonBackReference;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import m2pfe.elivret.EQuestion.AbstractEQuestion;

/**
 * <p>
 * Entity used to represent an answer to an EQuestion in the application.
 * </p>
 * <p>
 * An EAnswer <b>primary key</b> is its <b>id</b>.
 * </p>
 * 
 * @see AbstractEQuestion
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EAnswer {
    /**
     * Primary key of the EAnswer.
     * Used to identify an EAnswer.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    /**
     * The value of the answer.
     */
    @Basic
    private String value;

    /**
     * The EQuestion this EAnswer is attached.
     * 
     * @see AbstractEQuestion
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", referencedColumnName = "id")
    @JsonBackReference
    @NonNull
    private AbstractEQuestion question;
}
