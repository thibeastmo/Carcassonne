package be.kdg.backend_game.domain.user_management;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

@Getter
public class UserLevel {
    private static final Map<Integer, Integer> levelThresholds = new HashMap<>();

    static {
        levelThresholds.put(1, 0);
        levelThresholds.put(2, 1500);
        levelThresholds.put(3, 3000);
        levelThresholds.put(4, 4500);
        levelThresholds.put(5, 6000);
        levelThresholds.put(6, 7500);
        levelThresholds.put(7, 9000);
        levelThresholds.put(8, 10500);
        levelThresholds.put(9, 12000);
        levelThresholds.put(10, 20000);
    }

    public static int getLevelByExperiencePoints(int experiencePoints) {
        int level = 0;
        for (Map.Entry<Integer, Integer> entry : levelThresholds.entrySet()) {
            if (experiencePoints >= entry.getValue()) {
                level = entry.getKey();
            } else {
                break;
            }
        }
        return level;
    }

    public static int getMaxLevel() {
        return levelThresholds.size();
    }
}
