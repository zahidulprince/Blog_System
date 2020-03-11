import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;


public class DatabaseController{

    Configuration config = new Configuration().configure().addAnnotatedClass(Category.class).addAnnotatedClass(User.class).addAnnotatedClass(Articles.class).addAnnotatedClass(UserName.class);

    SessionFactory sf = config.buildSessionFactory();


    public void addData() {
        Category category = new Category();
        Category category2 = new Category();
        Category category3 = new Category();
        Category category4 = new Category();
        Category category5 = new Category();
        Category category6 = new Category();

        category.setName("Technology");
        category2.setName("TV Series");
        category3.setName("Human Bonding");
        category4.setName("check");
        category5.setName("check2");
        category6.setName("check3");

        Session s = sf.openSession();
        Transaction tx = s.beginTransaction();
        s.save(category);
        s.save(category2);
        s.save(category3);
        s.save(category4);
        s.save(category5);
        s.save(category6);

        User user5 = new User();
        User user = new User();
        User user2 = new User();
        User user3 = new User();
        User user4 = new User();

        UserName username = new UserName();
        UserName username2 = new UserName();
        UserName username3 = new UserName();
        UserName username4 = new UserName();
        UserName username5 = new UserName();

        username.setfName("Zahidul");
        username.setmName("Islam");
        username.setlName("Prince");

        username2.setfName("My");
        username2.setmName("Fucking");
        username2.setlName("Ass");

        username3.setfName("check");
        username3.setmName("again");
        username3.setlName("bitch");

        username4.setfName("fuck");
        username4.setmName("you");
        username4.setlName("bitch");

        username5.setfName("CHECK");
        username5.setmName("AGAIN");
        username5.setlName("FUCKER");

        user.setName(username);
        user.setEmail("zahidulisprince@gmail.com");
        user.setPassword("checkpass");

        user2.setName(username2);
        user2.setEmail("myfuckingass@gmail.com");
        user2.setPassword("myAss");

        user3.setName(username3);
        user3.setEmail("check@gmail.com");
        user3.setPassword("check");

        user4.setName(username4);
        user4.setEmail("check2@gmail.com");
        user4.setPassword("check2");

        user5.setName(username5);
        user5.setEmail("check3@gmail.com");
        user5.setPassword("check3");

        s.save(user);
        s.save(user2);
        s.save(user3);
        s.save(user4);
        s.save(user5);

        Articles articles = new Articles();
        Articles articles2 = new Articles();
        Articles articles3 = new Articles();
        Articles articles4 = new Articles();
        Articles articles5 = new Articles();
        Articles articles6 = new Articles();

        articles.setTitle("Breaking Bad");
        articles.setDate(new Date());
        articles.setDescription("Scarface + Tyler Durden + Crystal Meth = Heisenberg");

        articles2.setTitle("Game of Thrones");
        articles2.setDate(new Date());
        articles2.setDescription("Shame of Thrones");

        articles3.setTitle("Spartacas");
        articles3.setDate(new Date());
        articles3.setDescription("This is Sparta");

        articles4.setTitle("Prison Break");
        articles4.setDate(new Date());
        articles4.setDescription("Michael");

        articles5.setTitle("Vampire Diaries");
        articles5.setDate(new Date());
        articles5.setDescription("Kalus");

        articles6.setTitle("The Originals");
        articles6.setDate(new Date());
        articles6.setDescription("Elijah");

        s.save(articles);
        s.save(articles2);
        s.save(articles3);
        s.save(articles4);
        s.save(articles5);
        s.save(articles6);

        articles.setCategory(category);
        articles2.setCategory(category2);
        articles3.setCategory(category3);
        articles4.setCategory(category4);
        articles5.setCategory(category5);
        articles6.setCategory(category6);

        articles.setUser(user);
        articles2.setUser(user2);
        articles3.setUser(user3);
        articles4.setUser(user4);
        articles5.setUser(user5);
        articles6.setUser(user5);

        tx.commit();
        s.close();
    }
}
