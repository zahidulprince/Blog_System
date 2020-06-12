package AdminController;

import BlogArchitecture.Articles;
import BlogArchitecture.Category;
import BlogController.App;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;

import static Util.ViewUtil.baseModel;

public class ManageArticlesController extends App {
    public static void getManageArticles(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session session = DatabaseController.sf.openSession();

        List<Articles> articlesList;
        Map<String, Object> model = baseModel(ctx);

        articlesList = session.createQuery("FROM Articles order by id", Articles.class).getResultList();

        model.put("articles", articlesList);
        if (ctx.sessionAttribute("isRedirected") == "fromManageArticles") {
            model.put("deleted", true);
        }
        if (ctx.sessionAttribute("isRedirected") == "fromManageArticlesAfterUpdate") {
            model.put("updated", true);
        }

        ctx.render("templates/admin/Article/manageArticles.html.pebble", model);
        ctx.sessionAttribute("isRedirected", "itself");
    }

    public static void deleteArticle(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        int articleId = Integer.parseInt(ctx.pathParam("an"));

        Articles articleToDelete = s.get(Articles.class, articleId);
        s.delete(articleToDelete);

        ctx.redirect(domain + "/admin/manageArticles");
        ctx.sessionAttribute("isRedirected", "fromManageArticles");

        tx.commit();
        s.close();
    }

    public static void editArticle(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        Map<String, Object> model = baseModel(ctx);
        List<Articles> articlesList;
        List<Category> categoryList;

        int articleId = Integer.parseInt(ctx.pathParam("an"));

        String updateArticleTitle = ctx.formParam("articleTitle");
        String updateArticleCategory = ctx.formParam("articleCategory");
        String updateArticleDescription = ctx.formParam("tinymcetext");
        String updateArticleImageLink = ctx.formParam("articleHeroImageLink");

        if (updateArticleImageLink.equals(""))
            updateArticleImageLink = null;

        if (updateArticleTitle.equals(""))
            updateArticleTitle = null;

        if (updateArticleCategory.equals(""))
            updateArticleCategory = null;

        boolean isThere = false;
        boolean categoryCheckedRight = false;
        Category categoryToSet = null;

        articlesList = s.createQuery("FROM Articles order by id", Articles.class).getResultList();
        categoryList = s.createQuery("FROM Category order by id", Category.class).getResultList();

        if (updateArticleCategory != null) {
            for (Category category : categoryList) {
                if (updateArticleCategory.equals(category.getName())) {
                    categoryToSet = category;
                    categoryCheckedRight = true;
                    break;
                }
            }
        }

        if (updateArticleTitle != null) {
            for (Articles checkArticles : articlesList) {
                if (updateArticleTitle.equals(checkArticles.getTitle())) {
                    isThere = true;
                    break;
                }
            }
        }

        if ((updateArticleCategory != null && !categoryCheckedRight) || (updateArticleTitle != null && isThere)) {

            if (updateArticleCategory != null && !categoryCheckedRight) {
                ctx.sessionAttribute("isRedirected", "fromManageArticlesAfterFailCategory");
                ctx.redirect(domain + "/admin/updateArticle/" + articleId);
            }

            if (updateArticleTitle != null && isThere) {
                ctx.sessionAttribute("isRedirected", "fromManageArticlesAfterFailArticle");
                ctx.redirect(domain + "/admin/updateArticle/" + articleId);
            }

            if ((updateArticleCategory != null && !categoryCheckedRight) && (updateArticleTitle != null && isThere)) {
                ctx.sessionAttribute("isRedirected", "fromManageArticlesAfterFailBoth");
                ctx.redirect(domain + "/admin/updateArticle/" + articleId);
            }
        }

        if (updateArticleTitle == null && updateArticleCategory == null && updateArticleImageLink == null) {
            updateArticle(ctx, model, s, tx, updateArticleDescription, null, null, null, articleId);
        }
//        -----------------------------------
        if (updateArticleTitle != null && !isThere && updateArticleCategory == null && updateArticleImageLink == null) {
            updateArticle(ctx, model, s, tx, updateArticleDescription, updateArticleTitle, null, null, articleId);
        }
        if (updateArticleTitle == null && updateArticleCategory != null && categoryCheckedRight && updateArticleImageLink == null) {
            updateArticle(ctx, model, s, tx, updateArticleDescription, null, categoryToSet, null, articleId);
        }
        if (updateArticleTitle == null && updateArticleCategory == null && updateArticleImageLink != null) {
            updateArticle(ctx, model, s, tx, updateArticleDescription, null, null, updateArticleImageLink, articleId);
        }
//        -----------------------------------
        if (updateArticleTitle != null && !isThere && updateArticleCategory != null && categoryCheckedRight && updateArticleImageLink == null) {
            updateArticle(ctx, model, s, tx, updateArticleDescription, updateArticleTitle, categoryToSet, null, articleId);
        }
        if (updateArticleTitle != null && !isThere && updateArticleCategory == null && updateArticleImageLink != null) {
            updateArticle(ctx, model, s, tx, updateArticleDescription, updateArticleTitle, null, updateArticleImageLink, articleId);
        }
        if (updateArticleTitle == null && updateArticleCategory != null && categoryCheckedRight && updateArticleImageLink != null) {
            updateArticle(ctx, model, s, tx, updateArticleDescription, null, categoryToSet, updateArticleImageLink, articleId);
        }
//        -----------------------------------
        if (updateArticleTitle != null && !isThere && updateArticleCategory != null && updateArticleImageLink != null) {
            updateArticle(ctx, model, s, tx, updateArticleDescription, updateArticleTitle, categoryToSet, updateArticleImageLink, articleId);
        }
    }

