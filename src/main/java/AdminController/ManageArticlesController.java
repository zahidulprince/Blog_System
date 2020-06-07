package AdminController;

import BlogArchitecture.Articles;
import BlogArchitecture.User;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;

public class ManageArticlesController {
    public static void getManageArticles(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session session = DatabaseController.sf.openSession();

        List<Articles> renderArticle;
        HashMap<String, Object> renderData = new HashMap<>();

        renderArticle = session.createQuery("FROM Articles order by id", Articles.class).getResultList();

        renderData.put("articles", renderArticle);
        if (ctx.sessionAttribute("isRedirected") == "delete") {
            renderData.put("deleted", true);
        }

        ctx.render("templates/admin/Article/manageArticles.html.pebble", renderData);
    }

    public static void deleteArticle(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        String str = ctx.pathParam("an");
        int articleId = Integer.parseInt(str);

        Articles articles = s.get(Articles.class, articleId);
        s.delete(articles);

        ctx.redirect("http://localhost:7000/admin/manageArticles");
        ctx.sessionAttribute("isRedirected", "delete");

        tx.commit();
        s.close();
    }

    public static void editArticle(Context ctx) {
    }
}
