import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.*;
import java.util.ArrayList;
import java.util.Set;

import javax.swing.*;



public class CartesianPlane extends JPanel {

	private static final long serialVersionUID = 1L;
	final double spacing = 20;
	static Set<Point> positive;
	static Set<Point> negative;
	static Individual ind;
	ArrayList<Point> polynomial = new ArrayList<>();
	ArrayList<Point> lastPolynomial = new ArrayList<>();
	private int[] pointX;
	private int[] pointY;
	
	public CartesianPlane(Set<Point> positive, Set<Point> negative) {
		setBackground(Color.WHITE);
		CartesianPlane.positive=positive;
		CartesianPlane.negative=negative;
		ind=Generation.fittestInd;
	}


	private void lines(Graphics2D g2, double x1, double y1, double x2, double y2) {

		g2.draw(new Line2D.Double(x1, y1, x2, y2));
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		final double width = getWidth();
		final double height = getHeight();

		final double xaxis = width / 2.0;
		final double yaxis = height / 2.0;
		final double x1 = 0;
		final double y1 = 0;
		final double x2 = width;
		final double y2 = height;

		Graphics2D g2 = (Graphics2D) g;
		

		g2.setColor(Color.GRAY);
		g2.setStroke(new BasicStroke(1));

		for (double x = spacing; x < width; x += spacing) {

			lines(g2, xaxis + x, y1, xaxis + x, y2);
			lines(g2, xaxis - x, y1, xaxis - x, y2);
		}

		for (double y = spacing; y < height; y += spacing) {

			lines(g2, x1, yaxis + y, x2, yaxis + y);
			lines(g2, x1, yaxis - y, x2, yaxis - y);
		}

		g.setColor(Color.BLACK);
		g2.setStroke(new BasicStroke(2));

		g2.draw(new Line2D.Double(x1, yaxis, x2, yaxis));
		g2.draw(new Line2D.Double(xaxis, y1, xaxis, y2));
		
		int x;
		int y;
		g2.setColor(Color.BLUE);
		for (Point p : positive) {
			//0 - 788
			//2 - 788 + 2 * (20)
			//x = 788 + p.getX()*20;
			
			//y - 0 - 473
			//y - 473 - p.getY()*20;
			x=788 + p.getX()*20;
			y=473 - p.getY()*20;
		    g2.fillOval(x, y, 6, 6);
		}
		g2.setColor(Color.RED);
		
		for (Point p : negative) {
			x=788 + p.getX()*20;
			y=473 - p.getY()*20;
		    g2.fillOval(x, y, 6, 6);
		} 
		
		if(polynomial.size()>0){
			
			try {
				for(int i=0; i<polynomial.size() && i < pointX.length; i++){
					pointX[i]=polynomial.get(i).getX();
					pointY[i]=polynomial.get(i).getY();
				}
				
				int i=0;
				for(Point p : polynomial){
					pointX[i] = 788 + p.getX()*20;
					pointY[i] = 473 - p.getY()*20;
					i++;
				}
				
				g2.drawPolyline(pointX, pointY, polynomial.size());
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
	}
}

	public void setPolynomial(Individual currentFittest) { 
		polynomial.clear();
		ind = currentFittest;
		int counter = ind.genes.size()-1;
		int result=0;
		for(int i=-100; i<=100; i++){
			for(Integer gene : ind.genes){
		    	if(counter!=0){
		    		int power = (int) Math.pow(i, counter);
		    		result = result + (gene*power);
					counter--;
		    	}
		    	else{
		    		result = result + gene;
		    		Point p = new Point(i, result);
		    		polynomial.add(p);
		    	}
			 }	
			result=0;
			counter = ind.genes.size()-1;
		}
		pointX = new int[201];
		pointY = new int[201];
		repaint();
	}
}