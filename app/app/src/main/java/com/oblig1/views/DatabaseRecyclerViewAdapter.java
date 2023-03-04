package com.oblig1.views;

import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.oblig1.entities.Picture;
import com.oblig1.R;
import com.oblig1.repository.Repository;

import java.util.List;

public class DatabaseRecyclerViewAdapter extends RecyclerView.Adapter<DatabaseRecyclerViewAdapter.ViewHolder> {

  private List<Picture> pictures;
  private Context mContext;
  private Repository repository;

  public DatabaseRecyclerViewAdapter(Context mContext) {
    this.mContext = mContext;
    this.repository=Repository.getInstance(mContext);
    pictures=repository.pictureDAO().getAllPictures();
    notifyDataSetChanged();
  }

  @NonNull
  @Override
  public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
    View view = LayoutInflater
            .from(parent.getContext())
            .inflate(R.layout.database_item_picture, parent, false);
    return new ViewHolder(view);
  }

  @Override
  public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
    holder.pictureNameText.setText(pictures.get(position).getName());

    Glide.with(mContext)
            .asBitmap()
            .load(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/" + pictures.get(position).getFilename() + ".jpg")
            .into(holder.pictureImageView);

    holder.removeButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Toast.makeText(mContext, pictures.get(position).getName() + " removed", Toast.LENGTH_SHORT).show();
        repository.pictureDAO().deletePicture(pictures.get(position));
        pictures=repository.pictureDAO().getAllPictures();
        notifyDataSetChanged();
      }
    });
  }

  @Override
  public int getItemCount() {
    return pictures.size();
  }

  public class ViewHolder extends RecyclerView.ViewHolder {

    private CardView parent;
    private ImageView pictureImageView;
    private TextView pictureNameText;
    private Button removeButton;

    public ViewHolder(@NonNull View itemView) {
      super(itemView);
      parent = (CardView) itemView.findViewById(R.id.parent);
      pictureImageView = (ImageView) itemView.findViewById(R.id.pictureImage);
      pictureNameText = (TextView) itemView.findViewById(R.id.pictureText);
      removeButton = (Button) itemView.findViewById(R.id.removeButton);
    }

  }

}
