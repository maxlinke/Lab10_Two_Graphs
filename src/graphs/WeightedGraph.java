package graphs;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.LinkedList;

public class WeightedGraph implements WeightedGraphInterface {

	private ArrayList<Vertex> adjacencyList;
	
	public WeightedGraph(){
		adjacencyList=new ArrayList<>();
	}
	
	public WeightedGraph(String filename){
		adjacencyList=new ArrayList<>();
		try(BufferedReader br=Files.newBufferedReader(Paths.get(filename))){
			for(String line; (line=br.readLine())!=null; ){
				String[] vertexInfo=line.split("\\s");
				String originVertex=vertexInfo[0];
				addVertex(originVertex);
				for(int i=1; i<vertexInfo.length; i++){
					String[] edgeInfo=vertexInfo[i].split(",");
					String targetVertex=edgeInfo[0];
					int weight=Integer.parseInt(edgeInfo[1]);
					addEdge(originVertex, targetVertex, weight);
				}
			}
		}catch(IOException e){ e.printStackTrace();	}		
	}
	
		//mutators
	
	@Override
	public void addVertex(String name){
		if(!listContains(name))	adjacencyList.add(new Vertex(name));
	}

	@Override
	public void addEdge(String from, String to, int weight) {
		if(!listContains(from)) adjacencyList.add(new Vertex(from));
		if(!listContains(to)) adjacencyList.add(new Vertex(to));
		if(!getVertex(from).hasEdgeTo(getVertex(to))) getVertex(from).addEdge(getVertex(to), weight);		
	}
	
		//accessors
	
	@Override
	public boolean isEmpty(){
		return adjacencyList.isEmpty();
	}
	
	private boolean listContains(String name){
		return getVertex(name)!=null;
	}
	
	private Vertex getVertex(String name){
		for(Vertex v: adjacencyList){
			if(v.getName().equals(name)) return v;
		}
		return null;
	}
	
	@Override
	public GraphPath getShortestPath(String start, String end) {
		return shortestPaths(start, true, end)[0];
	}
	
	@Override
	public GraphPath[] getShortestPaths(String start){
		return shortestPaths(start, false, "");
	}
	
	private GraphPath[] shortestPaths(String start, boolean terminate, String end){
		Vertex root=getVertex(start);
		Vertex target=getVertex(end);
		ArrayList<Vertex> unvisited=new ArrayList<>();
		LinkedList<Vertex> queue=new LinkedList<>();
		for(Vertex v: adjacencyList){		//init
			v.parent=null;					//alle parents löschen
			unvisited.add(v);				//alle vertices sind unbesucht...
		}
		Vertex current=root;				//"arbeitsvertex"
		if(current==target && terminate){						//wenn arbeitsvertex unser ziel ist, müssen wir gar nichts mehr machen
			GraphPath[] out=new GraphPath[1];					//sondern können einfach returnen
			out[0]=new GraphPath(target);
			return out;
		}
		while(!unvisited.isEmpty()){			
			unvisited.remove(current);										//da aktuelle arbeitsvertex besucht ist, wird sie aus liste entfernt
			for(Vertex child: current.getAdjacentVertices()){				//getten alle benachbarten
				if(unvisited.contains(child) && !queue.contains(child)){	//wenn "children" weder besucht, noch in der queue
					queue.addLast(child);									//an queue anhängen
					child.parent=current;									//und parent setzen
					if(child==target && terminate){							//wenn child unser ziel ist, müssen wir gar nichts mehr machen
						GraphPath[] out=new GraphPath[1];					//sondern können einfach returnen
						out[0]=new GraphPath(target);
						return out;
					}
				}
			}				
			if(!queue.isEmpty()) current=queue.removeFirst();				//ansonsten nehmen wir am ende jeder schleife stets die erste vertex aus der queue			
		}
		return getAllPaths();
	}

	@Override
	public GraphPath getCheapestPath(String start, String end) {
		return cheapestPaths(start, true, end)[0];
	}
	
	@Override
	public GraphPath[] getCheapestPaths(String start){
		return cheapestPaths(start, false, "");
	}
	
	private GraphPath[] cheapestPaths(String start, boolean terminate, String end){
		LinkedList<Vertex> unvisited=new LinkedList<>();	//wieder dank an wikipedia..
		for(Vertex v: adjacencyList){				//https://en.wikipedia.org/wiki/Dijkstra%27s_algorithm
			v.parent=null;
			v.distance=Integer.MAX_VALUE;			//distance zur root
			unvisited.add(v);
		}
		Vertex root=getVertex(start);
		Vertex target=getVertex(end);
		root.distance=0;
		while(!unvisited.isEmpty()){
			Vertex vMin=unvisited.getFirst();			//getten vertex aus liste
			for(Vertex v: unvisited){					//vergleichen distanz mit allen anderen, um die mit geringster zu finden
				if(v.distance<vMin.distance) vMin=v;//vMin= vertex minimum distance (oder so...)
			}										//vMin ist arbeitsvertex, da bereits geringster pfad zu ihr gefunden
			if(vMin==target && terminate){			//wenn vMin ziel ist und wir terminaten, dann tun wir das
				GraphPath[] out=new GraphPath[1];
				out[0]=new GraphPath(target);
				return out;
			}
			unvisited.remove(vMin);						//vMin aus liste von unbesuchten entfernen
			for(Vertex v: vMin.getAdjacentVertices()){		//iterieren über benachbarte vertices
				int newDistance=vMin.distance+vMin.getEdgeTo(v).getWeight();	//berechnen der potentiellen neuen distanz
				if(newDistance<v.distance){					//wenn neue distanz < der vorher bekannten
					v.distance=newDistance;					//dann ist das wohl ein besserer weg
					v.parent=vMin;							//und wir setzen die werte passend
				}
			}
		}
		return getAllPaths();
	}
	
	private GraphPath[] getAllPaths(){
		Vertex current;
		GraphPath[] out=new GraphPath[adjacencyList.size()];
		for(int i=0; i<adjacencyList.size(); i++){
			current=adjacencyList.get(i);
			GraphPath p=new GraphPath(current);
			if(!p.isEmpty()) out[i]=p;
			else out[i]=null;
		}
		return out;
	}
	
	public Vertex[] getAllVertices(){
		Vertex[] out=new Vertex[adjacencyList.size()];
		for(int i=0; i<adjacencyList.size(); i++){
			out[i]=adjacencyList.get(i);
		}
		return out;
	}
	
		//other
	
	public void printAdjacencyList(){
		for(Vertex v: adjacencyList){
			System.out.print(v.getName()+" ->\t");
			Vertex[] adjacent=v.getAdjacentVertices();
			for(int i=0; i<adjacent.length; i++){
				System.out.print("| "+adjacent[i].getName()+" , "+v.getWeightTo(adjacent[i])+"\t");
			}
			System.out.println("|");
		}
		System.out.println();
	}
	
}
