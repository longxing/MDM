package com.shwootide.mdm.view;

import android.os.Bundle;

import com.ab.activity.AbActivity;
import com.shwootide.mdm.R;
import com.shwootide.mdm.service.ExitApplication;
import com.shwootide.mdm.tools.ActivityTools;

public class ProductIntroduceView extends AbActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.productintroduce);
        ExitApplication.getInstance().addActivity(this);
        ActivityTools.headView(ProductIntroduceView.this,"我们的产品");
    }
}
