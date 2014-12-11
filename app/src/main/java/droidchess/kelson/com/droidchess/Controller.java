package droidchess.kelson.com.droidchess;

import java.util.Objects;

/**
 * Created by Kelson on 11/21/2014.
 */
public class Controller {

    private Piece getPiece(int cx, int cy) {//get pieces of the particular cell on the board
        // TODO Auto-generated method stub

        //Piece[][] board=new Piece[8][8];
        Piece piece;//temp variable to store cell piece
        try {
            piece = ChessView.board[cx][cy];//try to get this particular cell
        } catch (ArrayIndexOutOfBoundsException e) {
            piece = Piece.OUT;//if the cell is not on board, out of bound
        }

        return piece;//give back the piece
    }

    private Boolean getState(boolean[][] top, int cx, int cy) {//get state of the particular cell on the board
        // TODO Auto-generated method stub
        Boolean cannotmove=true;//temp variable to store cell piece
        try {
            cannotmove = top[cx][cy];//try to get this particular cell
        } catch (ArrayIndexOutOfBoundsException e) {
            cannotmove = true;//if the cell is not on board, out of bound
        }

        return cannotmove;//give back the piece
    }

    public boolean cantkingmove(boolean[][] route,int kx,int ky){//worked in stalemate
        boolean movecheck=false;
        int flag=0;
        for(int dx=-1;dx<2;dx++){
            for(int dy=-1;dy<2;dy++){
                if (!(dx==0 && dy==0)) {
                    if(getState(route,(kx+dx),(ky+dy))){
                    flag++;}
                }
            }
        }
        if(flag==8){movecheck=true;}

        return movecheck;
    }

public boolean pawnswap(Piece pawn,int sx,int sy){
    boolean swapflag=false;
    switch (pawn){
        case WHITE_PAWN:{
            if(sy==0){
                swapflag=true;
            }
            break;
        }
        case BLACK_PAWN:{
            if(sy==7){
                swapflag=true;
            }
            break;
        }
    }

    return swapflag;
}


    public boolean[][] syncBox(boolean[][] store,boolean[][] add){
        boolean[][] stored=new boolean[8][8];
        for(int x=0;x<8;x++){
            for(int y=0;y<8;y++){
                if(store[x][y] || add[x][y]){
                    stored[x][y]=true;
                }
            }
        }

        return stored;
    }

    private boolean[][] pawnkill(int px,int py,int pd){boolean[][] stp=new boolean[8][8];
    if(oppose(px,py,px+1,py+pd)){
        stp[px+1][py+pd]=true;
    }
    if(oppose(px,py,px-1,py+pd)){
        stp[px-1][py+pd]=true;
    }
return stp;
}

    private boolean[][] pawnmove(int px,int py,int pd){
        boolean[][] stp=new boolean[8][8];
           int con=0;
        if(pd==-1){
            con=6;
        }else if(pd==1){con=1;}

        if (py==con) {
            if(getPiece(px,py+pd+pd)==Piece.EMPTY){
                if(getPiece(px,py+pd)==Piece.EMPTY){
                stp[px][py+pd+pd]=true;}
            }
        }
        if(getPiece(px,py+pd)==Piece.EMPTY){
            stp[px][py+pd]=true;
        }

        return stp;
    }

public boolean isStalemate(){//called last
    //int[] white=new int [5];
    //int[] black=new int [5];
boolean stalemate=false;
    int whiteP=1;//king
    int blackP=1;//king
    int wx=-1;
    int wy=-1;
    int bx=-1;
    int by=-1;
    boolean event1w=false;
    boolean event2w=false;
    boolean event1b=false;
    boolean event2b=false;


    for(int px=0;px<8;px++){
        for(int py=0;py<8;py++){
            switch(getPiece(px,py)) {
                case WHITE_PAWN:{whiteP+=3;break;}
                case WHITE_ROOK:{whiteP+=3;break;}
                case WHITE_KNIGHT:{whiteP++;break;}//at least 2 or 3
                case WHITE_BISHOP:{whiteP++;break;}//at least 2 or 3
                case WHITE_QUEEN:{whiteP+=3;break;}
                case WHITE_KING:{wx=px;wy=py;break;}//get white king
                case BLACK_PAWN:{blackP+=3;break;}
                case BLACK_ROOK:{blackP+=3;break;}
                case BLACK_KNIGHT:{blackP++;break;}//at least 2 or 3
                case BLACK_BISHOP:{blackP++;break;}//at least 2 or 3
                case BLACK_QUEEN:{blackP+=3;break;}
                case BLACK_KING:{bx=px;by=py;break;}//get black king
            }
        }
    }

    if(whiteP<3){
        if(whiteP==1){
            //check king surrounding
            if(cantkingmove(isCheck(getPiece(wx,wy)),wx,wy)){
                event2w=true;
            }

        }
        event1w=true;
    }//white lacks check piece
    if(blackP<3){
        if(whiteP==1){
        //check king surrounding
        if(cantkingmove(isCheck(getPiece(bx,by)),bx,by)){
            event2b=true;
        }

    }
        event1b=true;}//black lacks check piece


//check and return array
if((event2w&&event2b)||(event1w&&event1b)){
    stalemate=true;
}

return stalemate;

}

