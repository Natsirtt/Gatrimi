package gatrimi.model;

import java.awt.Color;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

public abstract class AbstractGraph extends Observable implements Graph {

	//ATTRIBUTS	
	private Set<Edge> vertexes;
	private File file;
	private boolean isSynchronized;
	
	//CONSTRUCTEUR
	
	protected AbstractGraph() {
		vertexes = new HashSet<Edge>();
		setChanged();
	}
	
	protected AbstractGraph(Graph g) {
		if (g == null) {
			throw new IllegalArgumentException();
		}
		vertexes = new HashSet<Edge>(g.getVertexes());
		file = g.getFile();
		setChanged();
	}
	
	
	//REQUETES
	
	protected Set<Edge> getVertexesSet() {
		return vertexes;
	}
	
	public File getFile() {
		return file;
	}
	
	public boolean isSynchronized() {
		return isSynchronized;
	}
	
	@Override
	public Set<Edge> getVertexes() {
		Set<Edge> res = new HashSet<Edge>();
		for (Edge v : vertexes) {
			res.add(v);
		}
		return res;
	}

	@Override
	public int getVertexesNb(PositionedNode n) {
		if (n == null || !getNodes().contains(n)) {
			throw new IllegalArgumentException();
		}
		
		int nb = 0;
		for (Edge v : vertexes) {
			PositionedNode[] node = v.getNodes();
			if (node[0].equals(n) || node[1].equals(n)) {
				nb++;
			}
		}
		return nb;
	}

	@Override
	public int getVertexesNb() {
		return vertexes.size();
	}

	@Override
	public Set<Edge> getVertexes(PositionedNode n) {
		if (n == null || !getNodes().contains(n)) {
			throw new IllegalArgumentException();
		}
		
		Set<Edge> res = new HashSet<Edge>();
		for (Edge v : vertexes) {
			PositionedNode[] node = v.getNodes();
			if (node[0].equals(n) || node[1].equals(n)) {
				res.add(v);
			}
		}
		return res;
	}

	@Override
	public boolean areAdjacent(Edge v1, Edge v2) {
		if (v1 == null || v2 == null
				|| !getVertexes().contains(v1) || !getVertexes().contains(v2)) {
			throw new IllegalArgumentException();
		}
		
		PositionedNode[] n1 = v1.getNodes();
		PositionedNode[] n2 = v2.getNodes();
		return n1[0].equals(n2[0]) || n1[0].equals(n2[1])
				|| n1[1].equals(n2[0]) || n1[1].equals(n2[1]);
	}

	@Override
	public Set<Edge> getAdjacents(Edge v) {
		if (v == null || !getVertexes().contains(v)) {
			throw new IllegalArgumentException();
		}
		
		Set<Edge> res = new HashSet<Edge>();
		for (Edge v2 : vertexes) {
			if (areAdjacent(v, v2)) {
				res.add(v2);
			}
		}
		return res;
	}

	
	//COMMANDES
	
	protected void setVertexesSet(Set<Edge> s) {
		vertexes = s;
	}
	
	protected void setSynchronized(boolean b) {
		isSynchronized = b;
	}
	
	public void setFile(File file) {
		this.file = file;
		setChanged();
	}
	
	@Override
	public void addVertex(Edge v) {
		if (v == null || !getNodes().contains(v.getNodes()[0])
					  || !getNodes().contains(v.getNodes()[1])) {
			throw new IllegalArgumentException();
		}
		
		vertexes.add(v);
		setChanged();
	}

	@Override
	public void removeVertex(Edge v) {
		if (v == null) {
			throw new IllegalArgumentException();
		}
		boolean isContained = false;
		for (Edge v2 : vertexes) {
			if (v2.equals(v)) {
				isContained = true;
			}
		}
		if (!isContained) {
			throw new IllegalArgumentException();
		}
		vertexes.remove(v);
		setChanged();
	}

	@Override
	public void removeVertexes(Set<Edge> vertexes) {
		if (vertexes == null || vertexes.size() == 0) {
			throw new IllegalArgumentException();
		}
		for (Edge v : vertexes) {
			if (!this.vertexes.contains(v)) {
				throw new IllegalArgumentException();
			}
		}
		
		this.vertexes.removeAll(vertexes);
		setChanged();
	}

	/**
	 * Coloriage des arêtes en passant par un graphe intermédiaire.
	 * @throws NotEnoughColorsException
	 */
	@Override
	public void colorizeVertexes() throws NotEnoughColorsException {		 
		// Table des correspondances
		Map<Edge, PositionedNode> transposition;
		
		// Le graphe intermédiaire
		Graph convertedGraph;
		
		transposition = new HashMap<Edge, PositionedNode>();
		convertedGraph = new ListGraph();
		
		//Pour chaque arêtes de this, on créé un nouveau noeud dans
		//convertedGraph, puis on met à jour la table

		for (Edge v : vertexes) {
			convertedGraph.addNode();
			
			for (PositionedNode n : convertedGraph.getNodes()) {
				if (n.getNumber() == convertedGraph.getNodesNb()) {
					transposition.put(v, n);
				}
			}
		}
		
		// On créé les arêtes de convertedGraph
		for (Edge v : transposition.keySet()) {
			PositionedNode n1 = transposition.get(v);
			for (Edge adjV : getAdjacents(v)) {
				PositionedNode n2 = transposition.get(adjV);
				convertedGraph.addVertex(new StdEdge(n1, n2));
			}
		}
		
		// On colorie les noeuds de convertedGraph
		convertedGraph.colorizeNodes();
		
		//Les couleurs des noeuds de convertedGraph correspondent aux couleurs
		//des arêtes de this

		for (PositionedNode n : transposition.values()) {
			for (Edge v : transposition.keySet()) {
				if (transposition.get(v).equals(n)) {
					v.setColor(n.getColor());
				}
			}
		}
		
		setChanged();
	}

