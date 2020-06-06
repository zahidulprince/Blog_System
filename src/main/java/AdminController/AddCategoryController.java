package AdminController;

import BlogArchitecture.Category;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.HashMap;
import java.util.List;

public class AddCategoryController {
    public static void getAddCategory(Context ctx) {

        ctx.sessionAttribute("loginRedirect", ctx.path());

        HashMap<String, Object> renderData = new HashMap<>();
        renderData.put("form", true);

        ctx.render("templates/admin/Category/addCategory.html.pebble", renderData);
    }

    public static void addCategory(Context ctx) {

        ctx.sessionAttribute("loginRedirect", ctx.path());

        HashMap<String, Object> renderData = new HashMap<>();
        List<Category> allCategories;

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        String categoryName = ctx.formParam("addCat");

        boolean isThere = false;

        allCategories = s.createQuery("FROM Category", Category.class).getResultList();

        if (allCategories.isEmpty()) {
            createCategory(ctx, renderData, s, tx, categoryName);
        } else {

            for (Category allCategory : allCategories) {
                if (categoryName.equals(allCategory.getName())) {
                    isThere = true;
                    break;
                }
            }

            if (!isThere) {
                createCategory(ctx, renderData, s, tx, categoryName);
            } else {
                renderData.put("addedAlready", true);
                ctx.render("templates/admin/Category/addCategory.html.pebble", renderData);
            }
        }
    }

    private static void createCategory(Context ctx, HashMap<String, Object> renderData, Session s, Transaction tx, String categoryName) {
        Category category = new Category();
        category.setName(categoryName);
        s.save(category);
        tx.commit();
        s.close();
        renderData.put("added", true);
        ctx.render("templates/admin/Category/addCategory.html.pebble", renderData);
    }
}
