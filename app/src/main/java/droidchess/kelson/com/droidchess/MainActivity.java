package droidchess.kelson.com.droidchess;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;

import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;


public class MainActivity extends Activity {

    static TextView whitePieces;
    static TextView blackPieces;
    static TextView whitePiecesw;
    static TextView blackPiecesw;
    static TextView currentTurnw;
    static TextView currentTurnb;
    static int lastSwapChoice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        whitePieces = (TextView) findViewById(R.id.white);
        blackPieces = (TextView) findViewById(R.id.black);
        whitePiecesw = (TextView) findViewById(R.id.whitew);
        blackPiecesw = (TextView) findViewById(R.id.blackw);
        currentTurnw = (TextView) findViewById(R.id.turnw);
        currentTurnb = (TextView) findViewById(R.id.turnb);
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

}
