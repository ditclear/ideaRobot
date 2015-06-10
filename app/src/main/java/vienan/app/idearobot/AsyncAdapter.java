package vienan.app.idearobot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

/**
 * Created by lenovo on 2015/6/8.
 */
public class AsyncAdapter extends BaseAdapter implements AbsListView.OnScrollListener{
    private List<NewsBean> mList;
    private LayoutInflater inflater;
    private ImageLoader imageLoader;
    private int mStart,mEnd;
    public static String[] URLS;
    private boolean isFirst;
    public AsyncAdapter(List<NewsBean> list, AsyncTaskTest context,ListView listView) {
        mList = list;
        inflater = LayoutInflater.from(context);
        imageLoader=new ImageLoader(listView);
        URLS=new String[list.size()];
        for (int i = 0; i < list.size(); i++) {
            URLS[i]=list.get(i).newsIconUrl;
        }
        isFirst=true;
        listView.setOnScrollListener(this);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder = null;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item_async, null);
            mViewHolder.mImageView = (ImageView) convertView.findViewById(R.id.iv_async);
            mViewHolder.tv_title = (TextView) convertView.findViewById(R.id.tv_async);
            mViewHolder.tv_content = (TextView) convertView.findViewById(R.id.tv_async_small);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
        String url=mList.get(position).newsIconUrl;
        mViewHolder.mImageView.setTag(url);
        //new ImageLoader().showImageByThread(mViewHolder.mImageView, url);
        imageLoader.showBitmapByTask(mViewHolder.mImageView, url);
        mViewHolder.tv_title.setText(mList.get(position).newsTitle);
        mViewHolder.tv_content.setText(mList.get(position).newsContent);
        return convertView;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if(scrollState==SCROLL_STATE_IDLE){
            imageLoader.loadImages(mStart,mEnd);
        }else {
            imageLoader.cancelAllTasks();
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        mStart=firstVisibleItem;
        mEnd=mStart+visibleItemCount;
        //首次预加载
        if(isFirst && visibleItemCount>0){
            imageLoader.loadImages(mStart,mEnd);
            isFirst=false;
        }
    }

    class ViewHolder {
        public ImageView mImageView;
        public TextView tv_title, tv_content;
    }
}
