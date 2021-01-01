package AdminController;

import BlogArchitecture.User;
import BlogController.App;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;

import static Util.ViewUtil.baseModel;

public class AddUserController extends App {
    public static void getAddUser(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Map<String, Object> model = baseModel(ctx);
        model.put("form", true);

        ctx.render("templates/admin/User/addUser.html.pebble", model);
    }

    public static void addUser(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Map<String, Object> model = baseModel(ctx);
        List<User> userList;

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        String userName = ctx.formParam("userName");
        String userEmail = ctx.formParam("userEmail");
        String userPass = ctx.formParam("userPass");

        boolean isThere = false;

        userList = s.createQuery("FROM User", User.class).getResultList();

        if (userList.isEmpty()) {
            createUser(ctx, model, s, tx, userName, userEmail, userPass);
        }else {
            for (User allUser : userList) {
                if (userEmail.equals(allUser.getEmail())) {
                    isThere = true;
                    break;
                }
            }
            if (!isThere) {
                createUser(ctx, model, s, tx, userName, userEmail, userPass);
            } else {
                model.put("addedAlready", true);
                ctx.render("templates/admin/User/addUser.html.pebble", model);
            }
        }
    }

    private static void createUser(Context ctx, Map<String, Object> model, Session s, Transaction tx, String userName, String userEmail, String userPass) {
        User user = new User();
        user.setName(userName);
        user.setEmail(userEmail);
        user.setPassword(userPass);
        s.save(user);
        tx.commit();
        s.close();
        model.put("added", true);
        ctx.render("templates/admin/User/addUser.html.pebble", model);
    }
}