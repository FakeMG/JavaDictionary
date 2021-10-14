package groupId.JavaDictionary;

import java.sql.*;
import java.util.Locale;

public class dataBase {
    static Connection conn;
    static PreparedStatement pstmt;
    static Statement stmt;
    static final String DB_URL = "jdbc:sqlite:words.db";
    static final String READ = "SELECT * FROM words";
    static final String INSERT = "INSERT INTO words (english_word, meaning) SELECT ?, ? " +
            "WHERE NOT EXISTS (SELECT * FROM words WHERE words.english_word = ?)";
    static final String DELETE = "DELETE FROM words WHERE english_word IN (?)";
    static final String REPEAT = "SELECT english_word FROM words WHERE words.english_word = ?";
    static final String UPDATE = "UPDATE words SET meaning = ? WHERE english_word = ?";
    static final String LOOKUP = "SELECT * FROM words WHERE english_word LIKE ? || '%' ORDER BY english_word ASC, meaning DESC";

    public static void connect() {
        try {
            //open connection
            conn = DriverManager.getConnection(DB_URL);
            stmt = conn.createStatement();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void insertToDatabase(String englishWords, String meaning) {
        //TODO: thông báo trùng lặp
        try {
            //xóa dấu cách để tránh lỗi sau này
            englishWords = englishWords.replace(" ", "");

            //chuyển sang lower case để tránh lỗi
            englishWords = englishWords.toLowerCase(Locale.ROOT);
            meaning = meaning.toLowerCase(Locale.ROOT);


            pstmt = conn.prepareStatement(INSERT);
            pstmt.setString(1, englishWords);
            pstmt.setString(2, meaning);
            pstmt.setString(3, englishWords);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String readFromDatabase() {
        StringBuilder result = new StringBuilder();
        try {
            if (checkEmptyDatabase()) {
                System.out.println("Dictionary is empty! Please add more words!");
                return "";
            }

            //read
            ResultSet rs = stmt.executeQuery(READ);
            while (rs.next()) {
                result.append(rs.getString("english_word")).append("\t-\t")
                        .append(rs.getString("meaning")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result.toString();
    }

    public static void deleteFromDatabase(String target) {
        try {
            //xóa dấu cách để tránh lỗi sau này
            target = target.replace(" ", "");

            pstmt = conn.prepareStatement(DELETE);
            pstmt.setString(1, target);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateDatabase(String target, String explain) {
        try {
            pstmt = conn.prepareStatement(UPDATE);
            pstmt.setString(1, explain);
            pstmt.setString(2, target);
            pstmt.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    public static String lookup(String target) {
        StringBuilder result = new StringBuilder();
        try {
            pstmt = conn.prepareStatement(LOOKUP);
            pstmt.setString(1, target);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                result.append(rs.getString("english_word")).append(" \t-\t")
                        .append(rs.getString("meaning")).append("\n");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return result.toString();
    }

    public static boolean checkEmptyDatabase() throws SQLException {
        ResultSet rs = stmt.executeQuery(READ);
        return !rs.next();
    }

    public static boolean checkRepeatedWord(String target) {
        try {
            pstmt = conn.prepareStatement(REPEAT);
            pstmt.setString(1, target);
            ResultSet rs = pstmt.executeQuery();
            return rs.next();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return false;
    }
}
