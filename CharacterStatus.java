public class CharacterStatus {
    private int hp;
    private int mp;
    private int maxHp;
    private int maxMp;
    private int atk = 200;
    private int def = 0;
    private int gold = 0; // เงินของผู้เล่น
    private int totalScore = 0;

    private boolean hasWingedBoots = false;
    private boolean hasLuckyCharm = false;

    public CharacterStatus(int hp, int mp) {
        this.hp = this.maxHp = hp;
        this.mp = this.maxMp =  mp;
    }

    public int getHp() { return hp; }
    public int getMp() { return mp; }
    public int getMaxHp() { return maxHp; }
    public int getMaxMp() { return maxMp; }
    public int getAtk() { return atk; }
    public int getDef() { return def; }
    public int getGold() { return gold; }

    public boolean hasWingedBoots() { return hasWingedBoots; }
    public boolean hasLuckyCharm() { return hasLuckyCharm; }

    public void loseHP(int amount) {
        hp = Math.max(0, hp - amount);
    }

    public void loseMP(int amount) {
        int actualLoss = hasWingedBoots ? (int)(amount * 0.95) : amount;
        mp = Math.max(0, mp - actualLoss);
    }

    public void heal(int amount) {
        this.hp = Math.min(this.hp + amount, maxHp);
    }

    public void restoreMP(int amount) {
        mp += amount;
        if (mp > maxMp) {
            mp = maxMp;
        }
    }
    
    public void damage(int amount) {
        int reduced = Math.max(0, amount - def);
        hp = Math.max(0, hp - reduced);
    }

    public void gainHP(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    public void gainMP(int amount) {
        mp += amount;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public void applyItemEffect(String itemName, int value) {
        switch (itemName) {
            case "WingedBoots":
                hasWingedBoots = true;
                break;
            case "GreenPotion":
                maxHp += value;
                hp = Math.min(hp, maxHp);
                break;
            case "BluePotion":
                gainMP(value);
                break;
            case "Sword":
                atk += value;
                break;
            case "Shield":
                def += value;
                break;
            case "LuckyCharm":
                hasLuckyCharm = true;
                break;
        }
    }

    // ---------- ระบบเงิน ----------
    public void gainGold(int amount) {
        int bonus = hasLuckyCharm ? (int)(amount * 0.2) : 0;
        gold += amount + bonus;
    }

    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }
    
    public void addScore(int score) {
        totalScore += score;
    }

    public int getTotalScore() {
        return totalScore;
    }
}