	/**
	 * Coloration du graphe selon l'algorithme DSATUR.
	 * @throws NotEnoughColorsException 
	 */
	@Override
	public void colorizeNodes() throws NotEnoughColorsException {
		for (PositionedNode n : getNodes()) {
			n.setColor(PositionedNode.COLOR_DEFAULT);
		}
		//La map associant chaque noeud à son degré (tab[0]) et à son degré
		//de saturation (tab[1])
		Map<PositionedNode, Integer[]> degreesMap 
			= new HashMap<PositionedNode, Integer[]>();
			
		//L'ensemble des noeuds restant à colorier
		Set<PositionedNode> nodesToColorize =
									new HashSet<PositionedNode>(getNodes());
		
		// Initialisation des degrés et degrés de saturations de tout les noeuds
		getDegrees(degreesMap, getNodes());
		
		// 1. Ordonner les sommets par ordre décroissant de degré.
		PositionedNode maxDegreeNode = getMaxDegreeNode(degreesMap);
		if (maxDegreeNode != null) {
	     	// 2. Colorer un des sommets de degré maximum avec la couleur 1.
			colorizeNode(maxDegreeNode,
						PositionedNode.COLORS[PositionedNode.INDEX_COLOR_INIT]);
			nodesToColorize.remove(maxDegreeNode);
			
			
			while (!nodesToColorize.isEmpty()) {
		     	// 3. Choisir un sommet non coloré avec DSAT maximum.
				maxDegreeNode = getMaxSatDegreeNode(degreesMap);
				
		     	// 4. Colorer ce sommet par la plus petite couleur possible
				Color newColor = null;
				Set<Color> neighbourColors = getNeighbourColors(maxDegreeNode);
				for (int i = PositionedNode.INDEX_COLOR_INIT;
					 i < PositionedNode.COLORS.length; i++) {
					
					Color c = PositionedNode.COLORS[i];
					
					if (!neighbourColors.contains(c)) {
						newColor = c;
						break;
					}
				}
				
				if (newColor == null) {
					throw new NotEnoughColorsException();
				}
				
				colorizeNode(maxDegreeNode, newColor);
				nodesToColorize.remove(maxDegreeNode);
			}
	     	// 5. Si tous les sommets sont colorés alors stop. Sinon aller en 3.
		}
		setChanged();
	}
	
	@Override
	public void save() throws IOException {
		if (file == null) {
			throw new IllegalArgumentException();
		}
		if (!file.exists()) {
			file.createNewFile();
		}
		
		ObjectOutputStream output;
		try {
			output = new ObjectOutputStream(
						new BufferedOutputStream(
								new FileOutputStream(file)));
		} catch (FileNotFoundException e) {
			throw new InternalError("Fichier non trouvé"); //Impossible
		}
		
		try {
			output.writeObject(new ListGraph(this));
		} finally {
			output.close();
		}
		isSynchronized = true;
		setChanged();
	}
	
	
	
	
	// Outils
	
	/**
	 * Renvoie la map des degrés de saturation.
	 */
	private void getDegrees(Map<PositionedNode, Integer[]> dSatMap,
			Set<PositionedNode> nodes) {
		
		for (PositionedNode n : nodes) {
			Integer[] degrees = new Integer[2];
			degrees[0] = getVertexesNb(n);
			if (getNeighbourColors(n).size() > 0) {
				degrees[1] = getNeighbourColors(n).size();
				dSatMap.put(n, degrees);
			} else {
				degrees[1] = degrees[0];
				dSatMap.put(n, degrees);
			}
		}
	}
	
	/**
	 * Renvoie la liste des couleurs utilisées parmi les noeuds adjacents à n.
	 */
	private Set<Color> getNeighbourColors(PositionedNode n) {
		Set<Color> res = new HashSet<Color>();
		
		for (PositionedNode node : getAdjacents(n)) {
			if (node.getColor() != PositionedNode.COLOR_DEFAULT) {
				res.add(node.getColor());
			}
		}
		
		return res;
	}
	
	/**
	 * Renvoie le sommet de plus degré maximum.
	 */
	private PositionedNode getMaxDegreeNode(
			Map<PositionedNode, Integer[]> degreesMap) {
		int maxDegree = 0;
		PositionedNode maxDegreeNode = null;
		for (PositionedNode n : degreesMap.keySet()) {
			if (degreesMap.get(n)[0] >= maxDegree) {
				maxDegreeNode = n;
			}
		}
		return maxDegreeNode;
	}
	
	/**
	 * Renvoie le sommet non coloré de degré de saturation maximum, en cas de 
	 * conflit, renvoie celui de degré maximum.
	 */
	private PositionedNode getMaxSatDegreeNode(
			Map<PositionedNode, Integer[]> degreesMap) {
		int maxSatDegree = -1; //il peut y avoir 0 pour les neouds sans arcs
		PositionedNode maxSatDegreeNode = null;
		for (PositionedNode n : degreesMap.keySet()) {
			if (n.getColor() == PositionedNode.COLOR_DEFAULT) {
				if (degreesMap.get(n)[1] > maxSatDegree) {
					maxSatDegreeNode = n;
				} else if (degreesMap.get(n)[1] == maxSatDegree) {
					if (degreesMap.get(n)[0]
							> degreesMap.get(maxSatDegreeNode)[0]) {
						maxSatDegreeNode = n;
					}
				}
			}
		}
		return maxSatDegreeNode;
	}
}
