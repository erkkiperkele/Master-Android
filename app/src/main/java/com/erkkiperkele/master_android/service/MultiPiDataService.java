package com.erkkiperkele.master_android.service;

import com.erkkiperkele.master_android.entity.JResult;

public class MultiPiDataService {

    @SuppressWarnings("FieldCanBeLocal")
    private final String DataNameMultiPi = "multi_pi";

    @SuppressWarnings("FieldCanBeLocal")
    private final String DataNameResults = "results";

    private final FirebaseService _firebaseService;

    // WARNING: singleton pattern -> make sure '_instance' is the last field else runtime error
    private final static MultiPiDataService _instance = new MultiPiDataService();

    private MultiPiDataService() {

        _firebaseService = FirebaseService.getInstance();
    }

    public static MultiPiDataService getInstance() {

        return _instance;
    }

    public void saveResult(JResult result) {

        _firebaseService.getFireDb()
                .child("users/" + _firebaseService.getFirebaseUid())
                .child(DataNameMultiPi)
                .child(DataNameResults)
                .child(result.getId().toString())
                .setValue(result);
    }
}