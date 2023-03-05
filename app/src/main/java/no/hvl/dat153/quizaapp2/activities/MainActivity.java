package no.hvl.dat153.quizaapp2.activities;

import static android.Manifest.permission.INTERNET;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import no.hvl.dat153.quizaapp2.R;
import no.hvl.dat153.quizaapp2.repository.Repository;

public class MainActivity extends AppCompatActivity {

    private Button quizButton;
    private Button databaseButton;
    private Button addButton;

    private static final int PERMISSION_REQUEST_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.initializeView();
        if (!this.checkPermission()) {
            this.requestPermission();
        }
        ;
    }

    private void initializeView() {
        quizButton = findViewById(R.id.Quizbutton);
        databaseButton = findViewById(R.id.databaseButton);
        addButton = findViewById(R.id.Addbutton);

        databaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, DatabaseActivity.class);
                startActivity(intent);
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddPictureActivity.class);
                startActivity(intent);
            }
        });

        quizButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Repository.getInstance(getApplicationContext()).pictureDAO().getAllPictures().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Add an item to the database to start quiz", Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(MainActivity.this, QuizActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private boolean checkPermission() {
        int r1 = ContextCompat.checkSelfPermission(getApplicationContext(), INTERNET);
        int r2 = ContextCompat.checkSelfPermission(getApplicationContext(), WRITE_EXTERNAL_STORAGE);
        int r3 = ContextCompat.checkSelfPermission(getApplicationContext(), READ_EXTERNAL_STORAGE);
        return r1 == PackageManager.PERMISSION_GRANTED && r2 == PackageManager.PERMISSION_GRANTED && r3 == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{INTERNET, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, PERMISSION_REQUEST_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                if (grantResults.length > 0) {

                    boolean internetAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeExternalAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    boolean readExternalAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (internetAccepted && writeExternalAccepted && readExternalAccepted) {
                        Toast.makeText(this, "Permission granted. You can now use the app.", Toast.LENGTH_SHORT);
                    } else {
                        Toast.makeText(this, "Permission denied. The app won't work.", Toast.LENGTH_SHORT);
                   }
                    /*
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        if (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                            this.showMessageOKCancel("You need to allow access to all of the permissions",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                                requestPermissions(new String[]{INTERNET, WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                                                        PERMISSION_REQUEST_CODE);
                                            }
                                        }
                                    });
                            return;
                        }
                    }
                }
            default:
                break;
        }

        private void showMessageOKCancel (String message, DialogInterface.OnClickListener okListener)
        {
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage(message)
                    .setPositiveButton("OK", okListener)
                    .setNegativeButton("Cancel", null)
                    .create()
                    .show();
        }
   }*/
}

        }
    }
};