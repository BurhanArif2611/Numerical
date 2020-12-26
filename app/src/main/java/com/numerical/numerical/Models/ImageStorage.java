package com.numerical.numerical.Models;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.FileProvider;

import com.numerical.numerical.Utility.ErrorMessage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ImageStorage {
    public static String saveToSdCard(Bitmap bitmap, String filename, Context context) {


        String stored = null;

       /* File sdcard = Environment.getExternalStorageDirectory() ;
        File folder = new File(sdcard.getAbsoluteFile(), ".your_specific_directory");//the dot makes this directory hidden to the user
        folder.mkdir();*/
        File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Numerical");
        if (!f.isFile()) {
            if (!(f.isDirectory())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    try {
                        Files.createDirectory(Paths.get(f.getAbsolutePath()));
                    } catch (IOException e) {
                        e.printStackTrace();

                    }
                } else {
                    f.mkdir();
                }
            }
        }

        File file = new File(f, filename);
        if (file.exists()) {
            try {
                File fdelete = new File(file.getPath());
                if (fdelete.delete()) {
                  //  Log.e("file Deleted :","" + file.getPath());
                    try {
                        FileOutputStream out = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                        out.flush();
                        out.close();
                        stored = "success";
                        try {
                            Uri uri;
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                                uri = FileProvider.getUriForFile(context, "com.numerical.numerical.provider", file);
                            } else {
                                uri = Uri.fromFile(file);
                            }
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            intent.setDataAndType(uri, "image/*");
                            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            context.startActivity(intent);
                        } catch (Exception e) {
                            ErrorMessage.E("Exception" + e.toString());
                            Toast.makeText(context, "Application not found", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        ErrorMessage.E("Exception <<" + e.toString());
                    }
                } else {
                  //  Log.e("file not Deleted :","" + file.getPath());
                }

            } catch (Exception e) {
                ErrorMessage.E("Exception" + e.toString());
                Toast.makeText(context, "Application not found", Toast.LENGTH_SHORT).show();
            }
            return stored;
        }
        ErrorMessage.E("Path" + file.getAbsolutePath());
        ErrorMessage.E("Path1" + file.getPath());


        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
            out.flush();
            out.close();
            stored = "success";
            try {
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    uri = FileProvider.getUriForFile(context, "com.numerical.numerical.provider", file);
                } else {
                    uri = Uri.fromFile(file);
                }
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setDataAndType(uri, "image/*");
                intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                context.startActivity(intent);
            } catch (Exception e) {
                ErrorMessage.E("Exception" + e.toString());
                Toast.makeText(context, "Application not found", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
            ErrorMessage.E("Exception >>" + e.toString());
        }
        return stored;
    }

    public static File getImage(String imagename) {

        File mediaImage = null;
        try {
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Numerical");
            if (!f.isFile()) {
                if (!(f.isDirectory())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                        try {
                            Files.createDirectory(Paths.get(f.getAbsolutePath()));
                        } catch (IOException e) {
                            e.printStackTrace();

                        }
                    } else {
                        f.mkdir();
                    }
                }
            }
            /*
             * */

            mediaImage = new File(f, imagename);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return mediaImage;
    }

    public static boolean checkifImageExists(String imagename) {
        Bitmap b = null;
        File file = ImageStorage.getImage(imagename);
        String path = file.getAbsolutePath();
        //  Log.e("checkifImageExists", ">>" + path);
        if (path != null) b = BitmapFactory.decodeFile(path);

        if (b == null || b.equals("")) {
            return false;
        }
        return true;
    }
}
