# 憂鬱檢測量表 (PHQ-9) 系統 - 專案說明文件

## 1. 專案簡介
本專案為基於 Java Swing GUI 架構開發的「憂鬱檢測自我篩檢量表 (PHQ-9)」桌面應用程式。系統實作了臨床常見的患者健康問卷（Patient Health Questionnaire-9, 簡稱 PHQ-9），透過 9 道標準化問題評估使用者近期的情緒與身心狀態。軟體具備題目光標切換、動態即時時鐘、作答防呆機制、自動計分分級建議，以及檢測報表列印等實用功能。專案採用經典的 MVC（Model-View-Controller）設計概念進行解耦，將核心邏輯（`Melancholy`）與圖形介面（`MelancholyUI`）明確劃分，非常適合作為 Java 物件導向程式設計（OOP）與 GUI 桌面開發的實戰範例。

---

## 2. 學習目標
1. **掌握物件導向設計原則（OOP）**：理解類別封裝（Encapsulation）、資料隱藏（Data Hiding）以及責任分離（Separation of Concerns）。
2. **熟悉 Java Swing GUI 介面開發**：學習視窗容器（`JFrame`）、標籤（`JLabel`）、單選按鈕群組（`JRadioButton`、`ButtonGroup`）、文字區塊（`JTextArea`、`JScrollPane`）與按鈕（`JButton`）之佈局與樣式控制。
3. **深入事件驅動程式設計（Event-Driven Programming）**：熟練 `ActionListener`、匿名內部類別（Anonymous Inner Class）及按鈕觸發回呼處理。
4. **學習陣列與狀態管理**：掌握一維陣列在題目、選項以及使用者作答狀態（動態計分與回填）的資料保存與檢索。
5. **掌握多執行緒與定時排程**：運用 `javax.swing.Timer` 與 `java.time.LocalDateTime` 打造無凍結、定時更新的動態即時時鐘。
6. **整合 Java 列印服務**：利用 Swing 內建的 `JTextArea.print()` API 實現桌面端的硬體列印或 PDF 輸出。

---

## 3. 開發環境
- **作業系統**：Windows 10 / 11、macOS 或 Linux
- **開發工具（IDE）**：Eclipse IDE for Java Developers (或 IntelliJ IDEA / VS Code)
- **JDK 版本**：Java SE Development Kit (JDK) 8 或更高版本（建議 JDK 11 / JDK 17 / JDK 21 LTS）
- **文字編碼**：UTF-8（依專案配置 `.settings/org.eclipse.core.resources.prefs` 規範）

---

## 4. 使用技術
- **Java 核心語言**：Java SE、物件導向語法、陣列資料結構。
- **Swing 圖形化介面工具庫**：`javax.swing.*`（JFrame, JLabel, JRadioButton, ButtonGroup, JButton, JTextArea, JScrollPane, JOptionPane, Timer）。
- **AWT 抽象視窗工具集**：`java.awt.*`（Font, EventQueue, print.PrinterException）。
- **現代日期時間 API**：`java.time.LocalDateTime`、`java.time.format.DateTimeFormatter`（Java 8+ 標準 API）。
- **列印 API**：`javax.swing.text.JTextComponent.print()` 介面。

---

## 5. 核心功能
1. **標準 PHQ-9 九道題目檢測**：完整內建 9 道國際通用的憂鬱症篩檢量表題目與 4 級評分標準。
2. **題目導覽與隨時回溯修訂**：支援「上一題」與「下一題」切換，並自動回填使用者先前的作答選項。
3. **作答未填防呆機制**：若使用者尚未完成當前題目即點擊「下一題」或「看結果」，系統彈跳對話框主動警示。
4. **即時動態時鐘顯示**：右上角以秒為單位動態顯示當前系統時間（格式：`yyyy-MM-dd HH:mm:ss`）。
5. **智慧動態按鈕切換**：題目處於最後一題時，導覽按鈕自動由「下一題」切換為「看結果」。
6. **評估分級與建議報告**：自動加總分數並根據臨床分級（0-4 分、5-9 分、10-14 分、15-19 分、20-27 分）產生專業建議。
7. **一鍵重設測驗**：重置所有作答分數與索引，回到第一題讓使用者或下一位受試者重新填寫。
8. **實體/虛擬印表機列印**：支援將產生的檢測評估文字一鍵送往作業系統列印服務或另存為 PDF。

---

