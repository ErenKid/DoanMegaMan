package com.gamestudio.userinterface;

import com.gamestudio.database.DatabaseManager;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class PlayerNameScreen extends JFrame {

    private JTextField nameField;
    private JButton startButton;
    private DatabaseManager dbManager;

    public PlayerNameScreen() {
        dbManager = new DatabaseManager();

        setTitle("Enter Player Name");
        setSize(800, 600);
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

        gbc.gridy = 1;
        panel.add(nameField, gbc);

        gbc.gridy = 2;
        panel.add(startButton, gbc);

        add(panel, BorderLayout.CENTER);

        // Lắng nghe sự kiện nút "Start Game"
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String playerName = nameField.getText().trim();
                if (!playerName.isEmpty()) {
                    int userId = dbManager.getUserId(playerName);

                    if (userId == -1) {
                        JOptionPane.showMessageDialog(PlayerNameScreen.this, "User does not exist. Please use a different name.", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        // Lưu lịch sử trò chơi với điểm số ban đầu là 0
                        dbManager.saveGameHistory(userId, 0);

                        // Chuyển sang màn hình game và truyền tên người chơi
                        GameFrame gameFrame = new GameFrame(playerName, userId);
                        gameFrame.startGame();
                        dispose(); // Đóng màn hình nhập tên
                    }
                } else {
                    JOptionPane.showMessageDialog(PlayerNameScreen.this, "Please enter your name.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlayerNameScreen().setVisible(true));
    }
}
