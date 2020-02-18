package dogVictim;

public class Dog extends Animal {

	private Owner owner;

	public Dog() {
		super();
	}

	public Dog(Owner owner, String name, int age) {
		super(name, age);
		this.owner = owner;
	}

	public final Owner getOwner() {
		return owner;
	}

	public final void setOwner(Owner owner) {
		this.owner = owner;
	}

	@Override
	public String toString() {
		return "Dog name is " + name + ", age is " + age + ", owner is " + owner.getName() + ".";
	}

}
