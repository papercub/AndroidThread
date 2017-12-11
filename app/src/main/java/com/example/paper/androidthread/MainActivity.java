package com.example.paper.androidthread;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView ivImage;
    private Button btnThread;
    private Button btnAsync;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImage = (ImageView) findViewById(R.id.iv_image);

        btnThread = (Button) findViewById(R.id.btn_thread);
        btnAsync = (Button) findViewById(R.id.btn_async);

        initButton();
    }

    private void initButton() {
        btnThread.setOnClickListener(this);
        btnAsync.setOnClickListener(this);
    }

    private void showPictureOnClick() throws IOException {
        new Thread(new Runnable() {
            @Override
            public void run() {
                URL url = null;

                try {
                    url = new URL("http://i0.kym-cdn.com/entries/icons/original/000/000/554/picard-facepalm.jpg");
                }
                catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                Bitmap bmp = null;
                try {
                    bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                } catch (IOException e) {
                    e.printStackTrace();
                }

                final Bitmap finalBmp = bmp;
                ivImage.post(new Runnable() {
                    @Override
                    public void run() {
                        ivImage.setImageBitmap(finalBmp);
                    }
                });
            }
        }).start();
    }

    private class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        @Override
        protected Bitmap doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

            Bitmap bmp = null;
            try {
                bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmp;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            ivImage.setImageBitmap(bitmap);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_thread:
                try {
                    showPictureOnClick();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.btn_async:
                new LoadImageTask().execute("https://pixel.nymag.com/imgs/daily/vulture/2016/04/19/19-sad-affleck.w1200.h630.jpg");
                break;
        }
    }
}
