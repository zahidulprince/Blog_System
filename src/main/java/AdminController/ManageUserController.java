package AdminController;

import BlogArchitecture.Articles;
import BlogArchitecture.User;
import BlogController.App;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;

import static Util.ViewUtil.baseModel;

public class ManageUserController extends App {
    public static void getManageUser(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session session = DatabaseController.sf.openSession();

        List<User> userList;
        Map<String, Object> model = baseModel(ctx);

        userList = session.createQuery("FROM User order by id", User.class).getResultList();
        model.put("users", userList);

        if (ctx.sessionAttribute("isRedirected") == "fromManageUsers") {
            model.put("deleted", true);
        }
        if (ctx.sessionAttribute("isRedirected") == "isRedirectedAfterUpdate") {
            model.put("updated", true);
        }
        if (ctx.sessionAttribute("isRedirected") == "isRedirectedUserExists") {
            model.put("alreadyThere", true);
        }

        ctx.render("templates/admin/User/manageUsers.html.pebble", model);
    }

    public static void askToSelectOptionsToUpdate(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Map<String, Object> model = baseModel(ctx);
        model.put("askForm", true);

        ctx.render("templates/admin/User/addUser.html.pebble", model);
    }

    public static void getUserEditForm(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Map<String, Object> model = baseModel(ctx);

        String name = ctx.queryParam("name");
        String email = ctx.queryParam("email");
        String password = ctx.queryParam("password");

        if (name == null && email == null && password == null) {
            model.put("nothingSelected", true);
            ctx.render("templates/admin/User/addUser.html.pebble", model);
        }
//---------------------------------------------------------------------
        if (name != null && email == null && password == null) {
            model.put("nameUpdateForm", true);
            ctx.render("templates/admin/User/addUser.html.pebble", model);
        }
        if (name == null && email != null && password == null) {
            model.put("emailUpdateForm", true);
            ctx.render("templates/admin/User/addUser.html.pebble", model);
        }
        if (name == null && email == null && password != null) {
            model.put("passwordUpdateForm", true);
            ctx.render("templates/admin/User/addUser.html.pebble", model);
        }
//----------------------------------------------------------------------
        if (name != null && email != null && password == null) {
            model.put("nameAndEmailUpdateForm", true);
            ctx.render("templates/admin/User/addUser.html.pebble", model);
        }
        if (name != null && email == null && password != null) {
            model.put("nameAndPasswordUpdateForm", true);
            ctx.render("templates/admin/User/addUser.html.pebble", model);
        }
        if (name == null && email != null && password != null) {
            model.put("emailAndPasswordUpdateForm", true);
            ctx.render("templates/admin/User/addUser.html.pebble", model);
        }
//----------------------------------------------------------------------
        if (name != null && email != null && password != null) {
            model.put("allUpdateForm", true);
            ctx.render("templates/admin/User/addUser.html.pebble", model);
        }
    }

    public static void deleteUser(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        int userId = Integer.parseInt(ctx.pathParam("un"));

        User user = s.get(User.class, userId);

        for (Articles articles: user.getArticles()){
            s.delete(articles);
        }

        s.delete(user);

        ctx.redirect(domain + "/admin/manageUsers");
        ctx.sessionAttribute("isRedirected", "fromManageUsers");

        tx.commit();
        s.close();
    }

    public static void editUser(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        Map<String, Object> model = baseModel(ctx);
        List<User> userList;
        boolean passwordMatched = false;
        boolean emailIsThere = false;
        int userId = 0;

        String nameNew = ctx.formParam("userNameNew");
        String emailNew = ctx.formParam("userEmailNew");
        String passNew = ctx.formParam("userPassNew");
        String passOld = ctx.formParam("userPassOld");

        userList = s.createQuery("FROM User", User.class).getResultList();

        for (User user: userList) {
            if (emailNew != null && emailNew.equals(user.getEmail())) {
                emailIsThere = true;
            }
            if (passOld.equals(user.getPassword())) {
                passwordMatched = true;
                userId = user.getId();
                break;
            }
        }

        if (passwordMatched) {
            if (nameNew != null && emailNew == null && passNew == null) {
                updateUser(ctx, model, s, tx, nameNew, null, null, userId);
            }
            if (nameNew == null && emailNew != null && passNew == null) {
                if (!emailIsThere) {
                    updateUser(ctx, model, s, tx, null, emailNew, null, userId);
                } else {
                    System.out.println("am at the top");
                    ctx.sessionAttribute("isRedirected", "isRedirectedUserExists");
                    ctx.redirect(domain + "/admin/manageUsers");
                }
            }
            if (nameNew == null && emailNew == null && passNew != null) {
                updateUser(ctx, model, s, tx, null, null, passNew, userId);
            }
            if (nameNew != null && emailNew != null && passNew == null) {
                updateUser(ctx, model, s, tx, nameNew, emailNew, null, userId);
            }
        } else {
            model.put("wrongPass", true);
            ctx.render("templates/admin/User/addUser.html.pebble", model);
        }
    }

    private static void updateUser(Context ctx, Map<String, Object> model, Session s, Transaction tx, String nameNew, String emailNew, String passNew, int userId) {
        User userToUpdate = s.get(User.class, userId);

        if (nameNew != null && emailNew == null && passNew == null) {
            userToUpdate.setName(nameNew);
        }
        if (nameNew == null && emailNew != null && passNew == null) {
            userToUpdate.setEmail(emailNew);
        }
        if (nameNew == null && emailNew == null && passNew != null) {
            userToUpdate.setPassword(passNew);
        }
//----------------------------------------------------------------------
        if (nameNew != null && emailNew != null && passNew == null) {
            userToUpdate.setName(nameNew);
            userToUpdate.setEmail(emailNew);
        }
        if (nameNew != null && emailNew == null && passNew != null) {
            userToUpdate.setName(nameNew);
            userToUpdate.setPassword(passNew);
        }
        if (nameNew == null && emailNew != null && passNew != null) {
            userToUpdate.setEmail(emailNew);
            userToUpdate.setPassword(passNew);
        }
//----------------------------------------------------------------------
        if (nameNew != null && emailNew != null && passNew != null) {
            userToUpdate.setName(nameNew);
            userToUpdate.setEmail(emailNew);
            userToUpdate.setPassword(passNew);
        }

        s.update(userToUpdate);
        s.save(userToUpdate);
        tx.commit();
        s.close();
        model.put("updated", true);
        System.out.println("am at the bottom");
        ctx.sessionAttribute("isRedirected", "isRedirectedAfterUpdate");
        ctx.redirect(domain + "/admin/manageUsers");
    }
}
