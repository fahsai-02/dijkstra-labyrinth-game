import java.util.*;

public class MonsterStatus {
    private String name;
    private int maxHp;
    private int currentHp;
    private int atk;
    private int def;
    private int rewardGold;
    private String imagePath;

    public MonsterStatus(String name, int maxHp, int atk, int def, int rewardGold, String imagePath) {
        this.name = name;
        this.maxHp = maxHp;
        this.currentHp = maxHp;
        this.atk = atk;
        this.def = def;
        this.rewardGold = rewardGold;
        this.imagePath = imagePath;
    }

    // ----------- Getters -----------
    public String getName() { return name; }
    public int getMaxHp() { return maxHp; }
    public int getHp() { return currentHp; }
    public int getAtk() { return atk; }
    public int getDef() { return def; }
    public int getRewardGold() { return rewardGold; }
    public String getImagePath() { return imagePath; }

    public void reduceHp(int amount) {
        currentHp = Math.max(0, currentHp - amount);
    }

    public void resetHp() {
        currentHp = maxHp;
    }

    public void scaleStats(double factor) {
        maxHp = (int)(maxHp * factor);
        atk = (int)(atk * factor);
        def = (int)(def * factor);
        rewardGold = (int)(rewardGold * factor);
        currentHp = maxHp;
    }

    // ---------- Static Data ----------
    private static final Map<String, MonsterStatus> monsterData = new HashMap<>();
    private static final Map<Integer, List<String>> monsterStageMap = new HashMap<>();

    static {
        // // Stage 1
        // monsterData.put("Glorbo", new MonsterStatus("Glorbo Frutodrilo", 50, 10, 0, 30, "assets/Monster/Glorbo-Frutodrilo.PNG"));
        // monsterData.put("Tralalero", new MonsterStatus("Tralalero Tralala", 40, 10, 5, 35,"assets/Monster/Tralalero-Tralala.PNG"));
        // monsterStageMap.put(1, Arrays.asList("Glorbo", "Tralalero"));

        // // Stage 2
        // monsterData.put("Shimpanzini", new MonsterStatus("Shimpanzini Bananinni", 60, 12, 2, 50, "assets/Monster/Shimpanzini-Bananinni.PNG"));
        // monsterData.put("Balerinna", new MonsterStatus("Balerinna Cappuccinna", 80, 5, 8, 40, "assets/Monster/Balerinna-Cappuccinna.PNG"));
        // monsterData.put("Fruli", new MonsterStatus("Fruli Frulla", 50, 22, 10, 65, "assets/Monster/Fruli-Frulla.PNG"));
        // monsterStageMap.put(2, Arrays.asList("Shimpanzini", "Balerinna", "Fruli"));

        // // Stage 3
        // monsterData.put("Trulmero", new MonsterStatus("Trulmero Trulicina", 100, 25, 0, 80, "assets/Monster/Trulmero-Trulicina.PNG"));
        // monsterData.put("Saturno", new MonsterStatus("La Vaca Saturno Saturnita", 140, 17, 4, 55 ,"assets/Monster/La-Vaca-Saturno-Saturnita.PNG"));
        // monsterData.put("Cappuccino", new MonsterStatus("Cappuccino Assasino", 130, 20, 7, 68, "assets/Monster/Cappuccino-Assasino.PNG"));
        // monsterData.put("Burbaloni", new MonsterStatus("Burbaloni Lulioli", 110, 23, 12, 77, "assets/Monster/Burbaloni-Lulioli.PNG"));
        // monsterData.put("Brr", new MonsterStatus("Brr Brr Patapim", 150, 30, 15, 75, "assets/Monster/Brr-Brr-Patapim.PNG"));
        // monsterStageMap.put(3, Arrays.asList("Trulmero", "Saturno", "Cappuccino", "Burbaloni", "Brr"));
        
        // Stage 1
        monsterData.put("Glorbo", new MonsterStatus("Glorbo Frutodrilo", 1, 10, 0, 25, "assets/Monster/Glorbo-Frutodrilo.PNG"));
        monsterData.put("Tralalero", new MonsterStatus("Tralalero Tralala", 1, 10, 5, 35,"assets/Monster/Tralalero-Tralala.PNG"));
        monsterStageMap.put(1, Arrays.asList("Glorbo", "Tralalero"));

        // Stage 2
        monsterData.put("Shimpanzini", new MonsterStatus("Shimpanzini Bananinni", 1, 12, 2, 20, "assets/Monster/Shimpanzini-Bananinni.PNG"));
        monsterData.put("Balerinna", new MonsterStatus("Balerinna Cappuccinna", 1, 5, 8, 40, "assets/Monster/Balerinna-Cappuccinna.PNG"));
        monsterData.put("Fruli", new MonsterStatus("Fruli Frulla", 1, 22, 10, 50, "assets/Monster/Fruli-Frulla.PNG"));
        monsterStageMap.put(2, Arrays.asList("Shimpanzini", "Balerinna", "Fruli"));

        // Stage 3
        monsterData.put("Trulmero", new MonsterStatus("Trulmero Trulicina", 1, 25, 0, 30, "assets/Monster/Trulmero-Trulicina.PNG"));
        monsterData.put("Saturno", new MonsterStatus("La Vaca Saturno Saturnita", 1, 17, 4, 32 ,"assets/Monster/La-Vaca-Saturno-Saturnita.PNG"));
        monsterData.put("Cappuccino", new MonsterStatus("Cappuccino Assasino", 1, 20, 7, 45, "assets/Monster/Cappuccino-Assasino.PNG"));
        monsterData.put("Burbaloni", new MonsterStatus("Burbaloni Lulioli", 1, 23, 12, 60, "assets/Monster/Burbaloni-Lulioli.PNG"));
        monsterData.put("Brr", new MonsterStatus("Brr Brr Patapim", 1, 30, 15, 75, "assets/Monster/Brr-Brr-Patapim.PNG"));
        monsterStageMap.put(3, Arrays.asList("Trulmero", "Saturno", "Cappuccino", "Burbaloni", "Brr"));

        // Boss
        monsterData.put("Tung", new MonsterStatus("Tung Tung Tung Sahur", 1, 120, 20, 1_000_000, "assets/Monster/BOSS.PNG"));
    }

    public static MonsterStatus getMonster(String name, int stageLevel) {
        if (!monsterData.containsKey(name)) {
            System.err.println("⚠️ Monster not found: " + name);
            return new MonsterStatus("UNKNOWN", 1, 1, 0, 0, "");
        }

        MonsterStatus base = monsterData.get(name);

        MonsterStatus clone = new MonsterStatus(
            base.name, base.maxHp, base.atk, base.def, base.rewardGold, base.imagePath
        );

        double scaleFactor = 1 + 0.2 * (stageLevel - 1); // ด่าน 1 = 1.0, ด่าน 2 = 1.2, ...
        clone.scaleStats(scaleFactor);

        return clone;
    }

    public static Set<String> getAvailableMonsterNames() {
        return monsterData.keySet();
    }

    public static List<String> getMonsterNamesForStage(int stageLevel) {
        return monsterStageMap.getOrDefault(stageLevel, Collections.emptyList());
    }
}