## 6. 系統流程
```
[ 啟動程式 (MelancholyUI.main) ]
               │
               ▼
[ 實例化 Model 與 View ] ──> [ 啟動即時時鐘 Timer (每秒更新) ]
               │
               ▼
[ 載入第 1 題 (loadQuestion(0)) ]
               │
               ▼
       [ 使用者操作介面 ]
       ┌───────┴─────────────────────────────────────────┐
       ▼                                                 ▼
[ 點擊單選選項 ]                                  [ 點擊功能按鈕 ]
       │                                                 │
       ▼                                                 ├─► [ 上一題 ] : 若 current > 0 則切換至上一題
[ Model.setScore(idx, score) ]                           │
                                                         ├─► [ 下一題 / 看結果 ] :
                                                         │        │
                                                         │        ├─ 未作答 (score == -1) ──► 彈出警示對話框
                                                         │        │
                                                         │        ├─ 還有後續題目 ──────────► 載入下一題
                                                         │        │
                                                         │        └─ 為最後一題 ────────────► 累加總分、產出報告、更新 JTextArea
                                                         │
                                                         ├─► [ 重新測驗 ] : resetQuiz()、清空文字區、跳回第 1 題
                                                         │
                                                         ├─► [ 列印結果 ] : 檢查結果是否空白 ──► 調用 taResult.print()
                                                         │
                                                         └─► [ 離開程式 ] : 調用 System.exit(0)
```

---

## 7. 專案結構
```
Melancholy/
├── .classpath                          # Eclipse 類別路徑配置
├── .project                            # Eclipse 專案設定檔
├── .settings/
│   ├── org.eclipse.core.resources.prefs # 專案編碼設定 (UTF-8)
│   └── org.eclipse.jdt.core.prefs       # Java 編譯器規範設定
├── bin/                                # 編譯後字節碼目錄 (.class)
│   └── com/
│       ├── Melancholy.class            # 核心商業邏輯類別
│       ├── MelancholyUI.class          # GUI 視窗主類別
│       └── MelancholyUI$1 ~ $8.class   # 事件處理匿名內部類別
└── src/                                # 原始碼目錄 (.java)
    └── com/
        ├── Melancholy.java             # 題目、計分與評估邏輯 Model
        └── MelancholyUI.java           # Swing 視窗介面與事件 View/Controller
```

---

## 8. Class 說明
1. **`com.Melancholy`**：
   - **角色**：Model（資料模型層與業務邏輯）。
   - **職責**：封裝 9 道 PHQ-9 題目、選項文字、每題得分陣列與目前題號索引；負責總分計算、邏輯判斷與評估報告文字生成。完全獨立於 GUI，具備高可測試性與重用性。
2. **`com.MelancholyUI`**：
   - **角色**：View & Controller（檢視與控制層）。
   - **職責**：建構視窗畫面、配置 Swing 元件、綁定使用者事件（按鈕點擊、選項勾選）、透過 Timer 排程時鐘更新，並協調 `Melancholy` 模型物件進行畫面刷新與輸出。

---

## 9. Field 說明

### `Melancholy` 類別
- `private String[] questions`：儲存 9 道 PHQ-9 題目的字串陣列。
- `private String[] options`：儲存 4 個評分選項描述的字串陣列（0 分至 3 分）。
- `private int[] scores`：長度為 9 的整數陣列，記錄每題的得分；預設值皆為 `-1`（代表尚未作答）。
- `private int currentQuestionIndex`：整數，表示使用者目前正在瀏覽/作答的題號索引（範圍 `0` 至 `8`）。

### `MelancholyUI` 類別
- `private JFrame frame`：主視窗框架（視窗大小 720 × 520）。
- `private JLabel lblClock`：右上角即時時鐘顯示標籤。
- `private JLabel lblQuestionTitle`：當前題目文字標籤。
- `private JRadioButton[] rbOptions`：長度為 4 的單選按鈕陣列，供使用者選擇當題頻率。
- `private ButtonGroup btnGroup`：單選按鈕群組物件，確保同時間只能選取一個選項。
- `private JTextArea taResult`：下方顯示檢測評估結果建議的不可編輯多行文字框。
- `private JButton btnPrev`：「上一題」按鈕。
- `private JButton btnNext`：「下一題」/「看結果」導覽按鈕。
- `private JButton btnReset`：「重新測驗」按鈕。
- `private JButton btnPrint`：「列印結果」按鈕。
- `private JButton btnExit`：「離開程式」按鈕。
- `private Melancholy model`：關聯之業務模型物件實例。

