package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class ProfileRepositoryAdapter extends BaseAdapter {
    private List<String> reposNameList;
    private Context context;

    public ProfileRepositoryAdapter(List<String> reposNameList, Context context) {
        this.reposNameList = reposNameList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return reposNameList.size();
    }

    @Override
    public Object getItem(int i) {
        return reposNameList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            LayoutInflater layoutInflater;
            layoutInflater = LayoutInflater.from(context);
            view = layoutInflater.inflate(R.layout.item_repository_name, null);
        }
        TextView textView = view.findViewById(R.id.repos_name);
        ImageView imageView = view.findViewById(R.id.folder_img);
        textView.setText(reposNameList.get(i));
        imageView.setImageResource(R.drawable.ic_folder_gray_24dp);
        return view;
    }
}
