package com.numerical.numerical.Models;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import com.numerical.numerical.Utility.ErrorMessage;

import java.io.FileOutputStream;
import java.net.URL;
import java.net.URLConnection;

public class GetImages extends AsyncTask<Object, Object, Object> {
    private String requestUrl, imagename_;
    private ImageView view;
    private Bitmap bitmap ;
    private FileOutputStream fos;
    ProgressDialog mProgressDialog;
    Context context;
    public GetImages(String requestUrl, String _imagename_, Context context) {
        this.requestUrl = requestUrl;
       // this.view = view;
        this.imagename_ = _imagename_ ;
        this.context = context ;
         mProgressDialog = new ProgressDialog(context);
        mProgressDialog.setTitle("Downloading Collection as a JPEG file.");
        mProgressDialog.setMessage("Loading...");
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.show();
    }

    @Override
    protected Object doInBackground(Object... objects) {
        try {
            URL url = new URL(requestUrl);
            URLConnection conn = url.openConnection();
            bitmap = BitmapFactory.decodeStream(conn.getInputStream());
        } catch (Exception ex) {
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        ImageStorage.saveToSdCard(bitmap, imagename_,context);
        mProgressDialog.dismiss();

        /*if(!ImageStorage.checkifImageExists(imagename_))
        {
            //view.setImageBitmap(bitmap);
            ImageStorage.saveToSdCard(bitmap, imagename_,context);
        }
        else{
            Log.e("else is working",">>");

        }*/
    }
}