    private boolean[][] kingLine(int xl,int yl){//no line can be made by pawn knight king
        boolean[][] checkLine=new boolean[8][8];
        int fx = xl;
        int fy = yl;
        boolean found =false;

        switch (getPiece(xl,yl)) {
            case WHITE_ROOK: {
                fx = xl;
                fy = yl;
                found =false;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if ((((dx * dx) + (dy * dy)) == 1)&& (!found)) {
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                                checkLine[fx + dx][fy + dy] = true;
                                fx += dx;
                                fy += dy;
                            }
                            if (getPiece(fx + dx, fy + dy) == Piece.BLACK_KING) {
                                checkLine[fx + dx][fy + dy] = true;
                                found=true;
                            }else {checkLine=new boolean[8][8];}
                            fx = xl;
                            fy = yl;
                        }
                    }
                }

            }
            case BLACK_ROOK: {
                fx = xl;
                fy = yl;
                found =false;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if ((((dx * dx) + (dy * dy)) == 1)&& (!found)) {
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                                checkLine[fx + dx][fy + dy] = true;
                                fx += dx;
                                fy += dy;
                            }
                            if (getPiece(fx + dx, fy + dy) == Piece.WHITE_KING) {
                                checkLine[fx + dx][fy + dy] = true;
                                found=true;
                            }else {checkLine=new boolean[8][8];}
                            fx = xl;
                            fy = yl;
                        }
                    }
                }

            }
            case WHITE_BISHOP: {
                fx = xl;
                fy = yl;
                found =false;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if ((((dx * dx) + (dy * dy)) == 2)&& (!found)) {
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                                checkLine[fx + dx][fy + dy] = true;
                                fx += dx;
                                fy += dy;
                            }
                            if (getPiece(fx + dx, fy + dy) == Piece.BLACK_KING) {
                                checkLine[fx + dx][fy + dy] = true;
                                found=true;
                            }else {checkLine=new boolean[8][8];}
                            fx = xl;
                            fy = yl;
                        }
                    }
                }

            }
            case BLACK_BISHOP: {
                fx = xl;
                fy = yl;
                found =false;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if ((((dx * dx) + (dy * dy)) == 2)&& (!found)) {
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                                checkLine[fx + dx][fy + dy] = true;
                                fx += dx;
                                fy += dy;
                            }
                            if (getPiece(fx + dx, fy + dy) == Piece.WHITE_KING) {
                                checkLine[fx + dx][fy + dy] = true;
                                found=true;
                            }else {checkLine=new boolean[8][8];}
                            fx = xl;
                            fy = yl;
                        }
                    }
                }

            }
            case WHITE_QUEEN: {
                fx = xl;
                fy = yl;
                found =false;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if ((!(dx==0 && dy==0))&& (!found)) {
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                                checkLine[fx + dx][fy + dy] = true;
                                fx += dx;
                                fy += dy;
                            }
                            if (getPiece(fx + dx, fy + dy) == Piece.BLACK_KING) {
                                checkLine[fx + dx][fy + dy] = true;
                                found=true;
                            }else {checkLine=new boolean[8][8];}
                            fx = xl;
                            fy = yl;
                        }
                    }
                }

            }
            case BLACK_QUEEN: {
                fx = xl;
                fy = yl;
                found =false;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if ((!(dx==0 && dy==0))&& (!found)) {
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                                checkLine[fx + dx][fy + dy] = true;
                                fx += dx;
                                fy += dy;
                            }
                            if (getPiece(fx + dx, fy + dy) == Piece.WHITE_KING) {
                                checkLine[fx + dx][fy + dy] = true;
                                found=true;
                            }else {checkLine=new boolean[8][8];}
                            fx = xl;
                            fy = yl;
                        }
                    }
                }

            }
        }
        return checkLine;
    }

    private boolean[][] kingarea(int sx,int sy){
        boolean[][] area=new boolean[3][3];

        for(int d3=-1;d3<2;d3++){
            for(int d4=-1;d4<2;d4++){//outof bound problem
                if(getState(getLine(sx,sy),sx+d3,sy+d4)){area[d3+1][d4+1]=true;}
            }
        }

        return area;
    }

    private boolean[][] getLine(int fx,int fy){
        boolean[][] whiteTarget = new boolean[8][8];
        boolean[][] blackTarget = new boolean[8][8];
        boolean[][] targetLine = new boolean[8][8];

        for(int kx=0;kx<8;kx++){
            for(int ky=0;ky<8;ky++){
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
            }
        }

        if (getPiece(fx,fy).name().contains("WHITE")){
           // if(blackTarget[fx][fy]){inLine=true;}
            targetLine=blackTarget;
        }
        else if (getPiece(fx,fy).name().contains("BLACK")){
           // if(whiteTarget[fx][fy]){inLine=true;}
            targetLine=whiteTarget;
        }
        return targetLine;
    }

    public boolean LineofFire(int fx,int fy){//need to test, passing last selected xy
       // boolean[][] whiteTarget = new boolean[8][8];
       // boolean[][] blackTarget = new boolean[8][8];
        boolean inLine=false;

        if(getLine(fx,fy)[fx][fy]){inLine=true;}

        return inLine;
    }

