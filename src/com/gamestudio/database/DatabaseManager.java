package com.gamestudio.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseManager {
    private static final String URL = "jdbc:mysql://localhost:3306/game_db";
    private static final String USER = "root";
    private static final String PASSWORD = "248600";
    
    private Connection connection;

    public DatabaseManager() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/game_db", "root", "248600");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Lấy user_id theo username, tạo mới nếu không tồn tại
    public int getUserId(String username) {
        int userId = -1;
        String query = "SELECT user_id FROM users WHERE username = ?";
        
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                userId = rs.getInt("user_id");
            } else {
                // Tạo người dùng mới nếu không tồn tại
                String insertQuery = "INSERT INTO users (username) VALUES (?)";
                try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery, PreparedStatement.RETURN_GENERATED_KEYS)) {
                    insertStmt.setString(1, username);
                    insertStmt.executeUpdate();
                    ResultSet generatedKeys = insertStmt.getGeneratedKeys();
                    if (generatedKeys.next()) {
                        userId = generatedKeys.getInt(1);
                        // Tạo bảng player_scores nếu chưa có
                        String scoreQuery = "INSERT INTO player_scores (user_id, high_score) VALUES (?, 0)";
                        try (PreparedStatement scoreStmt = connection.prepareStatement(scoreQuery)) {
                            scoreStmt.setInt(1, userId);
                            scoreStmt.executeUpdate();
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return userId;
    }

    // Cập nhật điểm cao nhất của người dùng
    public void updateHighScore(int userId, int highScore) {
        if (userExists(userId)) {
            int currentHighScore = getHighScore(userId);
            
            if (highScore > currentHighScore) {
                String query = "UPDATE player_scores SET high_score = ? WHERE user_id = ?";
                try (PreparedStatement stmt = connection.prepareStatement(query)) {
                    stmt.setInt(1, highScore);
                    stmt.setInt(2, userId);
                    int rowsUpdated = stmt.executeUpdate();
                    if (rowsUpdated > 0) {
                        System.out.println("High score updated successfully.");
                    } else {
                        System.out.println("No rows updated. User might not exist in player_scores.");
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            System.out.println("User ID does not exist: " + userId);
        }
    }

    // Lưu lịch sử lần chơi của người dùng
    public boolean saveGameHistory(int userId, int score) {
        if (userExists(userId)) {
            boolean isSaved = false;
            
            String checkQuery = "SELECT MAX(score) AS max_score FROM game_history WHERE user_id = ?";
            try (PreparedStatement checkStmt = connection.prepareStatement(checkQuery)) {
                checkStmt.setInt(1, userId);
                ResultSet rs = checkStmt.executeQuery();
                int maxScore = 0;
                if (rs.next()) {
                    maxScore = rs.getInt("max_score");
                }

                if (score > maxScore) {
                    String insertQuery = "INSERT INTO game_history (user_id, score, date_played) VALUES (?, ?, NOW())";
                    try (PreparedStatement insertStmt = connection.prepareStatement(insertQuery)) {
                        insertStmt.setInt(1, userId);
                        insertStmt.setInt(2, score);
                        int rowsAffected = insertStmt.executeUpdate();
                        isSaved = rowsAffected > 0;
                        if (isSaved) {
                            System.out.println("Game history saved with score: " + score);
                        }
                    }
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return isSaved;
        } else {
            System.out.println("User ID does not exist: " + userId);
            return false;
        }
    }

    // Lấy điểm cao nhất của người dùng
    public int getHighScore(int userId) {
        int highScore = -1;
        if (userExists(userId)) {
            String query = "SELECT high_score FROM player_scores WHERE user_id = ?";
            
            try (PreparedStatement stmt = connection.prepareStatement(query)) {
                stmt.setInt(1, userId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) {
                    highScore = rs.getInt("high_score");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("User ID does not exist: " + userId);
        }
        
        return highScore;
    }

    // Kiểm tra xem user_id có tồn tại trong bảng users không
    private boolean userExists(int userId) {
        String query = "SELECT 1 FROM users WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
