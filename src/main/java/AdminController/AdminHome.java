package AdminController;

import BlogController.App;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashMap;

public class AdminHome extends App {

    public static void getBlogHome(Context ctx) {

        ctx.sessionAttribute("loginRedirect", ctx.path());

        HashMap<String, Object> renderData = new HashMap<>();

        Session session = DatabaseController.sf.openSession();

        Query queryArticleNumber = session.createQuery("select count(*) from Articles");
        Query queryCategoryNumber = session.createQuery("select count(*) from Category");
        Query queryUserNumber = session.createQuery("select count(*) from User");
        Query querySubscriberNumber = session.createQuery("select count(*) from Email");

        long totalArticles = (long) queryArticleNumber.uniqueResult();
        long totalCategories = (long) queryCategoryNumber.uniqueResult();
        long totalUsers = (long) queryUserNumber.uniqueResult();
        long totalSubscribers = (long) querySubscriberNumber.uniqueResult();

        renderData.put("totalArticles", totalArticles);
        renderData.put("totalCategories", totalCategories);
        renderData.put("totalUsers", totalUsers);
        renderData.put("totalSubscribers", totalSubscribers);
        renderData.put("originalDomain", domain);

        ctx.render("templates/admin/index.html.pebble", renderData);
    }
}
