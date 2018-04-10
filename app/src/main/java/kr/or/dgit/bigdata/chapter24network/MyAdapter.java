package kr.or.dgit.bigdata.chapter24network;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by DGIT3-12 on 2018-04-10.
 */

public class MyAdapter extends BaseAdapter {
    Context context;
    int layout; //R.layout.itemrow
    List<Item> itemList;
    LayoutInflater inflater;

    public MyAdapter(Context context, int layout, List<Item> itemList) {
        this.context = context;
        this.layout = layout;
        this.itemList = itemList;
        this.inflater = LayoutInflater.from(context);//전개자 구함.
    }

    @Override
    public int getCount() {
        return itemList.size();
    }

    @Override
    public Object getItem(int position) {
        return itemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        TextView itemName;
        TextView makerName;
        TextView itemPrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View itemLayout = convertView;
        ViewHolder viewHoler = null;

        if (itemLayout == null){
            itemLayout = inflater.inflate(layout, parent, false);
            viewHoler = new ViewHolder();
            viewHoler.itemName = itemLayout.findViewById(R.id.itemName);
            viewHoler.makerName = itemLayout.findViewById(R.id.makerName);
            viewHoler.itemPrice = itemLayout.findViewById(R.id.itemPrice);
            itemLayout.setTag(viewHoler);
        }else{
            viewHoler = (ViewHolder) itemLayout.getTag();
        }
        Item item = itemList.get(position);
        viewHoler.itemName.setText(item.getItemName());
        viewHoler.makerName.setText(item.getMakerName());
        viewHoler.itemPrice.setText(item.getItemPrice()+"");
        return itemLayout;
    }
}
