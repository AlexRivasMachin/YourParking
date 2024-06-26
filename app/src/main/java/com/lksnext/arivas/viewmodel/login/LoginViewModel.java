package com.lksnext.arivas.viewmodel.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.lksnext.arivas.data.DataRepository;
import com.lksnext.arivas.data.Callback;

public class LoginViewModel extends ViewModel {

    MutableLiveData<Boolean> logged = new MutableLiveData<>(null);

    public LiveData<Boolean> isLogged(){
        return logged;
    }

    public void loginUser(String email, String password) {
        DataRepository.getInstance().login(email, password, new Callback() {
            @Override
            public void onSuccess() {
                //TODO
                logged.setValue(Boolean.TRUE);
            }

            @Override
            public void onFailure() {
                //TODO
                logged.setValue(Boolean.FALSE);
            }
        });
    }
}

