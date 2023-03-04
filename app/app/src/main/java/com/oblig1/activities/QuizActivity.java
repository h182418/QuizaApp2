package com.oblig1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.oblig1.MainActivity;
import com.oblig1.R;
import com.oblig1.entities.Picture;
import com.oblig1.models.Quiz;
import com.oblig1.repository.Repository;

public class QuizActivity extends AppCompatActivity {

  private Quiz quiz;
  private Repository repository;
  private TextView progressTextView;
  private TextView resultTextView;
  private TextView answerTextView;
  private Button nextQuizItemButton;
  private Button stopQuizButton;
  private Button checkAnswerButton;
  private ImageView quizItemImageView;
  private TextInputEditText answerNameInputText;
  private Picture currentQuizItem=null;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_quiz);
    initializeView();
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflate = getMenuInflater();
    inflate.inflate(R.menu.menu_quiz, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if(item.getItemId()==R.id.homeQuizMenuItem) {
      startActivity(new Intent(this, MainActivity.class));
      return true;
    } else if(item.getItemId()==R.id.restartQuizMenuItem) {
      startActivity(new Intent(this, QuizActivity.class));
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  public void initializeView() {
    repository = Repository.getInstance(this);
    quiz = new Quiz(repository.pictureDAO().getAllPictures());
    progressTextView = (TextView) findViewById(R.id.progressTextView);
    resultTextView = (TextView) findViewById(R.id.resultTextView);
    answerTextView = (TextView) findViewById(R.id.answerTextView);;
    nextQuizItemButton = (Button) findViewById(R.id.nextQuizItemButton);
    stopQuizButton = (Button) findViewById(R.id.stopQuizButton);
    quizItemImageView = (ImageView) findViewById(R.id.quizItemImageView);
    checkAnswerButton = (Button) findViewById(R.id.checkAnswerButton);
    answerNameInputText = (TextInputEditText) findViewById(R.id.answerNameInputText);
    progressTextView.setText(quiz.getProgressToString());


    answerNameInputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
          hideKeyboard(v);
        }
      }
    });

    currentQuizItem=quiz.getQuizItem();

    checkAnswerButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        quiz.incrementAttempts();
        quiz.checkAnswer(currentQuizItem, answerNameInputText.getText().toString());
        answerTextView.setText(quiz.getAnswerFeedback());
        progressTextView.setText(quiz.getProgressToString());
        answerNameInputText.setText("");
        quiz.setState(Quiz.DONE_CHECK);
        handleQuizState();
      }
    });

    nextQuizItemButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        answerNameInputText.setText("");
        currentQuizItem=quiz.getQuizItem();
        setQuizItemImage();
        quiz.setState(Quiz.AWAITING_CHECK);
        handleQuizState();
      }
    });

    stopQuizButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        resultTextView.setText(quiz.getResultsToString());
        quiz.setState(Quiz.STOPPED);
        handleQuizState();
      }
    });
    setQuizItemImage();
    handleQuizState();
  }

  public void handleQuizState() {
    switch(quiz.getState()) {
      case Quiz.AWAITING_CHECK:
        checkAnswerButton.setVisibility(View.VISIBLE);
        nextQuizItemButton.setVisibility(View.INVISIBLE);
        stopQuizButton.setVisibility(View.INVISIBLE);
        resultTextView.setVisibility(View.INVISIBLE);
        answerTextView.setVisibility(View.INVISIBLE);
        quizItemImageView.setVisibility(View.VISIBLE);
        progressTextView.setVisibility(View.VISIBLE);
        answerNameInputText.setVisibility(View.VISIBLE);
        break;
      case Quiz.DONE_CHECK:
        checkAnswerButton.setVisibility(View.INVISIBLE);
        nextQuizItemButton.setVisibility(View.VISIBLE);
        stopQuizButton.setVisibility(View.VISIBLE);
        resultTextView.setVisibility(View.INVISIBLE);
        answerTextView.setVisibility(View.VISIBLE);
        quizItemImageView.setVisibility(View.VISIBLE);
        progressTextView.setVisibility(View.VISIBLE);
        answerNameInputText.setVisibility(View.INVISIBLE);
        break;
      case Quiz.STOPPED:
        checkAnswerButton.setVisibility(View.INVISIBLE);
        answerTextView.setVisibility(View.INVISIBLE);
        stopQuizButton.setVisibility(View.INVISIBLE);
        nextQuizItemButton.setVisibility(View.INVISIBLE);
        progressTextView.setVisibility(View.INVISIBLE);
        answerNameInputText.setVisibility(View.INVISIBLE);
        quizItemImageView.setVisibility(View.INVISIBLE);
        resultTextView.setVisibility(View.VISIBLE);
    }
  }

  public void setQuizItemImage() {
    Glide.with(this)
            .asBitmap()
            .load(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + currentQuizItem.getFilename() + ".jpg")
            .into(quizItemImageView);
  }

  public void hideKeyboard(View view) {
    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

}