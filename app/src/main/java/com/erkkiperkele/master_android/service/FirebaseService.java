package com.erkkiperkele.master_android.service;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

class FirebaseService {

    @SuppressWarnings("FieldCanBeLocal")
    private final String _anonymousUser = "anonymousUser";    // DO NOT CHANGE! Defined in FB rules!

    private final FirebaseAuth _firebaseAuth;
    private final DatabaseReference _fireDb;

    private static final FirebaseService ourInstance = new FirebaseService();

    static FirebaseService getInstance() {
        return ourInstance;
    }

    private FirebaseService() {

        _fireDb = FirebaseDatabase.getInstance().getReference();
        _firebaseAuth = FirebaseAuth.getInstance();

    }

    String getFirebaseUid() {
        FirebaseUser user = _firebaseAuth.getCurrentUser();

        return user != null
                ? user.getUid()
                : _anonymousUser;
    }

    DatabaseReference getFireDb() {

        return _fireDb;
    }
}