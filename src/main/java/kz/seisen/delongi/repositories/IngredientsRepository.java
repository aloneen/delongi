package kz.seisen.delongi.repositories;


import kz.seisen.delongi.models.Ingredients;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientsRepository extends JpaRepository<Ingredients, Long> {
    Ingredients findByName(String name);
}
