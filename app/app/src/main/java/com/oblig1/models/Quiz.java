package com.oblig1.models;

import android.content.Context;
import android.util.Log;

import com.oblig1.entities.Picture;
import com.oblig1.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Quiz {

  public static final int DONE_CHECK=0, STOPPED=1, AWAITING_CHECK=2;

  private int state;
  private List<Picture> quizItems;
  private int attempts, countCorrectAnswers, countWrongAnswers;
  private boolean isRunning;
  private Random rand;
  private String answerFeedback;

  public Quiz(List<Picture> pictures) {
    quizItems = pictures;
    rand = new Random();
    restart();
  }

  public void restart() {
    attempts = 0;
    countCorrectAnswers = 0;
    countWrongAnswers = 0;
    state=AWAITING_CHECK;
  }

  public int getSize() {
    return quizItems.size();
  }

  public boolean checkAnswer(Picture quizItem, String answerName) {
    if (quizItem.getName().toLowerCase().equals(answerName.toLowerCase())) {
      countCorrectAnswers++;
      this.answerFeedback = answerName + " was the correct answer!";
      return true;
    } else {
      countWrongAnswers++;
      this.answerFeedback = answerName + " was wrong.\nCorrect answer was "+quizItem.getName();
      return false;
    }
  }

  public int getCountCorrectAnswers() {
    return countCorrectAnswers;
  }

  public int getCountWrongAnswers() {
    return countWrongAnswers;
  }

  public void incrementAttempts() {
    attempts++;
  }

  public Picture getQuizItem() {
    return quizItems.get(rand.nextInt(quizItems.size()));
  }

  public String getResultsToString() {
    String attemptsStr = Integer.toString(attempts);
    String countCorrectStr = Integer.toString(countCorrectAnswers);
    return "You answered " + countCorrectStr + "/" + attemptsStr + " correctly!";
  }

  public String getProgressToString() {
    String countCorrectStr = Integer.toString(countCorrectAnswers);
    String attemptsStr = Integer.toString(attempts);
    return "Your score " + countCorrectStr + "/" + attemptsStr;
  }
  public String getAnswerFeedback() {
    return answerFeedback;
  }

  public int getState() {
    return state;
  }

  public void setState(int state) {
    this.state = state;
  }
}
