package com.oblig1.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputEditText;
import com.oblig1.MainActivity;
import com.oblig1.entities.Picture;
import com.oblig1.R;
import com.oblig1.exceptions.DataValidationException;
import com.oblig1.handler.Handler;
import com.oblig1.repository.Repository;

public class AddPictureActivity extends AppCompatActivity {

  static final int REQUEST_IMAGE_OPEN = 1;
  private String filename="";
  private Repository repository;
  private Handler handler;
  private Button addFromDeviceButton;
  private Button saveFromDeviceButton;
  private TextInputEditText nameInputText;
  private ImageView fromDeviceImageView;
  private Bitmap imageBitmap;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_add_picture);
    this.initializeView();
  }

  public void initializeView() {
    addFromDeviceButton = (Button) findViewById(R.id.addFromDeviceButton);
    nameInputText = (TextInputEditText) findViewById(R.id.nameInputText);
    fromDeviceImageView = (ImageView) findViewById(R.id.fromDeviceImageView);
    saveFromDeviceButton = (Button) findViewById(R.id.saveFromDeviceButton);
    repository = Repository.getInstance(this);
    handler = new Handler(getApplicationContext());

    nameInputText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
      @Override
      public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
          hideKeyboard(v);
        }
      }
    });

    addFromDeviceButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
      }
    });

    saveFromDeviceButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        try {
          handler.savePicture(nameInputText.getText().toString(), filename);
          Intent intent = new Intent(AddPictureActivity.this, DatabaseActivity.class);
          startActivity(intent);
        } catch (DataValidationException e) {
          Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
        }
      }
    });
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
      Uri uri = data.getData();
      setFilenameFromUri(uri);
      Glide.with(this)
              .asBitmap()
              .load(uri)
              .into(fromDeviceImageView);

      this.showForm();
    }
  }

  public void showForm() {
    fromDeviceImageView.setVisibility(View.VISIBLE);
    saveFromDeviceButton.setVisibility(View.VISIBLE);
    nameInputText.setVisibility(View.VISIBLE);
  }

  public void setFilenameFromUri(Uri uri) {
    Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);
    if(returnCursor == null) {
      filename="hanks";
    } else {
      int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
      returnCursor.moveToFirst();
      String filenameWithExtension = returnCursor.getString(nameIndex);
      filename = filenameWithExtension.substring(0, filenameWithExtension.lastIndexOf('.'));
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflate = getMenuInflater();
    inflate.inflate(R.menu.menu_add, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if (item.getItemId() == R.id.databaseAddMenuItem) {
      startActivity(new Intent(this, DatabaseActivity.class));
      return true;
    } else if (item.getItemId() == R.id.homeAddMenuItem) {
      startActivity(new Intent(this, MainActivity.class));
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }

  public void hideKeyboard(View view) {
    InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
    inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

}