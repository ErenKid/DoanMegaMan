package com.gamestudio.userinterface;

import com.gamestudio.effect.CacheDataLoader;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GameFrame extends JFrame {

    public static final int SCREEN_WIDTH = 1000;
    public static final int SCREEN_HEIGHT = 600;
    private String playerName;
    private GamePanel gamePanel;

    // Constructor chính với playerName
    public GameFrame(String playerName) {
        super("Mega Man Java Game");
        this.playerName = playerName;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = this.getToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        
        try {
            CacheDataLoader.getInstance().LoadData();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.setBounds((screenSize.width - SCREEN_WIDTH) / 2, (screenSize.height - SCREEN_HEIGHT) / 2, SCREEN_WIDTH, SCREEN_HEIGHT);

        gamePanel = new GamePanel(playerName);
        addKeyListener(gamePanel);
        add(gamePanel);
    }

    // Constructor với playerName và userId (nếu cần sử dụng)
    public GameFrame(String playerName, int userId) {
        super("Mega Man Java Game");
        this.playerName = playerName;
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Toolkit toolkit = this.getToolkit();
        Dimension screenSize = toolkit.getScreenSize();
        
        try {
            CacheDataLoader.getInstance().LoadData();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        this.setBounds((screenSize.width - SCREEN_WIDTH) / 2, (screenSize.height - SCREEN_HEIGHT) / 2, SCREEN_WIDTH, SCREEN_HEIGHT);

        gamePanel = new GamePanel(playerName);
        addKeyListener(gamePanel);
        add(gamePanel);
        
        // Có thể sử dụng userId để tùy chỉnh gamePanel hoặc thực hiện các hành động khác
        // Ví dụ: gamePanel.setUserId(userId);
    }

    // Phương thức bắt đầu trò chơi
    public void startGame() {
        gamePanel.startGame();
        this.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new PlayerNameScreen().setVisible(true));
    }
}
