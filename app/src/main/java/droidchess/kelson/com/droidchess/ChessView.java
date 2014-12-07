package droidchess.kelson.com.droidchess;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Kelson on 11/24/2014.
 */

public class ChessView extends View {

    final String TAG = "ChessView";

    private boolean pieceSelected = false;

    private final Controller c = new Controller();
    private final Controller2 c2 = new Controller2();

    private boolean whiteTurn = true;
    Context context;

    // an 8x8 array that represents our game board
    public static Piece[][] board;

    private RectF[][] boxes;

    // information maintaining the width and height of a single cell
    private int cell_width, cell_height;

    // rectangle that will represent the size of a cell
    private RectF bounding_box;

    // paint objects for the pieces and the board
    private Paint black, white, blue;

    // determines the cell coordinates of the press and the release for making moves
    private int press_x, press_y;
    private int selected_x, selected_y;
    private int white_pieces = 16;
    private int black_pieces = 16;

    private Bitmap piece;

    public ChessView(Context context) {
        super(context);
        this.context = context;
        init();
    }

    public ChessView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        init();
    }

    public ChessView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        init();
    }

    private void init() {
        black = new Paint(Paint.ANTI_ALIAS_FLAG);
        white = new Paint(Paint.ANTI_ALIAS_FLAG);
        blue = new Paint(Paint.ANTI_ALIAS_FLAG);
        black.setColor(Color.DKGRAY);
        white.setColor(Color.WHITE);
        blue.setColor(Color.BLUE);
        board = new Piece[8][8];

        boxes = new RectF[8][8];

        press_x = 0;
        press_y = 0;
        selected_x = 0;
        selected_y = 0;
        bounding_box = new RectF();
        arrangePieces();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int i = 0;

        boolean[][]  movements = new boolean[8][8];

        cell_width = canvas.getWidth() / 8;

        canvas.save();

        if (pieceSelected) {
            movements = c.move(board[selected_x][selected_y], selected_x, selected_y);

        }
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


                switch (board[x][y]) {
                    case WHITE_PAWN: {
                        if (pieceSelected && whiteTurn && x == press_x && y == press_y)
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.pawn_selected);
                        else
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.white_pawn);
                        break;
                    }
                    case WHITE_ROOK: {
                        if (pieceSelected && whiteTurn && x == press_x && y == press_y)
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.rook_selected);
                        else
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.white_rook);
                        break;
                    }
                    case WHITE_KNIGHT: {
                        if (pieceSelected && whiteTurn && x == press_x && y == press_y)
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.knight_selected);
                        else
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.white_knight);
                        break;
                    }
                    case WHITE_BISHOP: {
                        if (pieceSelected && whiteTurn && x == press_x && y == press_y)
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.bishop_selected);
                        else
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.white_bishop);
                        break;
                    }
                    case WHITE_KING: {
                        if (pieceSelected && whiteTurn && x == press_x && y == press_y)
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.king_selected);
                        else
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.white_king);
                        break;
                    }
                    case WHITE_QUEEN: {
                        if (pieceSelected && whiteTurn && x == press_x && y == press_y)
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.queen_selected);
                        else
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.white_queen);
                        break;
                    }
                    case BLACK_PAWN: {
                        if (pieceSelected && !whiteTurn && x == press_x && y == press_y)
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.pawn_selected);
                        else
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.black_pawn);
                        break;
                    }
                    case BLACK_ROOK: {
                        if (pieceSelected && !whiteTurn && x == press_x && y == press_y)
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.rook_selected);
                        else
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.black_rook);
                        break;
                    }
                    case BLACK_KNIGHT: {
                        if (pieceSelected && !whiteTurn && x == press_x && y == press_y)
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.knight_selected);
                        else
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.black_knight);
                        break;
                    }
                    case BLACK_BISHOP: {
                        if (pieceSelected && !whiteTurn && x == press_x && y == press_y)
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.bishop_selected);
                        else
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.black_bishop);
                        break;
                    }
                    case BLACK_KING: {
                        if (pieceSelected && !whiteTurn && x == press_x && y == press_y)
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.king_selected);
                        else
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.black_king);
                        break;
                    }
                    case BLACK_QUEEN: {
                        if (pieceSelected && !whiteTurn && x == press_x && y == press_y)
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.queen_selected);
                        else
                            piece = BitmapFactory.decodeResource(getResources(), R.drawable.black_queen);
                        break;
                    }
                    default: {
                        piece = null;
                    }
                }

                if (pieceSelected) {
                    if (movements[x][y]) {
                        canvas.drawCircle(bounding_box.centerX(),bounding_box.centerY(),cell_width/2,blue);
                    }
                }

                if(piece != null) {
                    //if (board[x][y].name().contains("BLACK")) {
                    canvas.drawBitmap(piece, bounding_box.centerX() - (piece.getWidth() / 2), bounding_box.top + ((bounding_box.height() - piece.getHeight()) / 2), null);
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


        // White Pieces
        board[0][6] = Piece.WHITE_PAWN;
        board[1][6] = Piece.WHITE_PAWN;
        board[2][6] = Piece.WHITE_PAWN;
        board[3][6] = Piece.WHITE_PAWN;
        board[4][6] = Piece.WHITE_PAWN;
        board[5][6] = Piece.WHITE_PAWN;
        board[6][6] = Piece.WHITE_PAWN;
        board[7][6] = Piece.WHITE_PAWN;
        board[0][7] = Piece.WHITE_ROOK;
        board[1][7] = Piece.WHITE_KNIGHT;
        board[2][7] = Piece.WHITE_BISHOP;
        board[3][7] = Piece.WHITE_QUEEN;
        board[4][7] = Piece.WHITE_KING;
        board[5][7] = Piece.WHITE_BISHOP;
        board[6][7] = Piece.WHITE_KNIGHT;
        board[7][7] = Piece.WHITE_ROOK;

        // Black Pieces
        board[0][1] = Piece.BLACK_PAWN;
        board[1][1] = Piece.BLACK_PAWN;
        board[2][1] = Piece.BLACK_PAWN;
        board[3][1] = Piece.BLACK_PAWN;
        board[4][1] = Piece.BLACK_PAWN;
        board[5][1] = Piece.BLACK_PAWN;
        board[6][1] = Piece.BLACK_PAWN;
        board[7][1] = Piece.BLACK_PAWN;
        board[0][0] = Piece.BLACK_ROOK;
        board[1][0] = Piece.BLACK_KNIGHT;
        board[2][0] = Piece.BLACK_BISHOP;
        board[3][0] = Piece.BLACK_QUEEN;
        board[4][0] = Piece.BLACK_KING;
        board[5][0] = Piece.BLACK_BISHOP;
        board[6][0] = Piece.BLACK_KNIGHT;
        board[7][0] = Piece.BLACK_ROOK;

        for (int i  = 0; i < board.length; i++) {
            for (int j = 0; j < board.length; j++) {
                if (board[i][j] == null) {
                    board[i][j] = Piece.EMPTY;
                }
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {

            for (int i = 0; i < 8; i++) {

                for (int j = 0; j < 8; j++) {

                    if (boxes[i][j].contains(event.getX(),event.getY()) ) {
                        press_x = i;
                        press_y = j;
                    }
                }
            }

            if (pieceSelected) {

                if (selected_x == press_x && selected_y == press_y) {
                    pieceSelected = false;

                } else if (c.move(board[selected_x][selected_y],selected_x,selected_y)[press_x][press_y] == true) {
                    board[press_x][press_y] = board[selected_x][selected_y];
                    checkSwap();
                    board[selected_x][selected_y] = Piece.EMPTY;

                    pieceSelected = false;
                    whiteTurn = !whiteTurn;
                    MainActivity.swapTimer(whiteTurn);
                    updateUI();
                } else {
                    return true;
                }

            } else {
                if (board[press_x][press_y] == Piece.EMPTY) {
                    return true;
                }
                if (board[press_x][press_y].name().contains("WHITE") && whiteTurn) {
                    pieceSelected = true;
                }
                if (board[press_x][press_y].name().contains("BLACK") && !whiteTurn) {
                    pieceSelected = true;
                }
                selected_x = press_x;
                selected_y = press_y;


            }

            invalidate();
            return true;
        }

        return super.onTouchEvent(event);
    }

    private void checkSwap(){
            if (c2.pawnswap(board[press_x][press_y],press_x,press_y)) {

                MainActivity.showDialogFragment(context);

                switch (MainActivity.lastSwapChoice) {
                    case 0:
                        if (whiteTurn)
                            board[press_x][press_y] = Piece.WHITE_QUEEN;
                        else
                            board[press_x][press_y] = Piece.BLACK_QUEEN;
                        break;
                    case 1:
                        if (whiteTurn)
                            board[press_x][press_y] = Piece.WHITE_ROOK;
                        else
                            board[press_x][press_y] = Piece.BLACK_ROOK;
                        break;
                    case 2:
                        if (whiteTurn)
                            board[press_x][press_y] = Piece.WHITE_BISHOP;
                        else
                            board[press_x][press_y] = Piece.BLACK_BISHOP;
                        break;
                    case 3:
                        if (whiteTurn)
                            board[press_x][press_y] = Piece.WHITE_KNIGHT;
                        else
                            board[press_x][press_y] = Piece.BLACK_KNIGHT;
                        break;
                }
            }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        this.setMeasuredDimension(widthMeasureSpec, 8 * (widthMeasureSpec/8));

    }

    private void updateUI() {

        white_pieces = 0;
        black_pieces = 0;

        for (int i = 0; i < board.length; i++) {
            for (int k = 0; k < board.length; k++) {
                if (board[i][k].name().contains("WHITE")) {
                    white_pieces++;
                } else if (board[i][k].name().contains("BLACK")) {
                    black_pieces++;
                }
            }
        }

        MainActivity.whitePieces.setText(String.valueOf(white_pieces));
        MainActivity.blackPieces.setText(String.valueOf(black_pieces));
        MainActivity.whitePiecesw.setText(String.valueOf(white_pieces));
        MainActivity.blackPiecesw.setText(String.valueOf(black_pieces));

        if (whiteTurn) {
            MainActivity.currentTurnw.setText("White");
            MainActivity.currentTurnb.setText("White");
        } else {
            MainActivity.currentTurnw.setText("Black");
            MainActivity.currentTurnb.setText("Black");
        }
    }

}
