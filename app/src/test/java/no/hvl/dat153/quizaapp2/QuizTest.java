package no.hvl.dat153.quizaapp2;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import no.hvl.dat153.quizaapp2.entities.Picture;
import no.hvl.dat153.quizaapp2.models.Quiz;



public class QuizTest {

    private Quiz quiz;

    @Before
    public void setup() {
        List<Picture> pictures = new ArrayList<Picture>();
        pictures.add(new Picture("Jurgen Klopp", "klopp"));
        quiz = new Quiz(pictures);
    }

    @Test
    public void answerCorrectIncreasesScoreTest() {
        Picture picture = quiz.getQuizItem();
        int countCorrectAnswers = quiz.getCountCorrectAnswers();
        quiz.checkAnswer(picture, "Jurgen Klopp");
        assertEquals(countCorrectAnswers + 1, quiz.getCountCorrectAnswers());
    }

    @Test
    public void answerWrongGivesSameScoreTest() {
        Picture picture = quiz.getQuizItem();
        int countCorrectAnswers = quiz.getCountCorrectAnswers();
        quiz.checkAnswer(picture, "asdasd");
        assertEquals(countCorrectAnswers, quiz.getCountCorrectAnswers());
    }

}