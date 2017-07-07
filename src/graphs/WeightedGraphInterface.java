package graphs;

public interface WeightedGraphInterface {

	public void addVertex(String name);
	
	public void addEdge(String from, String to, int weight);
	
	public GraphPath getShortestPath(String start, String end);
	
	public GraphPath[] getShortestPaths(String start);
	
	public GraphPath getCheapestPath(String start, String end);
	
	public GraphPath[] getCheapestPaths(String start);

	boolean isEmpty();
	
}
