package com.shwootide.mdm.view;

import android.os.Bundle;

import com.ab.activity.AbActivity;
import com.shwootide.mdm.R;
import com.shwootide.mdm.service.ExitApplication;
import com.shwootide.mdm.tools.ActivityTools;

public class EmaStoreView extends AbActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emastore);
        ExitApplication.getInstance().addActivity(this);
        ActivityTools.headView(EmaStoreView.this,"EmaStoreΩÈ…‹");
    }
}
