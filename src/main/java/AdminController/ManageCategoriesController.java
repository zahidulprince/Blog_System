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
import java.util.Map;

import static Util.ViewUtil.baseModel;

public class ManageCategoriesController extends App {
    public static void getManageCategories(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session session = DatabaseController.sf.openSession();

        List<Category> categoryList;
        Map<String, Object> model = baseModel(ctx);

        categoryList = session.createQuery("FROM Category order by id", Category.class).getResultList();

        model.put("categories", categoryList);
        if (ctx.sessionAttribute("isRedirected") == "fromManageCategories") {
            model.put("deleted", true);
        }

        ctx.render("templates/admin/Category/manageCategories.html.pebble", model);
    }


    public static void deleteCategory(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        int categoryID = Integer.parseInt(ctx.pathParam("cn"));

        Category categoryToDelete = s.get(Category.class, categoryID);

        for (Articles articles: categoryToDelete.getArticles()){
            s.delete(articles);
        }

        s.delete(categoryToDelete);

        ctx.redirect(domain + "/admin/manageCategories");
        ctx.sessionAttribute("isRedirected", "fromManageCategories");

        tx.commit();
        s.close();
    }

    public static void editCategory(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        Map<String, Object> model = baseModel(ctx);
        List<Category> categoryList;

        int categoryID = 0;
        String matchCategoryName = ctx.formParam("oldCatName");
        String updateCategoryName = ctx.formParam("newCatName");

        boolean oldOneIsThere = false;
        boolean newOneIsThere = false;

        categoryList = s.createQuery("FROM Category", Category.class).getResultList();

        if (categoryList.isEmpty()) {
            model.put("noCat", true);
            ctx.render("templates/admin/Category/addCategory.html.pebble", model);
        } else {

            for (Category categoryCheckForOld : categoryList) {
                if (matchCategoryName.equals(categoryCheckForOld.getName())) {
                    categoryID = categoryCheckForOld.getId();
                    oldOneIsThere = true;
                    break;
                }
            }

            for (Category categoryCheckForNew: categoryList) {
                if (updateCategoryName.equals(categoryCheckForNew.getName())) {
                    newOneIsThere = true;
                    break;
                }
            }

            if (newOneIsThere) {
                model.put("addedAlready", true);
                ctx.render("templates/admin/Category/addCategory.html.pebble", model);
            } else {
                if (oldOneIsThere) {
                    updateCategory(ctx, model, s, tx, updateCategoryName, categoryID);
                }
            }
        }
    }

    private static void updateCategory(Context ctx, Map<String, Object> model, Session s, Transaction tx, String updateCategoryName, int categoryID) {
        Category categoryToUpdate = s.get(Category.class, categoryID);
        categoryToUpdate.setName(updateCategoryName);
        s.update(categoryToUpdate);
        s.save(categoryToUpdate);
        tx.commit();
        s.close();
        model.put("updated", true);
        ctx.render("templates/admin/Category/addCategory.html.pebble", model);
    }

    public static void getEditForm(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Map<String, Object> model = baseModel(ctx);
        model.put("updateForm", true);

        ctx.render("templates/admin/Category/addCategory.html.pebble", model);

    }
}
