package vienan.app.idearobot;

import android.os.AsyncTask;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by lenovo on 2015/5/24.
 */
public class HttpData extends AsyncTask<String,Void,String> {

    private HttpClient mHttpClient;
    private HttpGet mHttpGet;
    private HttpResponse mHttpResponse;
    private HttpEntity mHttpEntity;
    private InputStream in;
    private String url;
    private HttpGetDataListener listener;
    public HttpData(String url,HttpGetDataListener listener) {
        this.url = url;
        this.listener = listener;
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            mHttpClient=new DefaultHttpClient();//实例化客户端
            mHttpGet=new HttpGet(url);
            mHttpResponse=mHttpClient.execute(mHttpGet);
            mHttpEntity=mHttpResponse.getEntity();//通过Response对象来获取数据
            in=mHttpEntity.getContent();
            BufferedReader br=new BufferedReader(new InputStreamReader(in));
            String line = null;
            StringBuffer sb = new StringBuffer();//获得所有的数据
            while ((line = br.readLine()) != null) {//读取数据
                sb.append(line);
            }
            return sb.toString();
        }catch (Exception e){
        }
        return null;
    }

    @Override
    protected void onPostExecute(String result) {
        listener.getDataUrl(result);
        super.onPostExecute(result);
    }
}
