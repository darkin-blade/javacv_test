package com.example.javacv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_stitching.Stitcher;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;

public class LocalRecognize extends DialogFragment {
    String appPath = MainActivity.appPath;// TODO

    public int img_height = 400;
    public int img_margin = 20;

    public SelectImg selectImg;// 选取图片
    public SaveImg saveImg;

    public Bitmap combinedImg;// 合并后的图片

    public Button btnAdd;// 添加本地图片
    public Button btnDel;// 删除添加的图片
    public Button btnWork;// 生成->保存合并的图片
    public Button btnBack;// 返回主菜单

    static public View myView;
    static public FragmentManager fragmentManager;

    public ArrayList<String> imgList;// TODO 所有添加的图片
    public ArrayList<String> delList;// TODO 所有要删除的图片

    public LinearLayout imgLayout;// 图片显示列表

    @Override
    public void show(FragmentManager fragmentManager, String tag) {
        super.show(fragmentManager, tag);
        MainActivity.window_num = MainActivity.LOCAL_RECOGNIZE;

        this.fragmentManager = fragmentManager;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_FRAME, android.R.style.Theme);// 关闭背景(点击外部不能取消)
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.local_recognize, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));// 背景透明

        initData();
        initBtn();

        return myView;
    }

    public void initData() {// TODO
        imgList = new ArrayList<String>();
        delList = new ArrayList<String>();
        combinedImg = null;// TODO 初始化为空
        imgLayout = myView.findViewById(R.id.img_list);
        selectImg = new SelectImg();// 初始化文件浏览器
        saveImg = new SaveImg();// 初始化文件管理器
    }

    public void initBtn() {
        btnAdd = myView.findViewById(R.id.button_1);
        btnDel = myView.findViewById(R.id.button_2);
        btnWork = myView.findViewById(R.id.button_3);
        btnBack = myView.findViewById(R.id.button_4);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                combinedImg = null;// 清除结果
                selectImg.show(fragmentManager, "save");
            }
        });

        btnWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                combineImg();
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                combinedImg = null;// 清除结果
                for (int i = 0; i < delList.size(); i ++) {
                    imgList.remove(delList.get(i));
                }
                delList.clear();// 清空
                showImg();// 更新图片
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                combinedImg = null;// 清除结果
                dismiss();
            }
        });
    }

    public void showImg() {// 实时更新图片显示 TODO 以缩略图的方式显示
        imgLayout.removeAllViews();

        LinearLayout.LayoutParams frameParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, img_height);
        LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams boxParam = new LinearLayout.LayoutParams(0, 0);// 不显示
        imgParam.setMargins(img_margin, img_margin, img_margin, img_margin);
        Bitmap bitmap;// 重复使用

        for (int i = 0; i < imgList.size(); i ++) {
            // 外框
            final LinearLayout imageFrame = new LinearLayout(getContext());
            imageFrame.setLayoutParams(frameParam);

            // 只用于判断选取
            final CheckBox checkBox = new CheckBox(getContext());
            checkBox.setLayoutParams(boxParam);

            // 图片元素
            final ImageView imageView = new ImageView(getContext());
            imageView.setLayoutParams(imgParam);
            bitmap = BitmapFactory.decodeFile(imgList.get(i));
            imageView.setImageBitmap(bitmap);

            // 复选功能,用于删除
            final String name = imgList.get(i);// TODO
            MainActivity.infoLog("i: " + i + ", " + name + ": " + (imgList.get(i) == null));
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {// TODO
                    MainActivity.infoLog("name: " + name);
                    if (checkBox.isChecked()) {// 直接调用checkbox的监听,不需重复操作
                        imageFrame.setBackgroundResource(R.color.grey);
                        checkBox.setChecked(false);
                        delList.remove(name);
                    } else {
                        imageFrame.setBackgroundResource(R.color.grey_light);
                        checkBox.setChecked(true);
                        delList.add(name);
                    }
                }
            });

            // 添加元素
            imageFrame.addView(checkBox);
            imageFrame.addView(imageView);
            imgLayout.addView(imageFrame);
        }
    }

    public void combineImg() {// 合并图片 TODO 以缩略图的方式显示
        LinearLayout.LayoutParams frameParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, img_height);
        LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        imgParam.setMargins(img_margin, img_margin, img_margin, img_margin);

        // 读取图片
        MatVector imgVector = new MatVector();
        org.bytedeco.opencv.opencv_core.Mat mat;
        for (int i = 0; i < imgList.size(); i ++) {
            mat = imread(imgList.get(i));
            imgVector.push_back(mat);
        }

        // 合并
        Stitcher stitcher = Stitcher.create();
        org.bytedeco.opencv.opencv_core.Mat combined = new org.bytedeco.opencv.opencv_core.Mat();
        int result = stitcher.stitch(imgVector, combined);// 合并
        MainActivity.infoLog(combined.arrayWidth() + " " + combined.arrayHeight());

        // 显示合并的图片 TODO
        if (result == 0) {// 如果成功
            // 颜色转换
            Mat matBGR = new Mat(combined.address());
            Mat matRGB = new Mat();
            Imgproc.cvtColor(matBGR, matRGB, Imgproc.COLOR_BGR2RGB);// 将opencv默认的BGR转成RGB
            ImageView imageView = new ImageView(getContext());// TODO 合并之后的图片显示的位置
            imageView.setLayoutParams(imgParam);
            combinedImg = Bitmap.createBitmap(combined.arrayWidth(), combined.arrayHeight(), Bitmap.Config.RGB_565);
            Utils.matToBitmap(matRGB, combinedImg);
            imageView.setImageBitmap(combinedImg);

            // `保存`功能
            saveImg.combinedImg = combinedImg;
            imageView.setOnClickListener(new View.OnClickListener() {// 保存图片
                @Override
                public void onClick(View v) {// TODO 点击保存
                    saveImg.show(fragmentManager, "save");
                }
            });

            LinearLayout imageFrame = new LinearLayout(getContext());
            imageFrame.setLayoutParams(frameParam);
            imageFrame.addView(imageView);
            imgLayout.addView(imageFrame);
        } else {
            MainActivity.infoToast(getContext(), "failed");
        }
    }

}
