package AdminController;

import BlogController.App;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.Map;

import static Util.ViewUtil.baseModel;

public class AdminHome extends App {

    public static void getBlogHome(Context ctx) {

        ctx.sessionAttribute("loginRedirect", ctx.path());

        Map<String, Object> model = baseModel(ctx);

        Session session = DatabaseController.sf.openSession();

        Query queryArticleNumber = session.createQuery("select count(*) from Articles");
        Query queryCategoryNumber = session.createQuery("select count(*) from Category");
        Query queryUserNumber = session.createQuery("select count(*) from User");
        Query querySubscriberNumber = session.createQuery("select count(*) from Email");

        long totalArticles = (long) queryArticleNumber.uniqueResult();
        long totalCategories = (long) queryCategoryNumber.uniqueResult();
        long totalUsers = (long) queryUserNumber.uniqueResult();
        long totalSubscribers = (long) querySubscriberNumber.uniqueResult();

        model.put("totalArticles", totalArticles);
        model.put("totalCategories", totalCategories);
        model.put("totalUsers", totalUsers);
        model.put("totalSubscribers", totalSubscribers);

        ctx.render("templates/admin/index.html.pebble", model);
    }
}
