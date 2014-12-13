package droidchess.kelson.com.droidchess;

import java.util.Objects;

/**
 * Created by Kelson on 11/21/2014.
 */
public class Controller {

    private Piece getPiece(int cx, int cy) {//get pieces of the particular cell on the board
        // TODO Auto-generated method stub
         Piece piece;//temp variable to store cell piece
        try {
            piece = ChessView.board[cx][cy];//try to get this particular cell piece
        } catch (ArrayIndexOutOfBoundsException e) {
            piece = Piece.OUT;//if the cell is not on board, out of bound
        }
        return piece;//give back the piece
    }


    private boolean getPath(boolean[][] possiblemove,int ax,int ay){
        boolean oob=false;//variable to store move possibility
        try {
            oob = possiblemove[ax][ay];//try to get this particular cell state
        } catch (ArrayIndexOutOfBoundsException e) {
            oob = false;//if the cell is not on board, out of bound,not possible move
        }
        return oob;//if move is possible
    }


    private boolean kingcantmove(int kx,int ky){//if there no safe move for king
        boolean[][] shieldstate=kingmove(kx,ky);//king surrounding is empty or enemy
        boolean[][] temp=getLine(kx,ky);//get enemy line of sight exclude enemy
        int path=0;
        for(int px=0;px<8;px++){
            for(int py=0;py<8;py++){
                if(shieldstate[px][py] && temp[px][py]){shieldstate[px][py]=false;}//empty but not safe
            }
        }//check king surounding, if no move
        for(int dx=-1;dx<2;dx++){
            for(int dy=-1;dy<2;dy++){
                if(getPath(shieldstate,kx+dx,ky+dy)){//if it safe to move in the path
                    path++;//count path
                }
            }
        }
        if(path==0){return true;//no path is safe
        }else{return false;}//at least 1 safe path
    }



    public boolean pawnswap(Piece pawn,int sx,int sy){//check if the pawn get past the enemy wall
        boolean swapflag=false;
            switch (pawn){
                case WHITE_PAWN:{if(sy==0){swapflag=true;}break;}
                case BLACK_PAWN:{if(sy==7){swapflag=true;}break;}
            }
        return swapflag;
    }


    private boolean[][] syncBox(boolean[][] store,boolean[][] add){//'or' operation between 2 dimension boolean array
        boolean[][] stored=new boolean[8][8];//store result
        for(int x=0;x<8;x++){
            for(int y=0;y<8;y++){//going for every cell board
                if(store[x][y] || add[x][y]){
                    stored[x][y]=true;
                }
            }
        }

        return stored;//used in getting all (white/black) move or line of sight
    }

    private boolean[][] pawnkill(int px,int py,int pd){
        boolean[][] stp=new boolean[8][8];//to store possible move
        if(oppose(px,py,px+1,py+pd)){//check right forward
            stp[px+1][py+pd]=true;//if enemy, then can attack there
        }
        if(oppose(px,py,px-1,py+pd)){//check left forward
            stp[px-1][py+pd]=true;//if enemy, then can attack there
        }
        return stp;
    }

    private boolean[][] pawnmove(int px,int py,int pd){//pawn advances
        boolean[][] stp=new boolean[8][8];//to store possible move
        int con=0;//initialize temp variable for pawn y axis position
        if(pd==-1){//if pawn direction is up or -1
            con=6;//pawn y axis is 6
        }else if(pd==1){con=1;}//if not then pawn y axis is 1

        if(getPiece(px,py+pd)==Piece.EMPTY){//check if front side cell is empty
            if (py==con) {//if pawn y axis is in initial position. pawn 1st move
                if(getPiece(px,py+pd+pd)==Piece.EMPTY){//check if 2 cell in front of pawn is empty, because pawn can only advance if empty
                    stp[px][py+pd+pd]=true;//it is possible to move forward 2 cell
                }
            }
            stp[px][py+pd]=true;//it is possible to move forward 1 cell
        }
        return stp;
    }

