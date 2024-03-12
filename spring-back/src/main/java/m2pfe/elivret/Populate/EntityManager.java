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
import m2pfe.elivret.EUser.EUser;
import m2pfe.elivret.EUser.EUserRepository;

@Service
@Data
public class EntityManager {
    @Autowired
    private EUserRepository ur;

    @Autowired
    private ELivretRepository lr;

    @Autowired
    private ESectionRepository sr;

    @Autowired
    private EQestionRepository qr;

    @Autowired
    private EAnswerRepository ar;

    public EUser saveUser(EUser user) {
        return ur.save(user);
    }

    public ELivret saveLivret(ELivret livret) {
        ELivret currentLivret = lr.save(livret);

        if (livret.getSections() != null)
            for (ESection section : livret.getSections()) {
                section.setLivret(currentLivret);
                saveSection(section);
            }

        return currentLivret;
    }

    public ESection saveSection(ESection section) {
        ESection currentSection = sr.save(section);

        if (section.getQuestions() != null)
            for (EQuestion question : section.getQuestions()) {
                question.setSection(currentSection);
                saveQuestion(question);
            }

        return currentSection;
    }

    public EQuestion saveQuestion(EQuestion question) {
        EQuestion currentQuestion = qr.save(question);

        if (question.getAnswers() != null)
            for (EAnswer answer : question.getAnswers()) {
                answer.setQuestion(currentQuestion);
                saveAnswer(answer);
            }

        return currentQuestion;
    }

    public EAnswer saveAnswer(EAnswer answer) {
        return ar.save(answer);
    }

    public void deleteUser(EUser user) {
        ur.delete(user);
    }

    public void deleteLivret(ELivret livret) {
        for (ESection section : livret.getSections())
            deleteSection(section);

        lr.delete(livret);
    }

    public void deleteSection(ESection section) {
        for (EQuestion question : section.getQuestions())
            deleteQuestion(question);

        sr.delete(section);
    }

    public void deleteQuestion(EQuestion question) {
        for (EAnswer answer : question.getAnswers())
            deleteAnswer(answer);

        qr.delete(question);
    }

    public void deleteAnswer(EAnswer answer) {
        ar.delete(answer);
    }
}
