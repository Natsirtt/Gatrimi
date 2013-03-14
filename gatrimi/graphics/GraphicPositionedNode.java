package gatrimi.graphics;

import gatrimi.model.PositionedNode;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Observable;
import java.util.Observer;

import javax.swing.BorderFactory;
import javax.swing.JComponent;

public class GraphicPositionedNode extends JComponent {
		
	//ATTRIBUTS CONSTANTS
	
	/**
	 * Le diamètre d'un sommet.
	 */
	public static final int DIAMETER = 30;
	
	//ATTRIBUTS
	
	/**
	 * Le PositionedNode qui sert de modèle à ce composant.
	 */
	private PositionedNode model;
	
	//Attributs pour le drag'n'drop
	private int screenX;
	private int screenY;
	private int myX;
	private int myY;
	
	//CONSTRUCTEURS
	
	public GraphicPositionedNode(PositionedNode model) {
		if (model == null) {
			throw new IllegalArgumentException();
		}
		setSize(new Dimension(DIAMETER, DIAMETER));
		this.model = model;
		this.setLocation(model.getX(), model.getY());
		createController();
		model.notifyObservers();
	}
	
	//REQUETE
	
	public PositionedNode getModel() {
		return model;
	}
	
	//COMMANDES
	
	@Override
	protected void paintComponent(Graphics g) {
		drawNode(g);
	}
	
	//METHODES OUTILS
	
	/**
	 * Dessine un rond pour le noeud, bien placé sur le Graphics g.
	 */
	private void drawNode(Graphics g) {
		g.setColor(model.getColor());
		g.fillOval(model.getX(), model.getY(), DIAMETER, DIAMETER);
		g.setColor(Color.GRAY);
		g.drawOval(model.getX(), model.getY(), DIAMETER, DIAMETER);
		String nb = String.valueOf(model.getNumber());
		int stringX;
		int stringY;
		FontMetrics fm = g.getFontMetrics();
		stringX = model.getX() + DIAMETER / 2 - fm.stringWidth(nb) / 2;
		stringY = model.getY() + DIAMETER / 2 + fm.getAscent() / 2;
		if (model.getColor() == Color.WHITE
				|| model.getColor() == Color.YELLOW) {
			g.setColor(Color.BLACK);
		} else {
			g.setColor(Color.WHITE);
		}
		g.drawString(nb, stringX, stringY);
	}
	
	/**
	 * Ajoute un observateur au model afin de se repeindre automatiquement dès
	 * que nécessaire.
	 */
	private void createController() {
		model.addObserver(new Observer() {			
			@Override
			public void update(Observable o, Object arg) {
				setLocation(model.getX(), model.getY());
				Container parent = getParent();
				if (parent == null) {
					repaint();
				} else {
					//On repeint tout le graphe éventuellement présent
					//afin d'éviter les traces
					parent.repaint();
				}
			}
		});
		
		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				screenX = e.getXOnScreen();
				screenY = e.getYOnScreen();
				myX = model.getX();
				myY = model.getY();
			}
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1
						&& !isFocusOwner()) {
					requestFocus();
				}
			}
		});
		
		this.addMouseMotionListener(new MouseMotionListener() {
			@Override
			public void mouseDragged(MouseEvent e) {		
				//La souris est dragguée, et donc cliquée. On demande le focus
				requestFocus();
				
				int deltaX = e.getXOnScreen() - screenX;
				int deltaY = e.getYOnScreen() - screenY;
				if (myX + deltaX >= 0 
					&& myX + deltaX + DIAMETER <= getParent().getWidth()) {
					model.setPosition(myX + deltaX, model.getY());
				}
				if (myY + deltaY >= 0 
					&& myY + deltaY + DIAMETER <= getParent().getHeight()) {
					model.setPosition(model.getX(), myY + deltaY);
				}
				model.notifyObservers();
			}

			@Override
			public void mouseMoved(MouseEvent arg0) { }			
		});
		
		this.addFocusListener(new FocusListener() {			
			@Override
			public void focusLost(FocusEvent e) {
				setBorder(BorderFactory.createEmptyBorder());
				if (getParent() != null) {
					getParent().repaint(); //TODO on a parfois une exception
										   //comme quoi c'est null alors voila
				}
			}
			
			@Override
			public void focusGained(FocusEvent e) {
				setBorder(BorderFactory.createLineBorder(Color.RED, 1));
				repaint();
			}
		});
	}
}
