package DAO;
import Model.Account;
import Util.ConnectionUtil;
    
import java.sql.*;

public class AccountDAO {
    public Account insertAccount(Account account) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "INSERT INTO Account (username, password) VALUES (?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setString(1, account.getUsername());
        stmt.setString(2, account.getPassword());
        int affectedRows = stmt.executeUpdate();
        if (affectedRows == 1) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                return new Account(id, account.getUsername(), account.getPassword());
            }
        }
        return null;
    }

    public Account getAccountByUsername(String username) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM Account WHERE username = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, username);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Account(rs.getInt("account_id"), rs.getString("username"), rs.getString("password"));
        }
        return null;
    }
}
