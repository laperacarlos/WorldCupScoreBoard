package lapera.world.cup.board;

import lapera.world.cup.domain.Game;

import java.util.List;

public interface ScoreBoard {
    void addGame(final Game Game);

    void updateGame(final Game Game);
    
    void removeGame(final Game Game);
    
    List<Game> getLiveGames();

    List<Game> getFinishedGames();
}
