package BlogController;

import BlogArchitecture.Articles;
import io.javalin.http.Context;
import org.hibernate.Session;

import java.util.HashMap;
import java.util.List;

public class SingleCategoryController extends Main {

    public static void getDesiredCategory(Context ctx) {

        List<Articles> renderArticles;
        List<Articles> numOfArticles;

        HashMap<String, Object> renderData = new HashMap<>();

        try {
            String ctgn = ctx.pathParam("ctgn");
            int ctgID = Integer.parseInt(ctgn);

            String str = ctx.pathParam("pn");
            int pn = Integer.parseInt(str);

            Session session = dbController.sf.openSession();

            String query = "FROM Articles E where E.category=" + ctgID;

            renderArticles = session.createQuery(query, Articles.class).setFirstResult((pn-1)*6).setMaxResults(6).getResultList();
            numOfArticles = session.createQuery(query, Articles.class).getResultList();

            double listSize = numOfArticles.size();
            double maxTake = 6;
            double lastPageCheck = Math.ceil(listSize / maxTake);
            int LastPage = (int) lastPageCheck;

            String path = String.format("%s/category/%d/", domain, ctgID);
            String originalDomain = domain;

            renderData.put("pn", pn);
            renderData.put("moreArticles", renderArticles);
            renderData.put("path", path);
            renderData.put("goToLastPage", LastPage);
            renderData.put("lastPageCheck", lastPageCheck);
            renderData.put("listSize", listSize);
            renderData.put("originalDomain", originalDomain);

            ctx.render("templates/index.html.pebble", renderData);

            session.close();

        } catch (Exception e) {
            System.out.println("Error in data");
            e.printStackTrace();
        }
    }
}
