package com.example.inf0251atrabalho1.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.inf0251atrabalho1.R;
import com.example.inf0251atrabalho1.database.DBSQLiteHelper;
import com.example.inf0251atrabalho1.model.Photo;
import com.example.inf0251atrabalho1.util.PhotoRotationUtil;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

public class PhotoDetailActivity extends AppCompatActivity {

    private TextInputEditText edtTitle;
    private TextInputEditText edtDescription;
    private ImageView imgPhoto;
    private Button btnSave;
    private Button btnEdit;
    private Button btnDelete;

    private Photo photo;
    private String photoPath;

    private DBSQLiteHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);

        db = new DBSQLiteHelper(this);

        findViewsByIds();

        Intent it = getIntent();
        if (it.hasExtra("photoPath")) {
            btnEdit.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            photoPath = it.getStringExtra("photoPath");
            showPhoto();
        } else if (it.hasExtra("photo")) {
            btnSave.setVisibility(View.GONE);
            enableOrDisableTextInputEditTexts(false);
            photo = (Photo) it.getSerializableExtra("photo");
            photoPath = photo.getPhotoPath();
            edtTitle.setText(photo.getTitle());
            edtDescription.setText(photo.getDescription());
            showPhoto();
        }
    }

    private void findViewsByIds() {

        edtTitle = findViewById(R.id.edt_title);
        edtDescription = findViewById(R.id.edt_description);
        imgPhoto = findViewById(R.id.img_photo);
        btnSave = findViewById(R.id.btn_save);
        btnEdit = findViewById(R.id.btn_edit);
        btnDelete = findViewById(R.id.btn_delete);
    }

    private void showPhoto() {

        /*Bitmap bitmap = BitmapFactory.decodeFile(photoPath);
        bitmap = PhotoRotationUtil.rotate(bitmap, photoPath);
        imgPhoto.setImageBitmap(bitmap);*/
        Glide.with(this).load(photoPath).into(imgPhoto);
    }

    public void saveOrUpdatePhoto(View view) {

        boolean addPhoto = false;
        if (photo == null) {
            photo = new Photo();
            addPhoto = true;
        }

        photo.setTitle(edtTitle.getText().toString());
        photo.setDescription(edtDescription.getText().toString());
        photo.setPhotoPath(photoPath);

        int idOrQty;

        if (addPhoto) {
            idOrQty = db.addPhoto(photo);
        } else {
            idOrQty = db.updatePhoto(photo);
        }

        if (idOrQty > 0) {
            if (addPhoto)
                showToast(R.string.save_photo_success);
            else
                showToast(R.string.update_photo_success);
        } else {
            if (addPhoto)
                showToast(R.string.save_photo_fail);
            else
                showToast(R.string.update_photo_fail);
        }

        finish();
    }

    public void editPhoto(View view) {

        enableOrDisableTextInputEditTexts(true);
        btnEdit.setVisibility(View.GONE);
        btnDelete.setVisibility(View.GONE);
        btnSave.setVisibility(View.VISIBLE);
    }

    public void deletePhoto(View view) {

        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(R.string.delete);
        alertDialog.setMessage(getString(R.string.delete_photo_warning));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.yes),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // == ==
                        int qty = db.deletePhoto(photo);

                        if (qty > 0) {
                            File photoFile = new File(photoPath);
                            if (photoFile.exists())
                                photoFile.delete();
                            showToast(R.string.delete_photo_success);
                        } else {
                            showToast(R.string.delete_photo_fail);
                        }

                        finish();
                        // == ==
                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.no),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }

    public void openPhoto(View view) {

        Intent intent = new Intent(this, PhotoActivity.class);
        intent.putExtra("photoPath", photoPath);
        startActivity(intent);
    }

    private void enableOrDisableTextInputEditTexts(boolean enabled) {

        edtTitle.setEnabled(enabled);
        edtDescription.setEnabled(enabled);
    }

    private void showToast(int messageResId) {

        Toast.makeText(this, messageResId, Toast.LENGTH_LONG).show();
    }
}
