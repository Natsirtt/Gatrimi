package gatrimi.graphics;

import gatrimi.model.Graph;
import gatrimi.model.PositionedNode;
import gatrimi.model.StdEdge;
import gatrimi.model.Edge;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

import javax.swing.JComponent;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;

public class GraphicGraph extends JComponent {
	
	//ATTRIBUTS STATIQUES
	
	/**
	 * L'espace entre deux noeuds lors de la création de plusieurs noeuds.
	 */
	public static final int SPACE_DEFAULT = 10;
	
	//ATTRIBUTS
	
	/**
	 * Le graphe servant de modèle.
	 */
	private Graph model;
	
	/**
	 * L'ensemble des GraphicPositionedNode correspondants à ce graphe.
	 */
	private Set<GraphicPositionedNode> graphicNodes;
	
	/**
	 * L'ensemble des GraphicVertex correspondants à ce graphe.
	 */
	private Set<GraphicEdge> graphicVertexes;
	
	private static final int DEFAULT_WIDTH = 400;
	private static final int DEFAULT_HEIGHT = 300;
	
	//CONSTRUCTEURS
	
	public GraphicGraph(Graph model) {
		if (model == null) {
			throw new IllegalArgumentException();
		}
		this.model = model;
		graphicNodes = new HashSet<GraphicPositionedNode>();
		graphicVertexes = new HashSet<GraphicEdge>();
		Dimension preferredSize = new Dimension(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		setPreferredSize(preferredSize);
		createController();
		//model.notifyObservers();
	}
	
	//REQUÊTES
	
	public Dimension idealDrawDimension() {
		int idealX = -1;
		int idealY = -1;
		for (PositionedNode n : model.getNodes()) {
			if (n.getX() > idealX) {
				idealX = n.getX();
			}
			if (n.getY() > idealY) {
				idealY = n.getY();
			}
		}
		return new Dimension(idealX + 2 * GraphicPositionedNode.DIAMETER,
							 idealY + 2 * GraphicPositionedNode.DIAMETER);
	}
	
	public Dimension minXandMinY() {
		int minX = (int) idealDrawDimension().getWidth();
		int minY = (int) idealDrawDimension().getHeight();
		for (PositionedNode n : model.getNodes()) {
			if (n.getX() < minX) {
				minX = n.getX();
			}
			if (n.getY() < minY) {
				minY = n.getY();
			}
		}
		return new Dimension(minX, minY);
	}
	
	//COMMANDES
	
	public void translate(int deltaX, int deltaY) {
		for (PositionedNode n : model.getNodes()) {
			if (n.getX() + deltaX < 0 || n.getY() + deltaY < 0) {
				throw new IllegalArgumentException();
			}
		}
		for (PositionedNode n : model.getNodes()) {
			n.setPosition(n.getX() + deltaX, n.getY() + deltaY);
		}
	}
	
	//METHODES OUTILS
	
	private void createController() {
		model.addObserver(new Observer() {			
			@Override
			public void update(Observable o, Object arg) {
				makeSets();
				repaint();
			}
		});
		
		this.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				requestFocus();
				if (e.getButton() == MouseEvent.BUTTON3) {
					final int x = e.getX();
					final int y = e.getY();
					
					JPopupMenu popUp = new JPopupMenu("Action");
					JMenuItem addNode = new JMenuItem("Add Node"); {
						addNode.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent arg0) {
								model.addNode(
										x - GraphicPositionedNode.DIAMETER / 2, 
										y - GraphicPositionedNode.DIAMETER / 2);
								model.notifyObservers();
							}							
						});
					}
					
