package lapera.world.cup.board;

import lapera.world.cup.domain.Game;

import java.util.ArrayList;
import java.util.List;

public class WorldCupScoreBoard implements ScoreBoard {
    private final List<Game> liveGames = new ArrayList<>();
    private final List<Game> finishedGames = new ArrayList<>();
    
    @Override
    public void addGame(final Game game){
        liveGames.add(game);
    }
    
    @Override
    public void updateGame(final Game game) {
        liveGames.set(liveGames.indexOf(game), game);
    }

    @Override
    public void removeGame(final Game game) {
        finishedGames.add(game);
        liveGames.remove(game);
    }
    
    @Override
    public List<Game> getLiveGames() {
        return liveGames;
    }

    @Override
    public List<Game> getFinishedGames() {
        return finishedGames;
    }
}
