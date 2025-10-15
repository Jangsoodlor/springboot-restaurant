package th.ac.ku.restaurant.repository;

import th.ac.ku.restaurant.entity.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface RestaurantRepository
        extends JpaRepository<Restaurant, UUID> {

    Restaurant findByName(String name);

    List<Restaurant> findByLocation(String location);
}
