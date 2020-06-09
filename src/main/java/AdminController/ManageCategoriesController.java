package AdminController;

import BlogArchitecture.Articles;
import BlogArchitecture.Category;
import BlogController.App;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;

public class ManageCategoriesController extends App {
    public static void getManageCategories(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session session = DatabaseController.sf.openSession();

        List<Category> renderCategory;
        HashMap<String, Object> renderData = new HashMap<>();

        renderCategory = session.createQuery("FROM Category order by id", Category.class).getResultList();

        renderData.put("categories", renderCategory);
        renderData.put("originalDomain", domain);
        if (ctx.sessionAttribute("isRedirected") == "fromManageCategories") {
            renderData.put("deleted", true);
        }

        ctx.render("templates/admin/Category/manageCategories.html.pebble", renderData);
    }


    public static void deleteCategory(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        String str = ctx.pathParam("cn");
        int categoryID = Integer.parseInt(str);

        Category category = s.get(Category.class, categoryID);

        for (Articles articles: category.getArticles()){
            s.delete(articles);
        }

        s.delete(category);

        ctx.redirect(domain + "/admin/manageCategories");
        ctx.sessionAttribute("isRedirected", "fromManageCategories");

        tx.commit();
        s.close();
    }

    public static void editCategory(Context ctx) {

    }
}
