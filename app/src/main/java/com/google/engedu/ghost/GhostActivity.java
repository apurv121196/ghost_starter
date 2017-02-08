package com.google.engedu.ghost;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.Random;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;


public class GhostActivity extends ActionBarActivity {
    private static final String COMPUTER_TURN = "Computer's turn";
    private static final String USER_TURN = "Your turn";
    TextView text,label;
    Button challenge,restart;
    String wordFragment="";
    String currentWord="";
    private GhostDictionary dictionary;
    private boolean userTurn = false;
    private Random random = new Random();
    private AdView mAdView;
    private Button keyboard;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ghost);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
//        mAdView.loadAd(adRequest);
        onStart(null);

       text=(TextView)findViewById(R.id.ghostText);
        text.setText("");

        label=(TextView)findViewById(R.id.gameStatus);
        challenge=(Button)findViewById(R.id.challenge);
        restart=(Button)findViewById(R.id.restart);
        keyboard =  (Button)findViewById(R.id.keyboard);

        try {
            InputStream in = getAssets().open("words.txt");
//            dictionary = new SimpleDictionary(in);
            dictionary = new FastDictionary(in);
        }
        catch(IOException ex)
        {
            ex.printStackTrace();
        }

        challenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(wordFragment.length()>=4 && dictionary.isWord(wordFragment)){
                    label.setText("You Win!!!");
                    challenge.setEnabled(false);
                }
                else
                {
                    if(dictionary.getAnyWordStartingWith(wordFragment)!="")
                    {
                        label.setText("Computer Wins :D :D :D");
                        challenge.setEnabled(false);
                    }
                    else {
                        label.setText("You Win!!!");
                        challenge.setEnabled(false);
                    }
                }
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                label.setText(USER_TURN);
                challenge.setEnabled(true);
                wordFragment="";
                text.setText("");
            }
        });

        keyboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);
            }
        });

        if(!userTurn)computerTurn();

    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.e("Tag","Key daba di!");
        text.setFocusable(true);
        if(keyCode>=29 && keyCode<=54 && challenge.isEnabled())
        {
            text=(TextView)findViewById(R.id.ghostText);
            char c = (char)event.getUnicodeChar();
            currentWord=text.getText().toString();
            wordFragment=currentWord+Character.toString(c);
            text.setText(wordFragment);
            if(dictionary.isWord(wordFragment))
            {
                label.setText("Its a word!");
            }
            computerTurn();
            return true;
        }
        else return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ghost, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }

        return super.onOptionsItemSelected(item);
    }

    private void computerTurn() {
        TextView label = (TextView) findViewById(R.id.gameStatus);
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText(wordFragment);
        // Do computer turn stuff then make it the user's turn again
        if(wordFragment.length()>=4 && dictionary.isWord(wordFragment))
        {
            label.setText("Computer Wins :D :D :D");
            challenge.setEnabled(false);
            return;
        }
        else
        {
            String word;
            if(dictionary==null)
            {
                try
                {
                    InputStream in = getAssets().open("words.txt");
                    dictionary = new SimpleDictionary(in);
                }
                catch(IOException ex)
                {
                    ex.printStackTrace();
                }

            }
            word=dictionary.getAnyWordStartingWith(wordFragment);
            if(word.equals(""))
            {
                label.setText("Computer Wins :D :D :D");
                challenge.setEnabled(false);
                return;
            }
            else
            {
                if(!wordFragment.equals("")) wordFragment=word.substring(0,wordFragment.length()+1);
                else wordFragment+=word.substring(0,1);
                text.setText(wordFragment);
            }
        }
        userTurn = true;
        label.setText(USER_TURN);
    }

    /**
     * Handler for the "Reset" button.
     * Randomly determines whether the game starts with a user turn or a computer turn.
     * @param view
     * @return true
     */
    public boolean onStart(View view) {
        userTurn = random.nextBoolean();
        TextView text = (TextView) findViewById(R.id.ghostText);
        text.setText("");
        TextView label = (TextView) findViewById(R.id.gameStatus);
        if (userTurn) {
            label.setText(USER_TURN);
        } else {
            label.setText(COMPUTER_TURN);
            computerTurn();
        }
        return true;
    }
}
