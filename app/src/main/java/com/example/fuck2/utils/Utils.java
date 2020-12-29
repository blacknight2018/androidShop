package com.example.fuck2.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.widget.ImageView;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONArray;
import com.example.fuck2.ImagePreview;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Utils {
    public static String EmptyString = "";

    public static String MapToHttpParam(HashMap<String, String> param) {
        String ret = EmptyString;
        Object[] keys = param.keySet().toArray();
        int len = keys.length;
        for (int i = 0; i < len; i++) {
            System.out.println(keys[i]);
            if (i > 0)
                ret = ret + "&";
            ret = ret + keys[i];
            ret = ret + "=";
            ret = ret + param.get(keys[i]);
        }
        return ret;
    }

    public static List<String> ParseJSONString(String str) {
        JSONArray jsonArray = JSONArray.parseArray(str);
        List<String> stringList = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            stringList.add(jsonArray.get(i).toString());
        }
        return stringList;
    }

    public static String getRandomString(int length) {
        String str = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            int number = random.nextInt(62);
            sb.append(str.charAt(number));
        }
        return sb.toString();
    }

    public static void AllowFileSystemAccess(Context context, Activity activity) {
        int REQUEST_EXTERNAL_STORAGE = 1;
        String[] PERMISSIONS_STORAGE = {
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE};

        if (PackageManager.PERMISSION_GRANTED !=
                ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_CONTACTS)) {
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
        }
    }

    public static void PreviewImage(ImageView imageView, Context context) {
        String localPath = Utils.ImageViewToLocalPath((ImageView) imageView, context);
        Intent intent = new Intent(context, ImagePreview.class);
        intent.putExtra("url", localPath);
        context.startActivity(intent);
    }

    public static void PreviewImage(String imageUrl, Context context) {
        Intent intent = new Intent(context, ImagePreview.class);
        intent.putExtra("url", imageUrl);
        context.startActivity(intent);
    }

    private static String ImageViewToLocalPath(ImageView imageView, Context context) {
        String retPath = null;
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();


        File file = new File(context.getCacheDir(), getRandomString(10));
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArray, 0, byteArray.length);
            fileOutputStream.flush();
            retPath = file.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
        return retPath;
    }


    public static void WritePreferences(Context context, String key, String value) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String ReadPreference(Context context, String key) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("data", Context.MODE_PRIVATE);
        return sharedPreferences.getString(key, EmptyString);
    }

    public static void main(String[] args) {

    }

}
