package com.oblig1.handler;

import android.content.Context;

import com.oblig1.entities.Picture;
import com.oblig1.exceptions.DataValidationException;
import com.oblig1.repository.Repository;

public class Handler {

  private Repository repository;

  public Handler(Context context) {
    repository = Repository.getInstance(context);
  }

  public void savePicture(String name, String filename) throws DataValidationException {
    if (filename == null || filename.isEmpty()) {
      throw new DataValidationException("Missing filename.");
    }

    if (name == null || name.isEmpty()) {
      throw new DataValidationException("No name given.");
    }

    if (!name.matches("^\\p{L}+[\\p{L}\\p{Z}\\p{P}]*")) {
      throw new DataValidationException("Name contains illegal characters.");
    }

    Picture duplicate = repository.pictureDAO().getPictureByName(name);
    if(duplicate!=null) {
      throw new DataValidationException("Name already exists.");
    }

    repository.pictureDAO().insertPicture(new Picture(name, filename));
  }

}
