package com.example.slidingpuzzle;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.List;

public class TileAdapter extends BaseAdapter {
    private Context context;
    private int layout;
    private List<Tile> tileList;

    public TileAdapter(Context context, int layout, List<Tile> tileList) {
        this.context = context;
        this.layout = layout;
        this.tileList = tileList;
    }

    @Override
    public int getCount() {
        return tileList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    private class ViewHolder{
        ImageView image;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder holder;
        if(view==null){
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(layout,null);
            holder.image = (ImageView) view.findViewById(R.id.imageViewTile);
            view.setTag(holder);
        }else {
            holder = (ViewHolder) view.getTag();
        }
        Tile tile = tileList.get(i);
        holder.image.setImageResource(tile.getCorrectPosition());
        return view;
    }
}
