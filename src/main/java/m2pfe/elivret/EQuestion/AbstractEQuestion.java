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

@Entity
@Data
public abstract class AbstractEQuestion {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @Basic
    private QuestionType type;

    @Basic
    private String title;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", referencedColumnName = "id")
    @JsonBackReference
    private ESection section;

    abstract public List<EAnswer> getAnswers();

    public static enum QuestionType {
        LABEL, TEXT, SINGLE_CHECKBOX, MULTI_CHECKBOX, RATIOBUTTON
    }
}
