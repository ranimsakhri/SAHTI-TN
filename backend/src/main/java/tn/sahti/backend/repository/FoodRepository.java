package tn.sahti.backend.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import tn.sahti.backend.entity.Food;

import java.util.List;

public interface FoodRepository extends JpaRepository<Food, Long> {
    boolean existsByName(String name);

    @Query("""
            select f from Food f
            where (:query is null or lower(f.name) like lower(concat('%', :query, '%'))
                or lower(f.category) like lower(concat('%', :query, '%')))
              and (:category is null or lower(f.category) = lower(:category))
            order by f.name
            """)
    List<Food> search(@Param("query") String query, @Param("category") String category, Pageable pageable);
}
