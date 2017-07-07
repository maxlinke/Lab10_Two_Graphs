package vbb;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;

import graphs.GraphPath;
import graphs.Vertex;
import graphs.WeightedGraph;


public class VBBDrawer extends VBB{

	private JFrame frame;
	private JPanel panel;
	private double mostNorth, mostEast, mostSouth, mostWest;
	private double nsExtent, ewExtent;
	private double mapAspectRatio;
	private ArrayList<GraphPath> paths;
	private Random rng;
	
	private final static Color[] someColors={
			new Color(18, 118, 255),
			new Color(232, 50, 50),
			new Color(101, 225, 107),
			
			new Color(50, 50, 150),
			new Color(170, 65, 65),
			new Color(118, 182, 121),
			
			new Color(108, 169, 254),
			new Color(255, 107, 107),
			new Color(165, 255, 169)
	};
	
	private final static int SIZE=640;
	
	public VBBDrawer(String stationNameFile, String coordinateFile, WeightedGraph g){
		super(stationNameFile, g);
		getAllCoordinates(coordinateFile);
		paths=new ArrayList<>();
		rng=new Random();
		display();
	}
	
	public void getAllCoordinates(String coordinateFile){
		mostEast=Double.MIN_VALUE;
		mostWest=Double.MAX_VALUE;
		mostNorth=Double.MIN_VALUE;
		mostSouth=Double.MAX_VALUE;
		try(BufferedReader br=Files.newBufferedReader(Paths.get(coordinateFile))){
			String line;
			line=br.readLine();
			while((line=br.readLine())!=null){
				String id=line.split(",")[0];
				String[] parts=line.split("\"");
				//String name=parts[3];
				double latitude=Double.parseDouble(parts[5])*1.333d;	//TODO why do i have to multiply here? ... because it'd be equirectangular if i didnt
				double longitude=Double.parseDouble(parts[7]);
				if(hashMap.containsKey(id)){
					hashMap.get(id).setLatitude(latitude);
					mostNorth = latitude>mostNorth ? latitude : mostNorth;
					mostSouth = latitude<mostSouth ? latitude : mostSouth;
					hashMap.get(id).setLongitude(longitude);
					mostEast = longitude>mostEast ? longitude : mostEast;
					mostWest = longitude<mostWest ? longitude : mostWest;
				}
			}
		}catch(IOException i){
			i.printStackTrace();
		}
		nsExtent=Math.abs(mostNorth-mostSouth);
		ewExtent=Math.abs(mostEast-mostWest);
		mapAspectRatio=ewExtent/nsExtent;
	}
	
	@SuppressWarnings("serial")
	public void display(){
		frame=new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		panel=new JPanel(){
			@Override
			public void paint(Graphics g){
				super.paint(g);
				Graphics2D g2=(Graphics2D)g;
				paintTheThing(g2, getSize());
			}
		};
		panel.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				panel.repaint();
			}
		});
		frame.setLayout(new BorderLayout());
		frame.add(panel, BorderLayout.CENTER);
		frame.pack();
		frame.setSize(SIZE, SIZE);
		frame.setVisible(true);
	}
	
	private void paintTheThing(Graphics2D g2, Dimension size){
		g2.setBackground(Color.BLACK);
		g2.clearRect(0, 0, size.width, size.height);
		double windowAspectRatio=size.getWidth()/size.getHeight();
		double scaleFactor;
		if(windowAspectRatio>mapAspectRatio){	//window is wider than map / limiter is height
			scaleFactor=size.getHeight()/nsExtent;
		}else{									//window is higher than map / limiter is width
			scaleFactor=size.getWidth()/ewExtent;
		}
		g2.setColor(Color.RED);					//check if i forgot to set any colours
		
		drawEverything(g2, size, scaleFactor);
		drawOverlay(g2, size, scaleFactor);
		
		g2.setColor(Color.WHITE);
		g2.drawString("Daten © 2017 VBB Verkehrsverbund Berlin-Brandenburg GmbH", 1, 10);
		
	}

	private int toXCoord(double longitude, double scaleFactor){
		double value=longitude-mostWest;	//makes sure leftmost point is 0 and everything to it's right is >0
		return (int)(value*scaleFactor);
	}
	
	private int toYCoord(double latitude, double scaleFactor){
		double value=(latitude-mostNorth)*(-1);		//makes sure top point is 0 and everything below is >0 (because canvas coordinates are like that...)
		return (int)(value*scaleFactor);
	}
	
	public void drawEverything(Graphics2D g2, Dimension size, double scaleFactor){
		Vertex[] vertices=graph.getAllVertices();
		for(int i=0; i<vertices.length; i++){
			
			Vertex v=vertices[i];
			int x1=toXCoord(hashMap.get(v.getName()).getLongitude(), scaleFactor);
			int y1=toYCoord(hashMap.get(v.getName()).getLatitude() , scaleFactor);
			
			g2.setColor(Color.DARK_GRAY);
			Vertex[] adj=v.getAdjacentVertices();
			for(int j=0; j<adj.length; j++){
				Vertex v2=adj[j];
				int x2=toXCoord(hashMap.get(v2.getName()).getLongitude(), scaleFactor);
				int y2=toYCoord(hashMap.get(v2.getName()).getLatitude() , scaleFactor);
				g2.drawLine(x1, y1, x2, y2);
			}
			
			g2.setColor(Color.GRAY);
			g2.drawRect(x1, y1, 1, 1);
			
		}
	}
	
	private void drawOverlay(Graphics2D g2, Dimension size, double scaleFactor) {
		for(int i=0; i<paths.size(); i++){
			GraphPath p=paths.get(i);
			drawPath(g2, size, scaleFactor, p, 0, 0, someColors[i%someColors.length]);
		}
	}
	
	private void drawPath(Graphics2D g2, Dimension size, double scaleFactor, GraphPath p, int xOffset, int yOffset, Color color){
		String[] vertexNames=p.getNames();
		for(int i=0; i<vertexNames.length-1; i++){
			String curr=vertexNames[i];
			String next=vertexNames[i+1];
			int x1=toXCoord(hashMap.get(curr).getLongitude(), scaleFactor);
			int y1=toYCoord(hashMap.get(curr).getLatitude() , scaleFactor);
			int x2=toXCoord(hashMap.get(next).getLongitude(), scaleFactor);
			int y2=toYCoord(hashMap.get(next).getLatitude() , scaleFactor);
			g2.setColor(color);
			for(int k=-1; k<2; k++){
				for(int l=-1; l<2; l++){
					g2.drawLine(x1+xOffset+k, y1+yOffset+l, x2+xOffset+k, y2+yOffset+l);
				}
			}
			
		}
		//draw rect at start and end?
	}
	
	public void drawCheapestPath(String origin, String target){
		paths.add(graph.getCheapestPath(origin, target));
		frame.repaint();
	}
	
	public void drawShortestPath(String origin, String target){
		paths.add(graph.getShortestPath(origin, target));
		frame.repaint();
	}
	
}

