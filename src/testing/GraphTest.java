package testing;

import static org.junit.Assert.*;
import org.junit.Test;
import graphs.WeightedGraph;

public class GraphTest {
	
	WeightedGraph g=new WeightedGraph("own1.txt");
	
	@Test
	public void testingSamples(){
		assertEquals(g.getShortestPath("1", "5").getLength()==2, true);
		assertEquals(g.getCheapestPath("1", "5").getCost()==4, true);
		assertEquals(g.getShortestPath("1", "7").getCost()==10, true);
		assertEquals(g.getCheapestPath("1", "7").getLength()==4, true);
		assertEquals(g.getCheapestPath("7", "1").getLength()==4, true);
		assertEquals(g.getCheapestPath("8", "8").getLength()==0, true);
		assertEquals(g.getShortestPath("8", "8").getLength()==0, true);
	}
	
	@Test
	public void testShortAndCheap(){						//test, ob der billigere pfad nie teurer ist, 
		boolean result=true;								//als der kürzere und ob der kürzere nie länger
		for(int i=1; i<10; i++){							//ist, als der billigere
			for(int j=1; j<10; j++){
				result=result&&cheaperAndShorter(g, ""+i, ""+j);
			}
		}
		assertEquals(result, true);
	}
	
	@Test
	public void testZeroDistance(){							//test, distanz zu selbst stets null (war einmal ein problem!!!)
		boolean result=true;
		for(int i=1; i<10; i++){
			result=result&&g.getShortestPath(""+i, ""+i).getLength()==0;
			result=result&&g.getCheapestPath(""+i, ""+i).getLength()==0;
		}
		assertEquals(result, true);
	}

	private boolean cheaperAndShorter(WeightedGraph g, String start, String end){
		boolean cheapWins=(g.getCheapestPath(start, end).getCost()) <= (g.getShortestPath(start, end).getCost());
		boolean shortWins=(g.getShortestPath(start, end).getLength()) <= (g.getCheapestPath(start, end).getLength());
		return cheapWins && shortWins;
	}
	
}
