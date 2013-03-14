package gatrimi.model;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.HashSet;
import java.util.Set;

/**
 * Implémente un graphe par matrice d'adjacence.
 */
public class MatrixGraph extends AbstractGraph implements Graph {
	
	// Attributs.
	
	/*
	 * La matrice d'adjacence (lignes; colonnes);
	 */
	private int[][] matrix;
	
	private Set<PositionedNode> nodes;
	
	
	// Constructeurs.
	
	public MatrixGraph() {
		super();
		nodes = new HashSet<PositionedNode>();
		makeMatrix();
		setChanged();
	}
	
	public MatrixGraph(Graph g) {
		super(g);
		
		if (g == null) {
			throw new IllegalArgumentException();
		}
		
		nodes = new HashSet<PositionedNode>();
		for (PositionedNode n : g.getNodes()) {
			addNode(n);
		}
		makeMatrix();
		setSynchronized(g.isSynchronized());
		setChanged();
	}

	
	// Requêtes
	
	@Override
	public Set<PositionedNode> getNodes() {
		return new HashSet<PositionedNode>(nodes);
	}

	@Override
	public int getNodesNb() {
		return nodes.size();
	}

	@Override
	public boolean areAdjacent(PositionedNode n1, PositionedNode n2) {
		if (n1 == null || n2 == null
				|| !nodes.contains(n1) || !nodes.contains(n2)) {
			throw new IllegalArgumentException();
		}
		
		return matrix[n1.getNumber() - 1][n2.getNumber() - 1] == 1;
	}

	@Override
	public Set<PositionedNode> getAdjacents(PositionedNode n) {
		if (n == null || !nodes.contains(n)) {
			throw new IllegalArgumentException();
		}
		Set<PositionedNode> res = new HashSet<PositionedNode>();
		
		for (int j = 0; j < getNodesNb(); j++) {
			if (matrix[n.getNumber() - 1][j] == 1) {
				res.add(getNode(j + 1));
			}
		}
		
		return res;
	}

	
	// Commandes

	@Override
	public void addNode() {
		nodes.add(new StdPositionedNode(getNodesNb() + 1));
		makeMatrix();
		setSynchronized(false);
		setChanged();
		MemoryMXBean mb = ManagementFactory.getMemoryMXBean();
		System.out.println(mb.getHeapMemoryUsage() + " nodesNb = "
							+ getNodesNb());
	}
	
	@Override
	public void addNode(int x, int y) {
		nodes.add(new StdPositionedNode(x, y, getNodesNb() + 1));
		makeMatrix();
		setSynchronized(false);
		setChanged();
		MemoryMXBean mb = ManagementFactory.getMemoryMXBean();
		System.out.println(mb.getHeapMemoryUsage() + " nodesNb = "
							+ getNodesNb());
	}

	@Override
	public void removeNode(PositionedNode n) {
		if (n == null || !getNodes().contains(n)) {
			throw new IllegalArgumentException();
		}
		
		/*----------------VERSION 1------------------*/

		if (getVertexesNb(n) > 0) {
			removeVertexes(getVertexes(n));
		}
		nodes.remove(n);
		for (PositionedNode n2 : getNodes()) {
			if (n2.getNumber() > n.getNumber()) {
				n2.setNumber(n2.getNumber() - 1);
			}
		}
		makeMatrix();
		setSynchronized(false);
		setChanged();
		
		/*------------VERSION 2-------------*/

		
	}

	@Override
	public void removeNodes(Set<PositionedNode> nodes) {
		if (nodes == null || nodes.size() == 0) {
			throw new IllegalArgumentException();
		}
		for (PositionedNode n : nodes) {
			if (!this.nodes.contains(n)) {
				throw new IllegalArgumentException();
			}
		}
		for (PositionedNode n : nodes) {
			removeNode(n);
		}
		makeMatrix();
		setSynchronized(false);
		setChanged();
	}
	
	@Override
	public void addVertex(Edge v) {
		super.addVertex(v);
		makeMatrix();
		setSynchronized(false);
		setChanged();
		MemoryMXBean mb = ManagementFactory.getMemoryMXBean();
		System.out.println(mb.getHeapMemoryUsage());
	}
	
	@Override
	public void removeVertex(Edge v) {
		super.removeVertex(v);
		makeMatrix();
		setSynchronized(false);
		setChanged();
	}
	
	@Override
	public void removeVertexes(Set<Edge> vertexes) {
		super.removeVertexes(vertexes);
		makeMatrix();
		setSynchronized(false);
		setChanged();
	}

	@Override
	public void colorizeNode(PositionedNode n, Color c) {
		if (n == null || !getNodes().contains(n)
				|| c == null || !StdPositionedNode.isRightColor(c)) {
			throw new IllegalArgumentException();
		}
		
		for (PositionedNode n2 : nodes) {
			if (n.equals(n2)) {
				n2.setColor(c);
				return;
			}
		}
		setSynchronized(false);
		setChanged();
	}
	
	@Override
	public void load() throws IOException {
		if (getFile() == null || !getFile().exists()) {
			throw new IllegalArgumentException();
		}
		
		ObjectInputStream input;
		try {
			input = new ObjectInputStream(
						new BufferedInputStream(
								new FileInputStream(getFile())));
		} catch (FileNotFoundException e) {
			throw new InternalError("Fichier non trouvé"); //Impossible
		}
		
		Graph result;
		try {
			result = (Graph) input.readObject();
		} catch (ClassNotFoundException e) {
			throw new InternalError("Classe sérialisable non trouvée");
		} finally {
			input.close();
		}
		this.clear();
		for (PositionedNode n : result.getNodes()) {
			this.addNode(n);
		}
		for (Edge v : result.getVertexes()) {
			this.addVertex(v);
		}
		setSynchronized(true);
		setChanged();
	}
	
	//METHODES OUTILS
	
	private PositionedNode getNode(int num) {
		if (num <= 0 || num > getNodesNb()) {
			throw new IllegalArgumentException();
		}
		PositionedNode res = null;
		
		for (PositionedNode n : getNodes()) {
			if (n.getNumber() == num) {
				res = n;
				break;
			}
		}
		
		return res;
	}
	
	private void makeMatrix() {
		if (getNodes() == null || getVertexes() == null) {
			throw new IllegalStateException("makeMatrix : Set à null");
		}
		
		int mSize = getNodesNb();
		matrix = new int[mSize][mSize];
		
		for (int i = 0; i < mSize; i++) {
			for (int j = 0; j < mSize; j++) {
				matrix[i][j] = 0;
			}
		}
		
		for (Edge v : getVertexes()) {
			PositionedNode[] nodeTab = v.getNodes();
			int n0 = nodeTab[0].getNumber() - 1;
			int n1 = nodeTab[1].getNumber() - 1;
			matrix[n0][n1] = 1;
			matrix[n1][n0] = 1;
		}
	}
	
	private void addNode(PositionedNode n) {
		nodes.add(n);
		setSynchronized(false);
		setChanged();
	}
	
	private void clear() {
		nodes = new HashSet<PositionedNode>();
		matrix = new int[][] {};
		setVertexesSet(new HashSet<Edge>());
		setSynchronized(false);
		setChanged();
	}
}
