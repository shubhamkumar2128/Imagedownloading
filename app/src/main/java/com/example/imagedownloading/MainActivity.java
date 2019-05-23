package com.example.imagedownloading;

import android.Manifest;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    Button dwnld;
    EditText editText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.INTERNET}, 121);

        }
        dwnld = findViewById(R.id.downloadbtn);
        editText = findViewById(R.id.eturl);
        editText.setText("https://images.pexels.com/photos/257360/pexels-photo-257360.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500");
        dwnld.setOnClickListener(new View.OnClickListener() {
            @Override

            public void onClick(View v) {
                Thread mythread = new Thread(new DownloadThread());
                mythread.start();
            }
        });
    }

    public boolean downloadImageThroughThread(String url) {
        URL url1 = null;
        boolean success = false;
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        FileOutputStream fileOutputStream = null;
        File file = null;
        try {
            url1 = new URL(url);
            connection = (HttpURLConnection) url1.openConnection();
            inputStream = connection.getInputStream();
            file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + "/" + Uri.parse(url).getLastPathSegment());
            Log.d("msg", file.getAbsolutePath());
            fileOutputStream = new FileOutputStream(file);
            int read = -1;
            byte b[] = new byte[1024];
            while ((read = inputStream.read(b)) != -1) {

                fileOutputStream.write(b, 0, read);
            }
            success = true;

        } catch (MalformedURLException e) {
            Log.d("msg", e.getMessage());
        } catch (IOException e) {
            Log.d("msg", e.getMessage());
        } finally {
            if (connection != null)
                connection.disconnect();
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.d("msg", e.getMessage());
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    Log.d("msg", e.getMessage());
                }
            }
        }
        return success;

    }

    private class DownloadThread implements Runnable {

        @Override
        public void run() {
            Log.d("msg", "run()");
            if(downloadImageThroughThread(editText.getText().toString())==true)
            {
                Log.d("msg", "Downloaded successful");
            }

        }
    }
}
