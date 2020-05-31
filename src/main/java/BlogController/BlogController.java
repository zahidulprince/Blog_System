package BlogController;

import BlogArchitecture.Articles;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;

public class BlogController extends App {

    public static void getBlogHome(Context ctx) {

        List<Articles> renderArticles;

        HashMap<String, Object> renderData = new HashMap<>();

        Articles articles;

        try {
            String str = ctx.pathParam("pn");
            int pn = Integer.parseInt(str);

            Session session = DatabaseController.sf.openSession();

//          Getting The Latest Id'd Article
            Query query1 = session.createQuery("select max(id) from Articles");
            int article = (int) query1.uniqueResult();

            articles = session.load(Articles.class, article);

            //Getting how many rows are in there
            Query query = session.createQuery("select count(*) from Articles");
            long maxRowNT = (long) query.uniqueResult();
            long maxRow = maxRowNT - 1;

            double maxTake = 6;

            double lastPageCheck = Math.ceil(maxRow / maxTake);

            int LastPage = (int) lastPageCheck;

            String originalDomain = domain;

            String path = String.format("%s/blog/", domain);

            //showing all the articles
            renderArticles = session.createQuery("FROM Articles order by id desc", Articles.class).setFirstResult(((pn - 1) * 6) + 1).setMaxResults((int) maxTake).getResultList();

            renderData.put("latestArticle", articles);
            renderData.put("moreArticles", renderArticles);
            renderData.put("pn", pn);
            renderData.put("lastPageCheck", lastPageCheck);
            renderData.put("goToLastPage", LastPage);
            renderData.put("index", true);
            renderData.put("subscription", true);
            renderData.put("path", path);
            renderData.put("originalDomain", originalDomain);

            ctx.render("templates/index.html.pebble", renderData);

            session.close();

        } catch (Exception e) {
            System.out.println("check again");
            e.printStackTrace();
        }
    }
}
