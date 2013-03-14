package gatrimi.graphics;

import gatrimi.model.PositionedNode;
import gatrimi.model.Edge;

import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JComponent;

public class GraphicEdge extends JComponent {
	/**
	 * Le vertex servant de modèle.
	 */
	private Edge model;
	
	//CONSTRUCTEURS
	
	public GraphicEdge(Edge model) {
		if (model == null) {
			throw new IllegalArgumentException();
		}
		this.model = model;
		
		PositionedNode[] nodes = model.getNodes();
		int maxX = Math.max(nodes[0].getX(), nodes[1].getX());
		int minX = Math.min(nodes[0].getX(), nodes[1].getX());
		int maxY = Math.max(nodes[0].getY(), nodes[1].getY());
		int minY = Math.min(nodes[0].getX(), nodes[1].getY());
		
		int x = minX + maxX - minX / 2;
		int y = minY + maxY - minY / 2;
		setLocation(x, y);
		createController();
		model.notifyObservers();
	}
	
	//COMMANDES
	@Override
	protected void paintComponent(Graphics g) {
		drawVertex(g);
	}
	
	//METHODES OUTILS
	
	/**
	 * Dessine une ligne représentant un arc entre deux noeuds.
	 */
	private void drawVertex(Graphics g) {
		PositionedNode[] nodes = model.getNodes();
		g.setColor(model.getColor());
		int offset = GraphicPositionedNode.DIAMETER / 2;
		g.drawLine(nodes[0].getX() + offset, nodes[0].getY() + offset,
				   nodes[1].getX() + offset, nodes[1].getY() + offset);
	}
	
	/**
	 * Ajoute un observateur au model afin de se repeindre automatiquement.
	 */
	private void createController() {
		model.addObserver(new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				repaint();
			}
		});
		
		//Si un des noeuds change de position, on se redessine aussi
		final PositionedNode[] nodes = model.getNodes();
		Observer o = new Observer() {
			
			@Override
			public void update(Observable o, Object arg) {
				int maxX = Math.max(nodes[0].getX(), nodes[1].getX());
				int minX = Math.min(nodes[0].getX(), nodes[1].getX());
				int maxY = Math.max(nodes[0].getY(), nodes[1].getY());
				int minY = Math.min(nodes[0].getX(), nodes[1].getY());
				
				int x = minX + maxX - minX / 2;
				int y = minY + maxY - minY / 2;
				setLocation(x, y);
			
				repaint();
			}
		};
		nodes[0].addObserver(o);
		nodes[1].addObserver(o);
	}
}
