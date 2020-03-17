package com.example.myapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.network.NetworkService;
import com.example.myapplication.network.User;
import com.example.myapplication.network.UserList;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchFragment extends Fragment {
    private EditText editTextUser;
    private Button buttonSearch;
    private String userNameInput;
    private TextWatcher userNameTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            userNameInput = editTextUser.getText().toString().trim();
            buttonSearch.setEnabled(!userNameInput.isEmpty());
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    };
    private OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(View view) {
            UserDB.getInstance().setCurrentName(userNameInput);
            NetworkService.getInstance().jsonGitApi().getUsers(userNameInput, 1, 12).enqueue(new Callback<UserList>() {
                @Override
                public void onResponse(@NonNull Call<UserList> call, @NonNull Response<UserList> response) {
                    if (!response.isSuccessful()) {
                        Log.d("Search Network", "failed response");
                    } else {
                        List<User> userList = response.body().getItems();
                        Log.d("Search Network", "Successful response");
                        UserDB.getInstance().setUserList(userList);

                    }
                    getFragmentManager().beginTransaction().replace(R.id.fragment_container, new UsersFragment()).commit();
                }

                @Override
                public void onFailure(@NonNull Call<UserList> call, @NonNull Throwable t) {
                    Log.e("Search Network", "Failed connection");
                }
            });
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root_view = inflater.inflate(R.layout.fragment_search, container, false);
        editTextUser = root_view.findViewById(R.id.search_username);
        buttonSearch = root_view.findViewById(R.id.button_search);
        editTextUser.addTextChangedListener(userNameTextWatcher);
        buttonSearch.setOnClickListener(clickListener);
        return root_view;
    }
}
