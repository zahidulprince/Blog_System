package AdminController;

import BlogArchitecture.User;
import BlogController.App;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;

public class AddUserController extends App {
    public static void getAddUser(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        HashMap<String, Object> renderData = new HashMap<>();
        renderData.put("form", true);
        renderData.put("originalDomain", domain);

        ctx.render("templates/admin/User/addUser.html.pebble", renderData);
    }

    public static void addUser(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        HashMap<String, Object> renderData = new HashMap<>();
        List<User> allUsers;

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        String userName = ctx.formParam("userName");
        String userEmail = ctx.formParam("userEmail");
        String userPass = ctx.formParam("userPass");

        boolean isThere = false;

        allUsers = s.createQuery("FROM User", User.class).getResultList();

        if (allUsers.isEmpty()) {
            createUser(ctx, renderData, s, tx, userName, userEmail, userPass);
        }else {
            for (User allUser : allUsers) {
                if (userEmail.equals(allUser.getEmail())) {
                    isThere = true;
                    break;
                }
            }
            if (!isThere) {
                createUser(ctx, renderData, s, tx, userName, userEmail, userPass);
            } else {
                renderData.put("addedAlready", true);
                ctx.render("templates/admin/User/addUser.html.pebble", renderData);
            }
        }
    }

    private static void createUser(Context ctx, HashMap<String, Object> renderData, Session s, Transaction tx, String userName, String userEmail, String userPass) {
        User user = new User();
        user.setName(userName);
        user.setEmail(userEmail);
        user.setPassword(userPass);
        s.save(user);
        tx.commit();
        s.close();
        renderData.put("added", true);
        ctx.render("templates/admin/User/addUser.html.pebble", renderData);
    }
}
