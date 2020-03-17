package com.example.myapplication;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.network.NetworkService;
import com.example.myapplication.network.User;
import com.example.myapplication.network.UserList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UsersFragment extends Fragment implements UserListRecyclerViewAdapter.OnUserListener {
    private UserDB userDb = UserDB.getInstance();
    private List<User> userList = userDb.getUserList();
    private RecyclerView.LayoutManager layoutManager;
    private UserListRecyclerViewAdapter recyclerViewAdapter;
    private Boolean isScrolling = false;
    private ProgressBar progressBar;
    private int curPage = 1;
    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                isScrolling = true;
            }
        }

        @Override
        public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            int currentItems = layoutManager.getChildCount();
            int totalItems = layoutManager.getItemCount();
            int scrollOutItems = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();
            Log.d("State", "crItems: " + currentItems +
                    "| scrOutItems: " + scrollOutItems +
                    "| totalItems: " + totalItems
            );
            if (isScrolling && (currentItems + scrollOutItems + 12 >= totalItems)) {
                //fetch data
                curPage++;
                isScrolling = false;
                fetchData();
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_users, container, false);
        Context context = rootView.getContext();
        progressBar = rootView.findViewById(R.id.progress_bar);
        layoutManager = new LinearLayoutManager(context);
        recyclerViewAdapter = new UserListRecyclerViewAdapter(userList, context, this);
        RecyclerView recyclerView = rootView.findViewById(R.id.recyclerView_item);
        recyclerView.setAdapter(recyclerViewAdapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnScrollListener(scrollListener);
        return rootView;
    }

    private void fetchData() {
        progressBar.setVisibility(View.VISIBLE);
        Log.d("Network Counter", "CurPage: " + curPage);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                NetworkService.getInstance().jsonGitApi().getUsers(UserDB.getInstance().getCurrentName(), curPage, 12).enqueue(new Callback<UserList>() {
                    @Override
                    public void onResponse(@NonNull Call<UserList> call, @NonNull Response<UserList> response) {
                        if (!response.isSuccessful()) Log.d("Users Network", "Update not response");
                        else {
                            assert response.body() != null;
                            List<User> lst = response.body().getItems();
                            recyclerViewAdapter.addtUsersList(lst);
                            recyclerViewAdapter.notifyDataSetChanged();
                            Log.d("Users Network", "Successful update");
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UserList> call, @NonNull Throwable t) {
                        Log.d("Users Network", "Failed update");
                    }
                });
                progressBar.setVisibility(View.GONE);
            }
        }, 5000);
    }

    @Override
    public void OnUserClick(int position) {
        userDb.setLastUser(userList.get(position));
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, new ProfileFragment()).commit();
    }
}
