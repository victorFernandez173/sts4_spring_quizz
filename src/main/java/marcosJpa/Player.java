package marcosJpa;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "player")
public class Player implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "userId")
	private Integer id;

	@Column(name = "name")
	private String name;

	@Column(name = "score")
	private String score;

	@Column(name = "challengeDate")
	private LocalDateTime challengeDate;

	public Player() {
	}

	public Player(String name, String score, LocalDateTime challengeDate) {
		this.name = name;
		this.score = score;
		this.challengeDate = challengeDate;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getScore() {
		return score;
	}

	public void setScore(String score) {
		this.score = score;
	}

	public LocalDateTime getChallengeDate() {
		return challengeDate;
	}

	public void setChallengeDate(LocalDateTime challengeDate) {
		this.challengeDate = challengeDate;
	}
}
