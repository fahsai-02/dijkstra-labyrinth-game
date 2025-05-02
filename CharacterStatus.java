public class CharacterStatus {
    private int hp;
    private int mp;

    public CharacterStatus(int hp, int mp) {
        this.hp = hp;
        this.mp = mp;
    }

    public int getHp() { return hp; }
    public int getMp() { return mp; }

    public void loseHP(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void loseMP(int amount) {
        mp = Math.max(0, mp - amount);
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
