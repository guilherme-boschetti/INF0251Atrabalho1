package com.example.inf0251atrabalho1.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;

import com.example.inf0251atrabalho1.R;
import com.example.inf0251atrabalho1.adapter.PhotoAdapter;
import com.example.inf0251atrabalho1.database.DBSQLiteHelper;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final int PERMISSAO_REQUEST = 1;
    private final int CAMERA_REQUEST = 2;

    private RecyclerView recyclerView;

    private DBSQLiteHelper db;

    private File photoFile = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DBSQLiteHelper(this);

        recyclerView = findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        requestPermissions();
    }

    @Override
    protected void onStart() {
        super.onStart();

        PhotoAdapter photoAdapter = new PhotoAdapter(this, db.getAllPhotos());
        recyclerView.setAdapter(photoAdapter);
    }

    private void requestPermissions() {

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        boolean okCam =  result == PackageManager.PERMISSION_GRANTED;

        result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean okWrite =  result == PackageManager.PERMISSION_GRANTED;

        result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        boolean okRead =  result == PackageManager.PERMISSION_GRANTED;

        if (!okCam || !okWrite || !okRead) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSAO_REQUEST);
        }
    }

    public void takePhoto(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoFile = createFile();
            } catch (Exception ex) {
                showAlert(getString(R.string.error), getString(R.string.save_photo_fail));
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(getBaseContext(), getBaseContext().getApplicationContext().getPackageName() + ".provider", photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == CAMERA_REQUEST) {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.fromFile(photoFile)));
            savePhoto(photoFile.getAbsolutePath());
        }
    }

    private File createFile() {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File folder = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        return new File(folder.getPath() + File.separator + "JPG_" + timeStamp + ".jpg");
    }

    private void savePhoto(String photoPath) {
        Intent intent = new Intent(this, PhotoDetailActivity.class);
        intent.putExtra("photoPath", photoPath);
        startActivity(intent);
    }

    private void showAlert(String titulo, String mensagem) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle(titulo);
        alertDialog.setMessage(mensagem);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, getString(R.string.ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}
