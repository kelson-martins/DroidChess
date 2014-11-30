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

    public boolean isCheck(){
        boolean cflag=false;
        boolean[][] trueBox=new boolean[8][8];
        Object Kings=Piece.OUT;
        Object[] Player=new Object[6];
        int kingx=-1;
        int kingy=-1;
        int pawnd=0;
        if(true){//need current player
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
        if(getPiece(kingx,kingy)!=Piece.OUT) {
            if (trueBox[kingx][kingy])
            { cflag=true;}
        }
        return cflag;
    }



    public boolean[][] move(Object piece,int cx,int cy){
        boolean[][] state = new boolean[8][8];
        //int temp=0;

        switch (ChessView.board[cx][cy]){//can change by piece, or getpiece outside?
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