---

## 10. Constructor 說明

### `public Melancholy()`
- **功能**：建構子，實例化分數陣列 `scores = new int[questions.length]`，並呼叫 `resetQuiz()` 將題號歸零、所有題目分數重置為 `-1`。

### `public MelancholyUI()`
- **功能**：UI 建構子。實例化 `model = new Melancholy()`，接著呼叫 `initialize()` 完成所有視窗元件佈局與事件綁定，呼叫 `startClock()` 啟動定時器，最後呼叫 `loadQuestion(0)` 呈現第一題。

---

## 11. Method 說明

### `Melancholy` 類別
1. `public void resetQuiz()`：重設測驗狀態，將 `currentQuestionIndex` 設為 0，並將 `scores` 陣列元素全數填入 `-1`。
2. `public void setScore(int questionIdx, int score)`：驗證索引合法性（`0 <= questionIdx < scores.length`），並記錄指定題目的得分。
3. `public int getScore(int questionIdx)`：取得指定題目的得分；若未作答則回傳 `-1`。
4. `public int calculateTotalScore()`：巡訪 `scores` 陣列，累加所有大於 0 的有效得分並回傳整數總分。
5. `public String getEvaluationResult()`：取得總分並對照分級門檻，組裝成診斷建議字串。
6. `public String[] getQuestions()`：取得全部題目陣列。
7. `public String[] getOptions()`：取得全部選項陣列。
8. `public int getCurrentQuestionIndex()`：取得目前題號索引。
9. `public void setCurrentQuestionIndex(int index)`：設定目前題號索引。
10. `public int getTotalQuestions()`：取得總題目數量（`questions.length`，即 9）。

### `MelancholyUI` 類別
1. `public static void main(String[] args)`：主程式進入點，使用 `EventQueue.invokeLater()` 在 AWT 事件分派執行緒（EDT）中啟動視窗。
2. `private void initialize()`：初始化介面佈局、設定視窗屬性（絕對定位 `null layout`）、新增各項 Swing 元件並綁定 `ActionListener`。
3. `private void startClock()`：建立 `javax.swing.Timer`（間隔 1000 毫秒），每秒以 `DateTimeFormatter` 格式化 `LocalDateTime.now()` 並更新至 `lblClock`。
4. `private void loadQuestion(int index)`：切換當前題號，更新題目標籤文字，清空並同步單選按鈕的選取狀態（若先前已作答則自動勾選對應選項），並動態啟用/禁用按鈕與變更按鈕文字。

---

## 12. OOP 重點
1. **封裝性（Encapsulation）**：
   - `Melancholy` 與 `MelancholyUI` 的所有成員欄位（Fields）皆宣告為 `private`，避免外部直接修改內部狀態。
   - 外部只能透過公開的 Getter / Setter 與方法（如 `setScore()`, `getEvaluationResult()`）進行存取與運算。
2. **職責分離（Separation of Concerns / MVC 概念）**：
   - 介面排版、事件捕捉與彈跳視窗屬於 View / Controller（`MelancholyUI`）。
   - 計分規則、醫學評估文字與資料存儲屬於 Model（`Melancholy`）。兩者互不雜揉，便於維護與未來升級。
3. **物件的多型與介面實作（Polymorphism & Interface）**：
   - 大量使用 `java.awt.event.ActionListener` 介面，透過匿名內部類別（Anonymous Inner Class）實作 `actionPerformed(ActionEvent e)`，體現回呼（Callback）機制。

---

## 13. Array / Collection 說明
本專案使用一維陣列（1D Array）作為核心資料結構：
1. `String[] questions`：儲存定長的題目清單。因題目數量固定為 9 題，陣列具有最高效的記憶體連續性與存取效能。
2. `String[] options`：儲存 4 個固定的評分選項字串。
3. `int[] scores`：記錄 9 道題目的得分狀態。長度與題目數完全對應，透過 `questionIdx` 作為下標進行 O(1) 複雜度之快速讀取與更新。使用值 `-1` 明確代表「尚未作答」狀態。
4. `JRadioButton[] rbOptions`：管理 GUI 中的 4 個選項按鈕，方便使用 `for` 迴圈一次性初始化位置、字型、群組加入與事件綁定，大幅減少重複程式碼。

---

