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
    }
}
