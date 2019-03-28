package com.subitech.nepalese.guessgame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Random;

public class MainActivity extends Activity {

  private Button btnSubmit;
  private Button btnHelp;
  private EditText editText;

  private int word;
  private int guess;
  private int round = 1;

  private int limit = 0;
  private int totalLimit = 3;

  private ImageView i1;
  private ImageView i2;
  private ImageView i3;

  private TextView live;
  private TextView rxr;

  private boolean isInt = true;
  private boolean outOfLimit = false;
  private static final String SHARED_PREFS = "sharedPrefs";
  private static final String DATA = "data";

  private int rr;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    btnSubmit = findViewById(R.id.btn);
    btnHelp = findViewById(R.id.btn_help);
    editText = findViewById(R.id.et);

    i1 = findViewById(R.id.i1);
    i2 = findViewById(R.id.i2);
    i3 = findViewById(R.id.i3);

    live = findViewById(R.id.live);
    rxr = findViewById(R.id.round);

    generate();

    btnHelp.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            startActivity(new Intent(MainActivity.this, Help.class));
          }
        });

    btnSubmit.setOnClickListener(
        new View.OnClickListener() {
          @Override
          public void onClick(View v) {
            chkAnswer();

            if (rr < round) {
              rxr.setText("Round: " + round);
            }
          }
        });

    loadData();
  }

  private void generate() {
    Random random = new Random();
    word = random.nextInt(20);
    word++;
  }

  private void chkAnswer() {

    String rawGuess = editText.getText().toString().trim();
    if (!rawGuess.isEmpty()) {
      try {
        guess = Integer.parseInt(rawGuess);

        isInt = true;

      } catch (NumberFormatException e) {
        Toast.makeText(this, "Input is not an integer", Toast.LENGTH_SHORT).show();

        isInt = false;
      }
    }

    if (rawGuess.isEmpty()) {
      Toast.makeText(this, "Guess Can't be Empty", Toast.LENGTH_SHORT).show();
    } else if (word != guess && !rawGuess.isEmpty() && isInt) {

      limit++;
    }
    if (totalLimit < limit) {

      showLoose();
      outOfLimit = true;
    }

    if (guess < word && !rawGuess.isEmpty() && isInt && !outOfLimit) {
      Toast.makeText(
              this, "Sorry, You guessed smaller.", Toast.LENGTH_SHORT)
          .show();
    } else if (guess > word && !rawGuess.isEmpty() && isInt && !outOfLimit) {
      Toast.makeText(this, "Sorry, You guessed larger.", Toast.LENGTH_SHORT)
          .show();
    }

    if (guess == word) {

      showChoice();
      round++;
      saveData();
      outOfLimit = false;
    }

    switch (limit) {
      case 1:
        i1.setVisibility(View.GONE);
        break;

      case 2:
        i2.setVisibility(View.GONE);
        break;

      case 3:
        i3.setVisibility(View.INVISIBLE);
        live.setText("Live: 0");
        break;

      case 4:
        live.setText("Failed");
    }
  }

  private void showChoice() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setCancelable(false);
    builder.setTitle("Congratulation!");
    builder.setMessage(
        "You have successfully guessed my secret number. "
            + word
            + "\n\nDo you want to play again?");

    builder.setPositiveButton(
        "OK",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int Which) {
            dialogInterface.dismiss();

            limit = 0;
            outOfLimit = false;
            i1.setVisibility(View.VISIBLE);
            i2.setVisibility(View.VISIBLE);
            i3.setVisibility(View.VISIBLE);
            live.setText("Live: ");

            Random random = new Random();
            word = random.nextInt(20);
            word++;

            // empty editText
            editText.setText("");
          }
        });

    builder.setNegativeButton(
        "EXIT",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int Which) {
            dialogInterface.dismiss();

            finish();
          }
        });

    builder.create().show();
  }

  private void showLoose() {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);

    builder.setCancelable(false);
    builder.setTitle("Sorry!");
    builder.setMessage(
        "You did not guess my secret number within "
            + limit
            + " tries. Better luck next time.\n The number was "
            + word
            + "\n\nDo you want to try again?");

    builder.setPositiveButton(
        "OK",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int Which) {
            dialogInterface.dismiss();

            limit = 0;
            outOfLimit = false;
            i1.setVisibility(View.VISIBLE);
            i2.setVisibility(View.VISIBLE);
            i3.setVisibility(View.VISIBLE);
            live.setText("Live: ");

            Random random = new Random();
            word = random.nextInt(20);
            word++;

            // empty editText
            editText.setText("");
          }
        });

    builder.setNegativeButton(
        "EXIT",
        new DialogInterface.OnClickListener() {
          @Override
          public void onClick(DialogInterface dialogInterface, int Which) {
            dialogInterface.dismiss();

            finish();
          }
        });

    builder.create().show();
  }

  private void saveData() {
    SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

    SharedPreferences.Editor editor = prefs.edit();

    editor.putInt(DATA, round);
    editor.apply();
  }

  private void loadData() {
    SharedPreferences prefs = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

    rr = prefs.getInt(DATA, 1);
    round = rr;

    rxr.setText("Round: " + rr);
  }
}
