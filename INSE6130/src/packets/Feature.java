package packets;

public class Feature {

	String type;
	String value;
	
	//Default constructor
	public Feature(){
		this.type = "";
		this.value = "";
	}
	
	//Constructor that takes time and direction
	public Feature(String t, String v){
		this.type = t;
		this.value = v;
	}
	
	//Copy constructor
	public Feature(Feature f){
		this.type = f.getType();
		this.value = f.getValue();
	}
	
	//Accessors 
	
	public String getType(){
		return this.type;
	}
	
	public String getValue(){
		return this.value;
	}
	
	//Mutators
	
	public void setType(String t){
		this.type = t;
	}
	
	public void setValue(String v){
		this.value = v;
	}
	
	public boolean equals(Feature f){
		return this.getType().equals(f.getType()) && this.getValue().equals(f.getValue());
	}

}
