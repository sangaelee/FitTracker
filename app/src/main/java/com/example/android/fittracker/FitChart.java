package com.example.android.fittracker;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class FitChart extends AsyncTask<String,Void, Bitmap> {


        private static final String TAG = FitChart.class.getSimpleName();

        private Context mContext;

        FitChart(Context context) {
            mContext = context;
        }

        protected void onPreExecute() {
            // We could do some setup work here before doInBackground() runs
        }

        protected Bitmap doInBackground(String... urls) {
            Log.v("doInBackground", "doing download of image");
            Bitmap bmap = null;
            // return downloadImage(urls);

            try {
                bmap = downloadImageUrlConnection(urls);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bmap;
        }

        protected void onProgressUpdate(Integer... progress) {
            TextView mText = (TextView) ((Activity) mContext).findViewById(R.id.text);
            mText.setText("Progress so far: " + progress[0]);
        }

        protected void onPostExecute(Bitmap result) {
            if(result != null) {
                ImageView mImage = (ImageView) ((Activity) mContext).findViewById(R.id.graph);
                mImage.setImageBitmap(result);
            } else {
               // TextView errorMsg = (TextView) ((Activity) mContext).findViewById(R.id.errorMsg);
               // errorMsg.setText("Problem downloading image. Please try again later.");
            }
        }


        public byte[] getUrlBytes(String urlSpec) throws IOException {
            URL url = new URL(urlSpec);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            try {
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                InputStream in = connection.getInputStream();
                if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                    throw new IOException(connection.getResponseMessage() +
                            ": with " +
                            urlSpec);
                }
                int bytesRead = 0;
                byte[] buffer = new byte[1024];
                while ((bytesRead = in.read(buffer)) > 0) {
                    out.write(buffer, 0, bytesRead);
                }
                out.close();
                return out.toByteArray();
            } finally {
                connection.disconnect();
            }
        }

        private Bitmap downloadImageUrlConnection(String[] url) throws IOException {


            if (url == null) {
                return null;
            }

            byte[] bitmapBytes = this.getUrlBytes(url[0]);
            Bitmap bitmap = BitmapFactory
                    .decodeByteArray(bitmapBytes, 0, bitmapBytes.length);
            return bitmap;
        }


}
