import org.hibernate.cfg.Configuration;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.Date;


public class DatabaseController{

    Configuration config = new Configuration().configure().addAnnotatedClass(Category.class).addAnnotatedClass(User.class).addAnnotatedClass(Articles.class).addAnnotatedClass(UserName.class);

    SessionFactory sf = config.buildSessionFactory();


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

        articles.setTitle("How Heisenberg is Scarface");
        articles.setDate(new Date());
        articles.setDescription("Scarface, Tyler Durden & Crystal Meth amounts to being Heisenberg");

        articles2.setTitle("Game of Thrones or Shame of Thrones");
        articles2.setDate(new Date());
        articles2.setDescription("Shame of Thrones");

        articles3.setTitle("Prison Break");
        articles3.setDate(new Date());
        articles3.setDescription("Michael");

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
