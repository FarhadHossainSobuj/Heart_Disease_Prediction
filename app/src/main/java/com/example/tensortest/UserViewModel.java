package com.example.tensortest;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.example.tensortest.data.UserAccountDatabase;
import com.example.tensortest.data.UserRepository;

public class UserViewModel extends ViewModel {

    private UserRepository userRepository;

    public UserViewModel(Context context) {

        userRepository = UserRepository.getInstance(UserAccountDatabase.getAppDatabase(context).userAccountDao());
    }

    void createUser(String username, String password)
    {
        userRepository.insertUser(username, password);
    }

    boolean checkValidLogin(String username, String password)
    {
        return userRepository.isValidAccount(username, password);
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final Context ctxt;

        Factory(Context ctxt) {
            this.ctxt=ctxt.getApplicationContext();
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
            return((T)new UserViewModel(ctxt));
        }
    }
}
