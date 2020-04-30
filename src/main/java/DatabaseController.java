import com.github.javafaker.Faker;
import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;
import java.util.Random;


public class DatabaseController{

    Configuration config = new Configuration().configure().addAnnotatedClass(Category.class).addAnnotatedClass(User.class).addAnnotatedClass(Articles.class).addAnnotatedClass(UserName.class).addAnnotatedClass(Email.class);

    SessionFactory sf = config.buildSessionFactory();

    Faker faker = new Faker();
    Random random = new Random();

    public void addEmail (String givenEmail) {
        Session s = sf.openSession();
        Transaction tx = s.beginTransaction();

        Email email = new Email();
        email.setEmailID(givenEmail);
        s.save(email);

        tx.commit();
        s.close();
    }

    public void addData() {

        Session s = sf.openSession();
        Transaction tx = s.beginTransaction();


        Category category = new Category();
        Category category2 = new Category();
        Category category3 = new Category();

        category.setName("TV Series");
        category2.setName("TECHNOLOGY");
        category3.setName("HUMAN BONDING");

        s.save(category);
        s.save(category2);
        s.save(category3);

        User user = new User();
        UserName username = new UserName();

        username.setfName("Zahidul");
        username.setmName("Islam");
        username.setlName("Prince");

        user.setName(username);
        user.setEmail("zahidulisprince@gmail.com");
        user.setPassword("checkpass");

        s.save(user);

        Articles articles = new Articles();
        Articles articles2 = new Articles();
        Articles articles3 = new Articles();

        articles.setTitle(faker.lorem().sentence(3+random.nextInt(7)));
        articles.setDate(new Date());
        articles.setDescription(faker.lorem().sentence(250+random.nextInt(1000)));

        articles2.setTitle(faker.lorem().sentence(3+random.nextInt(7)));
        articles2.setDate(new Date());
        articles2.setDescription(faker.lorem().sentence(250+random.nextInt(1000)));

        articles3.setTitle(faker.lorem().sentence(3+random.nextInt(7)));
        articles3.setDate(new Date());
        articles3.setDescription(faker.lorem().sentence(250+random.nextInt(1000)));

        s.save(articles);
        s.save(articles2);
        s.save(articles3);

        articles.setCategory(category);
        articles2.setCategory(category2);
        articles3.setCategory(category3);

        articles.setUser(user);
        articles2.setUser(user);
        articles3.setUser(user);

        category.getArticles().add(articles);
        category.getArticles().add(articles2);
        category.getArticles().add(articles3);

        tx.commit();
        s.close();
    }
}
