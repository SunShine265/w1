package com.wo1haitao.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wo1haitao.R;

public class PrivacyPolicyActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        getActionbar(R.string.title_privacy_policy);
        Button btnReturn = (Button)findViewById(R.id.btn_return);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
