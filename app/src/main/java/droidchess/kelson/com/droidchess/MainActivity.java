package droidchess.kelson.com.droidchess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;


public class MainActivity extends Activity {

    static Activity activity = null;
    static Context context = null;
    static TextView whitePieces;
    static TextView blackPieces;
    static TextView whitePiecesw;
    static TextView blackPiecesw;
    static TextView currentTurnw;
    static TextView currentTurnb;
    static int lastSwapChoice = 0;
    static TextView whiteTimer;
    static TextView blackTimer;

    static CountDownTimer whiteCounter = new CountDownTimer(900000, 1000) {
        @Override
        public void onTick(long l) {
            int seconds = (int) (l / 1000) % 60 ;
            int minutes = (int) ((l / (1000*60)) % 60);
            whiteTimer.setText(minutes + ":" + seconds);
        }

        @Override
        public void onFinish() {
            endgame(false);

        }
    };

    static CountDownTimer blackCounter = new CountDownTimer(900000, 1000) {
        @Override
        public void onTick(long l) {
            int seconds = (int) (l / 1000) % 60 ;
            int minutes = (int) ((l / (1000*60)) % 60);
            blackTimer.setText(minutes + ":" + seconds);
        }

        @Override
        public void onFinish() {
            endgame(true);
        }
    };

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

        whiteCounter.start();
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
            super.recreate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    static void showDialogFragment(Context context) {
        String names[] ={"Queen","Rook","Bishop","Knight"};
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View convertView = (View) inflater.inflate(R.layout.swap, null);
        alertDialog.setView(convertView);
        alertDialog.setTitle("Swap Choices");
        ListView lv = (ListView) convertView.findViewById(R.id.listView1);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,android.R.layout.simple_list_item_1,names);
        lv.setAdapter(adapter);
        alertDialog.show();

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                lastSwapChoice = i;
                alertDialog.dismiss();
            }
        });

        alertDialog.setCancelable(false);
    }

    static void swapTimer(boolean whiteTurn) {
        if (whiteTurn) {
            blackCounter.cancel();
            blackTimer.setText(R.string.timer);
            whiteCounter.start();
        } else {
            whiteCounter.cancel();
            whiteTimer.setText(R.string.timer);
            blackCounter.start();
        }
    }

    static void endgame(boolean whiteWinner) {
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
       activity.recreate();
    }
}
