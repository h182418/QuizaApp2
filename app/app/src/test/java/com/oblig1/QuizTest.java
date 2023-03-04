package com.oblig1;

import android.content.Context;

import com.oblig1.activities.QuizActivity;
import com.oblig1.entities.Picture;
import com.oblig1.models.Quiz;
import com.oblig1.repository.Repository;

import org.junit.Before;
import org.junit.Test;

import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;


public class QuizTest {

  private Quiz quiz;

  @Before
  public void setup() {
    List<Picture> pictures = new ArrayList<Picture>();
    pictures.add(new Picture("Matt Damon", "damon"));
    quiz = new Quiz(pictures);
  }

  @Test
  public void answerCorrectIncreasesScoreTest() {
    Picture picture = quiz.getQuizItem();
    int countCorrectAnswers = quiz.getCountCorrectAnswers();
    quiz.checkAnswer(picture, "Matt Damon");
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