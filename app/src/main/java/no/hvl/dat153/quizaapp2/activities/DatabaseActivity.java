package no.hvl.dat153.quizaapp2.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import no.hvl.dat153.quizaapp2.R;
import no.hvl.dat153.quizaapp2.views.DatabaseRecyclerViewAdapter;

public class DatabaseActivity extends AppCompatActivity {

  private RecyclerView databaseRecyclerView;
  private DatabaseRecyclerViewAdapter adapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_database);

    adapter = new DatabaseRecyclerViewAdapter(this);
    databaseRecyclerView = (RecyclerView) findViewById(R.id.databaseRecyclerView);

    databaseRecyclerView.setAdapter(adapter);
    databaseRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    MenuInflater inflate = getMenuInflater();
    inflate.inflate(R.menu.menu_database, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(@NonNull MenuItem item) {
    if(item.getItemId()==R.id.homeDatabaseMenuItem) {
      startActivity(new Intent(this, MainActivity.class));
      return true;
    } else if(item.getItemId()== R.id.addDatabaseMenuItem) {
      startActivity(new Intent(this, AddPictureActivity.class));
      return true;
    } else {
      return super.onOptionsItemSelected(item);
    }
  }



}