## 14. GUI 元件
| 元件類別 | 變數名稱 | 用途與特點 |
| :--- | :--- | :--- |
| `JFrame` | `frame` | 主視窗容器，尺寸設為 720×520，關閉行為設為 `EXIT_ON_CLOSE` |
| `JLabel` | `lblHeader` | 標題文字「憂鬱檢測自我篩檢量表」，字型微軟正黑體 18 粗體 |
| `JLabel` | `lblClock` | 顯示當前年月日時分秒，靠右對齊 |
| `JLabel` | `lblQuestionTitle` | 顯示目前第 N 題題目內容 |
| `JRadioButton[]` | `rbOptions` | 4 個單選按鈕，呈現四個程度選項與分數 |
| `ButtonGroup` | `btnGroup` | 單選按鈕邏輯群組，確保同一題僅能單選 |
| `JButton` | `btnPrev` | 回到上一題（第一題時自動停用 `setEnabled(false)`） |
| `JButton` | `btnNext` | 前往下一題；最後一題時文字自動轉為「看結果」 |
| `JTextArea` | `taResult` | 呈現檢測總分與評估建議，設定 `setEditable(false)` 防止篡改 |
| `JScrollPane` | `scrollPane` | 包裹 `taResult`，提供垂直與水平捲軸支援 |
| `JButton` | `btnReset` | 清空所有分數與結果，回到第 1 題 |
| `JButton` | `btnPrint` | 啟動 Java 列印對話框列印檢測結果 |
| `JButton` | `btnExit` | 關閉並結束程式 |

---

## 15. Event Handling
1. **選項點擊事件**：
   - 每個 `JRadioButton` 綁定獨立的 `ActionListener`，於點擊時將對應的分數（0 ~ 3）透過 `model.setScore(currentIndex, scoreVal)` 寫入模型中。
2. **「上一題」按鈕事件**：
   - 檢查 `model.getCurrentQuestionIndex() > 0`，成立則載入 `index - 1` 的題目並重繪選項。
3. **「下一題 / 看結果」按鈕事件**：
   - 檢查當前題目得分是否為 `-1`。若為 `-1`，呼叫 `JOptionPane.showMessageDialog` 警告使用者作答。
   - 若非最後一題，載入 `index + 1` 題目。
   - 若已是最後一題，呼叫 `model.getEvaluationResult()` 將評估結果填入 `taResult`，並彈出完成對話框。
4. **「重新測驗」按鈕事件**：
   - 呼叫 `model.resetQuiz()`，清空 `taResult`，並呼叫 `loadQuestion(0)` 重設題號至第一題。
5. **「列印結果」按鈕事件**：
   - 檢查 `taResult` 是否為空，為空則跳出提示；非空則呼叫 `taResult.print()` 叫用系統列印精靈。
6. **時鐘排程事件**：
   - `javax.swing.Timer` 每 1000 毫秒觸發一次 `actionPerformed`，更新時間標籤。

---

## 16. 輸入資料
- **資料來源**：使用者在圖形介面上的單選按鈕選取。
- **選項與分值映射**：
  - `完全沒有` ──> **0 分**
  - `有幾天` ──> **1 分**
  - `超過一半的天數` ──> **2 分**
  - `幾乎每天` ──> **3 分**
- **資料防呆驗證**：未勾選時分數維持預設值 `-1`，系統阻擋跳題並強制要求完成。

---

## 17. 計算邏輯
- **總分累加**：
  $$	ext{Total Score} = \sum_{i=0}^{8} \max(0, 	ext{scores}[i])$$
  總分最高為 $9 	imes 3 = 27$ 分，最低為 0 分。
- **PHQ-9 臨床分級門檻**：
  | 總得分區間 | 評估程度 | 建議處置 |
  | :---: | :---: | :--- |
  | **0 ～ 4 分** | 無或極輕微憂鬱 | 請保持規律生活與良好心情。 |
  | **5 ～ 9 分** | 輕度憂鬱 | 建議多與親友傾訴，適度運動舒緩壓力。 |
  | **10 ～ 14 分** | 中度憂鬱 | 建議諮詢心理師或尋求醫療門診諮商。 |
  | **15 ～ 19 分** | 中重度憂鬱 | 建議積極前往身心科或精神科評估治療。 |
  | **20 ～ 27 分** | 重度憂鬱 | 強烈建議立即就醫尋求專業醫療協助。 |

---

## 18. 輸出結果
1. **即時時鐘**：格式為 `2026-09-17 16:30:00`。
2. **文字區域輸出（`taResult`）**：
   ```text
   總得分：12 分
   評估建議：中度憂鬱。建議諮詢心理師或尋求醫療門診諮商。
   ```
