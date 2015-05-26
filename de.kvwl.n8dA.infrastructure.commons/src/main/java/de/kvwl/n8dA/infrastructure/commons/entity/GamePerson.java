package de.kvwl.n8dA.infrastructure.commons.entity;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;


@NamedQueries ({
	@NamedQuery(name = "findAllGamesForPersonName", query = "SELECT gp FROM GamePerson gp JOIN gp.person p WHERE p.name = :personName"),
	@NamedQuery(name = "findAllPersonsForGameName", query = "SELECT gp FROM GamePerson gp JOIN gp.game g WHERE g.name = :gameName"),
	@NamedQuery(name = "findPersonInGame", query = "SELECT gp FROM GamePerson gp JOIN gp.game g JOIN gp.person p WHERE g.name = :gameName AND p.name = :personName"),
	@NamedQuery(name = "findAllGamePersons", query = "SELECT gp FROM GamePerson gp JOIN gp.game g"),
	@NamedQuery(name = "findFirst10PersonsForGameName", query = "SELECT gp FROM GamePerson gp JOIN gp.game g WHERE g.name = :gameName ORDER BY gp.points DESC")
})


@Entity
public class GamePerson implements Serializable {
	
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -2034939944782906644L;

	
	@EmbeddedId
	private GamePersonId pk = new GamePersonId();

	@ManyToOne(cascade={CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH}, fetch =FetchType.EAGER)
	@MapsId("gameName")
	@JoinColumn(name = "Game_Name")
	private Game game;
	
	@ManyToOne(cascade=CascadeType.ALL, fetch =FetchType.EAGER)
	@MapsId("personName")
	  @JoinColumn(name = "Person_Name")
	private Person person;
	
	private Integer points;

	public GamePerson() {
	}
	
	

	public GamePerson(Game game, Person person, Integer points) {
		super();
		this.game = game;
		this.person = person;
		this.points = points;
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
