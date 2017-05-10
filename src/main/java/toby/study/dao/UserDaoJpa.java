package toby.study.dao;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.transaction.annotation.Transactional;
import toby.study.domain.User;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;


/**
 * Users 테이블에 관련된 데이터 엑세스 로직을 다룬다.
 * JPA를 이용하여 기능을 구현한다.
 */
public class UserDaoJpa implements UserDao {

    /**
     * JPA로직에 사용되는 EntityManager
     */
	//@PersistenceContext의 기본 설정. type=..이하는 생략 가능
    @PersistenceContext(type = PersistenceContextType.TRANSACTION)
    private EntityManager entityManager;

    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public void add(User user) throws DataIntegrityViolationException {
        entityManager.persist(user);
    }

    @Override
    public User get(String id) {
        User user = entityManager.find(User.class, id);
        return user;
    }


    @Override
    public List<User> getAll() {
        List<User> allUsers = entityManager.createQuery("select u from User u", User.class).getResultList();
        return allUsers;
    }

    @Override
    @Transactional
    public void deleteAll() {

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaDelete<User> delete = criteriaBuilder.createCriteriaDelete(User.class);

        delete.from(User.class);
        entityManager.createQuery(delete).executeUpdate();
//        entityManager.createNativeQuery("DELETE FROM USERS", User.class).executeUpdate();
    }

    @Override
    public int getCount() {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> criteriaQuery = criteriaBuilder.createQuery(Long.class);
        criteriaQuery.select(criteriaBuilder.count(criteriaQuery.from(User.class)));
        return entityManager.createQuery(criteriaQuery).getSingleResult().intValue();
    }

    @Override
    public void update(User user) {
        entityManager.persist(user);
    }
}