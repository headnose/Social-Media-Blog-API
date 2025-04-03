package DAO;

import Model.Message;
import Util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    public Message insertMessage(Message message) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "INSERT INTO Message (posted_by, message_text, time_posted_epoch) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
        stmt.setInt(1, message.getPosted_by());
        stmt.setString(2, message.getMessage_text());
        stmt.setLong(3, message.getTime_posted_epoch());

        int affectedRows = stmt.executeUpdate();
        if (affectedRows == 1) {
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return new Message(
                        rs.getInt(1),
                        message.getPosted_by(),
                        message.getMessage_text(),
                        message.getTime_posted_epoch());
            }
        }
        return null;
    }

    public boolean userExists(int accountId) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM Account WHERE account_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, accountId);
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    }

    public List<Message> getAllMessages() throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM Message";
        PreparedStatement stmt = conn.prepareStatement(sql);
        ResultSet rs = stmt.executeQuery();

        List<Message> messages = new ArrayList<>();
        while (rs.next()) {
            Message m = new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
            messages.add(m);
        }

        return messages;
    }

    public Message getMessageById(int id) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM Message WHERE message_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();

        if (rs.next()) {
            return new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch"));
        }

        return null;
    }

    public Message deleteMessageById(int id) throws SQLException {
        Message toDelete = getMessageById(id);
        if (toDelete == null)
            return null;

        Connection conn = ConnectionUtil.getConnection();
        String sql = "DELETE FROM Message WHERE message_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, id);
        stmt.executeUpdate();

        return toDelete;
    }

    public Message updateMessageText(int messageId, String newText) throws SQLException {
        Message existing = getMessageById(messageId);
        if (existing == null)
            return null;

        Connection conn = ConnectionUtil.getConnection();
        String sql = "UPDATE Message SET message_text = ? WHERE message_id = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setString(1, newText);
        stmt.setInt(2, messageId);
        stmt.executeUpdate();

        return new Message(
                existing.getMessage_id(),
                existing.getPosted_by(),
                newText,
                existing.getTime_posted_epoch());
    }

    public List<Message> getMessagesByAccountId(int accountId) throws SQLException {
        Connection conn = ConnectionUtil.getConnection();
        String sql = "SELECT * FROM Message WHERE posted_by = ?";
        PreparedStatement stmt = conn.prepareStatement(sql);
        stmt.setInt(1, accountId);
        ResultSet rs = stmt.executeQuery();

        List<Message> messages = new ArrayList<>();
        while (rs.next()) {
            messages.add(new Message(
                    rs.getInt("message_id"),
                    rs.getInt("posted_by"),
                    rs.getString("message_text"),
                    rs.getLong("time_posted_epoch")));
        }

        return messages;
    }

}
