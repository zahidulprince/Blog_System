package BlogController;

import BlogArchitecture.User;

import io.javalin.http.Context;
import org.hibernate.Session;

import java.util.List;

public class AdminController extends Main {

    public static void getAdmin(Context ctx) {

        String emailId = ctx.formParam("email");
        String password = ctx.formParam("password");

        List<User> allUsers;

        try {
            Session session = dbController.sf.openSession();
            allUsers = session.createQuery("FROM User", User.class).getResultList();

            for (User allUser : allUsers) {
                if (emailId.equals(allUser.getEmail()) && password.equals(allUser.getPassword())) {
                    ctx.result("Accept");
                    break;
                }else {
                    ctx.result("Please, try again!");
                }
            }

        } catch (Exception e) {
            System.out.println("Error in data");
            e.printStackTrace();
        }
    }
}
