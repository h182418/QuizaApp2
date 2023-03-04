package com.oblig1.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.oblig1.dao.PictureDAO;
import com.oblig1.entities.Picture;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Database(entities = {Picture.class}, version = 4, exportSchema = false)
public abstract class Repository extends RoomDatabase {

  public abstract PictureDAO pictureDAO();
  public static final String DB_NAME = "picture_db";
  private static Repository instance;
  private static Context currentContext;

  public static Repository getInstance(Context context) {
    currentContext=context;
    if (instance == null) {
      instance = Room
              .databaseBuilder(context, Repository.class, DB_NAME)
              .fallbackToDestructiveMigration()
              .allowMainThreadQueries()
              .build();
      if(instance.pictureDAO().getAllPictures().isEmpty()) {
        initializePictures();
      }
    }
    return instance;
  }

  private static void initializePictures() {

    // Pictures that we can use in app from startup
    saveImage("https://upload.wikimedia.org/wikipedia/commons/8/82/Damon_cropped.jpg", "damon");
    instance.pictureDAO().insertPicture(new Picture("Matt Damon", "damon"));
    saveImage("https://upload.wikimedia.org/wikipedia/commons/b/bd/Glasto17-44_%2835547413626%29_Cropped.jpg", "cooper");
    instance.pictureDAO().insertPicture(new Picture("Bradley Cooper", "cooper"));
    saveImage("https://upload.wikimedia.org/wikipedia/commons/thumb/8/81/Kevin_Smith_%2848477230947%29_%28cropped%29.jpg/1024px-Kevin_Smith_%2848477230947%29_%28cropped%29.jpg","smith");
    instance.pictureDAO().insertPicture(new Picture("Kevin Smith", "smith"));

    // Pictures we can add to app
    saveImage("https://upload.wikimedia.org/wikipedia/commons/thumb/a/a9/Tom_Hanks_TIFF_2019.jpg/1024px-Tom_Hanks_TIFF_2019.jpg", "hanks");
    saveImage("https://upload.wikimedia.org/wikipedia/commons/8/8d/George_Clooney_2016.jpg", "clooney");
  }

  private static void saveImage(String url, final String filename) {
    Glide.with(currentContext).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
      @Override
      public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
        try {
          File myDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES).toString());
          if (!myDir.exists()) {
            myDir.mkdirs();
          }

          String fileUri = myDir.getAbsolutePath() + "/" + filename + ".jpg";
          FileOutputStream outputStream = new FileOutputStream(fileUri);
          bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);

          outputStream.flush();
          outputStream.close();
        } catch (IOException e) {
          e.printStackTrace();
        }
      }

      @Override
      public void onLoadCleared(Drawable placeholder) {
      }
    });
  }
}
