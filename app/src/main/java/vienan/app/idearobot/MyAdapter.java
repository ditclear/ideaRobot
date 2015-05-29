package vienan.app.idearobot;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by lenovo on 2015/5/24.
 */
public class MyAdapter extends BaseAdapter {
    private List<ListData> list;
    private Context context;
    private RelativeLayout layout;

    public MyAdapter(List<ListData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater=LayoutInflater.from(context);
        if(list.get(position).getFlag()==ListData.RECEIVER){
            layout= (RelativeLayout) inflater.inflate(R.layout.left,null);
        }else if(list.get(position).getFlag()==ListData.SEND){
            layout= (RelativeLayout) inflater.inflate(R.layout.right,null);
        }
        TextView textView= (TextView) layout.findViewById(R.id.tv);
        TextView time= (TextView) layout.findViewById(R.id.time);
        textView.setText(list.get(position).getContent());
        time.setText(list.get(position).getTime());
        return layout;
    }
}
