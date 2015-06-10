package vienan.app.idearobot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class AsyncTaskTest extends ActionBarActivity {
    private ListView mListView;
    private ProgressBar progress;
    MyAsyncTask myAsyncTask;
    private MediaPlayer myMediaPlayer;
    static String URL = "http://www.imooc.com/api/teacher?type=4&num=30";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.async_task);
        initView();
        myMediaPlayer=MediaPlayer.create(this,R.raw.freeloop);
        myMediaPlayer.setLooping(true);
        myMediaPlayer.start();

    }

    private void initView() {
        mListView = (ListView) findViewById(R.id.lv_async );
        progress = (ProgressBar) findViewById(R.id.progress);
        myMediaPlayer=MediaPlayer.create(this,R.raw.freeloop);
        myAsyncTask=new MyAsyncTask();
        myAsyncTask.execute(URL);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        myMediaPlayer.stop();
        myMediaPlayer.reset();
        myMediaPlayer.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        myMediaPlayer.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(myAsyncTask!=null&&myAsyncTask.getStatus()==AsyncTask.Status.RUNNING){
            //cancel方法只是将对应的AsyncTask状态标记为cancel，并不是真正的取消线程的执行。
            myAsyncTask.cancel(true);

        }
    }
    private List<NewsBean> getJsonNews(String url) {
        List<NewsBean> news=new ArrayList<NewsBean>();
        try {
            String jsonData=readStream(new URL(url).openStream());
            JSONObject jsonObject;
            NewsBean newsBean;
            jsonObject=new JSONObject(jsonData);
            JSONArray jsonArray=jsonObject.optJSONArray("data");
            for (int i = 0; i < jsonArray.length(); i++) {
                jsonObject=jsonArray.getJSONObject(i);
                newsBean=new NewsBean();
                newsBean.newsIconUrl=jsonObject.getString("picSmall");
                newsBean.newsTitle=jsonObject.getString("name");
                newsBean.newsContent=jsonObject.getString("description");
                news.add(newsBean);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return news;
    }
    private String readStream(InputStream is){
        InputStreamReader isr;
        String result="";
        try {
            String line="";
            isr=new InputStreamReader(is,"utf-8");
            BufferedReader br=new BufferedReader(isr);
            while((line=br.readLine())!=null){
                result+=line;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }
    class MyAsyncTask extends AsyncTask<String,Integer,List<NewsBean>>{



        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<NewsBean> doInBackground(String... params) {
            return getJsonNews(params[0]);
        }


        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if(isCancelled())
                return;
            progress.setProgress(values[0]);
        }

        @Override
        protected void onPostExecute(List<NewsBean> newsBeanList) {
            super.onPostExecute(newsBeanList);
            AsyncAdapter asyncAdapter=new AsyncAdapter(newsBeanList,AsyncTaskTest.this,mListView);
            mListView.setAdapter(asyncAdapter);
            LayoutAnimationController lac=new LayoutAnimationController(AnimationUtils.loadAnimation(AsyncTaskTest.this, R.anim.zoom_in));
            lac.setOrder(LayoutAnimationController.ORDER_NORMAL);
            mListView.setLayoutAnimation(lac);
            mListView.startLayoutAnimation();
            progress.setVisibility(View.GONE);
        }
    }

}
