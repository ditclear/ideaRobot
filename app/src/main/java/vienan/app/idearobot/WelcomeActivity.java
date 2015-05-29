package vienan.app.idearobot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;

public class WelcomeActivity extends Activity{


    @Override
    public void onCreate(Bundle savedInstanceState) {
        //去掉标题栏全屏显示
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.welcome);
        new Handler().postDelayed(new Runnable(){

            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent();
                intent.setClass(WelcomeActivity.this, ToDoActivity.class);
                WelcomeActivity.this.startActivity(intent);
                WelcomeActivity.this.finish();

            }

        }, 2000);

    };
}