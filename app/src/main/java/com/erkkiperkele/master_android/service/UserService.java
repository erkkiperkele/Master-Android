package com.erkkiperkele.master_android.service;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

public class UserService {

    private static final UserService ourInstance = new UserService();
    private GoogleSignInAccount _googleSignInAccount = null;

    public static UserService getInstance() {
        return ourInstance;
    }

    private UserService() {
    }

    public boolean isConnected() {
        return _googleSignInAccount != null;
    }

    public GoogleSignInAccount getGoogleSignInAccount() {

        return _googleSignInAccount;
    }

    public void setGoogleSignInAccount(GoogleSignInAccount googleSignInAccount) {

        _googleSignInAccount = googleSignInAccount;
    }
}