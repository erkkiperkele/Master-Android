package com.erkkiperkele.master_android.service;


import com.erkkiperkele.master_android.entity.JResult;

public class SimplePiDataService {

    @SuppressWarnings("FieldCanBeLocal")
    private final String DataNameSimplePi = "simple_pi";

    @SuppressWarnings("FieldCanBeLocal")
    private final String DataNameResults = "results";
    private final FirebaseService _firebaseService;

    // WARNING: singleton pattern -> make sure '_instance' is the last field else runtime error
    private final static SimplePiDataService _instance = new SimplePiDataService();

    private SimplePiDataService() {

        _firebaseService = FirebaseService.getInstance();
    }

    public static SimplePiDataService getInstance() {

        return _instance;
    }

    public void saveResult(JResult result) {

        _firebaseService.getFireDb()
                .child("users/" + _firebaseService.getFirebaseUid())
                .child(DataNameSimplePi)
                .child(DataNameResults)
                .child(result.getId().toString())
                .setValue(result);
    }
}