package AdminController;

import BlogArchitecture.Articles;
import BlogArchitecture.Category;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;

public class ManageCategoriesController {
    public static void getManageCategories(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session session = DatabaseController.sf.openSession();

        List<Category> renderCategory;
        HashMap<String, Object> renderData = new HashMap<>();

        renderCategory = session.createQuery("FROM Category order by id", Category.class).getResultList();

        renderData.put("categories", renderCategory);

        ctx.render("templates/admin/Category/manageCategories.html.pebble", renderData);
    }


    public static void deleteCategory(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        List<Category> renderCategory;
        List<Articles> renderArticles;
        HashMap<String, Object> renderData = new HashMap<>();

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        String str = ctx.pathParam("cn");
        int categoryID = Integer.parseInt(str);

        Category category = s.get(Category.class, categoryID);

        for (Articles articles: category.getArticles()){
            s.delete(articles);
        }

        s.delete(category);

        renderCategory = s.createQuery("FROM Category order by id", Category.class).getResultList();

        renderData.put("categories", renderCategory);

        ctx.redirect("http://localhost:7000/admin/manageCategories");
        ctx.render("templates/admin/Category/manageCategories.html.pebble", renderData);

        tx.commit();
        s.close();
    }
}
