package AdminController;

import BlogArchitecture.*;
import BlogController.App;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Map;

import static Util.ViewUtil.baseModel;

public class AddArticleController extends App {

    public static void getAddArticle(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Map<String, Object> model = baseModel(ctx);
        model.put("form", true);

        if (ctx.sessionAttribute("isRedirected") == "true") {
            model.put("wrongSubmit", true);
        }
        ctx.render("templates/admin/Article/addArticle.html.pebble", model);
    }

    public static void addArticle(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Map<String, Object> model = baseModel(ctx);
        List<Articles> articlesList;
        List<Category> categoryList;
        List<User> userList;

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        Category categoryToSet = null;
        User userToSet = null;

        String articleTitle = ctx.formParam("articleTitle");
        String articleCategory = ctx.formParam("articleCategory");
        String articleWriterEmail = ctx.formParam("articleWriter");
        String articleDescription = ctx.formParam("tinymcetext");
        String articleImageLink = ctx.formParam("articleHeroImageLink");

//        ctx.uploadedFiles("files").forEach(file -> {
//            FileUtil.streamToFile(file.getContent(), "D:/upload/" + file.getFilename());
//        });

        boolean isThere = false;
        boolean categoryCheckedRight = false;
        boolean userCheckedRight = false;

        articlesList = s.createQuery("FROM Articles", Articles.class).getResultList();
        categoryList = s.createQuery("FROM Category", Category.class).getResultList();
        userList = s.createQuery("FROM User", User.class).getResultList();

        for (Category category : categoryList) {
            if (articleCategory.equals(category.getName())) {
                categoryToSet = category;
                categoryCheckedRight = true;
                break;
            }
        }

        for (User user : userList) {
            if (articleWriterEmail.equals(user.getEmail())) {
                userToSet = user;
                userCheckedRight = true;
                break;
            }
        }

        if (!categoryCheckedRight || !userCheckedRight) {
            ctx.sessionAttribute("isRedirected", "true");
            ctx.redirect(domain + "/admin/addArticle");
        } else {
            if (articlesList.isEmpty()) {
                createArticle(ctx, model, s, tx,articleImageLink, articleDescription, articleTitle,  categoryToSet, userToSet);
            } else {

                for (Articles checkArticles : articlesList) {
                    if (articleTitle.equals(checkArticles.getTitle())) {
                        isThere = true;
                        break;
                    }
                }

                if (!isThere) {
                    createArticle(ctx, model, s, tx, articleDescription, articleImageLink, articleTitle,  categoryToSet, userToSet);
                } else {
                    model.put("addedAlready", true);
                    ctx.render("templates/admin/Article/addArticle.html.pebble", model);
                }
            }
        }
    }

    private static void createArticle(Context ctx, Map<String, Object> model, Session s, Transaction tx, String articleImageLink, String articleDescription, String articleTitle,  Category categoryToSet, User userToSet) {
        Articles articleToCreate = new Articles();
        articleToCreate.setTitle(articleTitle);
        articleToCreate.setDate(new Date());
        articleToCreate.setDescription(articleImageLink);
        articleToCreate.setCategory(categoryToSet);
        articleToCreate.setUser(userToSet);
        articleToCreate.setLink(articleDescription);
        s.save(articleToCreate);
        tx.commit();
        s.close();
        model.put("added", true);
        ctx.render("templates/admin/Article/addArticle.html.pebble", model);
    }
}