    private static void updateArticle(Context ctx, Map<String, Object> model, Session s, Transaction tx, String updateArticleDescription, String updateArticleTitle, Category updateArticleCategory, String updateArticleImageLink, int articleId) {
        Articles articleToUpdate = s.get(Articles.class, articleId);

        articleToUpdate.setDescription(updateArticleDescription);

        if (updateArticleTitle != null && updateArticleCategory == null && updateArticleImageLink == null) {
            articleToUpdate.setTitle(updateArticleTitle);
        }
        if (updateArticleTitle == null && updateArticleCategory != null && updateArticleImageLink == null) {
            articleToUpdate.setCategory(updateArticleCategory);
        }
        if (updateArticleTitle == null && updateArticleCategory == null && updateArticleImageLink != null) {
            articleToUpdate.setLink(updateArticleImageLink);
        }
//        -----------------------------------
        if (updateArticleTitle != null && updateArticleCategory != null && updateArticleImageLink == null) {
            articleToUpdate.setTitle(updateArticleTitle);
            articleToUpdate.setCategory(updateArticleCategory);
        }
        if (updateArticleTitle != null && updateArticleCategory == null && updateArticleImageLink != null) {
            articleToUpdate.setTitle(updateArticleTitle);
            articleToUpdate.setLink(updateArticleImageLink);
        }
        if (updateArticleTitle == null && updateArticleCategory != null && updateArticleImageLink != null) {
            articleToUpdate.setCategory(updateArticleCategory);
            articleToUpdate.setLink(updateArticleImageLink);
        }
//        -----------------------------------
        if (updateArticleTitle != null && updateArticleCategory != null && updateArticleImageLink != null) {
            articleToUpdate.setTitle(updateArticleTitle);
            articleToUpdate.setCategory(updateArticleCategory);
            articleToUpdate.setLink(updateArticleImageLink);
        }

        s.update(articleToUpdate);
        s.save(articleToUpdate);
        tx.commit();
        s.close();
        ctx.redirect(domain + "/admin/manageArticles");
        ctx.sessionAttribute("isRedirected", "fromManageArticlesAfterUpdate");
    }

    public static void getArticleEditForm(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session s = DatabaseController.sf.openSession();

        int articleId = Integer.parseInt(ctx.pathParam("an"));
        Articles articleToEdit = s.get(Articles.class, articleId);

        Map<String, Object> model = baseModel(ctx);
        model.put("articleToEdit", articleToEdit);
        model.put("articleEditTrue", true);

        if (ctx.sessionAttribute("isRedirected") == "fromManageArticlesAfterFailCategory") {
            model.put("failCategory", true);
        }
        if (ctx.sessionAttribute("isRedirected") == "fromManageArticlesAfterFailArticle") {
            model.put("failArticle", true);
        }
        if (ctx.sessionAttribute("isRedirected") == "fromManageArticlesAfterFailBoth") {
            model.put("failBoth", true);
        }

        ctx.render("templates/admin/Article/addArticle.html.pebble", model);
        ctx.sessionAttribute("isRedirected", "itself");
    }
}
