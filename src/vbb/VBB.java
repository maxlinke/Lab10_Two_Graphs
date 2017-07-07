package vbb;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;

import graphs.WeightedGraph;

public abstract class VBB {

	WeightedGraph graph;
	HashMap<String, VBBStation> hashMap;
	
	public VBB(String stationNameFile, WeightedGraph graph){
		this.graph=graph;
		hashMap=new HashMap<>();
		try(BufferedReader br=Files.newBufferedReader(Paths.get(stationNameFile))){
			String line;
			while((line=br.readLine())!=null){
				String[] parts=line.split(",");
				hashMap.put(parts[0], new VBBStation(parts[1].substring(1)));
			}
		}catch(IOException i){
			i.printStackTrace();
		}
	}
	
}
