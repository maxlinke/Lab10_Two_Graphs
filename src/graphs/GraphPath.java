package graphs;

import java.util.ArrayList;

public class GraphPath {
	
	ArrayList<Vertex> vertices;
	
	public GraphPath(){
		vertices=new ArrayList<>();
	}
	
	public GraphPath(Vertex endPoint){
		vertices=new ArrayList<>();
		Vertex current=endPoint;
		while(current.parent!=null){
			addNode(current);
			current=current.parent;
		}
		addNode(current);
	}
	
	public boolean isEmpty(){
		return vertices.isEmpty();
	}
	
	public void addNode(Vertex vertex){
		vertices.add(0, vertex);
	}
	
	public int getLength(){
		return vertices.size()-1;
	}
	
	public int getCost(){
		int cost=0;
		Vertex current;
		for(int i=0; i<vertices.size()-1; i++){
			current=vertices.get(i);
			cost+=current.getEdgeTo(vertices.get(i+1)).getWeight();
		}
		return cost;
	}
	
	public void printPath(){
		int size=vertices.size();
		for(int i=0; i<size; i++){
			System.out.print(vertices.get(i).getName()+" ");
		}
		System.out.println();
	}
	
	public void printDetails(){
		System.out.println(vertices.get(0).getName()+" to "+vertices.get(vertices.size()-1).getName());
		int size=vertices.size();
		int cost=getCost();
		for(int i=0; i<size; i++){
			System.out.print("("+vertices.get(i).getName()+") ");
			if(i<size-1) System.out.print(vertices.get(i).getEdgeTo(vertices.get(i+1)).getWeight()+" ");
		}
		System.out.println();
		System.out.println("length: "+getLength());
		System.out.println("cost: "+cost);
		System.out.println();
	}
	
	public Vertex getRoot(){
		return vertices.get(0);
	}
	
	public Vertex getEnd(){
		return vertices.get(vertices.size()-1);
	}
	
	public String[] getNames(){
		int size=vertices.size();
		String[] out=new String[size];
		for(int i=0; i<size; i++){
			out[i]=vertices.get(i).getName();
		}
		return out;
	}
	
}

