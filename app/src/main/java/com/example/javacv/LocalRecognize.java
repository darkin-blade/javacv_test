package com.example.javacv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;

import org.bytedeco.opencv.opencv_core.MatVector;
import org.bytedeco.opencv.opencv_core.Size;
import org.bytedeco.opencv.opencv_stitching.ImageFeatures;
import org.bytedeco.opencv.opencv_stitching.Stitcher;
import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.features2d.Feature2D;
import org.opencv.features2d.ORB;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;

public class LocalRecognize extends DialogFragment {
    String appPath = MainActivity.appPath;// TODO

    public int img_height = 400;
    public int img_margin = 20;

    public SelectImg selectImg;// 选取图片
    public SaveImg saveImg;

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
                selectImg.show(fragmentManager, "save");
            }
        });

        btnWork.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 combineImg();
//                test2();// TODO
            }
        });

        btnDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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

    public void test2() {// TODO 还在测试
        class Test2 extends Thread {
            @Override
            public void run() {
                int img_num = 2;
                if (imgList.size() < img_num) {
                    return;// TODO 不能运行
                }

                // 存储所有照片
                org.bytedeco.opencv.opencv_core.Mat img;
                MatVector imgs = new MatVector();
                Feature2D finder = ORB.create();
                ImageFeatures[] features = new ImageFeatures[2];
                for (int i = 0; i < img_num; i ++) {
                    features[i] = new ImageFeatures();
                }

                for (int i = 0; i < img_num; i ++) {
                    // 读取原图片
                    org.bytedeco.opencv.opencv_core.Mat full_img = imread(imgList.get(i));

                    // 放缩图片, cols 是宽
                    int width = 480;
                    int height = full_img.arrayHeight() * 480 / full_img.arrayWidth();
                    Size size = new Size(width, height);
                    img = new org.bytedeco.opencv.opencv_core.Mat(size, full_img.type());// TODO CV_32S
                    imgs.push_back(img);// 保存图片到向量
                }

                // TODO 查找特征点

                org.bytedeco.opencv.opencv_core.Mat result = null;

                int status = 0;

                // 显示结果
                if (status == 0) {// 如果成功
                    showResult(result);// TODO 显示结果并提供`保存`功能
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.infoToast(getContext(), "failed");
                        }
                    });
                }
            }
        }
        Test2 test2 = new Test2();
        test2.start();
    }

    public void combineImg() {// 合并图片 TODO 以缩略图的方式显示
        class AsyncCombine extends Thread {
            @Override
            public void run() {// 读取图片
                MatVector imgVector = new MatVector();
                org.bytedeco.opencv.opencv_core.Mat mat;
                for (int i = 0; i < imgList.size(); i ++) {
                    mat = imread(imgList.get(i));
                    imgVector.push_back(mat);
                }

                // 合并
                Stitcher stitcher = Stitcher.create();
                org.bytedeco.opencv.opencv_core.Mat combined = new org.bytedeco.opencv.opencv_core.Mat();
                int status = stitcher.stitch(imgVector, combined);// 合并
                MainActivity.infoLog(combined.arrayWidth() + " " + combined.arrayHeight());

                // 显示合并的图片 TODO
                if (status == 0) {// 如果成功
                    showResult(combined);// TODO 显示结果并提供`保存`功能
                } else {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            MainActivity.infoToast(getContext(), "failed");
                        }
                    });
                }
            }
        }
        AsyncCombine asyncCombine = new AsyncCombine();
        asyncCombine.start();
    }

    public void showResult(org.bytedeco.opencv.opencv_core.Mat result) {
        final Bitmap resultImg = Bitmap.createBitmap(result.arrayWidth(), result.arrayHeight(), Bitmap.Config.RGB_565);// 存放图片结果

        // 颜色转换
        Mat matBGR = new Mat(result.address());// 强制转换mat
        Mat matRGB = new Mat();// 颜色正确的mat
        Imgproc.cvtColor(matBGR, matRGB, Imgproc.COLOR_BGR2RGB);// 将opencv默认的BGR转成RGB
        Utils.matToBitmap(matRGB, resultImg);

        // 修改ui TODO
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // 创建layout参数
                LinearLayout.LayoutParams frameParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, img_height);
                LinearLayout.LayoutParams imgParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                imgParam.setMargins(img_margin, img_margin, img_margin, img_margin);

                // 将图片结果绑定到imageview
                ImageView imageView = new ImageView(getContext());// 合并之后的图片显示的位置
                imageView.setLayoutParams(imgParam);
                imageView.setImageBitmap(resultImg);

                // `保存`功能
                saveImg.resultImg = resultImg;// 将子类的图片设置为合并结果
                imageView.setOnClickListener(new View.OnClickListener() {// 保存图片
                    @Override
                    public void onClick(View v) {// 点击保存
                        saveImg.show(fragmentManager, "save");
                    }
                });

                // 将imageview显示到ui
                LinearLayout imageFrame = new LinearLayout(getContext());
                imageFrame.setLayoutParams(frameParam);
                imageFrame.addView(imageView);
                imgLayout.addView(imageFrame);
            }
        });
    }

}
