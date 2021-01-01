package AdminController;

import BlogArchitecture.Email;
import BlogController.DatabaseController;
import io.javalin.http.Context;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;
import java.util.Map;

import static BlogController.App.domain;
import static Util.ViewUtil.baseModel;

public class ManageSubscriberController {
    public static void getSubscriber(Context ctx){
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session session = DatabaseController.sf.openSession();

        List<Email> emailList;
        Map<String, Object> model = baseModel(ctx);

        emailList = session.createQuery("FROM Email order by id", Email.class).getResultList();

        model.put("emails", emailList);
        if (ctx.sessionAttribute("isRedirected") == "fromManageSubscribers") {
            model.put("deleted", true);
        }

        ctx.render("templates/admin/Subscribers/subscribers.html.pebble", model);
        ctx.sessionAttribute("isRedirected", "itself");
    }

    public static void deleteSubscriber(Context ctx) {
        ctx.sessionAttribute("loginRedirect", ctx.path());

        Session s = DatabaseController.sf.openSession();
        Transaction tx = s.beginTransaction();

        int emailId = Integer.parseInt(ctx.pathParam("eId"));

        Email emailToDelete = s.get(Email.class, emailId);
        s.delete(emailToDelete);

        ctx.redirect(domain + "/admin/manageSubscribers");
        ctx.sessionAttribute("isRedirected", "fromManageSubscribers");

        tx.commit();
        s.close();
    }
}