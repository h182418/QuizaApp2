package com.oblig1;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.oblig1.activities.DatabaseActivity;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

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