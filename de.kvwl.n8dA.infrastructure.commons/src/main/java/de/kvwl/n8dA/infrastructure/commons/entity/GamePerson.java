package de.kvwl.n8dA.infrastructure.commons.entity;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

@NamedQuery(name = "findAllGamesForPersonName", query = "SELECT gp FROM GamePerson gp JOIN gp.person p WHERE p.name = :personName")
@Entity
public class GamePerson implements Serializable {
	
	@Transient
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private GamePersonId pk = new GamePersonId();

	@ManyToOne
	@MapsId("gameName")
	@JoinColumn(name = "Game_Name")
	private Game game;
	
	@ManyToOne
	@MapsId("personName")
	  @JoinColumn(name = "Person_Name")
	private Person person;
	
	private Integer points;

	public GamePerson() {
	}

	public GamePersonId getPk() {
		return pk;
	}

	public void setPk(GamePersonId pk) {
		this.pk = pk;
	}

	public Integer getPoints() {
		return points;
	}

	public void setPoints(Integer points) {
		this.points = points;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}

	public Game getGame() {
		return game;
	}

	public void setGame(Game game) {
		this.game = game;
	}
	
	@Override
	public String toString() {
		return "Person: " + person +", Game:" + game + ", Points: " + points;
	}

}
