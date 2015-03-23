package de.kvwl.n8dA.infrastructure.commons.entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="person")
public class Person implements Serializable {
	
	
	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = 3693628157384738362L;

	@Id
	@Column(name="Person_Name")
	private String name;
	
	@OneToMany(mappedBy = "person")
	private Set<GamePerson> games = new HashSet<GamePerson>();

	public Person() {
	}
	
	
	public Person(String name) {
		super();
		this.name = name;
	}


	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Person other = (Person) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}


	public Set<GamePerson> getGames() {
		return games;
	}


	public void setGames(Set<GamePerson> games) {
		this.games = games;
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return  name;
	}

	

}
