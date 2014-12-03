package droidchess.kelson.com.droidchess;

import java.util.Objects;

/**
 * Created by Kelson on 11/21/2014.
 */
public class Controller {

    private Piece getPiece(int cx, int cy) {//get pieces of the particular cell on the board
        // TODO Auto-generated method stub

        Piece[][] board=new Piece[8][8];
        Piece piece;//temp variable to store cell piece
        try {
            piece = board[cx][cy];//try to get this particular cell
        } catch (ArrayIndexOutOfBoundsException e) {
            piece = Piece.OUT;//if the cell is not on board, out of bound
        }

        return piece;//give back the piece
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

public void pawnkill(boolean[][] stp,int px,int py,int pd){
    if(oppose(px,py,px+1,py+pd)){
        stp[px+1][px+pd]=true;
    }
    if(oppose(px,py,px-1,py+pd)){
        stp[px-1][px+pd]=true;
    }

}

    public void pawnmove(boolean[][] stp,int px,int py,int pd){
           int con=0;
        if(pd==-1){
            con=6;
        }else if(pd==1){con=1;}

        if (py==con) {
            if(getPiece(px,py+pd+pd)==Piece.EMPTY){
                stp[px][py+pd+pd]=true;
            }
        }
        if(getPiece(px,py+pd)==Piece.EMPTY){
            stp[px][py+pd]=true;
        }


    }

public void remaining(){
    //int[] white=new int [5];
    //int[] black=new int [5];
    boolean[][] true_state=new boolean[8][8];
    int whiteP=1;//king
    int blackP=1;//king
    int wx=-1;
    int wy=-1;
    int bx=-1;
    int by=-1;
    boolean event1=false;
    boolean event2=false;

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
        }
        event1=true;
    }//white lacks check piece
    if(blackP<3){event2=true;}//black lacks check piece






}

    public void checkmate(){
        //is called after check flag is true...
        //forget that...
        //2nd click, then check for checked() if true then next swap player
        // if checked() true, king attempt move and isCheck()?
        // well check king surounding ok found solution.
        //get box of truth...then get king surrounding?no...still not done
        //need attempt move? ok found other solution...
        //checked first...save xy, check king surrounding with box of truth
        //if king surrounding is all true, get opposing box of truth...
        //if xy saved is true on box of truth opposing then hope...
        //black box, white box, king position, piece position, called if checked()
        //make game record? from arraylist? nope just kidding

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

    public boolean[][] isCheck(Piece king){//called after king move
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
                if(getPiece(kx, ky) == Player[0]){pawnkill(trueBox,kx,ky,pawnd);break;}
                for(int kk=1;kk<6;kk++) {//look for player piece
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



    public boolean[][] move(Piece piece,int cx,int cy){
        boolean[][] state = new boolean[8][8];
        //int temp=0;

        switch (piece){//can change by piece, or getpiece outside?
            case WHITE_PAWN: {
                //pawn
                //pawn(x,y,d){
                // check flag 1st move
            //    if (cy==6) {
             //       if(getPiece(cx,cy-2)==Piece.EMPTY){state[cx][cy-2]=true;}
            //    }
            //        if(getPiece(cx,cy-1)==Piece.EMPTY){state[cx][cy-1]=true;}
            //        if(oppose(cx,cy,cx-1,cy-1)){state[cx-1][cy-1]=true;}
            //    if(oppose(cx,cy,cx+1,cy-1)){state[cx+1][cy-1]=true;}
                pawnmove(state,cx,cy,-1);
                pawnkill(state,cx,cy,-1);




                // get piece(x,y+d)==empty	>true on state[x,y+d]
                // 1st move && get piece(x,y+2d)==empty >true on state[x,y+2d]
                // get piece(x+1,y+d)==opposing >true on state[x+1,y+d]
                // get piece(x-1,y+d)==opposing >true on state[x-1,y+d]

                // }
            } break;
            case BLACK_PAWN: {
                //pawn
                //pawn(x,y,d){
                // check flag 1st move
                pawnmove(state,cx,cy,1);
                pawnkill(state,cx,cy,1);





                // get piece(x,y+d)==empty	>true on state[x,y+d]
                // 1st move && get piece(x,y+2d)==empty >true on state[x,y+2d]
                // get piece(x+1,y+d)==opposing >true on state[x+1,y+d]
                // get piece(x-1,y+d)==opposing >true on state[x-1,y+d]

                // }
            } break;
        case WHITE_ROOK: {
            //rook
            //private void rook(int x,int y){
            //int cx=x;
            //int cy=y;
            int fx = cx;
            int fy = cy;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (((dx * dx) + (dy * dy)) == 1) {
                        while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                            state[fx + dx][fy + dy] = true;
                            fx += dx;
                            fy += dy;
                        }
                        if (oppose(fx, fy, fx + dx, fy + dy)) {
                            state[fx + dx][fy + dy] = true;
                        }
                        fx = cx;
                        fy = cy;
                    }
                }
            }

        //for(int dx=-1;dx<=1;dx++){
         //   for(int dy=-1;dy<=1;dy++){
           //     if(((dx*dx)+(dy*dy))==1 ){
             //       while(getPiece(cx+dx,cy+dy)==empty){
               //         state[cx+dx][cy+dy]=true;cx+=dx;cy+=dy;
                 //   }if(getPiece(cx+dx,cy+dy)==opposing){state[cx+dx][cy+dy]=true;}

                   // cx=x;
                    //cy=y;

           //     }
         //   }
      //  }
   // }
        break;}
            case BLACK_ROOK: {
                //rook
                //private void rook(int x,int y){
                //int cx=x;
                //int cy=y;
                int fx = cx;
                int fy = cy;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (((dx * dx) + (dy * dy)) == 1) {
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                                state[fx + dx][fy + dy] = true;
                                fx += dx;
                                fy += dy;
                            }
                            if (oppose(fx, fy, fx + dx, fy + dy)) {
                                state[fx + dx][fy + dy] = true;
                            }
                            fx = cx;
                            fy = cy;
                        }
                    }
                }

            //for(int dx=-1;dx<=1;dx++){
            //   for(int dy=-1;dy<=1;dy++){
            //     if(((dx*dx)+(dy*dy))==1 ){
            //       while(getPiece(cx+dx,cy+dy)==empty){
            //         state[cx+dx][cy+dy]=true;cx+=dx;cy+=dy;
            //   }if(getPiece(cx+dx,cy+dy)==opposing){state[cx+dx][cy+dy]=true;}

            // cx=x;
            //cy=y;

            //     }
            //   }
            //  }
            // }
            break;}
        case WHITE_KNIGHT:{//knight
        //knight(x,y){//no check blocking
        if(oppose(cx,cy,cx+1,cy+2) || (getPiece(cx+1,cy+2)==Piece.EMPTY)){
            state[cx+1][cy+2]=true;//    get piece(x+1,y+2)==(opposing||empty)  >> true on state[x+1][y+2]
        }else if(oppose(cx,cy,cx+1,cy-2) || (getPiece(cx+1,cy-2)==Piece.EMPTY)){
            state[cx+1][cy-2]=true;//     get piece(x+1,y-2)==(opposing||empty)  >> true on state[x+1][y-2]
        }else if(oppose(cx,cy,cx+2,cy+1) || (getPiece(cx+2,cy+1)==Piece.EMPTY)){
            state[cx+2][cy+1]=true;//    get piece(x+2,y+1)==(opposing||empty)  >> true on state[x+2][y+1]
        }else if(oppose(cx,cy,cx+2,cy-1) || (getPiece(cx+2,cy-1)==Piece.EMPTY)){
            state[cx+2][cy-1]=true;//    get piece(x+2,y-1)==(opposing||empty)  >> true on state[x+2][y-1]
        }else if(oppose(cx,cy,cx-1,cy+2) || (getPiece(cx-1,cy+2)==Piece.EMPTY)){
            state[cx-1][cy+2]=true;//    get piece(x-1,y+2)==(opposing||empty)  >> true on state[x-1][y+2]
        }else if(oppose(cx,cy,cx-1,cy-2) || (getPiece(cx-1,cy-2)==Piece.EMPTY)){
            state[cx-1][cy-2]=true;//     get piece(x-1,y-2)==(opposing||empty)  >> true on state[x-1][y-2]
        }else if(oppose(cx,cy,cx-2,cy+1) || (getPiece(cx-2,cy+1)==Piece.EMPTY)){
            state[cx-2][cy+1]=true;//     get piece(x-2,y+1)==(opposing||empty)  >> true on state[x-2][y+1]
        }else if(oppose(cx,cy,cx-2,cy-1) || (getPiece(cx-2,cy-1)==Piece.EMPTY)){
            state[cx-2][cy-1]=true;//     get piece(x-2,y-1)==(opposing||empty)  >> true on state[x-2][y-1]
        }

       // }


        break;}
            case BLACK_KNIGHT:{//knight
                //knight(x,y){//no check blocking
                if(oppose(cx,cy,cx+1,cy+2) || (getPiece(cx+1,cy+2)==Piece.EMPTY)){
                    state[cx+1][cy+2]=true;//    get piece(x+1,y+2)==(opposing||empty)  >> true on state[x+1][y+2]
                }else if(oppose(cx,cy,cx+1,cy-2) || (getPiece(cx+1,cy-2)==Piece.EMPTY)){
                    state[cx+1][cy-2]=true;//     get piece(x+1,y-2)==(opposing||empty)  >> true on state[x+1][y-2]
                }else if(oppose(cx,cy,cx+2,cy+1) || (getPiece(cx+2,cy+1)==Piece.EMPTY)){
                    state[cx+2][cy+1]=true;//    get piece(x+2,y+1)==(opposing||empty)  >> true on state[x+2][y+1]
                }else if(oppose(cx,cy,cx+2,cy-1) || (getPiece(cx+2,cy-1)==Piece.EMPTY)){
                    state[cx+2][cy-1]=true;//    get piece(x+2,y-1)==(opposing||empty)  >> true on state[x+2][y-1]
                }else if(oppose(cx,cy,cx-1,cy+2) || (getPiece(cx-1,cy+2)==Piece.EMPTY)){
                    state[cx-1][cy+2]=true;//    get piece(x-1,y+2)==(opposing||empty)  >> true on state[x-1][y+2]
                }else if(oppose(cx,cy,cx-1,cy-2) || (getPiece(cx-1,cy-2)==Piece.EMPTY)){
                    state[cx-1][cy-2]=true;//     get piece(x-1,y-2)==(opposing||empty)  >> true on state[x-1][y-2]
                }else if(oppose(cx,cy,cx-2,cy+1) || (getPiece(cx-2,cy+1)==Piece.EMPTY)){
                    state[cx-2][cy+1]=true;//     get piece(x-2,y+1)==(opposing||empty)  >> true on state[x-2][y+1]
                }else if(oppose(cx,cy,cx-2,cy-1) || (getPiece(cx-2,cy-1)==Piece.EMPTY)){
                    state[cx-2][cy-1]=true;//     get piece(x-2,y-1)==(opposing||empty)  >> true on state[x-2][y-1]
                }

                // }


                break;}
        case WHITE_BISHOP:{ //bishop
        //private void bishop(int x,int y){
      //  int cx=x;
      //  int cy=y;
            int fx = cx;
            int fy = cy;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (((dx * dx) + (dy * dy)) == 2) {
                        while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                            state[fx + dx][fy + dy] = true;
                            fx += dx;
                            fy += dy;
                        }
                        if (oppose(fx, fy, fx + dx, fy + dy)) {
                            state[fx + dx][fy + dy] = true;
                        }
                        fx = cx;
                        fy = cy;
                    }
                }
            }
      //  for(int dx=-1;dx<=1;dx++){
      //      for(int dy=-1;dy<=1;dy++){
       //         if(((dx*dx)+(dy*dy))==2 ){
       //             while(getPiece(cx+dx,cy+dy)==empty){
       //                 state[cx+dx][cy+dy]=true;cx+=dx;cy+=dy;
        //            }if(getPiece(cx+dx,cy+dy)==opposing){state[cx+dx][cy+dy]=true;}

        //            cx=x;
         //           cy=y;

         //       }
         //   }
      //  }
  //  }

    break;}
            case BLACK_BISHOP:{ //bishop
                //private void bishop(int x,int y){
                //  int cx=x;
                //  int cy=y;
                int fx = cx;
                int fy = cy;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (((dx * dx) + (dy * dy)) == 2) {
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                                state[fx + dx][fy + dy] = true;
                                fx += dx;
                                fy += dy;
                            }
                            if (oppose(fx, fy, fx + dx, fy + dy)) {
                                state[fx + dx][fy + dy] = true;
                            }
                            fx = cx;
                            fy = cy;
                        }
                    }
                }
                //  for(int dx=-1;dx<=1;dx++){
                //      for(int dy=-1;dy<=1;dy++){
                //         if(((dx*dx)+(dy*dy))==2 ){
                //             while(getPiece(cx+dx,cy+dy)==empty){
                //                 state[cx+dx][cy+dy]=true;cx+=dx;cy+=dy;
                //            }if(getPiece(cx+dx,cy+dy)==opposing){state[cx+dx][cy+dy]=true;}

                //            cx=x;
                //           cy=y;

                //       }
                //   }
                //  }
                //  }

                break;}
        case WHITE_QUEEN:{//queen
        //private void queen(int x,int y){
     //   int cx=x;
     //   int cy=y;
            int fx = cx;
            int fy = cy;
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    if (!(dx==0 && dy==0)) {
                        while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                            state[fx + dx][fy + dy] = true;
                            fx += dx;
                            fy += dy;
                        }
                        if (oppose(fx, fy, fx + dx, fy + dy)) {
                            state[fx + dx][fy + dy] = true;
                        }
                        fx = cx;
                        fy = cy;
                    }
                }
            }
     //   for(int dx=-1;dx<=1;dx++){
     //       for(int dy=-1;dy<=1;dy++){
        //        if(!(dx==0 && dy==0)){
      //              while(getPiece(cx+dx,cy+dy)==empty){
         //               state[cx+dx][cy+dy]=true;cx+=dx;cy+=dy;
         //           }if(getPiece(cx+dx,cy+dy)==opposing){state[cx+dx][cy+dy]=true;}

          //          cx=x;
          //          cy=y;

         //       }
         //   }
      //  }
 //   }
        break;}
            case BLACK_QUEEN:{//queen
                //private void queen(int x,int y){
                //   int cx=x;
                //   int cy=y;
                int fx = cx;
                int fy = cy;
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        if (!(dx==0 && dy==0)) {
                            while (getPiece(fx + dx, fy + dy) == Piece.EMPTY) {
                                state[fx + dx][fy + dy] = true;
                                fx += dx;
                                fy += dy;
                            }
                            if (oppose(fx, fy, fx + dx, fy + dy)) {
                                state[fx + dx][fy + dy] = true;
                            }
                            fx = cx;
                            fy = cy;
                        }
                    }
                }
                //   for(int dx=-1;dx<=1;dx++){
                //       for(int dy=-1;dy<=1;dy++){
                //        if(!(dx==0 && dy==0)){
                //              while(getPiece(cx+dx,cy+dy)==empty){
                //               state[cx+dx][cy+dy]=true;cx+=dx;cy+=dy;
                //           }if(getPiece(cx+dx,cy+dy)==opposing){state[cx+dx][cy+dy]=true;}

                //          cx=x;
                //          cy=y;

                //       }
                //   }
                //  }
                //   }
                break;}
        case WHITE_KING:{ //king
        //king(x,y){
            if(oppose(cx,cy,cx-1,cy-1) || (getPiece(cx-1,cy-1)==Piece.EMPTY)){
                state[cx-1][cy-1]=true;//    get piece(x-1,y-1) ==(opposing||empty) >> true on state[x-1][y-1]
            }else if(oppose(cx,cy,cx-1,cy) || (getPiece(cx-1,cy)==Piece.EMPTY)){
                state[cx-1][cy]=true;//   get piece(x-1,y)==(opposing||empty) >> true on state[x-1][y]
            }else if(oppose(cx,cy,cx-1,cy+1) || (getPiece(cx-1,cy+1)==Piece.EMPTY)){
                state[cx-1][cy+1]=true;//   get piece(x-1,y+1)==(opposing||empty) >> true on state[x-1][y+1]
            }else if(oppose(cx,cy,cx,cy-1) || (getPiece(cx,cy-1)==Piece.EMPTY)){
                state[cx][cy-1]=true;//  get piece(x,y-1)==(opposing||empty) >> true on state[x][y-1]
            }else if(oppose(cx,cy,cx,cy+1) || (getPiece(cx,cy+1)==Piece.EMPTY)){
                state[cx][cy+1]=true;//  get piece(x,y+1)==(opposing||empty) >> true on state[x][y+1]
            }else if(oppose(cx,cy,cx+1,cy-1) || (getPiece(cx+1,cy-1)==Piece.EMPTY)){
                state[cx+1][cy-1]=true;// get piece(x+1,y-1)==(opposing||empty) >> true on state[x+1][y-1]
            }else if(oppose(cx,cy,cx+1,cy) || (getPiece(cx+1,cy)==Piece.EMPTY)){
                state[cx+1][cy]=true;// get piece(x+1,y)==(opposing||empty) >> true on state[x+1][y]
            }else if(oppose(cx,cy,cx+1,cy+1) || (getPiece(cx+1,cy+1)==Piece.EMPTY)){
                state[cx+1][cy+1]=true;//  get piece(x+1,y+1)==(opposing||empty) >> true on state[x+1][y+1]
            }
          //  //get piece(x,y)//king it self
      // }
        break;}
            case BLACK_KING:{ //king
                //king(x,y){
                if(oppose(cx,cy,cx-1,cy-1) || (getPiece(cx-1,cy-1)==Piece.EMPTY)){
                    state[cx-1][cy-1]=true;//    get piece(x-1,y-1) ==(opposing||empty) >> true on state[x-1][y-1]
                }else if(oppose(cx,cy,cx-1,cy) || (getPiece(cx-1,cy)==Piece.EMPTY)){
                    state[cx-1][cy]=true;//   get piece(x-1,y)==(opposing||empty) >> true on state[x-1][y]
                }else if(oppose(cx,cy,cx-1,cy+1) || (getPiece(cx-1,cy+1)==Piece.EMPTY)){
                    state[cx-1][cy+1]=true;//   get piece(x-1,y+1)==(opposing||empty) >> true on state[x-1][y+1]
                }else if(oppose(cx,cy,cx,cy-1) || (getPiece(cx,cy-1)==Piece.EMPTY)){
                    state[cx][cy-1]=true;//  get piece(x,y-1)==(opposing||empty) >> true on state[x][y-1]
                }else if(oppose(cx,cy,cx,cy+1) || (getPiece(cx,cy+1)==Piece.EMPTY)){
                    state[cx][cy+1]=true;//  get piece(x,y+1)==(opposing||empty) >> true on state[x][y+1]
                }else if(oppose(cx,cy,cx+1,cy-1) || (getPiece(cx+1,cy-1)==Piece.EMPTY)){
                    state[cx+1][cy-1]=true;// get piece(x+1,y-1)==(opposing||empty) >> true on state[x+1][y-1]
                }else if(oppose(cx,cy,cx+1,cy) || (getPiece(cx+1,cy)==Piece.EMPTY)){
                    state[cx+1][cy]=true;// get piece(x+1,y)==(opposing||empty) >> true on state[x+1][y]
                }else if(oppose(cx,cy,cx+1,cy+1) || (getPiece(cx+1,cy+1)==Piece.EMPTY)){
                    state[cx+1][cy+1]=true;//  get piece(x+1,y+1)==(opposing||empty) >> true on state[x+1][y+1]
                }
                //  //get piece(x,y)//king it self
                // }
                break;}
        }











        return state;
    }



    private boolean oppose(int x0,int y0,int x1,int y1){
    boolean opposing=false;
    int a=0;
    int b=0;

    switch (ChessView.board[x0][y0]){
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
        case BLACK_QUEEN: {a=2;break;}}

    switch (ChessView.board[x1][y1]){
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
        case BLACK_QUEEN: {b=2;break;}}

    if(a!=b){
        opposing=true;
    }

    return opposing;
}


}
