package com.erkkiperkele.master_android.service;


import com.erkkiperkele.master_android.entity.JResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MultiPiDataService {

    @SuppressWarnings("FieldCanBeLocal")
    private final String DatabaseNameMultiPi = "multi_pi";

    @SuppressWarnings("FieldCanBeLocal")
    private final String DatabaseTableResults = "results";

    private final DatabaseReference _multiPiDb;

    // WARNING: singleton pattern -> make sure '_instance' is the last field else runtime error
    private final static MultiPiDataService _instance = new MultiPiDataService();

    private MultiPiDataService() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        _multiPiDb = database.getReference(DatabaseNameMultiPi);
    }

    public static MultiPiDataService getInstance() {

        return _instance;
    }

    public void saveResult(JResult result) {

        _multiPiDb
                .child(DatabaseTableResults)
                .child(result.getId().toString())
                .setValue(result);
    }
}