package com.example.javacv;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.javacv.interfaces.NormalManager;

import java.io.File;

public class SelectImg extends NormalManager {
    public int box_width = 60;
    public int icon_height = 90;
    public int box_top = 35;
    public int box_right = 20;
    public int name_top = 10;
    public int name_right = 80;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.img_select, container);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(0x00000000));// 背景透明

        initPath();
        initButton();

        // 调用文件管理器
        readPath(MainActivity.appPath);// TODO 路径记忆
        return myView;
    }

    public void initButton() {
        // TODO
    }

    public LinearLayout createItem(int itemType, final String itemName, final String itemPath) {
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

        View icon = new View(getContext());// 图标
        icon.setLayoutParams(iconParam);
        if (itemType == 0) {// 文件
            icon.setBackgroundResource(R.drawable.item_file);
        } else {// 文件夹
            icon.setBackgroundResource(R.drawable.item_dir);
        }

        RelativeLayout detail = new RelativeLayout(getContext());
        detail.setLayoutParams(detailParam);

        CheckBox checkBox = new CheckBox(getContext());
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
        detail.addView(checkBox);
        item.addView(detail);

        // 设置靠父元素左/右
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) name.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        name.setLayoutParams(params);
        params = (RelativeLayout.LayoutParams) checkBox.getLayoutParams();
        params.addRule(RelativeLayout.ALIGN_RIGHT);// 单选框靠右
        checkBox.setLayoutParams(params);

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
        } else {// 获取手势库
            item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO
                }
            });
        }

        layout.addView(item);

        return item;
    }

}
