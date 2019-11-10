package com.example.javacv.interfaces;

import android.os.AsyncTask;

import org.bytedeco.javacpp.Loader;
import org.bytedeco.opencv.opencv_java;

public class MenuAsync extends AsyncTask<String, Integer, String> {// 异步加载主菜单
    @Override
    public void onPreExecute() {
        ;
    }

    @Override
    protected String doInBackground(String... strings) {
        Loader.load(opencv_java.class);// openCV初始化,不能直接放在class开头


        return null;
    }

    @Override
    public void onProgressUpdate(Integer... progresses) {
        ;
    }

    @Override
    public void onPostExecute(String result) {
        ;
    }

    @Override
    public void onCancelled() {
        ;
    }
}
