package AdminController;

import BlogArchitecture.Articles;
import BlogArchitecture.User;
import BlogController.App;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;

public class ManageUserController extends App {
    public static void getManageUser(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session session = DatabaseController.sf.openSession();

        List<User> renderUser;
        HashMap<String, Object> renderData = new HashMap<>();

        renderUser = session.createQuery("FROM User order by id", User.class).getResultList();

        renderData.put("users", renderUser);
        renderData.put("originalDomain", domain);
        if (ctx.sessionAttribute("isRedirected") == "fromManageUsers") {
            renderData.put("deleted", true);
        }

        ctx.render("templates/admin/User/manageUsers.html.pebble", renderData);
    }

    public static void deleteUser(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        String str = ctx.pathParam("un");
        int userId = Integer.parseInt(str);

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
