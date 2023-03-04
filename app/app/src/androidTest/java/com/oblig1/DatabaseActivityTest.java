package com.oblig1;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import androidx.recyclerview.widget.RecyclerView;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;

import com.bumptech.glide.Glide;
import com.oblig1.activities.AddPictureActivity;
import com.oblig1.activities.DatabaseActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.hasChildCount;
import static androidx.test.espresso.matcher.ViewMatchers.hasDescendant;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class DatabaseActivityTest {

  @Rule
  public ActivityTestRule<DatabaseActivity> activityRule = new ActivityTestRule<>(DatabaseActivity.class);

  @Rule
  public ActivityTestRule<AddPictureActivity> addActivityRule = new ActivityTestRule<>(AddPictureActivity.class);

  @Rule
  public GrantPermissionRule permissionRule = GrantPermissionRule.grant(Manifest.permission.READ_EXTERNAL_STORAGE);


  private int getItemCount() {
    Activity activity = activityRule.getActivity();
    RecyclerView view = activity.findViewById(R.id.databaseRecyclerView);
    RecyclerView.Adapter adapter = view.getAdapter();
    return adapter.getItemCount();
  }

  @Before
  public void setup() {
    Intents.init();
  }

  @After
  public void teardown() {
    Intents.release();
  }

  @Test
  public void removeItemFromDatabaseTest() {
    int countItemsBefore = this.getItemCount();
    if(countItemsBefore > 0) {
      TestViewActions testViewAction = new TestViewActions();
      onView(withId(R.id.databaseRecyclerView))
              .perform(RecyclerViewActions.actionOnItemAtPosition(0, testViewAction.clickChildViewWithId(R.id.removeButton)));
      int expectedCount = countItemsBefore - 1;
      onView(withId(R.id.databaseRecyclerView)).check(matches(hasChildCount(expectedCount)));
    }
  }

  @Test
  public void addItemToDatabaseTest() {
    int countItemsBefore = this.getItemCount();
    onView(withId(R.id.addDatabaseMenuItem)).perform(click());
    Activity addActivity = addActivityRule.getActivity();
    Instrumentation.ActivityResult result = createPictureResultStub(addActivity);
    intending(hasAction(Intent.ACTION_OPEN_DOCUMENT)).respondWith(result);
    onView(withId(R.id.addFromDeviceButton)).perform(click());
    onView(withId(R.id.nameInputText)).perform(typeText("Tom Hanks"), closeSoftKeyboard());
    onView(withId(R.id.saveFromDeviceButton)).perform(click());

    int exceptedItemsCount = countItemsBefore + 1;
    onView(withId(R.id.databaseRecyclerView)).check(matches(hasChildCount(exceptedItemsCount)));

    TestViewActions testViewAction = new TestViewActions();

    onView(withId(R.id.databaseRecyclerView))
            .perform(RecyclerViewActions.actionOnItemAtPosition(exceptedItemsCount-1, testViewAction.clickChildViewWithId(R.id.removeButton)));
  }

  public Instrumentation.ActivityResult createPictureResultStub(Activity activity) {
    Intent resultData = new Intent();
    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString(), "hanks.jpg");
    Uri uri = Uri.fromFile(file);
    resultData.setData(uri);
    return new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
  }

  public class TestViewActions {
    public ViewAction clickChildViewWithId(final int id) {
      return new ViewAction() {
        @Override
        public Matcher<View> getConstraints() {
          return null;
        }

        @Override
        public String getDescription() {
          return "Click on a child view with specified id.";
        }

        @Override
        public void perform(UiController uiController, View view) {
          View v = view.findViewById(id);
          v.performClick();
        }
      };
    }
  }

}
