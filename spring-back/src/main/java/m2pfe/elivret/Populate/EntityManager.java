package m2pfe.elivret.Populate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.Data;
import m2pfe.elivret.EAnswer.EAnswer;
import m2pfe.elivret.EAnswer.EAnswerRepository;
import m2pfe.elivret.ELivret.ELivret;
import m2pfe.elivret.ELivret.ELivretRepository;
import m2pfe.elivret.EQuestion.EQestionRepository;
import m2pfe.elivret.EQuestion.EQuestion;
import m2pfe.elivret.ESection.ESection;
import m2pfe.elivret.ESection.ESectionRepository;

@Service
@Data
public class EntityManager {
    @Autowired
    private ELivretRepository lr;

    @Autowired
    private ESectionRepository sr;

    @Autowired
    private EQestionRepository qr;

    @Autowired
    private EAnswerRepository ar;

    private ELivret currentLivret;
    private ESection currentSection;
    private EQuestion currentQuestion;
    private EAnswer currentAnswer;

    public ELivret saveLivret(ELivret livret) {
        currentLivret = lr.save(livret);

        for (ESection section : currentLivret.getSections())
            saveSection(section);

        return currentLivret;
    }

    public ESection saveSection(ESection section) {
        section.setLivret(currentLivret);

        currentSection = sr.save(section);

        for (EQuestion question : section.getQuestions())
            saveQuestion(question);

        return currentSection;
    }

    public EQuestion saveQuestion(EQuestion question) {
        question.setSection(currentSection);

        currentQuestion = qr.save(question);

        for (EAnswer answer : question.getAnswers())
            saveAnswer(answer);

        return currentQuestion;
    }

    public EAnswer saveAnswer(EAnswer answer) {
        answer.setQuestion(currentQuestion);

        currentAnswer = ar.save(answer);
        return currentAnswer;
    }

    public void deleteLivret(ELivret livret) {
        for (ESection section : livret.getSections())
            deleteSection(section);

        lr.delete(livret);
        currentLivret = null;
    }

    public void deleteSection(ESection section) {
        for (EQuestion question : section.getQuestions())
            deleteQuestion(question);

        sr.delete(section);
        currentSection = null;
    }

    public void deleteQuestion(EQuestion question) {
        for (EAnswer answer : question.getAnswers())
            deleteAnswer(answer);

        qr.delete(question);
        currentQuestion = null;
    }

    public void deleteAnswer(EAnswer answer) {
        ar.delete(answer);
        currentAnswer = null;
    }
}
