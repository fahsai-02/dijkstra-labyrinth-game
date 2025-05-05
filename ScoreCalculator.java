public class ScoreCalculator {

    /**
     * คำนวณคะแนนจากเส้นทางที่เดินเทียบกับเส้นทางที่ดีที่สุด
     *
     * @param shortestPath ความยาวของเส้นทางที่สั้นที่สุด
     * @param playerPath   ความยาวของเส้นทางที่ผู้เล่นเดินจริง
     * @param maxScore     คะแนนเต็มของด่าน (เช่น 1000)
     * @return คะแนนที่ได้ โดยจะไม่ต่ำกว่า 0
     */
    public static int calculateStageScore(int shortestPath, int playerPath, int maxScore) {
        if (shortestPath <= 0 || playerPath <= 0) return 0;

        int extraDistance = Math.max(0, playerPath - shortestPath);
        int penalty = extraDistance;

        return Math.max(0, maxScore - penalty);
    }

    /**
     * รวมคะแนนทุกด่าน พร้อม bonus คะแนนจากเงินและเลือด/มานาที่เหลือ
     *
     * @param stageScores    คะแนนรวมจากแต่ละด่าน (เช่น 3 ด่าน)
     * @param hp             HP ที่เหลือ
     * @param mp             MP ที่เหลือ
     * @param gold           ทองที่ได้
     * @return คะแนนรวมสุดท้าย
     */
    public static int calculateFinalScore(int[] stageScores, int hp, int mp, int gold) {
        int total = 0;

        for (int score : stageScores) {
            total += Math.max(0, score);
        }

        // เพิ่มโบนัสที่เหลือจากเลือด มานา และทอง
        int bonus = hp + mp + (gold / 10);  // ปรับสัดส่วนได้
        return total + bonus;
    }
}
