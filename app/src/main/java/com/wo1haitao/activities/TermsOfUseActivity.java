package com.wo1haitao.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.wo1haitao.R;

public class TermsOfUseActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_of_use);
        getActionbar(R.string.title_terms_of_use);
        Button btnReturn = (Button)findViewById(R.id.btn_return);

        btnReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
