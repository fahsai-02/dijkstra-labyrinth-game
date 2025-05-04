public class BossStatus {
    private String name;
    private int hp, mp;
    private int maxHp, maxMp;
    private int atk, def;

    public BossStatus(String name, int hp, int mp, int atk, int def) {
        this.name = name;
        this.hp = this.maxHp = hp;
        this.mp = this.maxMp = mp;
        this.atk = atk;
        this.def = def;
    }

    public String getName() { return name; }
    public int getHp() { return hp; }
    public int getMp() { return mp; }
    public int getATK() { return atk; }
    public int getDEF() { return def; }
    public int getMaxHp() { return maxHp; }
    public int getMaxMp() { return maxMp; }

    public void takeDamage(int amount) {
        hp = Math.max(0, hp - Math.max(0, amount - def));
    }

    public boolean isDefeated() {
        return hp <= 0;
    }
}
