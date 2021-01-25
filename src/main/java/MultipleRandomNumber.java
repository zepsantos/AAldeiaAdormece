import java.util.*;
import java.util.stream.Collectors;

public class MultipleRandomNumber {



    public static List<Integer> getRandomNumbers(int nOfRandomNumbers,int limit) {
        Set<Integer> numChosen = new HashSet<>();
        Random random = new Random();
        List<Integer> res = new ArrayList<>();
        for(int i = nOfRandomNumbers; i>0;i--) {
            int tmp = random.nextInt(limit);
            while(numChosen.contains(tmp)) {
                tmp = random.nextInt(limit);
            }
            numChosen.add(tmp);
            res.add(tmp);
        }
        return res;
    }
}
