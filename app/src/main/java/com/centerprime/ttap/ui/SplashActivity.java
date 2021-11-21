package com.centerprime.ttap.ui;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import com.centerprime.ttap.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class SplashActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
/*
        ActivityCompat.requestPermissions(this, new String[]{(Manifest.permission.WRITE_EXTERNAL_STORAGE)}, 1);
        ActivityCompat.requestPermissions(this, new String[]{(Manifest.permission.READ_EXTERNAL_STORAGE)}, 1);
*/

        ConstraintLayout constraintLayout = findViewById(R.id.constraint);


        new Handler(Looper.getMainLooper()).postDelayed((Runnable) () -> {

//            Bitmap bitmap = getScreenShotFromView(constraintLayout);
//
//            // if bitmap is not null then
//            // save it to gallery
//            if (bitmap != null) {
//                try {
//                    saveMediaToStorage(bitmap);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }

            startActivity(new Intent(this, WalletActivity.class));
            finish();

        }, 1500);
    }
    Bitmap getScreenShotFromView(View view) {
        Bitmap screenshot = null;
        try {
            screenshot = Bitmap.createBitmap(300, 700, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(screenshot);
            view.draw(canvas);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return screenshot;
    }
    public void saveMediaToStorage(Bitmap bitmap) throws IOException {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
        String mPath = Environment.getExternalStorageDirectory().toString() + "/" + now + ".jpg";
        File a = new File(getApplicationContext().getFileStreamPath(now + ".jpg").getPath());
        System.out.println(a.getPath());
        File imageFile = new File(mPath);



        FileOutputStream outputStream = new FileOutputStream(a);
        int quality = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
        outputStream.flush();
        outputStream.close();

        openScreenshot(a);

    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }

}
