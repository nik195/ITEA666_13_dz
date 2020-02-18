package dogVictim;

public class Owner {

	private String name;

	public Owner() {
	}

	public Owner(String name) {
		this.name = name;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "Owner [name=" + name + "]";
	}

}
