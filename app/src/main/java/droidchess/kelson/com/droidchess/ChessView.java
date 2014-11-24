package droidchess.kelson.com.droidchess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by Kelson on 11/24/2014.
 */

public class ChessView extends View {

    final String TAG = "ChessView";

    // an 8x8 array that represents our game board
    private static Piece[][] board;

    private RectF[][] boxes;

    // information maintaining the width and height of a single cell
    private int cell_width, cell_height;

    // rectangle that will represent the size of a cell
    private RectF bounding_box;

    // paint objects for the pieces and the board
    private Paint black, white;

    // determines the cell coordinates of the press and the release for making moves
    private int press_x, press_y;

    private Bitmap piece;

    public ChessView(Context context) {
        super(context);
        init();
    }

    public ChessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {

        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        white = new Paint(Paint.ANTI_ALIAS_FLAG);

        black.setColor(Color.BLACK);
        white.setColor(Color.WHITE);

        board = new Piece[8][8];

        boxes = new RectF[8][8];

        press_x = 0;
        press_y = 0;

        bounding_box = new RectF();
        arrangePieces();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int i = 0;
        cell_width = canvas.getWidth() / 8;

        canvas.save();

        // Draw the Boxes
        for (int x = 0; x < 8; x++) {

            for (int y = 0; y < 8; y++) {

                int l = x * cell_width;
                int t = y * cell_width;
                int r = l + cell_width;
                int b = t + cell_width;

                bounding_box.set(l,t,r,b);

                if (isEven(x, y))
                    canvas.drawRect(bounding_box,white);
                else
                    canvas.drawRect(bounding_box,black);

                boxes[x][y] = new RectF(l,t,r,b);


                if (board[x][y] == Piece.BLACK_KNIGHT) {
                    piece = BitmapFactory.decodeResource(getResources(),R.drawable.knight);
                    canvas.drawBitmap(piece,null,bounding_box,null);
                    Log.i(TAG,"Knight - [" + x + "][" + y + "]");
                }

                i++;
            }
        }

    }

    private boolean isEven(int posx, int posy) {
        if ( (posx + posy) % 2 == 0)
            return true;
        else
            return false;
    }

    void arrangePieces() {

        // Black Pieces
        board[1][0] = Piece.BLACK_KNIGHT;
        board[6][0] = Piece.BLACK_KNIGHT;

        // White Pieces

    }
}
