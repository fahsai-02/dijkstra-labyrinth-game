public class CharacterStatus {
    // ค่าสถานะต่าง ๆ ของตัวละคร
    private int hp;
    private int mp;
    private int maxHp;
    private int maxMp;
    private int atk = 20;    // พลังโจมตีเริ่มต้น
    private int def = 0;     // พลังป้องกันเริ่มต้น
    private int gold = 0;    // เงินเริ่มต้น
    private int totalScore = 0; // คะแนนสะสม

    // ไอเท็มพิเศษที่ตัวละครมีหรือไม่
    private boolean hasWingedBoots = false;  // ลดการเสีย MP
    private boolean hasLuckyCharm = false;   // โบนัสเงินทอง

    // Constructor กำหนดค่า HP และ MP เริ่มต้นและสูงสุดเท่ากัน
    public CharacterStatus(int hp, int mp) {
        this.hp = this.maxHp = hp;
        this.mp = this.maxMp =  mp;
    }

    // --- getter สำหรับดึงค่าต่าง ๆ ---
    public int getHp() { return hp; }
    public int getMp() { return mp; }
    public int getMaxHp() { return maxHp; }
    public int getMaxMp() { return maxMp; }
    public int getAtk() { return atk; }
    public int getDef() { return def; }
    public int getGold() { return gold; }

    public boolean hasWingedBoots() { return hasWingedBoots; }
    public boolean hasLuckyCharm() { return hasLuckyCharm; }

    // --- ฟังก์ชันจัดการ HP/MP ---

    // โดนโจมตีโดยตรง ลด HP
    public void loseHP(int amount) {
        hp = Math.max(0, hp - amount);  // ไม่ให้ติดลบ
    }

    // เสีย MP เวลาตัวละครใช้สกิล หรืออะไรบางอย่าง
    // ถ้ามี WingedBoots ลดการเสีย MP 5%
    public void loseMP(int amount) {
        int actualLoss = hasWingedBoots ? (int)(amount * 0.95) : amount;
        mp = Math.max(0, mp - actualLoss);
    }

    // ฟื้นฟูเลือด (แต่ไม่เกิน maxHp)
    public void heal(int amount) {
        this.hp = Math.min(this.hp + amount, maxHp);
    }

    // ฟื้นฟู MP (ถ้าเกิน maxMp จะถูกปรับลงมา)
    public void restoreMP(int amount) {
        mp += amount;
        if (mp > maxMp) {
            mp = maxMp;
        }
    }
    
    // โดนโจมตีที่คำนวณแล้ว ลดด้วย def ก่อน
    public void damage(int amount) {
        int reduced = Math.max(0, amount - def); // ป้องกันไม่ให้ลดติดลบ
        hp = Math.max(0, hp - reduced);
    }

    // เพิ่ม HP (ฟื้นฟูแบบตรง ๆ)
    public void gainHP(int amount) {
        hp = Math.min(maxHp, hp + amount);
    }

    // เพิ่ม MP แบบตรง ๆ (ไม่สน max)
    public void gainMP(int amount) {
        mp += amount;
    }

    // เช็คว่าตัวละครยังมีชีวิตอยู่ไหม (HP > 0)
    public boolean isAlive() {
        return hp > 0;
    }

    // ฟังก์ชันนี้จะเอาไว้ใช้ตอนรับไอเท็มต่าง ๆ มาปรับสถานะตัวละคร
    public void applyItemEffect(String itemName, int value) {
        switch (itemName) {
            case "WingedBoots":
                hasWingedBoots = true; // ลด MP loss
                break;
            case "GreenPotion":
                maxHp += value;    // เพิ่ม maxHP
                hp = Math.min(hp, maxHp); // ปรับ HP ปัจจุบันไม่เกิน max
                break;
            case "BluePotion":
                gainMP(value);     // เพิ่ม MP
                break;
            case "Sword":
                atk += value;      // เพิ่มพลังโจมตี
                break;
            case "Shield":
                def += value;      // เพิ่มพลังป้องกัน
                break;
            case "LuckyCharm":
                hasLuckyCharm = true; // โบนัสเงิน
                break;
        }
    }

    // -------- ระบบเงิน ---------

    // ได้เงิน ถ้ามี LuckyCharm จะได้โบนัส 20%
    public void gainGold(int amount) {
        int bonus = hasLuckyCharm ? (int)(amount * 0.2) : 0;
        gold += amount + bonus;
    }

    // ใช้เงิน ถ้ามีพอใช้จะคืน true ไม่งั้น false
    public boolean spendGold(int amount) {
        if (gold >= amount) {
            gold -= amount;
            return true;
        }
        return false;
    }
    
    // เพิ่มคะแนนสะสม
    public void addScore(int score) {
        totalScore += score;
    }

    // ดึงคะแนนสะสมปัจจุบัน
    public int getTotalScore() {
        return totalScore;
    }
}
