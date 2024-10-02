package lapera.world.cup.board;

import lapera.world.cup.domain.Game;

import java.util.List;

public interface ScoreBoard {
    void addGame(Game Game);

    void updateGame(Game Game);
    
    void removeGame(Game Game);
    
    List<Game> getLiveScoreBoard();
}
