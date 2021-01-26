import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class PoolVote {
    Map<String,Integer> pool;
    public PoolVote() {
        pool = new HashMap<>();
    }

    public void addVote(String name) {
        int res = pool.get(name);
        pool.put(name,++res);
    }

    public void removeVote(String name) {
        int res = pool.get(name);
        pool.put(name,--res);
    }

    public String getMostVoted() {
       var tmp =  pool.entrySet().stream().max(Comparator.comparingInt(Map.Entry::getValue));
        return tmp.map(Map.Entry::getKey).orElse(null);
    }

    public void setVotePlayer(String player , int numberVotes) {
        this.pool.put(player,numberVotes);
    }
}