    public boolean isStalemate(){//check stalemate conditions
        int whiteP=1;//white king must be alive, so 1 unit in white
        int blackP=1;//black king must be alive, so 1 unit in black
        int wx=-1;//default value for white king x position
        int wy=-1;//default value for white king y position
        int bx=-1;//default value for black king x position
        int by=-1;//default value for black king y position
        boolean event1w=false;//white king lacks of minimum pieces to force checkmate
        boolean event2w=false;//white king is alone and no possible move can be made
        boolean event1b=false;//black king lacks of minimum pieces to force checkmate
        boolean event2b=false;//black king is alone and no possible move can be made


        for(int px=0;px<8;px++){
            for(int py=0;py<8;py++){//check every board
                switch(getPiece(px,py)) {//if is piece
                    case WHITE_PAWN:{whiteP+=3;break;}
                    case WHITE_ROOK:{whiteP+=3;break;}
                    case WHITE_KNIGHT:{whiteP++;break;}//need at least 2 or 3
                    case WHITE_BISHOP:{whiteP++;break;}//need at least 2 or 3
                    case WHITE_QUEEN:{whiteP+=3;break;}
                    case WHITE_KING:{wx=px;wy=py;break;}//get white king position
                    case BLACK_PAWN:{blackP+=3;break;}
                    case BLACK_ROOK:{blackP+=3;break;}
                    case BLACK_KNIGHT:{blackP++;break;}//need at least 2 or 3
                    case BLACK_BISHOP:{blackP++;break;}//need at least 2 or 3
                    case BLACK_QUEEN:{blackP+=3;break;}
                    case BLACK_KING:{bx=px;by=py;break;}//get black king position
                }//check condition there should be 1 other piece than king(pawn,rook,queen)
            }//check that there should be 2 other piece of knight or bishop
        }

        if(whiteP<3){//if the piece is not meet minimum requirement of possible force checkmate
            if(whiteP==1){//if king by himself
                //check king surrounding
                if(kingcantmove(wx,wy)){event2w=true;}//if king cannot move
            }
            event1w=true;
        }//white lacks check piece
        if(blackP<3){//if the piece is not meet minimum requirement of possible force checkmate
            if(blackP==1){//if king by himself
                //check king surrounding
                if(kingcantmove(bx,by)){event2b=true;}//if king cannot move
            }
            event1b=true;
        }//black lacks check piece
        return (event1w&&event1b)||event2w||event2b;//both lack piece or either king cant move
     }

