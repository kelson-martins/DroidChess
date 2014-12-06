package droidchess.kelson.com.droidchess;

/**
 * Created by Kelson on 06/12/2014.
 */
public class Controller2 {

    public boolean pawnswap(Piece pawn, int sx, int sy) {
        boolean swapflag = false;
        switch (pawn) {
            case WHITE_PAWN: {
                if (sy == 0) {
                    swapflag = true;
                }
                break;
            }
            case BLACK_PAWN: {
                if (sy == 7) {
                    swapflag = true;
                }
                break;
            }
        }

        return swapflag;
    }
}
