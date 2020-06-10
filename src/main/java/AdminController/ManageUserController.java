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

        System.out.println(name);
        System.out.println(email);
        System.out.println(password);

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
        ctx.result("You are in edit user");
    }
}
