package Controller;

import java.util.List;

import DAO.AccountDAO;
import Service.AccountService;
import io.javalin.Javalin;
import io.javalin.http.Context;
import Model.Account;
import Model.Message;
import DAO.MessageDAO;
import Service.MessageService;

/**
 * TODO: You will need to write your own endpoints and handlers for your
 * controller. The endpoints you will need can be
 * found in readme.md as well as the test cases. You should
 * refer to prior mini-project labs and lecture materials for guidance on how a
 * controller may be built.
 */
public class SocialMediaController {
    /**
     * In order for the test cases to work, you will need to write the endpoints in
     * the startAPI() method, as the test
     * suite must receive a Javalin object from this method.
     * 
     * @return a Javalin app object which defines the behavior of the Javalin
     *         controller.
     */
    AccountService accountService = new AccountService(new AccountDAO());
    MessageService messageService = new MessageService(new MessageDAO());

    public Javalin startAPI() {
        Javalin app = Javalin.create();
        app.post("/register", this::handleRegister);
        app.post("/login", this::handleLogin);
        app.post("/messages", this::handleCreateMessage);
        app.get("/messages", this::handleGetAllMessages);
        app.get("/messages/{message_id}", this::handleGetMessageById);
        app.delete("/messages/{message_id}", this::handleDeleteMessageById);
        app.patch("/messages/{message_id}", this::handleUpdateMessageText);
        app.get("/accounts/{account_id}/messages", this::handleGetMessagesByUser);

        return app;
    }

    /**
     * This is an example handler for an example endpoint.
     * 
     * @param context The Javalin Context object manages information about both the
     *                HTTP request and response.
     */
    private void handleRegister(Context ctx) {
        Account account = ctx.bodyAsClass(Account.class);

        try {
            Account newAccount = accountService.register(account);
            if (newAccount != null) {
                ctx.json(newAccount);
            } else {
                ctx.status(400);
                ctx.result("");
            }
        } catch (Exception e) {
            ctx.status(400);
            ctx.result("");
        }
    }

    private void handleCreateMessage(Context ctx) {
        Message msg = ctx.bodyAsClass(Message.class);
        try {
            Message saved = messageService.createMessage(msg);
            if (saved != null) {
                ctx.json(saved);
            } else {
                ctx.status(400);
                ctx.result("");
            }
        } catch (Exception e) {
            ctx.status(400);
            ctx.result("");
        }
    }

    private void handleLogin(Context ctx) {
        Account loginAttempt = ctx.bodyAsClass(Account.class);
        try {
            Account foundAccount = accountService.login(loginAttempt);
            if (foundAccount != null) {
                ctx.json(foundAccount);
            } else {
                ctx.status(401);
                ctx.result("");
            }
        } catch (Exception e) {
            ctx.status(401);
            ctx.result("");
        }
    }

    private void handleGetAllMessages(Context ctx) {
        try {
            List<Message> messages = messageService.getAllMessages();
            ctx.json(messages);
        } catch (Exception e) {
            ctx.status(500);
            ctx.result("Error retrieving messages");
        }
    }

    private void handleGetMessageById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("message_id"));
            Message m = messageService.getMessageById(id);
            if (m != null) {
                ctx.json(m);
            } else {
                ctx.result("");
            }
        } catch (Exception e) {
            ctx.status(400);
            ctx.result("Invalid message ID");
        }
    }

    private void handleDeleteMessageById(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("message_id"));
            Message deleted = messageService.deleteMessageById(id);
            if (deleted != null) {
                ctx.json(deleted);
            } else {
                ctx.result("");
            }
        } catch (Exception e) {
            ctx.status(400);
            ctx.result("Invalid message ID");
        }
    }

    private void handleUpdateMessageText(Context ctx) {
        try {
            int id = Integer.parseInt(ctx.pathParam("message_id"));
            Message temp = ctx.bodyAsClass(Message.class);

            Message updated = messageService.updateMessageText(id, temp.getMessage_text());
            if (updated != null) {
                ctx.json(updated);
            } else {
                ctx.status(400);
                ctx.result("");
            }
        } catch (Exception e) {
            ctx.status(400);
            ctx.result("");
        }
    }

    private void handleGetMessagesByUser(Context ctx) {
        try {
            int accountId = Integer.parseInt(ctx.pathParam("account_id"));
            List<Message> messages = messageService.getMessagesByAccountId(accountId);
            ctx.json(messages);
        } catch (Exception e) {
            ctx.status(400);
            ctx.result("");
        }
    }

}
