package BlogController;

import BlogArchitecture.Articles;

import io.javalin.http.Context;

import org.hibernate.Session;

import java.util.HashMap;

public class ArticleController extends App {

    public static void getArticle(Context ctx) {

        HashMap<String, Object> renderData = new HashMap<>();
        Articles articles;

        try {
            int pn = Integer.parseInt(ctx.pathParam("pn"));

            Session session = DatabaseController.sf.openSession();
            articles = session.load(Articles.class, pn);

            renderData.put("post", articles);
            renderData.put("originalDomain", domain);

            ctx.render("templates/post.html.pebble", renderData);
            session.close();

        } catch (Exception e) {
            System.out.println("Error in data");
            e.printStackTrace();
        }
    }
}