					JMenuItem refresh = new JMenuItem("Refresh"); {
						refresh.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								repaint();
							}
						});
					}
					
					popUp.add(addNode);
					popUp.add(refresh);
					//S'il n'y a pas de model, on ne fait rien
					if (model != null) {
						popUp.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		});
	}
	
	/**
	 * Reconstruit les deux ensembles.
	 */
	private void makeSets() {
		this.removeAll();
		makeNodesSet();
		makeVertexesSet();
	}
	
	private void addMouseBehavior(GraphicEdge v) {
		/*v.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				System.out.println("Edge cliqué");
			}
		});*/
	}
	
	private void addMouseBehavior(GraphicPositionedNode gNode) {
		gNode.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				final GraphicPositionedNode src =
								(GraphicPositionedNode) e.getSource();
				if (e.getButton() == MouseEvent.BUTTON3) {
					GraphicPositionedNode focusOwner = null;
					for (GraphicPositionedNode n2 : graphicNodes) {
						if (n2.isFocusOwner() && !n2.equals(src)) {
							focusOwner = n2;
						}
					}
					//Gestion du pop-up menu
					JPopupMenu popUp = new JPopupMenu("Action");
					JMenuItem delete = new JMenuItem("Delete node"); {
						final PositionedNode n3 = src.getModel();
						delete.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								model.removeNode(n3);
								model.notifyObservers();
							}							
						});
					}
					if (focusOwner != null) {
						final PositionedNode n1 = focusOwner.getModel();
						final PositionedNode n2 = src.getModel();
						
						final Edge v = new StdEdge(n1, n2);
						JMenuItem menuItem;
						
						if (!model.areAdjacent(n1, n2)) {
							menuItem = new JMenuItem("Add edge"); {
								menuItem.addActionListener(
														new ActionListener() {
									@Override
									public void actionPerformed(ActionEvent e) {
										model.addVertex(v);
										model.notifyObservers();
									}
								});
							}
						} else {
							menuItem = new JMenuItem("Delete edge"); {
								menuItem.addActionListener(
														new ActionListener() {
									
									@Override
									public void actionPerformed(ActionEvent e) {
										model.removeVertex(v);
										model.notifyObservers();
									}
								});
							}
						}
						popUp.add(menuItem);
					}
					
					JMenuItem menuItem = new JMenuItem("Delete all edges"); {
						menuItem.addActionListener(new ActionListener() {
							
							@Override
							public void actionPerformed(ActionEvent e) {
								PositionedNode n = src.getModel();
								model.removeVertexes(model.getVertexes(n));
								model.notifyObservers();
							}
						});
					}
					if (model.getVertexesNb(src.getModel()) > 0) {
						popUp.add(menuItem);
						popUp.add(new JSeparator());
					}
					popUp.add(delete);
					popUp.show(e.getComponent(), e.getX(), e.getY());
				}
			}
		});
	}
	
	/**
	 * Reconstruit l'ensemble des GraphicPositionedNode par rapport au graphe.
	 */
	private void makeNodesSet() {
		graphicNodes = new HashSet<GraphicPositionedNode>();
		for (PositionedNode n : model.getNodes()) {
			GraphicPositionedNode gNode = new GraphicPositionedNode(n);
			graphicNodes.add(gNode);
			//TODO vérifier que c'est inutile
			//gNode.setLocation(gNode.getX(), gNode.getY());
			addMouseBehavior(gNode);
			this.add(gNode);
		}
	}

	/**
	 * Reconstruit l'ensemble des GraphicVertex par rapport au graphe.
	 */
	private void makeVertexesSet() {
		graphicVertexes = new HashSet<GraphicEdge>();
		for (Edge v : model.getVertexes()) {
			final GraphicEdge gv = new GraphicEdge(v);
			graphicVertexes.add(gv);
			addMouseBehavior(gv);
			this.add(gv);
		}
	}
	
	/**
	 * Dessine le graphe.
	 */
	@Override
	protected void paintComponent(Graphics g) {
		for (GraphicEdge v : graphicVertexes) {
			//v.repaint();
			v.paintComponent(g);
		}
		for (GraphicPositionedNode n : graphicNodes) {
			//n.repaint();
			n.paintComponent(g);
		}
	}
}