3. **對話框提示（`JOptionPane`）**：
   - 未填提示：「請先選擇本題選項再前往下一題！」
   - 完成提示：「已完成全部檢測！結果已顯示於下方。」
   - 列印提示：「列印完成！」或「列印失敗: ...」
4. **實體/PDF 列印輸出**：將文字區內容透過系統印表機輸出成紙本或 PDF 檔案。

---

## 19. 範例執行結果
### 範例情境 A：受測者作答得分較低
- **題目作答**：9 題皆選擇「完全沒有 (0分)」或「有幾天 (1分)」，加總 3 分。
- **介面輸出**：
  ```text
  總得分：3 分
  評估建議：無或極輕微憂鬱。請保持規律生活與良好心情。
  ```

### 範例情境 B：受測者壓力較大
- **題目作答**：部分題目為 2 分或 3 分，加總 16 分。
- **介面輸出**：
  ```text
  總得分：16 分
  評估建議：中重度憂鬱。建議積極前往身心科或精神科評估治療。
  ```

---

## 20. 執行方式

### 方式一：使用 IDE（Eclipse / IntelliJ IDEA）執行
1. 下載並解壓縮專案。
2. 開啟 Eclipse，選擇 `File` -> `Import` -> `Existing Projects into Workspace`，選取 `Melancholy` 資料夾。
3. 確認專案編碼為 `UTF-8`（`Properties` -> `Resource` -> `Text file encoding`）。
4. 在 `com.MelancholyUI.java` 上按右鍵，選擇 `Run As` -> `Java Application`。

### 方式二：使用終端機（Command Line / Terminal）編譯與執行
```bash
# 進入專案根目錄
cd Melancholy

# 編譯 Java 原始檔至 bin 目錄（指定 UTF-8 編碼）
javac -encoding UTF-8 -d bin src/com/Melancholy.java src/com/MelancholyUI.java

# 執行主程式
java -cp bin com.MelancholyUI
```

---

## 21. 操作方式
1. **檢視題目**：畫面載入時顯示第 1 題。
2. **進行作答**：在 4 個單選按鈕中選擇符合自身近況的選項。
3. **前往下一題**：點擊「下一題」按鈕。未作答將跳出警示訊息。
4. **回溯修改**：若欲檢查或變更前題選項，點擊「上一題」即可返回，已選選項會自動保留。
5. **產生報告**：作答至第 9 題時，右下按鈕會自動變為「看結果」，點擊後在下方文字區即可查閱評估結果。
6. **列印或重測**：
   - 點擊「列印結果」：調用列印對話框。
   - 點擊「重新測驗」：分數全部歸零並返回第一題。
   - 點擊「離開程式」：退出應用程式。

---

## 22. 重要程式碼

### 1. 動態計分與建議產出（`Melancholy.java`）
```java
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
```

### 2. 題目動態載入與狀態還原（`MelancholyUI.java`）
```java
private void loadQuestion(int index) {
    model.setCurrentQuestionIndex(index);
    lblQuestionTitle.setText(model.getQuestions()[index]);
    btnGroup.clearSelection(); // 先清除選取狀態

    int recordedScore = model.getScore(index);
    if (recordedScore >= 0 && recordedScore < 4) {
        rbOptions[recordedScore].setSelected(true); // 若先前已作答則自動勾選
    }

    btnPrev.setEnabled(index > 0); // 第一題時禁用上一題
    btnNext.setText(index == model.getTotalQuestions() - 1 ? "看結果" : "下一題");
}
```

### 3. 動態即時時鐘 Timer（`MelancholyUI.java`）
```java
private void startClock() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent e) {
            lblClock.setText(LocalDateTime.now().format(dtf));
        }
    });
    timer.start();
}
```

---

## 23. 常見錯誤
1. **中文亂碼問題（Encoding Mismatch）**：
   - 專案檔案為 `UTF-8` 編碼，若在 Windows 下以預設 `MS950 / Big5` 開啟或編譯，中文字串會顯示為亂碼或編譯錯誤。須確保編譯加入 `-encoding UTF-8`。
2. **NullPointerException（空指標異常）**：
   - 若在尚未實例化 `model` 時即呼叫 `loadQuestion()` 或 `model.getQuestions()`，會拋出 `NullPointerException`。
3. **Swing 執行緒安全警告（EDT Violation）**：
   - 直接在主執行緒初始化視窗元件可能引起非同步競態問題，應遵照 Swing 標準規範使用 `EventQueue.invokeLater` 啟動。
