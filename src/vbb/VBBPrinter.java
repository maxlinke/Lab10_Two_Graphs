package vbb;

import graphs.GraphPath;
import graphs.WeightedGraph;

public class VBBPrinter extends VBB{
	
	public VBBPrinter(String stationNameFile, WeightedGraph g){
		super(stationNameFile, g);
		System.out.println("Daten © 2017 VBB Verkehrsverbund Berlin-Brandenburg GmbH\n");
	}
	
	private void printDetails(GraphPath p){
		System.out.println(" -"+hashMap.get(p.getRoot().getName()).getName()+" to "+hashMap.get(p.getEnd().getName()).getName());
		int cost=p.getCost();
		System.out.println(" -Time: at least "+cost/60+"min "+cost%60+"sec");
		String[] stations=p.getNames();
		System.out.println(" -"+stations.length+" Stations:");
		for(int i=0; i<stations.length; i++){
			System.out.println(hashMap.get(stations[i]).getName());
		}
		System.out.println();
	}

	public void printShortestPath(String origin, String destination) {
		GraphPath p=graph.getShortestPath(origin, destination);
		printDetails(p);
	}
	
	public void printCheapestPath(String origin, String destination){
		GraphPath p=graph.getCheapestPath(origin, destination);
		printDetails(p);
	}
	
}
