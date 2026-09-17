package com;

import java.awt.EventQueue;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.swing.*;

public class MelancholyUI {

    private JFrame frame;
    private JLabel lblClock;
    private JLabel lblQuestionTitle;
    private JRadioButton[] rbOptions;
    private ButtonGroup btnGroup;
    private JTextArea taResult;
    private JButton btnPrev;
    private JButton btnNext;
    private JButton btnReset;
    private JButton btnPrint;
    private JButton btnExit;

    
    private Melancholy model;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                	MelancholyUI window = new MelancholyUI();
                    window.frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public MelancholyUI() {
        model = new Melancholy();
        initialize();
        startClock();
        loadQuestion(0);
    }

    private void initialize() {
        frame = new JFrame();
        frame.setTitle("憂鬱檢測量表 (PHQ-9)");
        frame.setBounds(100, 100, 720, 520);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setLayout(null);

        
        JLabel lblHeader = new JLabel("憂鬱檢測自我篩檢量表");
        lblHeader.setFont(new Font("微軟正黑體", Font.BOLD, 18));
        lblHeader.setBounds(30, 15, 220, 30);
        frame.getContentPane().add(lblHeader);

        lblClock = new JLabel("時間載入中...");
        lblClock.setFont(new Font("微軟正黑體", Font.PLAIN, 14));
        lblClock.setHorizontalAlignment(SwingConstants.RIGHT);
        lblClock.setBounds(450, 20, 230, 25);
        frame.getContentPane().add(lblClock);

        
        lblQuestionTitle = new JLabel("題目載入中...");
        lblQuestionTitle.setFont(new Font("微軟正黑體", Font.BOLD, 15));
        lblQuestionTitle.setBounds(30, 60, 640, 30);
        frame.getContentPane().add(lblQuestionTitle);

       
        btnGroup = new ButtonGroup();
        rbOptions = new JRadioButton[4];
        for (int i = 0; i < 4; i++) {
            rbOptions[i] = new JRadioButton(model.getOptions()[i]);
            rbOptions[i].setFont(new Font("微軟正黑體", Font.PLAIN, 14));
            rbOptions[i].setBounds(40, 100 + (i * 35), 250, 30);
            btnGroup.add(rbOptions[i]);
            frame.getContentPane().add(rbOptions[i]);

            final int scoreVal = i;
            rbOptions[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    model.setScore(model.getCurrentQuestionIndex(), scoreVal);
                }
            });
        }

        
        btnPrev = new JButton("上一題");
        btnPrev.setBounds(40, 250, 95, 30);
        frame.getContentPane().add(btnPrev);
        btnPrev.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (model.getCurrentQuestionIndex() > 0) {
                    loadQuestion(model.getCurrentQuestionIndex() - 1);
                }
            }
        });

        btnNext = new JButton("下一題");
        btnNext.setBounds(150, 250, 95, 30);
        frame.getContentPane().add(btnNext);
        btnNext.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                int cur = model.getCurrentQuestionIndex();
                if (model.getScore(cur) == -1) {
                    JOptionPane.showMessageDialog(frame, "請先選擇本題選項再前往下一題！");
                    return;
                }
                if (cur < model.getTotalQuestions() - 1) {
                    loadQuestion(cur + 1);
                } else {
                    taResult.setText(model.getEvaluationResult());
                    JOptionPane.showMessageDialog(frame, "已完成全部檢測！結果已顯示於下方。");
                }
            }
        });

        
        taResult = new JTextArea();
        taResult.setFont(new Font("微軟正黑體", Font.PLAIN, 13));
        taResult.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(taResult);
        scrollPane.setBounds(30, 300, 640, 100);
        frame.getContentPane().add(scrollPane);

       
        btnReset = new JButton("重新測驗");
        btnReset.setBounds(140, 420, 110, 35);
        frame.getContentPane().add(btnReset);
        btnReset.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.resetQuiz();
                taResult.setText("");
                loadQuestion(0);
            }
        });

        btnPrint = new JButton("列印結果");
        btnPrint.setBounds(291, 420, 110, 35);
        frame.getContentPane().add(btnPrint);
        btnPrint.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (taResult.getText().trim().isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "尚未完成測驗，無法列印結果！");
                    return;
                }
                try {
                    boolean complete = taResult.print();
                    if (complete) {
                        JOptionPane.showMessageDialog(frame, "列印完成！");
                    }
                } catch (PrinterException pe) {
                    JOptionPane.showMessageDialog(frame, "列印失敗: " + pe.getMessage());
                }
            }
        });

        btnExit = new JButton("離開程式");
        btnExit.setBounds(437, 420, 110, 35);
        frame.getContentPane().add(btnExit);
        btnExit.addActionListener(new ActionListener() 
        {
            public void actionPerformed(ActionEvent e) 
            {
            		System.exit(0);
            }
                 
        });
    }

    
    private void startClock() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Timer timer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                lblClock.setText(LocalDateTime.now().format(dtf));
            }
        });
        timer.start();
    }

    
    private void loadQuestion(int index) {
        model.setCurrentQuestionIndex(index);
        lblQuestionTitle.setText(model.getQuestions()[index]);
        btnGroup.clearSelection();

        int recordedScore = model.getScore(index);
        if (recordedScore >= 0 && recordedScore < 4) {
            rbOptions[recordedScore].setSelected(true);
        }

        btnPrev.setEnabled(index > 0);
        btnNext.setText(index == model.getTotalQuestions() - 1 ? "看結果" : "下一題");
    }
}