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

/**
 * <p>
 * Service used to more easily save and delete the application entities.
 * </p>
 * 
 * @see EUser
 * @see ELivret
 * @see ESection
 * @see EQuestion
 * @see EAnswer
 * 
 * @author Gaëtan PUPET
 * @version 1.0
 */
@Service
@Data
public class EntityManager {
    /**
     * Repository for the Euser.
     */
    @Autowired
    private EUserRepository ur;

    /**
     * Repository for the ELivret.
     */
    @Autowired
    private ELivretRepository lr;

    /**
     * Repository for the ESection.
     */
    @Autowired
    private ESectionRepository sr;

    /**
     * Repository for the EQuestion.
     */
    @Autowired
    private EQestionRepository qr;

    /**
     * Repository for the EAnswer.
     */
    @Autowired
    private EAnswerRepository ar;

    /**
     * <p>
     * Save an user.
     * </p>
     * 
     * @see EUser
     * 
     * @return The save user.
     */
    public EUser saveUser(EUser user) {
        return ur.save(user);
    }

    /**
     * <p>
     * Save a livret.
     * </p>
     * <p>
     * If the livret has sections, also save its sections
     * </p>
     * 
     * @see ELivret
     * @see #saveSection(ESection)
     *
     * @return The save livret.
     */
    public ELivret saveLivret(ELivret livret) {
        ELivret currentLivret = lr.save(livret);

        if (livret.getSections() != null)
            for (ESection section : livret.getSections()) {
                section.setLivret(currentLivret);
                saveSection(section);
            }

        return currentLivret;
    }

    /**
     * <p>
     * Save a section.
     * </p>
     * <p>
     * If the section has questions, also save its questions
     * </p>
     * 
     * @see ESection
     * @see #saveQuestion(EQuestion)
     *
     * @return The save section.
     */
    public ESection saveSection(ESection section) {
        ESection currentSection = sr.save(section);

        if (section.getQuestions() != null)
            for (EQuestion question : section.getQuestions()) {
                question.setSection(currentSection);
                saveQuestion(question);
            }

        return currentSection;
    }

    /**
     * <p>
     * Save a question.
     * </p>
     * <p>
     * If the question has answers, also save its answers
     * </p>
     * 
     * @see EQuestion
     * @see #saveAnswer(EAnswer)
     *
     * @return The save question.
     */
    public EQuestion saveQuestion(EQuestion question) {
        EQuestion currentQuestion = qr.save(question);

        if (question.getAnswers() != null)
            for (EAnswer answer : question.getAnswers()) {
                answer.setQuestion(currentQuestion);
                saveAnswer(answer);
            }

        return currentQuestion;
    }

    /**
     * <p>
     * Save an answer.
     * </p>
     * 
     * @see EAnswer
     *
     * @return The save answer.
     */
    public EAnswer saveAnswer(EAnswer answer) {
        return ar.save(answer);
    }

    /**
     * <p>
     * Delete an user.
     * </p>
     * 
     * @param user The user to delete
     */
    public void deleteUser(EUser user) {
        ur.delete(user);
    }

    /**
     * <p>
     * Delete a livret.
     * </p>
     * <p>
     * If the livret has sections, delete the sections first.
     * </p>
     * 
     * @param livret The livret to delete
     */
    public void deleteLivret(ELivret livret) {
        for (ESection section : livret.getSections())
            deleteSection(section);

        lr.delete(livret);
    }

    /**
     * <p>
     * Delete a section.
     * </p>
     * <p>
     * If the section has questions, delete the questions first.
     * </p>
     * 
     * @param section The section to delete
     */
    public void deleteSection(ESection section) {
        for (EQuestion question : section.getQuestions())
            deleteQuestion(question);

        sr.delete(section);
    }

    /**
     * <p>
     * Delete a question.
     * </p>
     * <p>
     * If the question has answers, delete the answers first.
     * </p>
     * 
     * @param question The question to delete
     */
    public void deleteQuestion(EQuestion question) {
        for (EAnswer answer : question.getAnswers())
            deleteAnswer(answer);

        qr.delete(question);
    }

    /**
     * <p>
     * Delete an answer.
     * </p>
     * 
     * @param answer The answer to delete
     */
    public void deleteAnswer(EAnswer answer) {
        ar.delete(answer);
    }
}
