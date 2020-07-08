package com.example.parstagram;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.exifinterface.media.ExifInterface;

import com.example.parstagram.ui.fragments.camera.CameraFragment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Based on https://gist.github.com/nesquena/3885707fd3773c09f1bb
 */

public class BitmapManipulation {
    public static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 42;

    /**
     * Scale and keep aspect ratio.
     * @param b, Bitmap to scale
     * @param width, desired width
     * @return rescaled Bitmap
     */
    public static Bitmap scaleToFitWidth(Bitmap b, int width)
    {
        float factor = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, width, (int) (b.getHeight() * factor), true);
    }


    /**
     * Scale and keep aspect ratio.
     * @param b, Bitmap to scale
     * @param height, desired height
     * @return rescaled Bitmap
     */
    public static Bitmap scaleToFitHeight(Bitmap b, int height)
    {
        float factor = height / (float) b.getHeight();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factor), height, true);
    }


    /**
     * Scale and keep aspect ratio.
     * @param b, Bitmap to scale
     * @param width, desired width
     * @param height, desired height
     * @return rescaled Bitmap
     */
    public static Bitmap scaleToFill(Bitmap b, int width, int height)
    {
        float factorH = height / (float) b.getWidth();
        float factorW = width / (float) b.getWidth();
        float factorToUse = (factorH > factorW) ? factorW : factorH;
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorToUse),
                (int) (b.getHeight() * factorToUse), true);
    }


    /**
     * Scale and don't keep aspect ratio.
     * @param b, Bitmap to scale
     * @param width, desired width
     * @param height, desired height
     * @return stretched Bitmap
     */
    public static Bitmap strechToFill(Bitmap b, int width, int height)
    {
        float factorH = height / (float) b.getHeight();
        float factorW = width / (float) b.getWidth();
        return Bitmap.createScaledBitmap(b, (int) (b.getWidth() * factorW),
                (int) (b.getHeight() * factorH), true);
    }

    /**
     * Rotate Bitmap based on EXIF orientation.
     * Based on https://stackoverflow.com/questions/12933085/android-camera-intent-saving-image-landscape-when-taken-portrait/12933632#12933632
     * @param photoFilePath
     * @return
     */
    public static Bitmap rotateBitmapOrientation(String photoFilePath) {
        // Create and configure BitmapFactory
        BitmapFactory.Options bounds = new BitmapFactory.Options();
        bounds.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(photoFilePath, bounds);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        Bitmap bm = BitmapFactory.decodeFile(photoFilePath, opts);
        // Read EXIF Data
        ExifInterface exif = null;
        try {
            exif = new ExifInterface(photoFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String orientString = exif.getAttribute(ExifInterface.TAG_ORIENTATION);
        int orientation = orientString != null ?
                Integer.parseInt(orientString) : ExifInterface.ORIENTATION_NORMAL;
        int rotationAngle = 0;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_90) rotationAngle = 90;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_180) rotationAngle = 180;
        if (orientation == ExifInterface.ORIENTATION_ROTATE_270) rotationAngle = 270;
        // Rotate Bitmap
        Matrix matrix = new Matrix();
        matrix.setRotate(rotationAngle, (float) bm.getWidth() / 2, (float) bm.getHeight() / 2);
        Bitmap rotatedBitmap = Bitmap.createBitmap(bm, 0, 0,
                bounds.outWidth, bounds.outHeight, matrix, true);
        // Return result
        return rotatedBitmap;
    }


    public static void launchCamera(Activity context, String photoFileName, File photoFile, String TAG) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        photoFile = getPhotoFileUri(context, photoFileName, TAG);

        Uri fileProvider = FileProvider.getUriForFile(context,
                "com.codepath.fileprovider.Parstagram", photoFile);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider);

        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
        }
    }

    public static File getPhotoFileUri(Context context, String fileName, String TAG) {
        File mediaStorageDir = new File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                TAG);

        if (!mediaStorageDir.exists()
                && !mediaStorageDir.mkdirs()) {
            Log.d(TAG, "getPhotoFileUri: failed to create directory");
        }

        return new File(mediaStorageDir.getPath() + File.separator + fileName);
    }

    public static File writeResizedBitmap(Context context, String photoFileName,
                                          Bitmap changedBitmap, String resized, String TAG){
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        changedBitmap.compress(Bitmap.CompressFormat.JPEG, 40, bytes);

        String filename = photoFileName + "_" + resized;
        File resizedFile = getPhotoFileUri(context, filename, TAG);

        try {
            resizedFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(resizedFile);
            fos.write(bytes.toByteArray());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resizedFile;
    }

}