package com;

public class Melancholy {
    
    private String[] questions = {
        "1. 做事提不起勁或沒有樂趣",
        "2. 感到心情低落、沮喪或沒有希望",
        "3. 入睡困難、睡不安穩或睡得太多",
        "4. 感覺疲倦或沒有活力",
        "5. 食慾不振或吃得太多",
        "6. 覺得自己很糟，或讓自己及家人失望",
        "7. 很難集中注意力（例如看報紙或看電視）",
        "8. 行動或說話速度緩慢，或浮躁坐立不安",
        "9. 有不如死掉或想傷害自己的念頭"
    };

    private String[] options = {
        "完全沒有 (0分)",
        "有幾天 (1分)",
        "超過一半的天數 (2分)",
        "幾乎每天 (3分)"
    };

    private int[] scores;
    private int currentQuestionIndex;

    public Melancholy() {
        scores = new int[questions.length];
        resetQuiz();
    }

    public void resetQuiz() {
        currentQuestionIndex = 0;
        for (int i = 0; i < scores.length; i++) {
            scores[i] = -1; 
        }
    }

    public void setScore(int questionIdx, int score) {
        if (questionIdx >= 0 && questionIdx < scores.length) {
            scores[questionIdx] = score;
        }
    }

    public int getScore(int questionIdx) {
        return scores[questionIdx];
    }

    public int calculateTotalScore() {
        int total = 0;
        for (int score : scores) {
            if (score > 0) {
                total += score;
            }
        }
        return total;
    }

    public String getEvaluationResult() {
        int total = calculateTotalScore();
        String result = "總得分：" + total + " 分\n評估建議：";
        if (total <= 4) {
            result += "無或極輕微憂鬱。請保持規律生活與良好心情。";
        } else if (total <= 9) {
            result += "輕度憂鬱。建議多與親友傾訴，適度運動舒緩壓力。";
        } else if (total <= 14) {
            result += "中度憂鬱。建議諮詢心理師或尋求醫療門診諮商。";
        } else if (total <= 19) {
            result += "中重度憂鬱。建議積極前往身心科或精神科評估治療。";
        } else {
            result += "重度憂鬱。強烈建議立即就醫尋求專業醫療協助。";
        }
        return result;
    }

    
    public String[] getQuestions() { return questions; }
    public String[] getOptions() { return options; }
    public int getCurrentQuestionIndex() { return currentQuestionIndex; }
    public void setCurrentQuestionIndex(int index) { this.currentQuestionIndex = index; }
    public int getTotalQuestions() { return questions.length; }
}