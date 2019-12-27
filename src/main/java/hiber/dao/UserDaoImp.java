package hiber.dao;

import hiber.model.Car;
import hiber.model.User;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class UserDaoImp implements UserDao {

   @Autowired
   private SessionFactory sessionFactory;

   @Override
   public void addUser(User user) {
      Car car = user.getCar();
      if (!isCar(car)) {
         sessionFactory.getCurrentSession().save(user);
      }
   }

   @Override
   @SuppressWarnings("unchecked")
   public List<User> listUsers() {
      TypedQuery<User> query=sessionFactory.getCurrentSession().createQuery("from User");
      return query.getResultList();
   }

   @Override
   public void addCar(Car car) {
      if (!isCar(car)) {
         sessionFactory.getCurrentSession().save(car);
      }
   }

   @Override
   public List<Car> listCars() {
      TypedQuery<Car> query=sessionFactory.getCurrentSession().createQuery("from Car");
      return query.getResultList();
   }

   @Override
   @Transactional
   public User getUserByCar(String name, int series) {
      Car car = (Car) sessionFactory.getCurrentSession().createQuery("select c from Car c where c.name = :name and c.series = :series")
              .setParameter("name", name)
              .setParameter("series", series)
              .uniqueResult();

      if (car != null) {
         return car.getUser();
      }
      return null;
   }

   private boolean isCar(Car car) {
      if (car != null) {
         String name = car.getName();
         int series = car.getSeries();
         Car validateCar = (Car) sessionFactory.getCurrentSession().createQuery("select c from Car c where c.name = :name and c.series = :series")
                 .setParameter("name", name)
                 .setParameter("series", series)
                 .uniqueResult();

         if (validateCar != null) {
            return true;
         }
      }
      return false;
   }

}
