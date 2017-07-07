package graphs;

public class Edge{
	
	private Vertex to;
	private int weight;
	
	public Edge(Vertex to, int weight){
		this.to=to;
		this.weight=weight;
	}
	
	public int getWeight(){ return weight; }
	
	public Vertex getVertex(){ return to; }
	
}