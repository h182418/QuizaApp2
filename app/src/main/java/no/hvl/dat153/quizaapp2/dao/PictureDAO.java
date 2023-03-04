package no.hvl.dat153.quizaapp2.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

import no.hvl.dat153.quizaapp2.entities.Picture;

@Dao
public interface PictureDAO {

  @Insert
  public void insertPicture(Picture picture);

  @Delete
  public void deletePicture(Picture picture);

  @Query("SELECT * FROM picture ORDER BY RANDOM() LIMIT 1")
  public Picture getRandomPicture();

  @Query("SELECT * FROM picture")
  public List<Picture> getAllPictures();

  @Query("SELECT * FROM picture")
  public List<Picture> loadAllPictures();

  @Query("SELECT * FROM picture WHERE name LIKE(:name)")
  public Picture getPictureByName(String name);

}
