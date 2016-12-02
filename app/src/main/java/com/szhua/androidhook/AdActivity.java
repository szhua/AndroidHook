package com.szhua.androidhook;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * 模拟插件广告Activity
 */
public class AdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);

        ImageView cancleBt = (ImageView) findViewById(R.id.cancle);

        cancleBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }

    /**
     * 设置不可返回；
     */
    @Override
    public void onBackPressed() {

    }
}
