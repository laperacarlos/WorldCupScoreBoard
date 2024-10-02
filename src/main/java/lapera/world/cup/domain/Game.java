package lapera.world.cup.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Game {
    private String homeTeam;
    private String awayTeam;
    private Integer homeScore;
    private Integer awayScore;
    private LocalDateTime startTime;
    private boolean isFinished;

    public Game(String homeTeam, String awayTeam, Integer homeScore, Integer awayScore, boolean isFinished) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.homeScore = homeScore;
        this.awayScore = awayScore;
        this.startTime = LocalDateTime.now();
        this.isFinished = isFinished;
    }
    
    public Integer getTotalScore() {
        return homeScore + awayScore;
    }
}
    
