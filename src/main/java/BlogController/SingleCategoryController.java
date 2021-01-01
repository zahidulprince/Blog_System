package BlogController;

import BlogArchitecture.Articles;

import io.javalin.http.Context;

import org.hibernate.Session;

import java.util.HashMap;
import java.util.List;

public class SingleCategoryController extends App {

    public static void getDesiredCategory(Context ctx) {

        List<Articles> renderArticles;
        List<Articles> numOfArticles;
        HashMap<String, Object> renderData = new HashMap<>();

        try {
            int ctgID = Integer.parseInt(ctx.pathParam("ctgn"));
            int pn = Integer.parseInt(ctx.pathParam("pn"));

            Session session = DatabaseController.sf.openSession();

            String query = "FROM Articles E where E.category=" + ctgID;

            renderArticles = session.createQuery(query, Articles.class).setFirstResult((pn-1)*6).setMaxResults(6).getResultList();
            numOfArticles = session.createQuery(query, Articles.class).getResultList();

            double listSize = numOfArticles.size();
            double maxTake = 6;
            int LastPage = (int) Math.ceil(listSize / maxTake);

            String path = String.format("%s/category/%d/", domain, ctgID);

            renderData.put("pn", pn);
            renderData.put("moreArticles", renderArticles);
            renderData.put("path", path);
            renderData.put("goToLastPage", LastPage);
            renderData.put("listSize", listSize);
            renderData.put("originalDomain", domain);

            ctx.render("templates/index.html.pebble", renderData);

            session.close();

        } catch (Exception e) {
            System.out.println("Error in data");
            e.printStackTrace();
        }
    }
}