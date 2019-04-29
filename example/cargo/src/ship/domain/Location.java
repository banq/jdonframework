package ship.domain;

/**
 * @stereotype place
 */

public class Location {
	private String ucode;
	private String name;

	public Location(String ucode, String name) {
		super();
		this.ucode = ucode;
		this.name = name;
	}

	public String getUcode() {
		return ucode;
	}

	public void setUcode(String ucode) {
		this.ucode = ucode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean equals(Object obj) {
		if (this.getUcode().equals(((Location) obj).getUcode()))
			return true;
		else
			return false;
	}
}
