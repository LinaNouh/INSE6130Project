import java.util.Comparator;


public class Packet implements Comparable<Packet> {

	float time = 0;
	int direction = 0;
	
	//Default constructor   
	public Packet(){
		this.time = 0;
		this.direction = 0;
	}
	
	//Constructor that takes time and direction
	public Packet(float t, int d){
		this.time = t;
		this.direction = d;
	}
	
	//Copy constructor
	public Packet(Packet p){
		this.time = p.getTime();
		this.direction = p.getDirection();
	}
	
	//Accessors 
	
	public float getTime(){
		return this.time;
	}
	
	public int getDirection(){
		return this.direction;
	}
	
	//Mutators
	
	public void setTime(float t){
		this.time = t;
	}
	
	public void setDirection(int d){
		this.direction = d;
	}

	@Override
	//Comparing packets based on time in order to sort them
	public int compareTo(Packet p) {
		return (int) (this.getTime() - p.getTime());
	}

}
