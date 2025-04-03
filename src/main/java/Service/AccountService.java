package Service;
import DAO.AccountDAO;
import Model.Account;
import java.sql.SQLException;

public class AccountService {
    AccountDAO accountDAO;

    public AccountService(AccountDAO dao) {
        this.accountDAO = dao;
    }

    public Account register(Account account) throws SQLException {
        if (account.getUsername() == null || account.getUsername().isBlank()) return null;
        if (account.getPassword() == null || account.getPassword().length() < 4) return null;
        if (accountDAO.getAccountByUsername(account.getUsername()) != null) return null;

        return accountDAO.insertAccount(account);
    }

    public Account login(Account attempt) throws SQLException {
        Account actual = accountDAO.getAccountByUsername(attempt.getUsername());
        if (actual != null && actual.getPassword().equals(attempt.getPassword())) {
            return actual;
        }
        return null;
    }
}