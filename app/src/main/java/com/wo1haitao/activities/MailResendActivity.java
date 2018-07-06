package com.wo1haitao.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.wo1haitao.R;

/**
 * Created by goodproductssoft on 4/14/17.
 */

public class MailResendActivity extends BaseActivity {
    EditText et_email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_resend);
        getActionbar(R.string.title_mail_resend);
        et_email = (EditText) findViewById(R.id.et_mail);


        Button btn_resend_mail = (Button)findViewById(R.id.btn_resend_mail);



        btn_resend_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MailResendActivity.this, LoginActivity.class);
                i.putExtra("screen", "mail-activity");
                PackageManager packageManager = getPackageManager();
                if (i.resolveActivity(packageManager) != null) {
                    startActivity(i);
                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(MailResendActivity.this, IntroActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_NEW_TASK);
        PackageManager packageManager = getPackageManager();
        if (i.resolveActivity(packageManager) != null) {
            startActivity(i);
            overridePendingTransition(R.anim.empty_ani, R.anim.old_activity);
        }
    }
}
