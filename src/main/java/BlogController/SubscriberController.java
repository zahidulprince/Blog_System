package BlogController;

import BlogArchitecture.Email;

import io.javalin.http.Context;

import org.hibernate.Session;

import java.util.HashMap;
import java.util.List;

public class SubscriberController extends App {

    public static void addSubscriber(Context ctx) {

        String emailID = ctx.formParam("email");

        boolean isThere = false;

        String subVar1 = "Subscribed";
        String subVar2 = "You'll the first to know when there's a new article. Thank you!";

        String alreadySubVar1 = "Oops!";
        String alreadySubVar2 = "You're already subscribed. Thank you!";

        List<Email> allEmails;
        HashMap<String, Object> renderData = new HashMap<>();

        try {
            Session session = DatabaseController.sf.openSession();

            allEmails = session.createQuery("FROM Email", Email.class).getResultList();

            if (allEmails.isEmpty()) {
                createSubscriber(ctx, renderData, session, subVar1, subVar2, emailID);
            } else {
                for (Email allEmail : allEmails) {
                    if (emailID.equals(allEmail.getEmailID())) {
                        isThere = true;
                        break;
                    }
                }
                if (!isThere) {
                    createSubscriber(ctx, renderData, session, subVar1, subVar2, emailID);
                } else {
                    renderData.put("alreadySubVar1", alreadySubVar1);
                    renderData.put("alreadySubVar2", alreadySubVar2);
                    renderData.put("alreadySubscribed", true);
                    renderData.put("originalDomain", domain);
                    ctx.render("templates/subscribed.html.pebble", renderData);
                }
            }

        } catch (Exception e) {
            System.out.println("Check Again");
            e.printStackTrace();
        }
    }

    private static void createSubscriber(Context ctx, HashMap<String, Object> renderData, Session session, String subVar1, String subVar2, String emailID) {
        DatabaseController.addEmail(emailID);
        renderData.put("subVar1", subVar1);
        renderData.put("subVar2", subVar2);
        renderData.put("subscribed", true);
        renderData.put("originalDomain", domain);
        ctx.render("templates/subscribed.html.pebble", renderData);
        session.close();
    }
}