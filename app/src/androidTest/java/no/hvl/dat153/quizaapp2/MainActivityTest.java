package no.hvl.dat153.quizaapp2;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.oblig1.activities.DatabaseActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

  @Rule
  public IntentsTestRule<MainActivity> activityRule = new IntentsTestRule<>(MainActivity.class);

  @Test
  public void databaseButtonRedirectTest() {
    onView(withId(R.id.databaseButton)).perform(click());
    intended(hasComponent(DatabaseActivity.class.getName()));
  }

}