    private boolean[][] kingLine(int xl,int yl){//no line can be made by pawn, knight, or king
        boolean[][] checkLine=new boolean[8][8];//line of sight that 'check' the king
        int fx = xl;//initialize temp variable for king x position
        int fy = yl;//initialize temp variable for king y position
        boolean found =false;//initialize temp variable for king line is not yet found

        switch (getPiece(xl,yl)) {//get what piece that 'check' the king
            case WHITE_ROOK: {//if its white rook
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {//check every direction
                        if ((((dx * dx) + (dy * dy)) == 1)&& (!found)) {//direction condition and not yet found
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {//if it is empty
                                checkLine[fx + dx][fy + dy] = true;//form the line
                                fx += dx;//check next cell
                                fy += dy;
                            }//if the empty space line is block
                            if (getPiece(fx + dx, fy + dy) == Piece.BLACK_KING) {//if it is blocking by the king
                                checkLine[fx + dx][fy + dy] = true;//form end of line
                                found=true;//set flag that it is already found, no need to check other direction
                            }else {checkLine=new boolean[8][8];}//if the blocking is not by king, destroy line
                            fx = xl;//revert to original position for check next direction
                            fy = yl;
                        }
                    }
                }
            break;
            }
            case BLACK_ROOK: {//if its black rook
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {//check every direction
                        if ((((dx * dx) + (dy * dy)) == 1)&& (!found)) {//direction condition and not yet found
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {//if it is empty
                                checkLine[fx + dx][fy + dy] = true;//form the line
                                fx += dx;//check next cell
                                fy += dy;
                            }//if the empty space line is block
                            if (getPiece(fx + dx, fy + dy) == Piece.WHITE_KING) {//if it is blocking by the king
                                checkLine[fx + dx][fy + dy] = true;//form end of line
                                found=true;//set flag that it is already found, no need to check other direction
                            }else {checkLine=new boolean[8][8];}//if the blocking is not by king, destroy line
                            fx = xl;//revert to original position for check next direction
                            fy = yl;
                        }
                    }
                }
                break;
            }
            case WHITE_BISHOP: {//if its white bishop
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {//check every direction
                        if ((((dx * dx) + (dy * dy)) == 2)&& (!found)) {//direction condition and not yet found
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {//if it is empty
                                checkLine[fx + dx][fy + dy] = true;//form the line
                                fx += dx;//check next cell
                                fy += dy;
                            }//if the empty space line is block
                            if (getPiece(fx + dx, fy + dy) == Piece.BLACK_KING) {//if it is blocking by the king
                                checkLine[fx + dx][fy + dy] = true;//form end of line
                                found=true;//set flag that it is already found, no need to check other direction
                            }else {checkLine=new boolean[8][8];}//if the blocking is not by king, destroy line
                            fx = xl;//revert to original position for check next direction
                            fy = yl;
                        }
                    }
                }
                break;
            }
            case BLACK_BISHOP: {//if its black bishop
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {//check every direction
                        if ((((dx * dx) + (dy * dy)) == 2)&& (!found)) {//direction condition and not yet found
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {//if it is empty
                                checkLine[fx + dx][fy + dy] = true;//form the line
                                fx += dx;//check next cell
                                fy += dy;
                            }//if the empty space line is block
                            if (getPiece(fx + dx, fy + dy) == Piece.WHITE_KING) {//if it is blocking by the king
                                checkLine[fx + dx][fy + dy] = true;//form end of line
                                found=true;//set flag that it is already found, no need to check other direction
                            }else {checkLine=new boolean[8][8];}//if the blocking is not by king, destroy line
                            fx = xl;//revert to original position for check next direction
                            fy = yl;
                        }
                    }
                }
                break;
            }
            case WHITE_QUEEN: {//if its white queen
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {//check every direction
                        if ((!(dx==0 && dy==0))&& (!found)) {//direction condition and not yet found
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {//if it is empty
                                checkLine[fx + dx][fy + dy] = true;//form the line
                                fx += dx;//check next cell
                                fy += dy;
                            }//if the empty space line is block
                            if (getPiece(fx + dx, fy + dy) == Piece.BLACK_KING) {//if it is blocking by the king
                                checkLine[fx + dx][fy + dy] = true;//form end of line
                                found=true;//set flag that it is already found, no need to check other direction
                            }else {checkLine=new boolean[8][8];}//if the blocking is not by king, destroy line
                            fx = xl;//revert to original position for check next direction
                            fy = yl;
                        }
                    }
                }
                break;
            }
            case BLACK_QUEEN: {//if its black queen
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {//check every direction
                        if ((!(dx==0 && dy==0))&& (!found)) {//direction condition and not yet found
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {//if it is empty
                                checkLine[fx + dx][fy + dy] = true;//form the line
                                fx += dx;//check next cell
                                fy += dy;
                            }//if the empty space line is block
                            if (getPiece(fx + dx, fy + dy) == Piece.WHITE_KING) {//if it is blocking by the king
                                checkLine[fx + dx][fy + dy] = true;//form end of line
                                found=true;//set flag that it is already found, no need to check other direction
                            }else {checkLine=new boolean[8][8];}//if the blocking is not by king, destroy line
                            fx = xl;//revert to original position for check next direction
                            fy = yl;
                        }
                    }
                }
                break;
            }
        }
        return checkLine;//return line of sight that 'check' the king
    }


    private boolean[][] getMove(int fx,int fy){//get enemy possible move, fx and fy is allied last position
        boolean[][] whiteTarget = new boolean[8][8];//white move
        boolean[][] blackTarget = new boolean[8][8];//black move
        boolean[][] targetLine = new boolean[8][8];//enemy move based on (fx,fy)

        for(int kx=0;kx<8;kx++){
            for(int ky=0;ky<8;ky++){//check every cell
                if(getPiece(kx,ky)==Piece.WHITE_PAWN){whiteTarget=syncBox(whiteTarget,pawnmove(kx,ky,-1));continue;}
                if(getPiece(kx,ky)==Piece.BLACK_PAWN){blackTarget=syncBox(blackTarget,pawnmove(kx,ky,1));continue;}
                if(getPiece(kx,ky)==Piece.WHITE_ROOK){whiteTarget=syncBox(whiteTarget,rookmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.BLACK_ROOK){blackTarget=syncBox(blackTarget,rookmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.WHITE_KNIGHT){whiteTarget=syncBox(whiteTarget,knightmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.BLACK_KNIGHT){blackTarget=syncBox(blackTarget,knightmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.WHITE_BISHOP){whiteTarget=syncBox(whiteTarget,bishopmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.BLACK_BISHOP){blackTarget=syncBox(blackTarget,bishopmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.WHITE_QUEEN){whiteTarget=syncBox(whiteTarget,queenmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.BLACK_QUEEN){blackTarget=syncBox(blackTarget,queenmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.WHITE_KING){whiteTarget=syncBox(whiteTarget,kingmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.BLACK_KING){blackTarget=syncBox(blackTarget,kingmove(kx,ky));continue;}
            }//get white and black moves
        }

        if (getPiece(fx,fy).name().contains("WHITE")){targetLine=blackTarget;}//if (fx,fy)isWhite, enemy move is blacktarget
        else if (getPiece(fx,fy).name().contains("BLACK")){targetLine=whiteTarget;}//else if (fx,fy)isBlack, enemy move is whitetarget
        return targetLine;//return enemy move
    }

    private boolean[][] getLine(int fx,int fy){//get enemy line of sight
        boolean[][] whiteTarget = new boolean[8][8];//white line of sight
        boolean[][] blackTarget = new boolean[8][8];//black line of sight
        boolean[][] targetLine = new boolean[8][8];//enemy line of sight based on (fx,fy)

        for(int kx=0;kx<8;kx++){
            for(int ky=0;ky<8;ky++){//check every cell
                if(getPiece(kx,ky)==Piece.WHITE_PAWN){whiteTarget=syncBox(whiteTarget,pawnkill(kx,ky,-1));continue;}
                if(getPiece(kx,ky)==Piece.BLACK_PAWN){blackTarget=syncBox(blackTarget,pawnkill(kx,ky,1));continue;}
                if(getPiece(kx,ky)==Piece.WHITE_ROOK){whiteTarget=syncBox(whiteTarget,rookmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.BLACK_ROOK){blackTarget=syncBox(blackTarget,rookmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.WHITE_KNIGHT){whiteTarget=syncBox(whiteTarget,knightmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.BLACK_KNIGHT){blackTarget=syncBox(blackTarget,knightmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.WHITE_BISHOP){whiteTarget=syncBox(whiteTarget,bishopmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.BLACK_BISHOP){blackTarget=syncBox(blackTarget,bishopmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.WHITE_QUEEN){whiteTarget=syncBox(whiteTarget,queenmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.BLACK_QUEEN){blackTarget=syncBox(blackTarget,queenmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.WHITE_KING){whiteTarget=syncBox(whiteTarget,kingmove(kx,ky));continue;}
                if(getPiece(kx,ky)==Piece.BLACK_KING){blackTarget=syncBox(blackTarget,kingmove(kx,ky));continue;}
            }//get white and black line of sight
        }

        if (getPiece(fx,fy).name().contains("WHITE")){targetLine=blackTarget;}//if (fx,fy)isWhite, enemy line is blacktarget
        else if (getPiece(fx,fy).name().contains("BLACK")){targetLine=whiteTarget;}//else if (fx,fy)isBlack, enemy line is whitetarget
        return targetLine;//return enemy line
    }

    public boolean LineofFire(int fx,int fy){//check if the (x,y) piece has been sighted by the enemy
        boolean inLine=false;//set default flag, that piece haven't been sighted by enemy
        if(getLine(fx,fy)[fx][fy]){inLine=true;}//if position is compromise by enemy, sighted by enemy
        return inLine;//alert condition,can be killed flag
    }

    public boolean Checkmate(Piece kingg,int tx,int ty) {//need to assume event 2 and 3 more
        boolean event1=false;//king cannot move
        boolean event2=false;//enemy cannot be slain
        boolean event3=false;//enemy sight cannot be block

        boolean[][] kgb=kingLine(tx,ty);//checked enemy line of sight to king
        boolean[][] kga=getMove(tx,ty);//possible move of allied Pieces
        int wx=-1;int wy=-1;//initialize king position

        for (int xk = 0; xk < 8; xk++) {
            for (int yk = 0; yk < 8; yk++) {//check every cell
                if(getPiece(xk,yk)==kingg){wx=xk;wy=yk;}//get king position
                if(kga[xk][yk]&&kgb[xk][yk]){event3=true;}//if kingline can be block by allied Pieces
            }
        }

        if(kingcantmove(wx,wy)){event1=true;}//king cant move
        if(!LineofFire(tx,ty) ){event2=true;}//king is in direct line of fire

        return event1&&event2&&event3;//all condition meet
    }



    public boolean isCheck(Piece king){//is king in 'check'
        boolean cflag=false;//'check' flag
        boolean[][] trueBox=new boolean[8][8];//temp variable
        Piece Kings=Piece.OUT;//initialize temp variable to hold king
        Piece[] Player=new Piece[5];//initialize array to get enemy pieces
        int kingx=-1;//initialize variable for king x position
        int kingy=-1;//initialize variable for king y position
        int pawnd=0;//initialize variable for pawn direction
        if(king==Piece.WHITE_KING){//check which king it is
            Kings=Piece.WHITE_KING;//save the targeted king
            Player[0]=Piece.BLACK_PAWN;//save the enemy pieces
            Player[1]=Piece.BLACK_ROOK;
            Player[2]=Piece.BLACK_KNIGHT;
            Player[3]=Piece.BLACK_BISHOP;
            Player[4]=Piece.BLACK_QUEEN;//king cannot checked enemy
            pawnd=1;//black pawn direction is +1
        }else{Kings=Piece.BLACK_KING;//save the targeted king
            Player[0]=Piece.WHITE_PAWN;//save the enemy pieces
            Player[1]=Piece.WHITE_ROOK;
            Player[2]=Piece.WHITE_KNIGHT;
            Player[3]=Piece.WHITE_BISHOP;
            Player[4]=Piece.WHITE_QUEEN;
            pawnd=-1;//white pawn direction is -1
        }

        for(int kx=0;kx<8;kx++){
            for(int ky=0;ky<8;ky++){//check every cell
                if(getPiece(kx,ky)==Kings){kingx=kx;kingy=ky;continue;}//get king location then check other cell
                if(getPiece(kx, ky) == Player[0]){trueBox=syncBox(trueBox, pawnkill(kx,ky,pawnd));continue;}
                for(int kk=1;kk<5;kk++) {//look for player piece
                    if (getPiece(kx, ky) == Player[kk]) {//if it player pieces
                        trueBox = syncBox(trueBox, move(Player[kk], kx, ky));//inside generate get line of sight for move
                        break;//out of this cell,check other cell
                    }
                }
            }
        }
        if(getPiece(kingx,kingy)!=Piece.OUT) {//if king position is not default or if king is found
            if (trueBox[kingx][kingy]){ cflag=true;}//if king is in enemy sight
        }
        return cflag;//return check flag
    }


    private boolean[][] rookmove(int cx,int cy){
        boolean[][] staterook = new boolean[8][8];//temp variable to store possible move
        int fx = cx;//variable to store temporary x position
        int fy = cy;//variable to store temporary x position
        for (int dx = -1; dx <= 1; dx++) {//every direction
            for (int dy = -1; dy <= 1; dy++) {
                if (((dx * dx) + (dy * dy)) == 1) {//direction condition for horizontal and vertical
                    while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {//if next cell of certain direction is empty
                        staterook[fx + dx][fy + dy] = true;//it is possible move
                        fx += dx;//go to next cell of certain x direction
                        fy += dy;//go to next cell of certain y direction
                    }//after out of the loop
                    if (oppose(cx, cy, fx + dx, fy + dy)) {//if the next cell in direction is enemy of original cell
                        staterook[fx + dx][fy + dy] = true;//it is possible move
                    }
                    fx = cx;//revert to original x position to check other direction
                    fy = cy;//revert to original y position to check other direction
                }
            }
        }return staterook;//rook normal move possibility
    }
    private boolean[][] knightmove(int cx,int cy){//knight own unique move
        boolean[][] stateknight = new boolean[8][8];//temp variable to store possible move
        if(oppose(cx,cy,cx+1,cy+2) || (getPiece(cx+1,cy+2)==Piece.EMPTY)){//if is it enemy or empty
            stateknight[cx+1][cy+2]=true;//move is possible
        } if(oppose(cx,cy,cx+1,cy-2) || (getPiece(cx+1,cy-2)==Piece.EMPTY)){//if is it enemy or empty
            stateknight[cx+1][cy-2]=true;//move is possible
        } if(oppose(cx,cy,cx+2,cy+1) || (getPiece(cx+2,cy+1)==Piece.EMPTY)){//if is it enemy or empty
            stateknight[cx+2][cy+1]=true;//move is possible
        } if(oppose(cx,cy,cx+2,cy-1) || (getPiece(cx+2,cy-1)==Piece.EMPTY)){//if is it enemy or empty
            stateknight[cx+2][cy-1]=true;//move is possible
        } if(oppose(cx,cy,cx-1,cy+2) || (getPiece(cx-1,cy+2)==Piece.EMPTY)){//if is it enemy or empty
            stateknight[cx-1][cy+2]=true;//move is possible
        } if(oppose(cx,cy,cx-1,cy-2) || (getPiece(cx-1,cy-2)==Piece.EMPTY)){//if is it enemy or empty
            stateknight[cx-1][cy-2]=true;//move is possible
        } if(oppose(cx,cy,cx-2,cy+1) || (getPiece(cx-2,cy+1)==Piece.EMPTY)){//if is it enemy or empty
            stateknight[cx-2][cy+1]=true;//move is possible
        } if(oppose(cx,cy,cx-2,cy-1) || (getPiece(cx-2,cy-1)==Piece.EMPTY)){//if is it enemy or empty
            stateknight[cx-2][cy-1]=true;//move is possible
        }
        return stateknight;//knight normal move possibility
    }
    private boolean[][] bishopmove(int cx,int cy){
        boolean[][] statebishop = new boolean[8][8];//temp variable to store possible move
        int fx = cx;//variable to store temporary x position
        int fy = cy;//variable to store temporary x position
        for (int dx = -1; dx <= 1; dx++) {//every direction
            for (int dy = -1; dy <= 1; dy++) {
                if (((dx * dx) + (dy * dy)) == 2) {//direction condition for diagonal
                    while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {//if next cell of certain direction is empty
                        statebishop[fx + dx][fy + dy] = true;//it is possible move
                        fx += dx;//go to next cell of certain x direction
                        fy += dy;//go to next cell of certain y direction
                    }//after out of the loop
                    if (oppose(cx, cy, fx + dx, fy + dy)) {//if the next cell in direction is enemy of original cell
                        statebishop[fx + dx][fy + dy] = true;//it is possible move
                    }
                    fx = cx;//revert to original x position to check other direction
                    fy = cy;//revert to original y position to check other direction
                }
            }
        }
        return statebishop;//bishop normal move possibility
    }
    private boolean[][] queenmove(int cx,int cy){//queen alldirectional move
        boolean[][] statequeen = new boolean[8][8];//temp variable to store possible move
        int fx = cx;//variable to store temporary x position
        int fy = cy;//variable to store temporary x position
        for (int dx = -1; dx <= 1; dx++) {//every direction
            for (int dy = -1; dy <= 1; dy++) {
                if (!(dx==0 && dy==0)) {//direction condition for horizontal, vertical and diagonal
                    while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {//if next cell of certain direction is empty
                        statequeen[fx + dx][fy + dy] = true;//it is possible move
                        fx += dx;//go to next cell of certain x direction
                        fy += dy;//go to next cell of certain y direction
                    }//after out of the loop
                    if (oppose(cx, cy, fx + dx, fy + dy)) {//if the next cell in direction is enemy of original cell
                        statequeen[fx + dx][fy + dy] = true;//it is possible move
                    }
                    fx = cx;//revert to original x position to check other direction
                    fy = cy;//revert to original y position to check other direction
                }
            }
        }
        return statequeen;//queen normal move possibility
    }
    private boolean[][] kingmove(int cx,int cy){//king normal move
        boolean[][] stateking = new boolean[8][8];//temp variable to store possible move
        if(oppose(cx,cy,cx-1,cy-1) || (getPiece(cx-1,cy-1)==Piece.EMPTY)){//if is it enemy or empty
            stateking[cx-1][cy-1]=true;//move is possible
        } if(oppose(cx,cy,cx-1,cy) || (getPiece(cx-1,cy)==Piece.EMPTY)){//if is it enemy or empty
            stateking[cx-1][cy]=true;//move is possible
        } if(oppose(cx,cy,cx-1,cy+1) || (getPiece(cx-1,cy+1)==Piece.EMPTY)){//if is it enemy or empty
            stateking[cx-1][cy+1]=true;//move is possible
        } if(oppose(cx,cy,cx,cy-1) || (getPiece(cx,cy-1)==Piece.EMPTY)){//if is it enemy or empty
            stateking[cx][cy-1]=true;//move is possible
        } if(oppose(cx,cy,cx,cy+1) || (getPiece(cx,cy+1)==Piece.EMPTY)){//if is it enemy or empty
            stateking[cx][cy+1]=true;//move is possible
        } if(oppose(cx,cy,cx+1,cy-1) || (getPiece(cx+1,cy-1)==Piece.EMPTY)){//if is it enemy or empty
            stateking[cx+1][cy-1]=true;//move is possible
        } if(oppose(cx,cy,cx+1,cy) || (getPiece(cx+1,cy)==Piece.EMPTY)){//if is it enemy or empty
            stateking[cx+1][cy]=true;//move is possible
        } if(oppose(cx,cy,cx+1,cy+1) || (getPiece(cx+1,cy+1)==Piece.EMPTY)){//if is it enemy or empty
            stateking[cx+1][cy+1]=true;//move is possible
        }
        return stateking;//king normal move possibility
    }



    public boolean[][] move(Piece piece,int cx,int cy){//universal move for every piece
        boolean[][] state = new boolean[8][8];//variable to store possible move
        switch (piece){//what is the piece?
            case WHITE_PAWN: {//pawn normal move and pawn possible move if there enemy sighted
                state=syncBox(pawnmove(cx,cy,-1),pawnkill(cx,cy,-1));//pawn kill, -1 in Y axis direction is moving toward up
                break;}
            case BLACK_PAWN: {//pawn normal move and pawn possible move if there enemy sighted
                state=syncBox(pawnmove(cx,cy,1),pawnkill(cx,cy,1));//pawn kill, +1 in Y axis direction is moving toward down
                break; }
            case WHITE_ROOK: {state=rookmove(cx,cy);break;}//rook move vertical and horizontal
            case BLACK_ROOK: {state=rookmove(cx,cy);break;}//rook move vertical and horizontal
            case WHITE_KNIGHT:{state=knightmove(cx,cy);break;}//knight own unique move
            case BLACK_KNIGHT:{state=knightmove(cx,cy);break;}//knight own unique move
            case WHITE_BISHOP:{state=bishopmove(cx,cy);break;}//bishop diagonal move
            case BLACK_BISHOP:{state=bishopmove(cx,cy);break;}//bishop diagonal move
            case WHITE_QUEEN:{state=queenmove(cx,cy);break;}//queen all directional move
            case BLACK_QUEEN:{state=queenmove(cx,cy);break;}//queen all directional move
            case WHITE_KING:{state=kingmove(cx,cy);//king normal move
                boolean[][] temp=getLine(cx,cy);//get enemy sight based on what piece is in(cx,cy)
                for(int px=0;px<8;px++){
                    for(int py=0;py<8;py++){//if king move is in the enemy sight
                        if(state[px][py] && temp[px][py]){state[px][py]=false;}//disable that move,cause it is suicidal move
                    }
                }break;}
            case BLACK_KING:{state=kingmove(cx,cy);//king normal move
                boolean[][] temp=getLine(cx,cy);//get enemy sight based on what piece is in(cx,cy)
                for(int px=0;px<8;px++){
                    for(int py=0;py<8;py++){//if king move is in the enemy sight
                        if(state[px][py] && temp[px][py]){state[px][py]=false;}//disable that move,cause it is suicidal move
                    }
                }break;}
        }

        return state;//possible movement of piece in(cx,cy)
    }



    private boolean oppose(int x0,int y0,int x1,int y1){//passing x,y of 2 position
        boolean opposing=false;//temp variable for return, default false, not opposing
        int a=0;//temp value of 1st position
        int b=0;//temp value of 2nd position

        switch (getPiece(x0,y0)){//position 1
            case WHITE_PAWN: {a=1;break;}//is White
            case WHITE_ROOK: {a=1;break;}//is White
            case WHITE_KNIGHT: {a=1;break;}//is White
            case WHITE_BISHOP: {a=1;break;}//is White
            case WHITE_KING: {a=1;break;}//is White
            case WHITE_QUEEN: {a=1;break;}//is White
            case BLACK_PAWN: {a=2;break;}//is Black
            case BLACK_ROOK: {a=2;break;}//is Black
            case BLACK_KNIGHT: {a=2;break;}//is Black
            case BLACK_BISHOP: {a=2;break;}//is Black
            case BLACK_KING: {a=2;break;}//is Black
            case BLACK_QUEEN: {a=2;break;}//is Black
            case EMPTY:{a=3;break;}//irregular
            case OUT:{a=3;break;}//irregular
        }

        switch (getPiece(x1,y1)){//position 2
            case WHITE_PAWN: {b=1;break;}//is White
            case WHITE_ROOK: {b=1;break;}//is White
            case WHITE_KNIGHT: {b=1;break;}//is White
            case WHITE_BISHOP: {b=1;break;}//is White
            case WHITE_KING: {b=1;break;}//is White
            case WHITE_QUEEN: {b=1;break;}//is White
            case BLACK_PAWN: {b=2;break;}//is Black
            case BLACK_ROOK: {b=2;break;}//is Black
            case BLACK_KNIGHT: {b=2;break;}//is Black
            case BLACK_BISHOP: {b=2;break;}//is Black
            case BLACK_KING: {b=2;break;}//is Black
            case BLACK_QUEEN: {b=2;break;}//is Black
            case EMPTY:{b=3;break;}//irregular
            case OUT:{b=3;break;}//irregular
        }
        if((a+b)==3) {//if 1+2 or 2+1 ,white or black
            if (a != b) {//if 1!=2 or 2!=1
                opposing = true;//opposing
            }
        }//if a==b then either white and white or black or black
        //if a or b is irregular like empty or out-of-bound, then it is not opposing, which is default value
        return opposing;//return relationship between 2 position
    }


}