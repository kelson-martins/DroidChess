package droidchess.kelson.com.droidchess;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class MainActivity extends Activity {

    static Activity activity = null;
    static Context context = null;
    static TextView whitePieces;
    static TextView blackPieces;
    static TextView whitePiecesw;
    static TextView blackPiecesw;
    static TextView currentTurnw;
    static TextView currentTurnb;
    static TextView whiteTimer;
    static TextView blackTimer;

    static long whiteRemaining = 900000;
    static long blackRemaining = 900000;

    static CountDownTimer whiteCounter;
    static CountDownTimer blackCounter;

    static void whiteCounter() {
        whiteCounter = new CountDownTimer(whiteRemaining, 1000) {
            @Override
            public void onTick(long l) {
                int seconds = (int) (l / 1000) % 60 ;
                int minutes = (int) ((l / (1000*60)) % 60);
                whiteRemaining = l;
                whiteTimer.setText(formatTimer(minutes) + ":" + formatTimer(seconds));
            }

            @Override
            public void onFinish() {
                endgame(false,false);
            }


        }.start();
    }

    static void blackCounter() {
         blackCounter = new CountDownTimer(blackRemaining, 1000) {
            @Override
            public void onTick(long l) {
                blackRemaining = l;
                int seconds = (int) (l / 1000) % 60 ;
                int minutes = (int) ((l / (1000*60)) % 60);
                blackTimer.setText(formatTimer(minutes) + ":" + formatTimer(seconds));
            }

            @Override
            public void onFinish() {
                endgame(true,false);
            }
        }.start();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        activity = this;
        context = this;
        whitePieces = (TextView) findViewById(R.id.white);
        blackPieces = (TextView) findViewById(R.id.black);
        whitePiecesw = (TextView) findViewById(R.id.whitew);
        blackPiecesw = (TextView) findViewById(R.id.blackw);
        currentTurnw = (TextView) findViewById(R.id.turnw);
        currentTurnb = (TextView) findViewById(R.id.turnb);
        whiteTimer = (TextView) findViewById(R.id.white_timer);
        blackTimer = (TextView) findViewById(R.id.black_timer);

        // commented since we are starting the counter after the first move
        //whiteCounter();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.reset) {
            reset();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static void swapTimer(boolean whiteTurn) {
        if (whiteTurn) {

            blackCounter.cancel();
            whiteCounter();
        } else {
            if (whiteCounter != null) {
                whiteCounter.cancel();
            }
            blackCounter();
        }
    }

    static void endgame(boolean whiteWinner, boolean stalemate) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = (View) inflater.inflate(R.layout.endgame, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Winner");
        TextView tv = (TextView) convertView.findViewById(R.id.winner);
        Button b = (Button) convertView.findViewById(R.id.endok);
        if (whiteWinner) {
            tv.setText("White");
        } else {
            tv.setText("Black");
        }

        if (stalemate) {
            tv.setText("Draw");
        }
        if (blackCounter != null) {
            blackCounter.cancel();
        }
        if (whiteCounter != null) {
            whiteCounter.cancel();
        }


        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                reset();
            }
        });

        alertDialog.show();

        alertDialog.setCancelable(false);
    }

    static void reset() {


        if (blackCounter != null) {
            blackCounter.cancel();
        }

        if (whiteCounter != null) {
            whiteCounter.cancel();
        }

        whiteRemaining = 900000;
        blackRemaining = 900000;

        activity.recreate();

    }

    // formatting the timer
    static String formatTimer(int number) {

        String ret = String.valueOf(number);

        if (ret.length() < 2) {
            ret = "0" + ret;
        }

        return ret;
    }

    // we have to stop the timmer manually on backpressed since it triggers the timer.onfinish() automatically
    // if no movements were made generates null pointer
    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (whiteCounter != null) {
            whiteCounter.cancel();
        }

        if (blackCounter != null) {
            blackCounter.cancel();
        }

    }
}
