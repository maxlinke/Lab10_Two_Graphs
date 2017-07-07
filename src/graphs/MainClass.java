package graphs;
import vbb.*;

// VBB-Daten © 2017 VBB Verkehrsverbund Berlin-Brandenburg GmbH
// Quelltext : Max Linke

public class MainClass {
	
	public static void main(String[] args){
		MainClass m=new MainClass();
		m.vbb();				//das sbahn-zeug
		//m.own1();				//eigener graph
		//m.own2and3();			//test für cheapest path
	}
	
	public void vbb(){
		WeightedGraph g=new WeightedGraph("bvg.txt");
		VBBPrinter printer=new VBBPrinter("stations.txt", g);
		VBBDrawer drawer=new VBBDrawer("stations.txt", "stops.txt", g);
		
		//schöne ausgabe, davon abgesehen haben sich beide pfade in bis auf einem fall nicht unterschieden
		printer.printCheapestPath("060192001006", "060053301433");
		printer.printShortestPath("060192001006", "060066102852");
		printer.printShortestPath("060192001006", "060120003653");
		printer.printShortestPath("060192001006", "060068201511");

		//hier war der einzige unterschied zwischen kürzestem und billigstem pfad festzustellen
//		System.out.println("SHORTEST: ");
//		printer.printShortestPath("060192001006", "060053301433");
//		System.out.println("CHEAPEST: ");
//		printer.printCheapestPath("060192001006", "060053301433");
		
		//die gewollten pfade, gezeichnet.
		drawer.drawCheapestPath("060192001006", "060053301433");
		drawer.drawShortestPath("060192001006", "060053301433");
		drawer.drawShortestPath("060192001006", "060066102852");
		drawer.drawShortestPath("060192001006", "060120003653");
		drawer.drawShortestPath("060192001006", "060068201511");
		
//		drawer.drawCheapestPath("060192001006", "060053301433");	//schöneweide > wannsee
//		drawer.drawShortestPath("060152002051", "060171002001");	//gehrenseestr> fried. ost
//		drawer.drawShortestPath("060110004531", "060200005032");	//landsberger > oranienburg
//		drawer.drawShortestPath("060320008004", "060029101731");	//hoppegarten > spandau
//		drawer.drawCheapestPath("060320008004", "060260005671");	//hoppegarten > schönefeld
	}
	
	public void printBothPaths(WeightedGraph g, String from, String to){
		System.out.println("shortest: ");
		g.getShortestPath(from, to).printDetails();
		System.out.println("cheapest: ");
		g.getCheapestPath(from, to).printDetails();
	}
	
	public void own1(){
		WeightedGraph g=new WeightedGraph("own1.txt");
		g.printAdjacencyList();
		String from="1";
		GraphPath[] shortest=g.getShortestPaths(from);
		GraphPath[] cheapest=g.getCheapestPaths(from);
		System.out.println("shortest paths from "+from);
		for(int i=0; i<shortest.length; i++){ 
			System.out.print("to "+shortest[i].getEnd().getName()+"\t");
			shortest[i].printPath();
		}
		System.out.println();
		System.out.println("cheapest paths from "+from);
		for(int i=0; i<cheapest.length; i++){ 
			System.out.print("to "+cheapest[i].getEnd().getName()+"\t");
			cheapest[i].printPath(); 
		}
		System.out.println();
		System.out.println("done");
	}
	
	public void own2and3(){
		new WeightedGraph("own2.txt").getCheapestPath("1", "4").printDetails();
		new WeightedGraph("own3.txt").getCheapestPath("1", "4").printDetails();
	}
	
	/*
	public WeightedGraph createGraphOld(String filename){
		WeightedGraph g=new WeightedGraph();
		Path p=Paths.get(filename);
		try(BufferedReader br=Files.newBufferedReader(p)){
			int lastThing=10;						//letztes sonderzeichen
			long originVertex=-1;					//vertex, zu der angegebene edges gehören
			String mem="";							//string, enthält gelesenen input, wird ausgelesen für wichtige teile
			for(int i; (i=br.read())!=-1; ){				//string stück für stück lesen
				//i==9  ist TAB
				//i==10 ist LF bzw NEWLINE
				//i==32 ist SPACE
				if(i==9 || i==10 || i==32){								//sonderzeichen
					if(lastThing==9||lastThing==32){					//wenn letztes zeichen leer/tab, haben wir eine edge vorliegen
						String[] things=mem.split(",");					//vertex und weight aus string entnehmen
						long targetVertex=Long.parseLong(things[0]);	//vertexnamen parsen
						int weight=Integer.parseInt(things[1]);			//weight parsen
						g.addEdge(originVertex, targetVertex, weight);	//edge hinzufügen
					}else if(lastThing==10){							//wenn letztes zeichen newline, haben wir eine vertex vorliegen
						originVertex=Long.parseLong(mem);				//namen parsen
						g.addVertex(originVertex);						//hinzufügen
					}
					lastThing=i;										//sonderzeichen merken, damit wir nächstes mal wissen, was wir hatten
					mem="";												//speicherstring leeren
				}else{					//wenn kein sonderzeichen
					mem+=(char)i;		//character zum speicherstring hinzufügen
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		return g;
	}
	*/
	
}
