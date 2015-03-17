package de.kvwl.n8dA.robotwars.commons.network.messages;

public enum ClientProperty {

	CLIENT_UUID("CLIENT_UUID"), ALL_CLIENTS ("ALL"), READY_TO_START_THE_BATTLE("RDY2STARTTHEBATTLE");
	
	private String name;

	private ClientProperty(String name) {
		this.setName(name);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	
	
	
}
