package com.erkkiperkele.master_android.service;


import com.erkkiperkele.master_android.entity.JResult;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SimplePiDataService {

    @SuppressWarnings("FieldCanBeLocal")
    private final String DatabaseNameSimplePi = "simple_pi";

    @SuppressWarnings("FieldCanBeLocal")
    private final String DatabaseTableResults = "results";

    private final DatabaseReference _simplePiDb;

    // WARNING: singleton pattern -> make sure '_instance' is the last field else runtime error
    private final static SimplePiDataService _instance = new SimplePiDataService();

    private SimplePiDataService() {

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        _simplePiDb = database.getReference(DatabaseNameSimplePi);
    }

    public static SimplePiDataService getInstance() {

        return _instance;
    }

    public void saveResult(JResult result) {

        _simplePiDb
                .child(DatabaseTableResults)
                .child(result.getId().toString())
                .setValue(result);
    }
}