public boolean Checkmate(Piece kg,int tx,int ty) {//only call if isCheck is true
    boolean event1=false;//king cannot move
    boolean event2=false;//enemy cannot be slain
    boolean event3=false;//enemy sight cannot be block
    boolean[][] kgb=kingLine(tx,ty);//checked line of sight to king
    boolean[][] kga=getLine(tx,ty);//enemy line of sight to this position
    int wx=-1;int wy=-1;

    for (int xk = 0; xk < 8; xk++) {
        for (int yk = 0; yk < 8; yk++) {
            if(getPiece(xk,yk)==kg){wx=xk;wy=yk;}
            if(kga[xk][yk]&&kgb[xk][yk]){event3=true;}
        }
    }

    if(cantkingmove(isCheck(getPiece(wx,wy)),wx,wy)){event1=true;}
    if(!LineofFire(tx,ty) ){event2=true;}

    // kingline tx ty > king borad  ;
return event1&&event2&&event3;
 }

public boolean isCheckmate(Piece kg,int tx,int ty){

        boolean[][] whiteTarget = isCheck(Piece.BLACK_KING);
        boolean[][] blackTarget = isCheck(Piece.WHITE_KING);
        int wx=-1;
        int by=-1;
        int wy=-1;
        int bx=-1;
        boolean event1w=false;
        boolean event2w=false;
        boolean event1b=false;
        boolean event2b=false;

        for (int xk = 0; xk < 8; xk++) {
            for (int yk = 0; yk < 8; yk++) {
                if(getPiece(xk,yk)==Piece.WHITE_KING){wx=xk;wy=yk;continue;}
                if(getPiece(xk,yk)==Piece.BLACK_KING){bx=xk;by=yk;continue;}
            }
        }

        if(Checked(Piece.WHITE_KING,tx,ty)){
            if(cantkingmove(isCheck(Piece.WHITE_KING),wx,wy)){event1w=true;}
            if(!LineofFire(tx,ty) ){event2w=true;}
         }

         if(Checked(Piece.BLACK_KING,tx,ty)){
            if(cantkingmove(isCheck(Piece.BLACK_KING),bx,by)){event1b=true;}
            if(!LineofFire(tx,ty) ){event2b=true;}
        }

        return ((event1w&&event2w)||(event1b&&event2b));



    }

    public boolean Checked(Piece king,int ex,int ey){//called every move?
        boolean checkf=false;
        boolean[][] cst=new boolean[8][8];
        int tempx=-1;
        int tempy=-1;

        cst=move(getPiece(ex,ey),ex,ey);

        while(tempx==-1 &&tempy==-1) {//no need
            for (int xk = 0; xk < 8; xk++) {
                for (int yk = 0; yk < 8; yk++) {
                    if (getPiece(xk, yk) == king) {
                        tempx = xk;
                        tempy = yk;
                        break;//no need 2
                    }
                }
            }
        }


        return cst[tempx][tempy];



    }

    public boolean[][] isCheck(Piece king){//worked in stalemate
        //boolean cflag=false;
        boolean[][] trueBox=new boolean[8][8];
        Piece Kings=Piece.OUT;
        Piece[] Player=new Piece[6];
        int kingx=-1;
        int kingy=-1;
        int pawnd=0;
        if(king==Piece.WHITE_KING){//need current player
            Kings=Piece.WHITE_KING;
            Player[0]=Piece.BLACK_PAWN;
            Player[1]=Piece.BLACK_ROOK;
            Player[2]=Piece.BLACK_KNIGHT;
            Player[3]=Piece.BLACK_BISHOP;
            Player[4]=Piece.BLACK_QUEEN;
            Player[5]=Piece.BLACK_KING;
            pawnd=1;
        }else{Kings=Piece.BLACK_KING;
            Player[0]=Piece.WHITE_PAWN;
            Player[1]=Piece.WHITE_ROOK;
            Player[2]=Piece.WHITE_KNIGHT;
            Player[3]=Piece.WHITE_BISHOP;
            Player[4]=Piece.WHITE_QUEEN;
            Player[5]=Piece.WHITE_KING;
            pawnd=-1;
        }


        //need x,y of last position?
        //or check every pieces?
        //need current player...
        //not certain
        for(int kx=0;kx<8;kx++){
            for(int ky=0;ky<8;ky++){//check every cell
                if(getPiece(kx,ky)==Kings){
                    kingx=kx;kingy=ky;
                    continue;}//get king location then check other cell
                if(getPiece(kx, ky) == Player[0]){trueBox=syncBox(trueBox, pawnkill(kx,ky,pawnd));continue;}
                for(int kk=1;kk<5;kk++) {//look for player piece
                    if (getPiece(kx, ky) == Player[kk]) {//if it player piece
                        trueBox = syncBox(trueBox, move(Player[kk], kx, ky));//inside generate.
                        //get line of sight for move
                        break;//out of this cell,check other cell
                    }

                }

            }
        }
        //if(getPiece(kingx,kingy)!=Piece.OUT) {
        //    if (trueBox[kingx][kingy])
        //    { cflag=true;}
        //}
        return trueBox;
    }

    private boolean[][] rookmove(int cx,int cy){
        boolean[][] staterook = new boolean[8][8];
        int fx = cx;
        int fy = cy;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (((dx * dx) + (dy * dy)) == 1) {
                    while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                        staterook[fx + dx][fy + dy] = true;
                        fx += dx;
                        fy += dy;
                    }
                    if (oppose(cx, cy, fx + dx, fy + dy)) {
                        staterook[fx + dx][fy + dy] = true;
                    }
                    fx = cx;
                    fy = cy;
                }
            }
        }return staterook;
    }
    private boolean[][] knightmove(int cx,int cy){
        boolean[][] stateknight = new boolean[8][8];
        if(oppose(cx,cy,cx+1,cy+2) || (getPiece(cx+1,cy+2)==Piece.EMPTY)){
            stateknight[cx+1][cy+2]=true;//    get piece(x+1,y+2)==(opposing||empty)  >> true on state[x+1][y+2]
        } if(oppose(cx,cy,cx+1,cy-2) || (getPiece(cx+1,cy-2)==Piece.EMPTY)){
            stateknight[cx+1][cy-2]=true;//     get piece(x+1,y-2)==(opposing||empty)  >> true on state[x+1][y-2]
        } if(oppose(cx,cy,cx+2,cy+1) || (getPiece(cx+2,cy+1)==Piece.EMPTY)){
            stateknight[cx+2][cy+1]=true;//    get piece(x+2,y+1)==(opposing||empty)  >> true on state[x+2][y+1]
        } if(oppose(cx,cy,cx+2,cy-1) || (getPiece(cx+2,cy-1)==Piece.EMPTY)){
            stateknight[cx+2][cy-1]=true;//    get piece(x+2,y-1)==(opposing||empty)  >> true on state[x+2][y-1]
        } if(oppose(cx,cy,cx-1,cy+2) || (getPiece(cx-1,cy+2)==Piece.EMPTY)){
            stateknight[cx-1][cy+2]=true;//    get piece(x-1,y+2)==(opposing||empty)  >> true on state[x-1][y+2]
        } if(oppose(cx,cy,cx-1,cy-2) || (getPiece(cx-1,cy-2)==Piece.EMPTY)){
            stateknight[cx-1][cy-2]=true;//     get piece(x-1,y-2)==(opposing||empty)  >> true on state[x-1][y-2]
        } if(oppose(cx,cy,cx-2,cy+1) || (getPiece(cx-2,cy+1)==Piece.EMPTY)){
            stateknight[cx-2][cy+1]=true;//     get piece(x-2,y+1)==(opposing||empty)  >> true on state[x-2][y+1]
        } if(oppose(cx,cy,cx-2,cy-1) || (getPiece(cx-2,cy-1)==Piece.EMPTY)){
            stateknight[cx-2][cy-1]=true;//     get piece(x-2,y-1)==(opposing||empty)  >> true on state[x-2][y-1]
        }
        return stateknight;
    }
    private boolean[][] bishopmove(int cx,int cy){
        boolean[][] statebishop = new boolean[8][8];
        int fx = cx;
        int fy = cy;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (((dx * dx) + (dy * dy)) == 2) {
                    while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                        statebishop[fx + dx][fy + dy] = true;
                        fx += dx;
                        fy += dy;
                    }
                    if (oppose(cx, cy, fx + dx, fy + dy)) {
                        statebishop[fx + dx][fy + dy] = true;
                    }
                    fx = cx;
                    fy = cy;
                }
            }
        }
        return statebishop;
    }
    private boolean[][] queenmove(int cx,int cy){
        boolean[][] statequeen = new boolean[8][8];
        int fx = cx;
        int fy = cy;
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (!(dx==0 && dy==0)) {
                    while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                        statequeen[fx + dx][fy + dy] = true;
                        fx += dx;
                        fy += dy;
                    }
                    if (oppose(cx, cy, fx + dx, fy + dy)) {
                        statequeen[fx + dx][fy + dy] = true;
                    }
                    fx = cx;
                    fy = cy;
                }
            }
        }
        return statequeen;
    }
    private boolean[][] kingmove(int cx,int cy){
        boolean[][] stateking = new boolean[8][8];
        if(oppose(cx,cy,cx-1,cy-1) || (getPiece(cx-1,cy-1)==Piece.EMPTY)){
            stateking[cx-1][cy-1]=true;//    get piece(x-1,y-1) ==(opposing||empty) >> true on state[x-1][y-1]
        } if(oppose(cx,cy,cx-1,cy) || (getPiece(cx-1,cy)==Piece.EMPTY)){
            stateking[cx-1][cy]=true;//   get piece(x-1,y)==(opposing||empty) >> true on state[x-1][y]
        } if(oppose(cx,cy,cx-1,cy+1) || (getPiece(cx-1,cy+1)==Piece.EMPTY)){
            stateking[cx-1][cy+1]=true;//   get piece(x-1,y+1)==(opposing||empty) >> true on state[x-1][y+1]
        } if(oppose(cx,cy,cx,cy-1) || (getPiece(cx,cy-1)==Piece.EMPTY)){
            stateking[cx][cy-1]=true;//  get piece(x,y-1)==(opposing||empty) >> true on state[x][y-1]
        } if(oppose(cx,cy,cx,cy+1) || (getPiece(cx,cy+1)==Piece.EMPTY)){
            stateking[cx][cy+1]=true;//  get piece(x,y+1)==(opposing||empty) >> true on state[x][y+1]
        } if(oppose(cx,cy,cx+1,cy-1) || (getPiece(cx+1,cy-1)==Piece.EMPTY)){
            stateking[cx+1][cy-1]=true;// get piece(x+1,y-1)==(opposing||empty) >> true on state[x+1][y-1]
        } if(oppose(cx,cy,cx+1,cy) || (getPiece(cx+1,cy)==Piece.EMPTY)){
            stateking[cx+1][cy]=true;// get piece(x+1,y)==(opposing||empty) >> true on state[x+1][y]
        } if(oppose(cx,cy,cx+1,cy+1) || (getPiece(cx+1,cy+1)==Piece.EMPTY)){
            stateking[cx+1][cy+1]=true;//  get piece(x+1,y+1)==(opposing||empty) >> true on state[x+1][y+1]
        }
        return stateking;
    }



    public boolean[][] movetest(Piece piece,int cx,int cy){
        boolean[][] state = new boolean[8][8];
        switch (piece){//can change by piece, or getpiece outside?
            case WHITE_PAWN: {

                //state=pawnmove(cx,cy,-1);
               //state=pawnkill(cx,cy,-1);//only pawn kill moves
                state=syncBox(pawnmove(cx,cy,-1),pawnkill(cx,cy,-1));
                break;
            } case BLACK_PAWN: {

               // state=pawnmove(cx,cy,1);
              //state=pawnkill(cx,cy,1);
               // state=syncBox(pawnmove(cx,cy,1),pawnkill(cx,cy,1));
                state=syncBox(pawnmove(cx,cy,1),pawnkill(cx,cy,1));
                break;
            }
        }
            return state;
    }

    public boolean[][] move(Piece piece,int cx,int cy){
        boolean[][] state = new boolean[8][8];


        switch (piece){//can change by piece, or getpiece outside?
            case WHITE_PAWN: {

                state=syncBox(pawnmove(cx,cy,-1),pawnkill(cx,cy,-1));

                break;}
            case BLACK_PAWN: {

                state=syncBox(pawnmove(cx,cy,1),pawnkill(cx,cy,1));

                break; }
        case WHITE_ROOK: {

            state=rookmove(cx,cy);

        break;}
            case BLACK_ROOK: {

                state=rookmove(cx,cy);

            break;}
        case WHITE_KNIGHT:{//knight
        //knight(x,y){//no check blocking
            state=knightmove(cx,cy);

        break;}
            case BLACK_KNIGHT:{//knight
                //knight(x,y){//no check blocking
                state=knightmove(cx,cy);

                break;}
        case WHITE_BISHOP:{ //bishop

            state=bishopmove(cx,cy);

    break;}
            case BLACK_BISHOP:{ //bishop

                state=bishopmove(cx,cy);

                break;}
        case WHITE_QUEEN:{//queen

            state=queenmove(cx,cy);

        break;}
            case BLACK_QUEEN:{//queen

                state=queenmove(cx,cy);

                break;}
        case WHITE_KING:{ //king

            state=kingmove(cx,cy);
            boolean[][] temp=getLine(cx,cy);
            for(int px=0;px<8;px++){
                for(int py=0;py<8;py++){
                  if(state[px][py] && temp[px][py]){state[px][py]=false;}

                }
            }



        break;}
            case BLACK_KING:{ //king

                state=kingmove(cx,cy);
                boolean[][] temp=getLine(cx,cy);
                for(int px=0;px<8;px++){
                    for(int py=0;py<8;py++){
                        if(state[px][py] && temp[px][py]){state[px][py]=false;}

                    }
                }
                break;}
        }

        return state;
    }



    private boolean oppose(int x0,int y0,int x1,int y1){
    boolean opposing=false;
    int a=0;
    int b=0;

    switch (getPiece(x0,y0)){
        case WHITE_PAWN: {a=1;break;}
        case WHITE_ROOK: {a=1;break;}
        case WHITE_KNIGHT: {a=1;break;}
        case WHITE_BISHOP: {a=1;break;}
        case WHITE_KING: {a=1;break;}
        case WHITE_QUEEN: {a=1;break;}
        case BLACK_PAWN: {a=2;break;}
        case BLACK_ROOK: {a=2;break;}
        case BLACK_KNIGHT: {a=2;break;}
        case BLACK_BISHOP: {a=2;break;}
        case BLACK_KING: {a=2;break;}
        case BLACK_QUEEN: {a=2;break;}
        case EMPTY:{a=3;break;}
        case OUT:{a=3;break;}
    }

    switch (getPiece(x1,y1)){
        case WHITE_PAWN: {b=1;break;}
        case WHITE_ROOK: {b=1;break;}
        case WHITE_KNIGHT: {b=1;break;}
        case WHITE_BISHOP: {b=1;break;}
        case WHITE_KING: {b=1;break;}
        case WHITE_QUEEN: {b=1;break;}
        case BLACK_PAWN: {b=2;break;}
        case BLACK_ROOK: {b=2;break;}
        case BLACK_KNIGHT: {b=2;break;}
        case BLACK_BISHOP: {b=2;break;}
        case BLACK_KING: {b=2;break;}
        case BLACK_QUEEN: {b=2;break;}
        case EMPTY:{b=3;break;}
        case OUT:{b=3;break;}
    }
if((a+b)==3) {
    if (a != b) {
        opposing = true;
    }
}
    return opposing;
}


}
