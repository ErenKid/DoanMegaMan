package com.gamestudio.userinterface;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerNameScreen extends JFrame {

    private JTextField nameField;
    private JButton startButton;

    public PlayerNameScreen() {
        setTitle("Enter Player Name");
        setSize(GameFrame.SCREEN_WIDTH, GameFrame.SCREEN_HEIGHT); // Sử dụng kích thước màn hình của trò chơi
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Tạo các thành phần giao diện
        nameField = new JTextField(20);
        startButton = new JButton("Start Game");

        // Thiết lập layout
        setLayout(new BorderLayout());
        JPanel panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        
        gbc.gridx = 0;
        gbc.gridy = 0;
        panel.add(new JLabel("Enter your name:"), gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        panel.add(nameField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        panel.add(startButton, gbc);

        add(panel, BorderLayout.CENTER);

        // Lắng nghe sự kiện nút "Start Game"
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText();
                if (!playerName.trim().isEmpty()) {
                    // Chuyển sang màn hình game và truyền tên người chơi
                    GameFrame gameFrame = new GameFrame(playerName);
                    gameFrame.startGame();
                    dispose(); // Đóng màn hình nhập tên
                } else {
                    JOptionPane.showMessageDialog(PlayerNameScreen.this, "Please enter your name.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }
}
