package vienan.app.idearobot;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import vienan.app.idearobot.materialMenu.Material;
import com.capricorn.ArcMenu;
import com.capricorn.RayLayout;
import com.capricorn.RayMenu;


public class ToDoActivity extends ActionBarActivity {
    private static final int[] ITEM_DRAWABLES = { R.drawable.composer_camera, R.drawable.composer_music,
            R.drawable.composer_place, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.composer_with };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        initView();
    }

    private void initView() {
        RayMenu menu = (RayMenu) findViewById(R.id.arc_menu);
        initArcMenu(menu,ITEM_DRAWABLES);

    }
    private void initArcMenu(RayMenu menu, int[] itemDrawables) {
        final int itemCount = itemDrawables.length;
        for (int i = 0; i < itemCount; i++) {
            ImageView item = new ImageView(this);
            item.setImageResource(itemDrawables[i]);

            final int position = i;
            if(i==0) {
                menu.addItem(item, new ArcMenu.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                startActivity(new Intent(ToDoActivity.this, AsyncTaskTest.class));
                            }
                        }, 800);
                    }
                });
            }else if(i==1){
                menu.addItem(item, new ArcMenu.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                startActivity(new Intent(ToDoActivity.this,Material.class));
                            }
                        }, 800);
                    }
                });
            }else {
                menu.addItem(item, new ArcMenu.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        new Handler().postDelayed(new Runnable() {
                            public void run() {
                                Intent intent = new Intent();
                                intent.setClass(ToDoActivity.this, MainActivity.class);
                                ToDoActivity.this.startActivity(intent);
                                ToDoActivity.this.finish();
                            }
                        }, 800);
                    }
                });// Add a menu item
            }
        }
    }


}
