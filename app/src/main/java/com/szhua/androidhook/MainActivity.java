package com.szhua.androidhook;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

/**
 * 暂时保留这个Activity;可以去掉 ；（for test ） ;
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TextView he = (TextView) findViewById(R.id.helloworld);
       he.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent =new Intent(MainActivity.this,AdActivity.class) ;
               startActivity(intent);
           }
       });
    }
}
