package vienan.app.idearobot;

import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.capricorn.ArcMenu;

import org.json.JSONException;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class MainActivity extends ActionBarActivity implements RippleView.OnRippleCompleteListener,
       HttpGetDataListener,OnClickListener {
    private ListView lv;
    private List<ListData> lists;
    private EditText sendText;
    private RippleView rippleView;
    private String content_str;
    private MyAdapter adapter;
    private String[] welcome_array;
    private double currentTime=0, oldTime = 0;
    private HttpData httpData;//声明异步请求对象
    private static final int[] ITEM_DRAWABLES = { R.drawable.composer_camera, R.drawable.composer_music,
            R.drawable.composer_place, R.drawable.composer_sleep, R.drawable.composer_thought, R.drawable.composer_with };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        lv= (ListView) findViewById(R.id.lv);
        LayoutAnimationController lac = new LayoutAnimationController(
                AnimationUtils.loadAnimation(this, R.anim.zoom_in));
        lv.setLayoutAnimation(lac);
        lv.startLayoutAnimation();
        sendText = (EditText) findViewById(R.id.sendText);
        rippleView= (RippleView) findViewById(R.id.more2 );
        lists = new ArrayList<ListData>();
        rippleView.setOnRippleCompleteListener(this);
        adapter = new MyAdapter(lists, this);
        lv.setAdapter(adapter);
        ListData listData;
        listData = new ListData(getRandomWelcomeTips(), ListData.RECEIVER,
                getTime());
        lists.add(listData);
    }

    private String getRandomWelcomeTips() {
        String welcome_tip = null;
        welcome_array = this.getResources()
                .getStringArray(R.array.welcome_tips);
        int index = (int) (Math.random() * (welcome_array.length - 1));
        welcome_tip = welcome_array[index];
        return welcome_tip;
    }


    @Override
    public void getDataUrl(String data) {
        // System.out.println(data);
        parseText(data);
    }

    public void parseText(String str) {
        try {
            Log.i("str",str);
            JSONObject jb = new JSONObject(str);
            // System.out.println(jb.getString("code"));
            // System.out.println(jb.getString("text"));
            ListData listData;
            listData = new ListData(jb.getString("text"), ListData.RECEIVER,
                    getTime());
            lists.add(listData);
            adapter.notifyDataSetChanged();
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    private String getTime() {
        currentTime = System.currentTimeMillis();
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date();
        String str = format.format(curDate);
        if (currentTime - oldTime >= 500) {
            oldTime = currentTime;
            return str;
        } else {
            return "";
        }

    }

    @Override
    public void onComplete(RippleView rippleView) {
        getTime();
        content_str = sendText.getText().toString();
        sendText.setText("");
        String drop_k = content_str.replace(" ", "");
        String drop_h = drop_k.replace("\n", "");
        ListData listData;
        listData = new ListData(content_str, ListData.SEND, getTime());
        lists.add(listData);
        if (lists.size() > 30) {
            for (int i = 0; i < lists.size(); i++) {
                lists.remove(i);
            }
        }
        adapter.notifyDataSetChanged();
        httpData = (HttpData) new HttpData(
                "http://www.tuling123.com/openapi/api?key=e2109786d8d4593345fb3b75e65089c0&info="
                        + drop_h, this).execute();
    }

    @Override
    public void onClick(View v) {

    }
}
