package kr.or.dgit.bigdata.chapter24network;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by DGIT3-12 on 2018-04-10.
 */

public class MyAdapter extends BaseAdapter {
    Context context;
    int layout;
    ArrayList<Item> itemList;
    LayoutInflater inflater;

    public MyAdapter(Context context, int layout, ArrayList<Item> itemList) {
        this.context = context;
        this.layout = layout;
        this.itemList = itemList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int i) {
        return itemList.get(i).getItemName();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    class ViewHolder{
        TextView itemName;
        TextView makerName;
        TextView itemPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemLayout = convertView;
        ViewHolder viewHolder = null;
        if(itemLayout == null){
        itemLayout = inflater.inflate(layout, parent, false);
        viewHolder = new ViewHolder();
        viewHolder.itemName = (TextView) itemLayout.findViewById(R.id.itemName);
        viewHolder.makerName = (TextView) itemLayout.findViewById(R.id.makerName);
        viewHolder.itemPrice = (TextView) itemLayout.findViewById(R.id.itemPrice);
        itemLayout.setTag(viewHolder);
    }else{
        viewHolder = (ViewHolder) itemLayout.getTag();
    }

        viewHolder.itemName.setText(itemList.get(position).getItemName());
        viewHolder.makerName.setText(itemList.get(position).getMakerName());
        viewHolder.itemPrice.setText(itemList.get(position).getItemPrice());

        return itemLayout;
}
}
