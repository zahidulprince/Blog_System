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

        HashMap<String, Object> renderData = new HashMap<>();
        List<Category> allCategories;

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        String categoryName = ctx.formParam("inputFloatingLabel");

        boolean isThere = false;

        allCategories = s.createQuery("FROM Category", Category.class).getResultList();

        if (allCategories.isEmpty()) {
            Category category = new Category();
            category.setName(categoryName);
            s.save(category);
            tx.commit();
            s.close();
            renderData.put("added", true);
            ctx.render("templates/admin/Category/addCategory.html.pebble", renderData);
        } else {

            for (Category allCategory : allCategories) {
                if (categoryName.equals(allCategory.getName())) {
                    isThere = true;
                    break;
                }
            }

            if (!isThere) {
                Category category = new Category();
                category.setName(categoryName);
                s.save(category);
                tx.commit();
                s.close();
                renderData.put("added", true);
                ctx.render("templates/admin/Category/addCategory.html.pebble", renderData);
            } else {
                renderData.put("addedAlready", true);
                ctx.render("templates/admin/Category/addCategory.html.pebble", renderData);
            }
        }
    }
}
