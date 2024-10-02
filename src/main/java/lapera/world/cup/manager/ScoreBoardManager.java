package lapera.world.cup.manager;

import lapera.world.cup.domain.Game;

import java.util.List;

public interface ScoreBoardManager {
    void startGame(Game game);
    void updateScore(Game game);
    void endGame(Game game);
    List<Game> getLiveGames();
}
