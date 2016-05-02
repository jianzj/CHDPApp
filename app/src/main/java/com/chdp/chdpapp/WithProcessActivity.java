package com.chdp.chdpapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.chdp.chdpapp.bean.Prescription;
import com.chdp.chdpapp.bean.User;
import com.chdp.chdpapp.bean.Process;
import com.chdp.chdpapp.util.AuthHelper;

public class WithProcessActivity extends ActionBarActivity {
    public User user;
    public Prescription prescription;
    public Process presentProc;
    public Process previousProc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        user = AuthHelper.checkUser(this);
        prescription = (Prescription) intent.getSerializableExtra("prescription");
    }
}
