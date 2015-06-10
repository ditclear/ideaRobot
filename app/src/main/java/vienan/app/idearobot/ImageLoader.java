package vienan.app.idearobot;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by lenovo on 2015/6/8.
 */
public class ImageLoader {
    private ImageView mImageView;
    private String IconUrl;
    private LruCache<String,Bitmap> mCaches;
    private ListView mListView;
    private Set<NewsAsyncTask> mTask;
    public ImageLoader(ListView mListView){
        this.mListView=mListView;
        mTask=new HashSet<>();
        //获取最大可用内存
        int maxMemory= (int) Runtime.getRuntime().maxMemory();
        int cacheSize=maxMemory/4;
        mCaches=new LruCache<String,Bitmap>(cacheSize){
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //在每次存入缓存的时候调用
                return value.getByteCount();
            }
        };
    }
    public void addBitmapToCache(String url,Bitmap bitmap){
        if(getBitmapFromCache(url)==null)
            mCaches.put(url,bitmap);
    }
    public Bitmap getBitmapFromCache(String url){
        return mCaches.get(url);
    }
    private Handler mHandler=new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(mImageView.getTag().equals(IconUrl))
                mImageView.setImageBitmap((Bitmap) msg.obj);
        }
    };
    //加载可见的图片
    public void loadImages(int start ,int end){
        for (int i = start ; i < end; i++) {
            String imageUrl=AsyncAdapter.URLS[i];
            Bitmap bitmap=getBitmapFromCache(imageUrl);
            if(bitmap==null) {
               NewsAsyncTask task=new NewsAsyncTask(imageUrl);
                task.execute(imageUrl);
                mTask.add(task);
            }else {
                ImageView imageView= (ImageView) mListView.findViewWithTag(imageUrl);
                imageView.setImageBitmap(bitmap);
            }
        }
    }
    //采用线程的方式加载图片
    public void showImageByThread(ImageView imageView, final String imageUrl){
        mImageView=imageView;
        IconUrl=imageUrl;
        new Thread(){
            @Override
            public void run() {
                super.run();
                Bitmap bitmap=getBitmapFromUrl(imageUrl);
                Message message=Message.obtain();
                message.obj=bitmap;
                mHandler.sendMessage(message);
            }
        }.start();
    }
    private Bitmap getBitmapFromUrl(String imageUrl){
        Bitmap bitmap;
        InputStream is=null;
        try {
            URL url=new URL(imageUrl);
            HttpURLConnection connection= (HttpURLConnection) url.openConnection();
            is=new BufferedInputStream(connection.getInputStream());
            bitmap= BitmapFactory.decodeStream(is);
            connection.disconnect();
            return bitmap;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 采用异步任务的方式加载图片
     * @param imageView
     * @param imageUrl
     */
    public void showBitmapByTask(ImageView imageView, final String imageUrl){
        //从缓存中获取图片
        Bitmap bitmap=getBitmapFromCache(imageUrl);
        if(bitmap==null) {
            imageView.setImageResource(R.drawable.ic_launcher);
        }else {
            imageView.setImageBitmap(bitmap);
        }
    }

    public void cancelAllTasks() {
        if(mTask!=null){
            for(NewsAsyncTask task:mTask){
                task.cancel(false);
            }
        }
    }

    private class NewsAsyncTask extends AsyncTask<String, Void, Bitmap> {
//        private ImageView imageView;
        private String url;
        public NewsAsyncTask(String url){
//            this.imageView=imageView;
            this.url=url;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            String mUrl=params[0];
            //获取图片
            Bitmap bitmap=getBitmapFromUrl(mUrl);
            //将没有的图片加入缓存
            if(bitmap!=null){
                addBitmapToCache(mUrl,bitmap);
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            ImageView imageView= (ImageView) mListView.findViewWithTag(url);
            if(imageView!=null&&bitmap!=null)
                imageView.setImageBitmap(bitmap);
            mTask.remove(this);
        }


    }
}
