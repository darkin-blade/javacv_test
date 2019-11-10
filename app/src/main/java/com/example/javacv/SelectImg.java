package com.example.javacv;

import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.javacv.interfaces.ManagerImg;
import com.example.javacv.interfaces.NormalManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class SelectImg extends NormalManager {
    public String lastPath = null;// 路径记忆

    public Button select;// 确定
    public Button back;// 返回

    public int box_width = 60;
    public int icon_height = 90;
    public int box_top = 35;
    public int box_right = 10;
    public int name_top = 10;
    public int name_right = 80;

    public ArrayList<String> imgList;
    public ArrayList<ImageView> imageViews;// 集中保存所有imageview及对应路径
    public ArrayList<String> imgPaths;

    public ManagerImg managerImg;// 图片处理

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.img_select, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));// 背景透明

        initPath();// 路径框
        initData();
        initButton();

        // 调用文件管理器
        if (lastPath == null) {
            lastPath = MainActivity.appPath;
        }
        readPath(lastPath);

        // 异步加载图片
//        class LoadImg extends Thread {
//           @Override
//           public void run() {
//               for (int i = 0; i < imageViews.size(); i ++) {// 逐个异步加载图片
//                   Bitmap bitmap = managerImg.LoadThumb(imgPaths.get(i), 30, 30);// TODO 大小
//                   ImageView imageView = imageViews.get(i);
//                   imageView.setImageBitmap(bitmap);
//               }
//           }
//        }
//        LoadImg loadImg = new LoadImg();
//        loadImg.start();

        class LoadImg extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                for (int i = 0; i < imageViews.size(); i ++) {// 逐个异步加载图片
                    Bitmap bitmap = managerImg.LoadThumb(imgPaths.get(i), 30, 30);// TODO 大小
                    ImageView imageView = imageViews.get(i);
                    imageView.setImageBitmap(bitmap);
                }
                return null;
            }
        }
        LoadImg loadImg = new LoadImg();
        loadImg.execute();

        return myView;
    }

    public void initData() {
        MainActivity.window_num = MainActivity.SELECT_IMG;
        imgList = new ArrayList<String>();
        imageViews = new ArrayList<ImageView>();
        imgPaths = new ArrayList<String>();
        managerImg = new ManagerImg(getContext());
    }

    public void initButton() {
        back = myView.findViewById(R.id.button_1);
        select = myView.findViewById(R.id.button_2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imgList.clear();// 清空
                imageViews.clear();
                dismiss();
            }
        });

        select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// TODO 返回所有选中的图片路径
                MainActivity.localRecognize.imgList.addAll(imgList);// TODO 合并
                imgList.clear();// 清空
                imageViews.clear();
                dismiss();
            }
        });
    }

    public void readPath(final String dirPath) {
        lastPath = dirPath;// TODO 路径记忆

        // 特判根目录
        if (dirPath == null) {
            MainActivity.infoToast(getContext(), "can't access this path");
            lastPath = MainActivity.appPath;// 重置路径
            dismiss();// 强制返回
            return;
        }

        // 清空并显示父目录
        LinearLayout layout = myView.findViewById(R.id.item_list);
        layout.removeAllViews();
        createItem(2, "..", dirPath);// 父目录

        // 遍历文件夹
        File dir = new File(dirPath);
        File[] items = dir.listFiles();
        if (items != null) {
            for (int i = 0; i < items.length; i++) {
                if (items[i].isDirectory()) {
                    createItem(1, items[i].getName(), dirPath);
                } else {// TODO 特判图片文件
                    createItem(0, items[i].getName(), dirPath);
                }
            }
        }

        // 显示路径
        curPath.setText(dirPath);// TODO 简化路径
    }

    public LinearLayout createItem(final int itemType, final String itemName, final String itemPath) {
        LinearLayout layout = myView.findViewById(R.id.item_list);
        LinearLayout.LayoutParams itemParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, item_height);
        LinearLayout.LayoutParams typeParam = new LinearLayout.LayoutParams(icon_height, icon_height);
        LinearLayout.LayoutParams iconParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams detailParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams boxParam = new LinearLayout.LayoutParams(box_width, box_width);
        LinearLayout.LayoutParams nameParam = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);

        final LinearLayout item = new LinearLayout(getContext());// TODO 参数
        item.setLayoutParams(itemParam);
        item.setBackgroundResource(R.color.grey);

        LinearLayout type = new LinearLayout(getContext());// 图标的外圈
        typeParam.setMargins(type_padding, type_padding, type_padding, type_padding);
        type.setLayoutParams(typeParam);

        ImageView icon = new ImageView(getContext());// 图标
        icon.setLayoutParams(iconParam);
        if (itemType == 0) {// 文件
            icon.setBackgroundResource(R.drawable.item_file);

            // TODO 记录所有需要加载的文件
            imageViews.add(icon);
            imgPaths.add(itemPath + "/" + itemName);
        } else {// 文件夹
            icon.setBackgroundResource(R.drawable.item_dir);
        }

        RelativeLayout detail = new RelativeLayout(getContext());
        detail.setLayoutParams(detailParam);

        final CheckBox checkBox = new CheckBox(getContext());
        boxParam.setMargins(box_right, box_top, box_right, box_top);
        checkBox.setLayoutParams(boxParam);
        checkBox.setButtonDrawable(R.drawable.checkbox_library);

        TextView name = new TextView(getContext());// 文件名
        nameParam.setMargins(0, name_top, name_right, name_top);
        name.setLayoutParams(nameParam);
        name.setBackgroundResource(R.color.grey);// TODO
        name.setText(itemName);
        name.setPadding(name_padding, name_padding, name_padding, name_padding);
        name.setSingleLine();

        type.addView(icon);
        item.addView(type);
        detail.addView(name);
        if (itemType == 0) {// TODO 只有图片才有复选框
            detail.addView(checkBox);
        }
        item.addView(detail);

        // 设置靠父元素左/右
        if (itemType == 0) {// TODO 只有图片才有复选框
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) checkBox.getLayoutParams();
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);// 单选框靠右
            checkBox.setLayoutParams(params);
        }

        if (itemType == 2) {// 父文件夹
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File dir = new File(itemPath);
                    readPath(dir.getParent());
                }
            });
        } else if (itemType == 1) {// `点击`遍历子文件夹
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    readPath(itemPath + "/" + itemName);
                }
            });
        } else if (itemType == 0) {// TODO 图片的复选功能
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()) {// 直接调用checkbox的监听,不需重复操作
                        item.setBackgroundResource(R.color.grey);
                        checkBox.setChecked(false);
                    } else {
                        item.setBackgroundResource(R.color.grey_light);
                        checkBox.setChecked(true);
                    }
                }
            });
        }

        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    item.setBackgroundResource(R.color.grey_light);
                    imgList.add(itemPath + "/" + itemName);// TODO 添加到list
                    MainActivity.infoLog("size: " + imgList.size());
                } else {
                    item.setBackgroundResource(R.color.grey);
                    boolean result = imgList.remove(itemPath + "/" + itemName);// TODO 从list移出
                    MainActivity.infoLog("size: " + imgList.size() + ", " + result);
                }
            }
        });

        layout.addView(item);

        return item;
    }

}
