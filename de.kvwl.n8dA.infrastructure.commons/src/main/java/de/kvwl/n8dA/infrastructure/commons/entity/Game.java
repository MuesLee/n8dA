package de.kvwl.n8dA.infrastructure.commons.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="game")
public class Game implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@Column(name="Game_Name")
	private String name;
	
	@OneToMany(mappedBy = "game")
	private Set<GamePerson> persons =  new HashSet<GamePerson>();
	
	public Game() {
	}
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((getPersons() == null) ? 0 : getPersons().hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Game other = (Game) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (getPersons() == null) {
			if (other.getPersons() != null)
				return false;
		} else if (!getPersons().equals(other.getPersons()))
			return false;
		return true;
	}


	public Set<GamePerson> getPersons() {
		return persons;
	}


	public void setPersons(Set<GamePerson> persons) {
		this.persons = persons;
	}
	
	
	
	
}
