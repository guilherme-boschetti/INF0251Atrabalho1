package com.example.inf0251atrabalho1.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;

public class PhotoRotationUtil {

    private static int getRotationFromCamera(String photoPath) {
        int rotation = 0;
        try {
            ExifInterface exif = new ExifInterface(photoPath);
            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
            }
        } catch (Exception e) {
            // do nothing
        }
        return rotation;
    }

    public static Bitmap rotate(Bitmap bm, String photoPath) {
        int rotation = getRotationFromCamera(photoPath);
        if (rotation != 0) {
            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);
            return Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        }
        return bm;
    }
}
