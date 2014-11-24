package droidchess.kelson.com.droidchess;

/**
 * Created by Kelson on 11/21/2014.
 */
public class Controller {

    private int getPiece(int cx, int cy) {//get pieces of the particular cell on the board
        // TODO Auto-generated method stub
        int[][] board=new int[8][8];
        int piece;//temp variable to store cell piece
        try {
            piece = board[cx][cy];//try to get this particular cell
        } catch (ArrayIndexOutOfBoundsException e) {
            piece = -1;//if the cell is not on board, out of bound
        }

        return piece;//give back the piece
    }



    public boolean[][] move(Object piece,int cx,int cy){
        boolean[][] state = new boolean[8][8];
        int temp=0;

        switch (temp){
            case 1:
                //pawn
                //pawn(x,y,d){
               // check flag 1st move

               // get piece(x,y+d)==empty	>true on state[x,y+d]
               // 1st move && get piece(x,y+2d)==empty >true on state[x,y+2d]
               // get piece(x+1,y+d)==opposing >true on state[x+1,y+d]
               // get piece(x-1,y+d)==opposing >true on state[x-1,y+d]

       // }
                break;

        case 2:
        //rook
        //private void rook(int x,int y){
        //int cx=x;
        //int cy=y;

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
        break;
        case 3://knight
        //knight(x,y){//no check blocking

        //    get piece(x+1,y+2)==(opposing||empty)  >> true on state[x+1][y+2]
       //     get piece(x+1,y-2)==(opposing||empty)  >> true on state[x+1][y-2]
        //    get piece(x+2,y+1)==(opposing||empty)  >> true on state[x+2][y+1]
        //    get piece(x+2,y-1)==(opposing||empty)  >> true on state[x+2][y-1]
        //    get piece(x-1,y+2)==(opposing||empty)  >> true on state[x-1][y+2]
       //     get piece(x-1,y-2)==(opposing||empty)  >> true on state[x-1][y-2]
       //     get piece(x-2,y+1)==(opposing||empty)  >> true on state[x-2][y+1]
       //     get piece(x-2,y-1)==(opposing||empty)  >> true on state[x-2][y-1]
       // }


        break;
        case 4: //bishop
        //private void bishop(int x,int y){
      //  int cx=x;
      //  int cy=y;

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

    break;
        case 5://queen
        //private void queen(int x,int y){
     //   int cx=x;
     //   int cy=y;

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
        break;
        case 6: //king
        //king(x,y){

    //    get piece(x-1,y-1) ==(opposing||empty) >> true on state[x-1][y-1]
     //   get piece(x-1,y)==(opposing||empty) >> true on state[x-1][y]
     //   get piece(x-1,y+1)==(opposing||empty) >> true on state[x-1][y+1]
      //  get piece(x,y-1)==(opposing||empty) >> true on state[x][y-1]
      //  //get piece(x,y)//king it self
      //  get piece(x,y+1)==(opposing||empty) >> true on state[x][y+1]
       // get piece(x+1,y-1)==(opposing||empty) >> true on state[x+1][y-1]
       // get piece(x+1,y)==(opposing||empty) >> true on state[x+1][y]
      //  get piece(x+1,y+1)==(opposing||empty) >> true on state[x+1][y+1]

   // }
        break;

        }











        return state;
    }




}
