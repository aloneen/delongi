package kz.seisen.delongi.repositories;

import kz.seisen.delongi.models.Statistics;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatisticsRepository extends JpaRepository<Statistics, Long> {
    Statistics findByName(String name);
}
