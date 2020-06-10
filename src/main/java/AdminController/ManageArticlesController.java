package AdminController;

import BlogArchitecture.Articles;
import BlogController.App;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;

import static Util.ViewUtil.baseModel;

public class ManageArticlesController extends App {
    public static void getManageArticles(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session session = DatabaseController.sf.openSession();

        List<Articles> articlesList;
        Map<String, Object> model = baseModel(ctx);

        articlesList = session.createQuery("FROM Articles order by id", Articles.class).getResultList();

        model.put("articles", articlesList);
        if (ctx.sessionAttribute("isRedirected") == "fromManageArticles") {
            model.put("deleted", true);
        }

        ctx.render("templates/admin/Article/manageArticles.html.pebble", model);
    }

    public static void deleteArticle(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        int articleId = Integer.parseInt(ctx.pathParam("an"));

        Articles articleToDelete = s.get(Articles.class, articleId);
        s.delete(articleToDelete);

        ctx.redirect(domain + "/admin/manageArticles");
        ctx.sessionAttribute("isRedirected", "fromManageArticles");

        tx.commit();
        s.close();
    }

    public static void editArticle(Context ctx) {
    }

    public static void getArticleEditForm(Context context) {
    }
}
