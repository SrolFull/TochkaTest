package com.example.myapplication;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.network.NetworkService;
import com.example.myapplication.network.Repository;
import com.example.myapplication.network.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment {
    private User user = UserDB.getInstance().getLastUser();
    private List<String> repList = new ArrayList<>();
    private ProfileRepositoryAdapter repAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        ImageView imageView = rootView.findViewById(R.id.profileImg);
        TextView textView = rootView.findViewById(R.id.user_username);
        SetUserImageAndName(imageView, textView);
        repAdapter = new ProfileRepositoryAdapter(repList, getContext());
        ListView repListView = rootView.findViewById(R.id.repository_ListV);
        repListView.setAdapter(repAdapter);
        getUserRepositories();
        return rootView;
    }

    private void SetUserImageAndName(ImageView imageView, TextView textView) {
        if (user != null) {
            Picasso.with(getContext())
                    .load(user.getAvatarUrl())
                    .placeholder(R.drawable.no_avatar_img)
                    .error(R.drawable.no_avatar_img)
                    .into(imageView);
            textView.setText(user.getLogin());
        }
    }

    private void getUserRepositories() {
        if (user != null)
            NetworkService.getInstance().jsonGitApi().getRepository(user.getLogin()).enqueue(new Callback<List<Repository>>() {

                @Override
                public void onResponse(@NonNull Call<List<Repository>> call, @NonNull Response<List<Repository>> response) {
                    if (!response.isSuccessful()) Log.d("Profile repsonse", "Respsone is wrong");
                    else {
                        for (Repository rep : response.body()) {
                            Log.i("Repository: ", rep.getName());
                            repList.add(rep.getName());
                            repAdapter.notifyDataSetChanged();
                        }
                        Log.d("Profile Network", "Response successful");
                    }
                }

                @Override
                public void onFailure(@NonNull Call<List<Repository>> call, @NonNull Throwable t) {
                    Log.d("Profile Network", "Failed connection");
                }
            });
    }
}
