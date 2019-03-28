package com.subitech.nepalese.guessgame;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Help extends Activity {

  private Button btn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.help);

    btn = findViewById(R.id.btn);

    btn.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            contactUs();
          }
        });
  }

  private void contactUs() {

    try {
      String[] email = new String[] {"subihelp123@gmail.com"};
      String sub = "User from Simple Guess Game v1.0 beta";

      Intent intent = new Intent(Intent.ACTION_SEND);
      intent.putExtra(Intent.EXTRA_EMAIL, email);
      intent.putExtra(Intent.EXTRA_SUBJECT, sub);

      intent.setType("message/text");
      intent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");

      startActivity(intent);

    } catch (Exception e) {
      Toast.makeText(this, "Sorry, " + e, Toast.LENGTH_SHORT).show();
    }
  }
}
