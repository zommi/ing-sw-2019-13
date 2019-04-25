package Cards;

import Map.Square;
import Map.SquareAbstract;
import Player.Character;

import java.util.ArrayList;

public class PullWhereCanSeeCommand implements Command {
    public PullWhereCanSeeCommand(){
    }

    /**
     * Returns the characters that, if moved of 0,1,2 movements, end up being in a square that the player can see.
     * @param square where the player is
     * @return  an arraylist of arralist of the characters that, if moved of 0,1,2 movements, end up being in a square that the player can see.
     */
    public ArrayList<ArrayList<Character>> execute(SquareAbstract square){
        int ok = 0;
        ArrayList<ArrayList<Character>> e = new ArrayList<ArrayList<Character>>();
        ArrayList<Character> e1 = new ArrayList<Character>();


        ArrayList<SquareAbstract> zeromove = new ArrayList<SquareAbstract>();
        ArrayList<SquareAbstract> onemove;
        ArrayList<SquareAbstract> twomove;

        ArrayList<Character> taken = (ArrayList<Character>) Character.getTakenCharacters();
        ArrayList<SquareAbstract> visible = (ArrayList<SquareAbstract>) square.getVisibleSquares();

        for(int i = 0; i < taken.size(); i++)
        {
            zeromove.add(taken.get(i).getPosition());
            onemove = (ArrayList<SquareAbstract>) taken.get(i).getPosition().getAdjacentSquares(); //with this i get all the 1 movements square
            twomove = (ArrayList<SquareAbstract>) taken.get(i).getPosition().getExactlyTwoMovementsSquares(); //with this i get all the 2 movements square



            for (Square t : (ArrayList<Square>)visible.clone()) {
                if(zeromove.contains(t) || onemove.contains(t) || twomove.contains(t))
                    ok++;
            }

            if(ok > 0) //it means that character is ok.
            {
                e1.add(taken.get(i));
                e.add(e1);
                ok = 0;
                e1 = null;
            }
        }

        return e;
    }
}
