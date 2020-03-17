package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.network.User;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserListRecyclerViewAdapter extends RecyclerView.Adapter {
    private OnUserListener mOnUserListener;
    private List<User> userList;
    private Context context;

    public UserListRecyclerViewAdapter(List<User> userList, Context context, OnUserListener onUserListener) {
        this.userList = userList;
        this.context = context;
        mOnUserListener = onUserListener;
    }

    public void addtUsersList(List<User> userList) {
        this.userList.addAll(userList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user, parent, false);
        return new ListViewHolder(view, mOnUserListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, final int position) {
        ((ListViewHolder) holder).bindView(position);
    }

    @Override
    public int getItemCount() {
        return (userList == null) ? 0 : userList.size();
    }

    public interface OnUserListener {
        void OnUserClick(int position);
    }

    private class ListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        OnUserListener onUserListener;
        private TextView userViewName;
        private ImageView userViewImg;

        public ListViewHolder(View itemView, OnUserListener onUserListener) {
            super(itemView);
            userViewName = itemView.findViewById(R.id.item_user_name);
            userViewImg = itemView.findViewById(R.id.item_user_image);
            this.onUserListener = onUserListener;
            itemView.setOnClickListener(this);
        }

        public void bindView(int position) {
            userViewName.setText(userList.get(position).getLogin());
            Picasso.with(context)
                    .load(userList.get(position).getAvatarUrl())
                    .placeholder(R.drawable.no_avatar_img)
                    .error(R.drawable.no_avatar_img)
                    .into(userViewImg);
        }

        @Override
        public void onClick(View view) {
            onUserListener.OnUserClick(getAdapterPosition());
        }
    }

}
