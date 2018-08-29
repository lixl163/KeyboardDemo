package com.lnyp.pswkeyboard.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputType;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;

import com.lnyp.pswkeyboard.R;
import com.lnyp.pswkeyboard.widget.VirtualKeyboardView;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map;

/**
 * 一般键盘：自定义键盘(纯数字)，不弹出系统键盘
 */
public class NormalKeyBoardActivity extends AppCompatActivity {

    private EditText textAmount;
    private VirtualKeyboardView virtualKeyboardView;
    private GridView gridView;

    private ArrayList<Map<String, String>> valueList;

    private Animation enterAnim;
    private Animation exitAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_normal_key_board);

        initAnim();

        initView();

        valueList = virtualKeyboardView.getValueList();
    }

    /**
     * 数字键盘显示动画
     */
    private void initAnim() {
        enterAnim = AnimationUtils.loadAnimation(this, R.anim.push_bottom_in);
        exitAnim = AnimationUtils.loadAnimation(this, R.anim.push_bottom_out);
    }

    /**
     * 初始化控件
     */
    private void initView() {

        textAmount = (EditText) findViewById(R.id.textAmount);
        if (android.os.Build.VERSION.SDK_INT <= 10) {  // 设置不调用系统键盘
            textAmount.setInputType(InputType.TYPE_NULL);
        } else {
            this.getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(textAmount, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        virtualKeyboardView = (VirtualKeyboardView) findViewById(R.id.virtualKeyboardView);
        virtualKeyboardView.getLayoutBack().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                virtualKeyboardView.startAnimation(exitAnim);
                virtualKeyboardView.setVisibility(View.GONE);
            }
        });

        gridView = virtualKeyboardView.getGridView();
        gridView.setOnItemClickListener(onItemClickListener);

        textAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                virtualKeyboardView.setFocusable(true);
                virtualKeyboardView.setFocusableInTouchMode(true);

                virtualKeyboardView.startAnimation(enterAnim);
                virtualKeyboardView.setVisibility(View.VISIBLE);
            }
        });

    }

    private AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        //position从0开始
        if (position < 11 && position != 9) {    //点击0~9按钮

            String amount = textAmount.getText().toString().trim();
            amount = amount + valueList.get(position).get("name");

            textAmount.setText(amount);

            Editable ea = textAmount.getText();
            textAmount.setSelection(ea.length());
        } else {

            if (position == 9) {      //点击"."按钮
                String amount = textAmount.getText().toString().trim();
                if (!amount.contains(".")) {  //小数点只能允许有一个
                    amount = amount + valueList.get(position).get("name");
                    textAmount.setText(amount);

                    Editable ea = textAmount.getText();
                    textAmount.setSelection(ea.length());
                }
            }

            if (position == 11) {      //点击退格键
                String amount = textAmount.getText().toString().trim();
                if (amount.length() > 0) {  //防止出现数组下标越界
                    amount = amount.substring(0, amount.length() - 1);
                    textAmount.setText(amount);

                    Editable ea = textAmount.getText();
                    textAmount.setSelection(ea.length());
                }
            }
        }
        }
    };
}
