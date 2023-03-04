package com.oblig1.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Picture {

  @PrimaryKey(autoGenerate = true)
  private int id;

  private String name;
  private String filename;

  public Picture(String name, String filename) {
    this.name = name;
    this.filename = filename;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getFilename() {
    return filename;
  }

  public void setFilename(String filename) {
    this.filename = filename;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }
}
