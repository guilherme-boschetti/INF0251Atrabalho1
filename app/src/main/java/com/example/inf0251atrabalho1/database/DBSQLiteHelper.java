package com.example.inf0251atrabalho1.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.inf0251atrabalho1.model.Photo;

import java.util.ArrayList;
import java.util.List;

public class DBSQLiteHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "PhotoDB";
    private static final String TABLE_PHOTOS = "photos";
    private static final String ID = "id";
    private static final String TITLE = "title";
    private static final String DESCRIPTION = "description";
    private static final String PHOTO_PATH = "photo_path";
    private static final String[] COLUNAS = {ID, TITLE, DESCRIPTION, PHOTO_PATH};

    public DBSQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_PHOTOS + " (" +
                ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                TITLE + " TEXT," +
                DESCRIPTION + " TEXT," +
                PHOTO_PATH + " TEXT)" ;
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PHOTOS);
        this.onCreate(db);
    }

    public int addPhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(TITLE, photo.getTitle());
        values.put(DESCRIPTION, photo.getDescription());
        values.put(PHOTO_PATH, photo.getPhotoPath());
        long id = db.insert(TABLE_PHOTOS, null, values);
        db.close();
        return (int) id; // id do objeto inserido
    }

    public Photo getPhotoByID(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PHOTOS,       // a. tabela
                COLUNAS,                             // b. colunas
                " id = ?",                  // c. colunas para comparar
                new String[] { String.valueOf(id) }, // d. parâmetros
                null,                       // e. group by
                null,                        // f. having
                null,                       // g. order by
                null);                        // h. limit
        if (cursor == null) {
            return null;
        } else {
            cursor.moveToFirst();
            Photo photo = cursorToPhoto(cursor);
            return photo;
        }
    }

    private Photo cursorToPhoto(Cursor cursor) {
        Photo photo = new Photo();
        photo.setId(cursor.getInt(0));
        photo.setTitle(cursor.getString(1));
        photo.setDescription(cursor.getString(2));
        photo.setPhotoPath(cursor.getString(3));
        return photo;
    }

    public List<Photo> getAllPhotos() {
        List<Photo> listPhotos = new ArrayList<>();
        String query = "SELECT * FROM " + TABLE_PHOTOS + " ORDER BY " + ID;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                Photo photo = cursorToPhoto(cursor);
                listPhotos.add(photo);
            } while (cursor.moveToNext());
        }
        return listPhotos;
    }

    public int updatePhoto(Photo photo) {
        ContentValues values = new ContentValues();
        values.put(TITLE, photo.getTitle());
        values.put(DESCRIPTION, photo.getDescription());
        values.put(PHOTO_PATH, photo.getPhotoPath());

        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.update(TABLE_PHOTOS,                          //tabela
                values,                                          // valores
                ID + " = ?",                         // colunas para comparar
                new String[] { String.valueOf(photo.getId()) }); //parâmetros
        db.close();
        return i; // número de linhas modificadas
    }

    public int deletePhoto(Photo photo) {
        SQLiteDatabase db = this.getWritableDatabase();
        int i = db.delete(TABLE_PHOTOS,  //tabela
                ID + " = ?", // colunas para comparar
                new String[] { String.valueOf(photo.getId()) });
        db.close();
        return i; // número de linhas excluídas
    }
}
