package vbb;

public class VBBStation {

	private String name;
	private double latitude;
	private double longitude;
	
	public VBBStation(String name){
		this.name=name;
	}	
	
	public String getName(){
		return name;
	}
	
	public void setLatitude(double latitude){
		this.latitude=latitude;
	}
	
	public void setLongitude(double longitude){
		this.longitude=longitude;
	}
	
	public double getLatitude(){
		return latitude;
	}
	
	public double getLongitude(){
		return longitude;
	}
	
}
