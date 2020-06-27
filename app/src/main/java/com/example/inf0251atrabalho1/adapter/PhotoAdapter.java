package com.example.inf0251atrabalho1.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.inf0251atrabalho1.R;
import com.example.inf0251atrabalho1.activity.PhotoDetailActivity;
import com.example.inf0251atrabalho1.model.Photo;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder> {

    private Context context;
    private List<Photo> photos;

    public PhotoAdapter(Context context, List<Photo> photos) {
        this.context = context;
        this.photos = photos;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.photo_item_layout, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final Photo photo = photos.get(position);
        viewHolder.setData(photo);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PhotoDetailActivity.class);
                intent.putExtra("photo", photo);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return photos.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgPhotoItem;
        private TextView txtTitleItem;
        private TextView txtDescriptionItem;
        private TextView txtPhotoPathItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPhotoItem = itemView.findViewById(R.id.img_photo_item);
            txtTitleItem = itemView.findViewById(R.id.txt_title_item);
            txtDescriptionItem = itemView.findViewById(R.id.txt_description_item);
            txtPhotoPathItem = itemView.findViewById(R.id.txt_photo_path_item);
        }

        private void setData(Photo photo) {
            String photoTitle = photo.getTitle();
            String photoPath = photo.getPhotoPath();
            String photoDescription = photo.getDescription();

            //Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
            //bitmap = PhotoRotationUtil.rotate(bitmap, photoPath);
            //imgPhotoItem.setImageBitmap(bitmap);
            Glide.with(context).load(photoPath).into(imgPhotoItem);
            txtTitleItem.setText(photoTitle);
            txtDescriptionItem.setText(photoDescription);
            txtPhotoPathItem.setText(photoPath);

            if ((photoTitle == null || photoTitle.isEmpty()) && (photoDescription == null || photoDescription.isEmpty())) {
                txtTitleItem.setVisibility(View.GONE);
                txtDescriptionItem.setVisibility(View.GONE);
                txtPhotoPathItem.setVisibility(View.VISIBLE);
            }
        }
    }
}
