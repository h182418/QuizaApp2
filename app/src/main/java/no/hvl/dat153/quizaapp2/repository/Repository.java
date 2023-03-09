package no.hvl.dat153.quizaapp2.repository;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Environment;
import android.transition.Transition;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import no.hvl.dat153.quizaapp2.dao.PictureDAO;
import no.hvl.dat153.quizaapp2.entities.Picture;

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
   saveImage("https://no.wikipedia.org/wiki/Erik_ten_Hag#/media/Fil:Erik_ten_Hag_2017.jpg", "tenhag");
    instance.pictureDAO().insertPicture(new Picture("Matt Damon", "damon"));
    saveImage("https://en.wikipedia.org/wiki/J%C3%BCrgen_Klopp#/media/File:J%C3%BCrgen_Klopp,_Liverpool_vs._Chelsea,_UEFA_Super_Cup_2019-08-14_04.jpg", "klopp");
    instance.pictureDAO().insertPicture(new Picture("Bradley Cooper", "cooper"));
    saveImage("https://no.wikipedia.org/wiki/Mikel_Arteta#/media/Fil:Mikel_Arteta_2021.png","arteta");
    instance.pictureDAO().insertPicture(new Picture("Kevin Smith", "smith"));

    // Pictures we can add to app
    saveImage("https://en.wikipedia.org/wiki/Lars_Ulrich#/media/File:Lars_Ulrich_(26060414430).jpg", "ulrich");
    saveImage("https://en.wikipedia.org/wiki/James_Hetfield#/media/File:James_Hetfield_2017.jpg", "hetfield");
  }

  private static void saveImage(String url, final String filename) {
    Glide.with(currentContext).asBitmap().load(url).into(new CustomTarget<Bitmap>() {
      public void onResourceReady(Bitmap bitmap, Transition transition) {
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
      public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {

      }

      @Override
      public void onLoadCleared(Drawable placeholder) {
      }
    });
  }
}
