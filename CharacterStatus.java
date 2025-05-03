public class CharacterStatus {
    private int hp, mp;
    private int maxHp, maxMp;

    public CharacterStatus(int hp, int mp) {
        this.hp = this.maxHp = hp;
        this.mp = this.maxMp = mp;
    }

    public int getHp() { return hp; }
    public int getMp() { return mp; }

    public void loseHP(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void loseMP(int amount) {
        mp = Math.max(0, mp - amount);
    }

    public void healFull() {
        this.hp = maxHp;
        this.mp = maxMp;
    }

    public void damage(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void gainHP(int amount) {
        hp += amount;
    }

    public void gainMP(int amount) {
        mp += amount;
    }

    public boolean isAlive() {
        return hp > 0;
    }
}
