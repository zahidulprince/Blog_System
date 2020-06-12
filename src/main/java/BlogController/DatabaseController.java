package BlogController;

import BlogArchitecture.*;

import com.github.javafaker.Faker;

import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import io.javalin.http.Context;

import java.util.Date;
import java.util.Random;


public class DatabaseController{

    static Configuration config = new Configuration().configure().addAnnotatedClass(Category.class).addAnnotatedClass(User.class).addAnnotatedClass(Articles.class).addAnnotatedClass(Email.class);

    public static SessionFactory sf = config.buildSessionFactory();

    static Faker faker = new Faker();
    static Random random = new Random();

    public static void addEmail(String givenEmail) {
        Session s = sf.openSession();
        Transaction tx = s.beginTransaction();

        Email email = new Email();
        email.setEmailID(givenEmail);
        s.save(email);

        tx.commit();
        s.close();
    }

    public static void addData(Context ctx) {

        Session s = sf.openSession();
        Transaction tx = s.beginTransaction();

        Category category = new Category();
        Category category2 = new Category();
        Category category3 = new Category();

        category.setName("TV Series");
        category2.setName("Technology");
        category3.setName("Human Bonding");

        s.save(category);
        s.save(category2);
        s.save(category3);

        User user = new User();
        User user1 = new User();
        User user2 = new User();

        user.setName("Zahidul Islam Prince");
        user.setEmail("zahidulisprince@gmail.com");
        user.setPassword("checkpass");

        user1.setName("Rafsan Prince");
        user1.setEmail("rafsanprince@gmail.com");
        user1.setPassword("checkpass1");

        user2.setName("Aysha Amin Nishi");
        user2.setEmail("nishiamin00@gmail.com");
        user2.setPassword("checkpass3");

        s.save(user);
        s.save(user1);
        s.save(user2);

        Articles articles = new Articles();
        Articles articles2 = new Articles();
        Articles articles3 = new Articles();

        articles.setTitle(faker.lorem().sentence(3+random.nextInt(7)));
        articles.setDate(new Date());
        articles.setDescription(faker.lorem().sentence(250+random.nextInt(1000)));
        articles.setLink("https://source.unsplash.com/1600x900");

        articles2.setTitle(faker.lorem().sentence(3+random.nextInt(7)));
        articles2.setDate(new Date());
        articles2.setDescription(faker.lorem().sentence(250+random.nextInt(1000)));
        articles2.setLink("https://source.unsplash.com/1600x900");

        articles3.setTitle(faker.lorem().sentence(3+random.nextInt(7)));
        articles3.setDate(new Date());
        articles3.setDescription(faker.lorem().sentence(250+random.nextInt(1000)));
        articles3.setLink("https://source.unsplash.com/1600x900");

        articles.setCategory(category);
        articles2.setCategory(category2);
        articles3.setCategory(category3);

        articles.setUser(user);
        articles2.setUser(user1);
        articles3.setUser(user2);

        s.save(articles);
        s.save(articles2);
        s.save(articles3);

        tx.commit();
        s.close();
    }
}
