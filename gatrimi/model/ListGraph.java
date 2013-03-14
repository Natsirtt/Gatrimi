package gatrimi.model;

import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class ListGraph extends AbstractGraph implements Graph {
	
	//ATTRIBUTS
	private Map<PositionedNode, Set<PositionedNode>> lists;
	
	//CONSTRUCTEURS
	
	public ListGraph() {
		super();
		lists = new HashMap<PositionedNode, Set<PositionedNode>>();
		setFile(null);
		setChanged();
	}
	
	public ListGraph(Graph g) {
		super(g);
		lists = new HashMap<PositionedNode, Set<PositionedNode>>();
		for (Edge v : getVertexes()) {
			PositionedNode[] n = v.getNodes();
			addNode(n[0]);
			addNode(n[1]);
		}
		for (PositionedNode n : g.getNodes()) {
			addNode(n);
		}
		setSynchronized(g.isSynchronized());
		setChanged();
	}
	
	//REQUETES
	
	@Override
	public Set<PositionedNode> getNodes() {
		Set<PositionedNode> set = new HashSet<PositionedNode>(lists.keySet());
		return set;
	}

	@Override
	public int getNodesNb() {
		return lists.keySet().size();
	}

	@Override
	public boolean areAdjacent(PositionedNode n1, PositionedNode n2) {
		if (n1 == null || n2 == null 
				|| !getNodes().contains(n1) || !getNodes().contains(n2)) {
			throw new IllegalArgumentException();
		}
		Set<PositionedNode> list = lists.get(n1);
		return list.contains(n2);
	}

	@Override
	public Set<PositionedNode> getAdjacents(PositionedNode n) {
		if (n == null || !getNodes().contains(n)) {
			throw new IllegalArgumentException();
		}
		Set<PositionedNode> list = lists.get(n);
		return new HashSet<PositionedNode>(list);
	}
	//COMMANDES
	
	@Override
	public void addNode() {
		PositionedNode n = new StdPositionedNode(getNodesNb() + 1);
		lists.put(n, new HashSet<PositionedNode>());
		setSynchronized(false);
		setChanged();
		MemoryMXBean mb = ManagementFactory.getMemoryMXBean();
		System.out.println(mb.getHeapMemoryUsage() + " nodesNb = "
							+ getNodesNb());
	}
	
	@Override
	public void addNode(int x, int y) {
		PositionedNode n = new StdPositionedNode(x, y, getNodesNb() + 1);
		lists.put(n, new HashSet<PositionedNode>());
		setSynchronized(false);
		setChanged();
	}

	@Override
	public void removeNode(PositionedNode n) {
		if (n == null || !getNodes().contains(n)) {
			throw new IllegalArgumentException();
		}
		
		/*----------------VERSION 1------------------*/
		
		//on retire les arcs liés a n
		removeVertexes(getVertexes(n));
			
		//on retire n de l'ensemble des noeuds		
		lists.keySet().remove(n);
		
		//On reconstruit la map en changeant le numero des noeuds
		Map<PositionedNode, Set<PositionedNode>> newLists = 
				new HashMap<PositionedNode, Set<PositionedNode>>();
		for (PositionedNode n2 : lists.keySet()) {
			if (n2.getNumber() > n.getNumber()) {
				n2.setNumber(n2.getNumber() - 1);
			}
			newLists.put(n2, new HashSet<PositionedNode>());
		}
		for (Edge v : getVertexes()) {
			PositionedNode[] nTab = v.getNodes();
			newLists.get(nTab[0]).add(nTab[1]);
			newLists.get(nTab[1]).add(nTab[0]);
		}
		lists = newLists;	
		setChanged();
	}

	@Override
	public void removeNodes(Set<PositionedNode> nodes) {
		if (nodes == null) {
			throw new IllegalArgumentException();
		}
		for (PositionedNode n : nodes) {
			if (n == null || !getNodes().contains(n)) {
				throw new IllegalArgumentException();
			}
		}
		for (PositionedNode n : nodes) {
			removeNode(n);
		}
		setSynchronized(false);
		setChanged();
	}
	
	@Override
	public void colorizeNode(PositionedNode n, Color c) {
		if (n == null || !getNodes().contains(n)
				|| c == null || !StdPositionedNode.isRightColor(c)) {
			throw new IllegalArgumentException();
		}
		
		for (PositionedNode n2 : lists.keySet()) {
			if (n2.equals(n)) {
				n2.setColor(c);
				return;
			}
		}
		setSynchronized(false);
		setChanged();
	}
	
	public void addVertex(Edge v) {
		super.addVertex(v);
		PositionedNode[] nTab = v.getNodes();
		lists.get(nTab[0]).add(nTab[1]);
		lists.get(nTab[1]).add(nTab[0]);
		setSynchronized(false);
		setChanged();
	}
	
	public void removeVertex(Edge v) {
		super.removeVertex(v);
		PositionedNode[] nTab = v.getNodes();
		lists.get(nTab[0]).remove(nTab[1]);
		lists.get(nTab[1]).remove(nTab[0]);
		setSynchronized(false);
		setChanged();
	}
	
	public void removeVertexes(Set<Edge> v) {
		for (Edge vert : v) {
			removeVertex(vert);
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
	
	//OUTILS
	/*
	 * Méthode utilisée uniquement dans le cas de l'appel au constructeur à
	 * un paramètre.
	 * Dans ce cas, on assure que en sortie du constructeur l'invariant est tout
	 * de même respecté.
	 */
	private void addNode(PositionedNode n) {
		lists.put(n, new HashSet<PositionedNode>());
		setSynchronized(false);
		setChanged();
	}
	
	private void clear() {
		lists = new HashMap<PositionedNode, Set<PositionedNode>>();
		setVertexesSet(new HashSet<Edge>());
		setSynchronized(false);
		setChanged();
	}
}
