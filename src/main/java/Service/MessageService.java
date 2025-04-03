package Service;

import DAO.MessageDAO;
import Model.Message;

import java.sql.SQLException;
import java.util.List;

public class MessageService {
    MessageDAO messageDAO;

    public MessageService(MessageDAO dao) {
        this.messageDAO = dao;
    }

    public List<Message> getAllMessages() throws SQLException {
        return messageDAO.getAllMessages();
    }

    public Message createMessage(Message message) throws SQLException {
        if (message.getMessage_text() == null || message.getMessage_text().isBlank())
            return null;
        if (message.getMessage_text().length() > 255)
            return null;
        if (!messageDAO.userExists(message.getPosted_by()))
            return null;

        return messageDAO.insertMessage(message);
    }

    public Message getMessageById(int id) throws SQLException {
        return messageDAO.getMessageById(id);
    }

    public Message deleteMessageById(int id) throws SQLException {
        return messageDAO.deleteMessageById(id);
    }

    public Message updateMessageText(int messageId, String newText) throws SQLException {
        if (newText == null || newText.isBlank() || newText.length() > 255) {
            return null;
        }
        return messageDAO.updateMessageText(messageId, newText);
    }

    public List<Message> getMessagesByAccountId(int accountId) throws SQLException {
        return messageDAO.getMessagesByAccountId(accountId);
    }

}
