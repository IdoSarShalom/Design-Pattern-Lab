package test;

public class Goblin {
	
	private final String name;  // required
	private final String color; // required
	private final int size;		// not required
	private final byte iq; 		// not required
	
	____________ Goblin(___________ builder) {
	}
	
	___________  ___________ class GoblinBuilder{
		private final String name;
		private final String color;
		________________
		________________
		
		_______ GoblinBuilder(_______________________) {
		}
		
		_______ ____________ withSize(int size){}
		
		_______ ____________ withIq(byte iq){}
		
		_______ ________ build(){
			return ____________;
		}
	}

	public String getName() { return name; }

	public String getColor() { return color;}

	public int getSize() { return size;}

	public byte getIq() { return iq;}

}
