package marcosJpa;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

public interface IPlayers extends JpaRepository<Player, Integer>{
	
    public List<Player> findByScore(@Param("score") String score);
}