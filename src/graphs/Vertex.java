package graphs;

import java.util.ArrayList;

public class Vertex {

	public Vertex parent;	//für pathing
	public int distance;	//für pathing
	
	private String name;
	private ArrayList<Edge> edges;
	
	public Vertex(String name){
		edges=new ArrayList<>();
		this.name=name;
	}
	
	public void addEdge(Vertex to, int weight){
		edges.add(new Edge(to, weight));
	}
	
	public String getName(){
		return name;
	}
	
	public Edge getEdgeTo(String name){
		for(Edge e: edges){
			if(e.getVertex().getName().equals(name)) return e;
		}
		return null;
	}
	
	public Edge getEdgeTo(Vertex vertex){
		return getEdgeTo(vertex.getName());
	}
	
	public boolean hasEdgeTo(String name){
		return getEdgeTo(name)!=null;
	}

	public boolean hasEdgeTo(Vertex vertex) {
		return hasEdgeTo(vertex.getName());
	}
	
	public Vertex[] getAdjacentVertices(){
		Vertex[] adjacent=new Vertex[edges.size()];
		for(int i=0; i<edges.size(); i++){
			adjacent[i]=edges.get(i).getVertex();
		}
		return adjacent;
	}
	
	public int getWeightTo(String name){
		for(Edge e: edges){
			if(e.getVertex().getName().equals(name)) return e.getWeight();
		}
		return -1;
	}
	
	public int getWeightTo(Vertex vertex){
		return getWeightTo(vertex.getName());
	}
	
}