4. **列印異常（PrinterException）**：
   - 系統若未安裝實體印表機或虛擬印表機（如 Microsoft Print to PDF），呼叫 `taResult.print()` 可能拋出印表機連線異常，程式碼中已包含 `try-catch` 進行防禦。

---

## 24. Debug 重點
1. **檢查分數陣列預設值**：
   - 確保未作答題目分數為 `-1`，而非 `0`。若預設為 `0`，則防呆檢查會誤以為使用者已選擇「完全沒有 (0分)」，喪失未填驗證功能。
2. **單選按鈕群組同步問題**：
   - 換題時務必先呼叫 `btnGroup.clearSelection()`，再根據記錄的分數勾選。若未清除，可能導致多個題目間勾選狀態殘留或混亂。
3. **陣列索引邊界檢查（ArrayIndexOutOfBoundsException）**：
   - 題目切換時檢查邊界（`0 <= index < totalQuestions`），防止越界存取引發異常。
4. **按鈕狀態動態刷新驗證**：
   - 測試邊界條件：在第 1 題時「上一題」應為 Disabled；在第 9 題時「下一題」應顯示為「看結果」。

---

## 25. 練習題
1. **新增題目進度條（ProgressBar）**：
   - 在視窗中加入 `JProgressBar`，隨當前題號動態更新進度百分比（如完成第 3 題顯示 33%）。
2. **加入受測者個人基本資料**：
   - 在檢測前新增姓名、年齡、性別輸入框，並將這些資訊整合進最終的評估報告與列印頁面中。
3. **作答記錄儲存功能**：
   - 增加「儲存記錄」按鈕，將測驗時間、受測者代號與檢測總分、建議文字寫入本機 CSV 或文字檔。
4. **第 9 題自殺意念特別警示**：
   - PHQ-9 的第 9 題涉及自殺或自傷念頭。若該題得分大於 0（非 0 分），無論總分為何，在評估報告中額外插入紅色字樣的高風險關懷專線資訊（如 1925 安心專線）。

---

## 26. 進階延伸
1. **重構為現代化 JavaFX 介面**：
   - 將 Swing 視窗升級至 JavaFX，使用 FXML 進行 UI 與邏輯分離，並搭配 CSS 美化檢測量表外觀。
2. **資料庫持久化儲存（JDBC / SQLite）**：
   - 引入 SQLite 嵌入式資料庫，支援多次檢測歷史紀錄查詢與折線圖趨勢分析。
3. **擴充更多臨床量表**：
   - 運用繼承或策略模式（Strategy Pattern），將量表抽象化，支援切換為「GAD-7 廣泛性焦慮量表」或「BDI 貝克憂鬱量表」。
4. **跨平台匯出功能**：
   - 整合 iText 或 PDFBox 套件，一鍵生成格式精美的 PDF 健檢報告單。

---

## 27. 適合對象
- **Java 初學者與進階自學者**：希望透過完整專案實踐物件導向與 GUI 程式設計者。
- **大專院校資工/資管系學生**：適合修習「物件導向程式設計」、「Java 視窗程式設計」之期末專題或課堂作業。
- **醫學資訊與跨領域開發者**：希望了解心理評估量表如何以軟體工程化方式落地實現之開發者。

---

## 28. 先備知識
1. **Java 基本語法**：變數、資料型態、條件控制語句（`if-else`）、迴圈（`for`）。
2. **物件導向核心概念**：類別（Class）、物件（Object）、建構子（Constructor）、封裝（Encapsulation）。
3. **Java 陣列（Array）操作**：一維陣列宣告、存取、長度計算與邊界判斷。
4. **基本 Swing GUI 觀念**：元件層級概念（Container 與 Component）、常見按鈕與標籤。

---

## 29. 完成後能力
1. **具備獨立開發 Java Swing 桌面應用程式之能力**。
2. **能夠靈活運用 MVC 設計模式實現前後端邏輯解耦**。
3. **熟練處理視窗互動事件、動態排程（Timer）與防呆驗證機制**。
4. **能夠將實務領域量表（如心理衛生、健康評估）轉化為標準化軟體演算法與資料結構**。

---

## 30. 版本資訊
- **專案名稱**：Melancholy（憂鬱檢測量表 PHQ-9 系統）
- **當前版本**：v1.0.0
- **最新發布日期**：2026-09-11
- **授權協議**：MIT License / 教育用途開源
- **開發維護者**：Java 專題開發小組
