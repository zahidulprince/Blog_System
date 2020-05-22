package BlogController;

import BlogArchitecture.Articles;
import io.javalin.http.Context;
import org.hibernate.Session;

import java.util.HashMap;

public class ArticleController extends Main {

    public static void getAricle(Context ctx) {

        HashMap<String, Object> renderData = new HashMap<>();
        Articles articles;

        try {
            String str = ctx.pathParam("pn");
            int pn = Integer.parseInt(str);

            String originalDomain = domain;

            String descriptions = "check";

            Session session = dbController.sf.openSession();
            articles = session.load(Articles.class, pn);

            renderData.put("post", articles);
            renderData.put("des", descriptions);
            renderData.put("originalDomain", originalDomain);

            ctx.render("templates/post.html.pebble", renderData);
            session.close();

        } catch (Exception e) {
            System.out.println("Error in data");
            e.printStackTrace();
        }
    }
}

