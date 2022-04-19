package study.jpa_study.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import study.jpa_study.domain.item.Item;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ItemRepository {

    private final EntityManager em;

    public void save(Item item) {
        if (item.getId() == null) { //새로 생성이 되는 것이기 때문에 null
            em.persist(item);
        } else {                    //새로 생성이 되지 않는 경우 (update)
            //머지는 실무에서 잘 쓰지 않는다. (수정시 모든 필드를 다루지 않을 경우 null 까지 다 merge 시킴)
            em.merge(item);
        }
    }

    public Item findOne(Long id) {
        return em.find(Item.class, id);
    }

    public List<Item> findAll() {
        return em.createQuery("select i from Item i", Item.class)
                .getResultList();
    }
}
