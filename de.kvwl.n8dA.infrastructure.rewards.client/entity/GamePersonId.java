package de.kvwl.n8dA.infrastructure.commons.entity;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.Transient;

@Embeddable
public class GamePersonId implements Serializable {

	

	/**
	 * 
	 */
	@Transient
	private static final long serialVersionUID = -6804291918524983882L;

	private String gameName;

	private String personName;

	public String getPersonName() {
		return personName;
	}

	public void setPersonName(String personName) {
		this.personName = personName;
	}

	public String getGameName() {
		return gameName;
	}

	public void setGameName(String gameName) {
		this.gameName = gameName;

		
		
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((gameName == null) ? 0 : gameName.hashCode());
		result = prime * result
				+ ((personName == null) ? 0 : personName.hashCode());
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
		GamePersonId other = (GamePersonId) obj;
		if (gameName == null) {
			if (other.gameName != null)
				return false;
		} else if (!gameName.equals(other.gameName))
			return false;
		if (personName == null) {
			if (other.personName != null)
				return false;
		} else if (!personName.equals(other.personName))
			return false;
		return true;
	}
}
