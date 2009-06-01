package ubadb.tools.scheduleAnalyzer.common;

import java.applet.Applet;
import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.GeneralPath;
import java.util.List;

public class GrafoApplet extends Applet {

	public GrafoApplet(ScheduleGraph grafo) {
		DibujaGrafo canvas = new DibujaGrafo(grafo);
		this.add(canvas, BorderLayout.CENTER);
	}

	public Dimension preferredSize() {
		return new Dimension(800, 600);
	}
}

class DibujaGrafo extends Canvas {
	ScheduleGraph grafo;

	public DibujaGrafo(ScheduleGraph grafo) {
		this.grafo = grafo;
		setBackground(Color.white);
	}

	public void update(Graphics g) {
		paint(g);
	}

	private int getIndex(List<String> lista, String transaccion){
		for (int i = 0; i < lista.size(); i++) {
			if(transaccion.equals(lista.get(i)))
				return i+1;
		}
		return 0;
	}
	
	public void paint(Graphics g) {
		Dimension d = preferredSize();
		int diametroNodo = 10;
		int radioNodo = diametroNodo / 2;
		Image imagenRect = createImage(d.width, d.height);
		int total = this.grafo.getTransactions().size();
		int aumento = 100;

		for (int i = 1; i < total + 1; i++) {
			double coordX = d.width / 2
					+ Math.sin(Math.toRadians((360 / total) * i)) * aumento;
			double coordY = d.height / 2
					+ Math.cos(Math.toRadians((360 / total) * i)) * aumento;

			g.setColor(Color.blue);
			g.fillOval((int) coordX - radioNodo, (int) coordY - radioNodo, diametroNodo, diametroNodo);
			g.drawString(this.grafo.getTransactions().get(i-1), (int) coordX + 10, (int) coordY);
		}

		System.out.println("arcos="+this.grafo.getArcs().size());
		for (int j = 0; j < this.grafo.getArcs().size(); j++) {
			
			int it1 = this.getIndex(this.grafo.getTransactions(), this.grafo.getArcs().get(j).getStartTransaction());
			int it2 = this.getIndex(this.grafo.getTransactions(), this.grafo.getArcs().get(j).getEndTransaction());
				
			int coordX1 = (int) (d.width / 2 + Math.sin(Math.toRadians((360 / total) * it1))* aumento);
			int coordY1 = (int) (d.height / 2 + Math.cos(Math.toRadians((360 / total) * it1))* aumento);
			int coordX2 = (int) (d.width / 2 + Math.sin(Math.toRadians((360 / total) * it2))* aumento);
			int coordY2 = (int) (d.height / 2 + Math.cos(Math.toRadians((360 / total) * it2))* aumento);
			
			
			double rotacion; 
			if(it2>it1){
				rotacion = (it1*(360/total)) + ((it2-it1)*(360/(total*2)));
			}else{
				rotacion = (it1*(360/total)) + ((total-it1+it2)*(360/(total*2)));
			}
			if(rotacion>360) rotacion-=360;
			
			dibujarFlecha(g, coordX1, coordY1, coordX2, coordY2,rotacion);
		}

	}

	private void dibujarFlecha(Graphics g, int x0, int y0, int x1, int y1,double rotacion) {
		Graphics2D graphics2D = (Graphics2D) g;
		int b = 12;
		AffineTransform at = AffineTransform.getTranslateInstance(x1, y1);
		double theta = 25;

		GeneralPath path = new GeneralPath();

		int x;
		int y;
			
		x = (int) ( -b * Math.cos(Math.toRadians(theta)));
		y = (int) ( -b * Math.sin(Math.toRadians(theta)));
		path.moveTo(0, 0);
		path.lineTo(x, y);
		x = (int) ( -b * Math.cos(Math.toRadians(theta)));
		y = (int) ( b * Math.sin(Math.toRadians(theta)));
		path.moveTo(0, 0);
		path.lineTo(x, y);

		at.rotate(Math.toRadians(360-rotacion));
		Shape shape = at.createTransformedShape(path);

		graphics2D.setPaint(Color.black);
		graphics2D.draw(shape);

		g.setColor(Color.black);
		g.drawLine(x0, y0, x1, y1);
	}

	public Dimension preferredSize() {
		return new Dimension(800, 600);
	}
}