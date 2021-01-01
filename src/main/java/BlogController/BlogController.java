package BlogController;

import BlogArchitecture.Articles;

import io.javalin.http.Context;

import org.hibernate.Session;
import org.hibernate.query.Query;

import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BlogController extends App {

//  Start --- Removing HTML Tags
    private static final Pattern REMOVE_TAGS = Pattern.compile("<.+?>");

    public static String removeTags(String string) {
        if (string == null || string.length() == 0) {
            return string;
        }

        Matcher m = REMOVE_TAGS.matcher(string);

        return m.replaceAll("");
    }
//  End

    public static void getBlogHome(Context ctx) {

        List<Articles> renderArticles;
        HashMap<String, Object> renderData = new HashMap<>();
        String path = String.format("%s/", domain);

        Articles latestArticle;

        try {
            String str = ctx.pathParam("pn");
//            System.out.println(str);
            int pn = Integer.parseInt(str);
//            System.out.println(pn);

            Session session = DatabaseController.sf.openSession();

//          Start --- Getting The Latest Id'd Article
            Query query1 = session.createQuery("select max(id) from Articles");
            int latestArticleID = (int) query1.uniqueResult();

            latestArticle = session.load(Articles.class, latestArticleID);

            String latestArticleDescription = latestArticle.getDescription();
            latestArticleDescription = removeTags(latestArticleDescription);
//          End

            //Getting how many rows are in there and counting last page
            Query query = session.createQuery("select count(*) from Articles");

            long maxRow = (long) query.uniqueResult() - 1;
            double maxTake = 6;
            int LastPage = (int) Math.ceil(maxRow / maxTake);
//          End

//          showing all the articles
            renderArticles = session.createQuery("FROM Articles order by id desc", Articles.class).setFirstResult(((pn - 1) * 6) + 1).setMaxResults((int) maxTake).getResultList();

//          start --- removing HTML Tags by calling removeTags
            String[] descriptions = new String[6];
            int iterateDescription = 0;

            for (Articles articles : renderArticles) {
                descriptions[iterateDescription] = articles.getDescription();
                descriptions[iterateDescription] = removeTags(descriptions[iterateDescription]);
                articles.setDescription(descriptions[iterateDescription]);
                iterateDescription++;
            }
//          End

            renderData.put("latestArticle", latestArticle);
            renderData.put("moreArticles", renderArticles);
            renderData.put("pn", pn);
            renderData.put("goToLastPage", LastPage);
            renderData.put("index", true);
            renderData.put("subscription", true);
            renderData.put("path", path);
            renderData.put("originalDomain", domain);
            renderData.put("latestArticleDescription", latestArticleDescription);

            ctx.render("templates/index.html.pebble", renderData);

            session.close();

        } catch (Exception e) {
            System.out.println("check again");
            e.printStackTrace();
        }
    }
}