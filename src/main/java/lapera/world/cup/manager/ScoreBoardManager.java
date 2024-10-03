package lapera.world.cup.manager;

import lapera.world.cup.domain.Game;

import java.util.List;

public interface ScoreBoardManager {
    void processGame(final Game game);
    
    List<Game> getLiveGames();
    
    List<Game> getFinishedGames();
}
