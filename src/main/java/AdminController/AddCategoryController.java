package AdminController;

import BlogArchitecture.Category;
import BlogController.App;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;

import static Util.ViewUtil.baseModel;

public class AddCategoryController extends App {
    public static void getAddCategory(Context ctx) {

        ctx.sessionAttribute("loginRedirect", ctx.path());

        Map<String, Object> model = baseModel(ctx);
        model.put("form", true);

        ctx.render("templates/admin/Category/addCategory.html.pebble", model);
    }

    public static void addCategory(Context ctx) {

        ctx.sessionAttribute("loginRedirect", ctx.path());

        Map<String, Object> model = baseModel(ctx);
        List<Category> categoryList;

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        String categoryName = ctx.formParam("addCat");

        boolean isThere = false;

        categoryList = s.createQuery("FROM Category", Category.class).getResultList();

        if (categoryList.isEmpty()) {
            createCategory(ctx, model, s, tx, categoryName);
        } else {

            for (Category allCategory : categoryList) {
                if (categoryName.equals(allCategory.getName())) {
                    isThere = true;
                    break;
                }
            }

            if (!isThere) {
                createCategory(ctx, model, s, tx, categoryName);
            } else {
                model.put("addedAlready", true);
                ctx.render("templates/admin/Category/addCategory.html.pebble", model);
            }
        }
    }

    private static void createCategory(Context ctx, Map<String, Object> model, Session s, Transaction tx, String categoryName) {
        Category category = new Category();
        category.setName(categoryName);
        s.save(category);
        tx.commit();
        s.close();
        model.put("added", true);
        ctx.render("templates/admin/Category/addCategory.html.pebble", model);
    }
}
