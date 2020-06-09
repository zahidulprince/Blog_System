package BlogController;

import BlogArchitecture.Category;

import io.javalin.http.Context;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;

public class CategoriesController extends App {

    public static void getCategories(Context ctx) {

        List<Category> renderCategory;
        HashMap<String, Object> renderData = new HashMap<>();

        try {
            String str = ctx.pathParam("pn");
            int pn = Integer.parseInt(str);

            Session session = DatabaseController.sf.openSession();

            Query query = session.createQuery("select count(*) from Category");
            long maxRow = (long) query.uniqueResult();

            double maxTake = 2;

            int LastPage = (int) Math.ceil(maxRow / maxTake);

            renderCategory = session.createQuery("FROM Category order by id desc", Category.class).setFirstResult(((pn - 1) * 2)).setMaxResults((int) maxTake).getResultList();

            renderData.put("categories", renderCategory);
            renderData.put("pn", pn);
            renderData.put("goToLastPage", LastPage);
            renderData.put("originalDomain", domain);

            ctx.render("templates/categories.html.pebble", renderData);

            session.close();

        } catch (Exception e) {
            System.out.println("check again");
            e.printStackTrace();
        }
    }
}
