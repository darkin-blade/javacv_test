package com.example.javacv;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.javacv.interfaces.NormalManager;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static org.bytedeco.opencv.global.opencv_imgcodecs.imread;

public class SaveImg extends NormalManager {
    public String lastPath = null;// 路径记忆
    public String imgPath;

    public EditText imgName;
    public Button save;// 确定
    public Button back;// 返回
    public Bitmap combinedImg;// 合并后的图片

    public int box_width = 60;
    public int icon_height = 90;
    public int box_top = 35;
    public int box_right = 10;
    public int name_top = 10;
    public int name_right = 80;

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
        return myView;
    }

    public void initData() {
        MainActivity.window_num = MainActivity.SAVE_IMG;
        imgPath = null;
        imgName = myView.findViewById(R.id.img_name);
    }

    public void initButton() {
        back = myView.findViewById(R.id.button_1);
        save = myView.findViewById(R.id.button_2);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {// TODO 返回所有选中的图片路径
                imgPath = curPath.getText().toString() + "/" + imgName.getText().toString();// 图片保存的路径

                // 图片名不能为空
                if (imgName.getText().toString().length() == 0) {
                    MainActivity.infoToast(getContext(), "image name can't be empty");
                    return;
                }

                // 图片保存路径必须有效
                if (curPath.getText().toString().length() == 0) {
                    MainActivity.infoToast(getContext(), "invalid path");
                    return;
                }

                // 不能重名
                File file = new File(imgPath);
                if (file.exists()) {// 有重名
                    MainActivity.infoToast(getContext(), imgName + " already exists");
                    return;
                }

                try {
                    FileOutputStream fileOutputStream = new FileOutputStream(file);
                    if (combinedImg != null) {// TODO 有合法的结果
                        combinedImg.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
                        fileOutputStream.flush();// TODO
                        fileOutputStream.close();
                        MainActivity.infoToast(getContext(), "saved as " + imgPath);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }

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
                    org.bytedeco.opencv.opencv_core.Mat mat = imread(dirPath + "/" + items[i].getName());
                    if (mat.arrayWidth() != 0 && mat.arrayHeight() != 0) {// TODO 测试是否为图片文件
                        createItem(3, items[i].getName(), dirPath);
                    } else {
                        createItem(0, items[i].getName(), dirPath);
                    }
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
        } else if (itemType == 3) {// TODO 显示图片缩略图
            Bitmap bitmap = BitmapFactory.decodeFile(itemPath + "/" + itemName);
            int width = 60;
            int height = bitmap.getHeight() * 60 / bitmap.getWidth();
            Bitmap thumbnail = Bitmap.createScaledBitmap(bitmap, width, height, true);// TODO 缩略图
            icon.setImageBitmap(thumbnail);
        } else {// 文件夹
            icon.setBackgroundResource(R.drawable.item_dir);
        }

        RelativeLayout detail = new RelativeLayout(getContext());
        detail.setLayoutParams(detailParam);

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
        item.addView(detail);

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
        }

        layout.addView(item);

        return item;
    }

}
