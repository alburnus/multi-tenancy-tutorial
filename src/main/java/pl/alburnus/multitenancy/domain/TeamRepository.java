package pl.alburnus.multitenancy.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.alburnus.multitenancy.domain.Team;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}
