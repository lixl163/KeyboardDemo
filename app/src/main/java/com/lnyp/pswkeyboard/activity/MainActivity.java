package com.lnyp.pswkeyboard.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.lnyp.pswkeyboard.R;

/**
 * 主界面
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void toNormalKeyBoard(View view) {
        startActivity(new Intent(this, NormalKeyBoardActivity.class));
    }

    public void toPayKeyBoard(View view) {
        startActivity(new Intent(this, PaymentKeyBoardActivity.class));
    }
}
