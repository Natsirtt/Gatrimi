package gatrimi.model;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.Set;

/**
 * Spécification d'un graphe utilisant des sommets et des arcs coloriables.
 * Le graphe peut être colorié (sommets et arcs).
 * @inv
 * 		for Vertex v in getVertexes():
 * 			getNodes().contains(v.getNodes()[0])
 * 			getNodes().contains(v.getNodes()[1])
 * 		
 * 		getVertexNb(n) == m ==>
 * 			Soit Vertex[v1..vm] tq vi.getNode().contains(n), 1 <= i <= m
 * 			alors getVertexes().contains[v1..vm]
 * 
 * 		getNodesNb() == getNodes.size()
 * 		getNodesNb() >= 0
 * 		getVertexesNb() == getVertexes.size()
 * 		getVertexesNb() >= 0
 * 		getVertexesNb(PositionedNode n) == getVertexes(n).size()
 * 		getVertexesNb(PositionedNode n) >= 0
 * 		
 * 		n1.equals(n2) ==> n1 == n2
 * 
 * 		Soit n1, n2 des PositionedNode
 * 		vertex v.getNodes().contains(n1)
 * 			&& v.getNodes().contains(n2)
 * 			&& getVertexes().contains(v)
 * 		<==>   areAdjacent(n1, n2)
 * 
 * 		Soit v1, v2 des Vertex
 * 		areAdjacent(v1, v2)
 * 		==> v1.getNodes()[i] == v2.getNodes()[j]	i,j = 0 | 1
 * 
 * 		soit n1, n2 des PositionedNode
 * 		getAdjacents(n1).contains(n2) ==> areAdjacent(n1, n2)
 * 
 * 		soit v1, v2 des Vertex
 * 		getAdjacents(v1).contains(v2) ==> areAdjacent(v1, v2)
 * 		
 * 		forall v: getVertexes()
 * 			v.getNodes().contains(PositionedNode n)
 * 				==> getVertexes(PositionedNode n).contains(v)
 * 
 * @cons
 * 		$ARGS$
 * 		$PRE$
 * 		$POST$
 * 			getNodes().isEmpty()
 * 			getVertexes().isEmpty()
 * 
 * @cons
 * 		$ARGS$
 * 			Graph g
 * 		$PRE$
 * 			g != null
 * 		$POST$
 * 			getNodes().equals(g.getNodes())
 * 			getVertexes().equals(g.getVertexes())
 */
public interface Graph extends ObservableModel, Serializable {
	
	//REQUETES
	/**
	 * Retourne le fichier associé à ce graphe.
	 */
	File getFile();
	
	/**
	 * Le graphe est en synchronisation avec son fichier.
	 */
	boolean isSynchronized();
	
	/**
	 * Retourne l'ensemble des noeuds du graphe.
	 */
	Set<PositionedNode> getNodes();
	
	/**
	 * Retourne l'ensemble des arcs du graphe.
	 */
	Set<Edge> getVertexes();
	
	/**
	 * Retourne le nombre d'arc dont l'une des extrémités est le noeud n.
	 * 
	 * @pre
	 * 		n != null
	 * 		getNodes().contains(n)
	 */
	int getVertexesNb(PositionedNode n);
	
	/**
	 * Retourne le nombre d'arcs du graphes. 
	 */
	int getVertexesNb();
	
	/**
	 * Retourne le nombre de noeuds du graphe.
	 */
	int getNodesNb();
	
	/**
	 * Retourne l'ensemble des arcs dont l'une des extrémités est le noeud n.
	 * 
	 * @pre
	 * 		n != null
	 * 		getNodes().contains(n)
	 */
	Set<Edge> getVertexes(PositionedNode n);
	
	/**
	 * Retourne true si les deux noeuds sont adjacents pour ce graphe (c'est
	 * à dire s'ils sont liés par un arc).
	 * 
	 * @pre
	 * 		n1 != null
	 * 		n2 != null
	 * 		getNodes().contains(n1)
	 * 		getNodes().contains(n2)
	 */
	boolean areAdjacent(PositionedNode n1, PositionedNode n2);
	
	/**
	 * Retourne true si les deux arcs sont adjacents pour ce graphe (c'est à
	 * dire s'ils ont un noeud en commun).
	 * 
	 * @pre
	 * 		v1 != null
	 * 		v2 != null
	 * 		getVertexes.contains(v1)
	 * 		getVertexes.contains(v2)
	 */
	boolean areAdjacent(Edge v1, Edge v2);
	
	/**
	 * Retourne l'ensemble des noeuds adjacents au noeud passé en paramètre.
	 * 
	 * @pre
	 * 		n != null
	 * 		getNodes().contains(n)
	 */
	Set<PositionedNode> getAdjacents(PositionedNode n);
	
	/**
	 * Retourne l'ensemble des arcs adjacents à l'arc passé en paramètre.
	 * 
	 * @pre
	 * 		v != null
	 * 		getVertexes().contains(v)
	 */
	Set<Edge> getAdjacents(Edge v);
	
	//COMMANDES
	/**
	 * Fixe le fichier associé a ce graphe a file.	
	 * @post 
	 * 		!isSynchronized()
	 * 		getFile == file
	 */
	void setFile(File file);
	
	/**
	 * Créé un nouveau noeud et l'ajoute au graphe.
	 * 
	 * @post
	 * 		!isSynchronized()
	 * 		getNodesNb() == old getNodesNb() + 1
	 * 		Il existe n appartenant à getNodes() tel que 
	 * 			n.getNumber() == getNodesNb(),
	 * 			n.getX() == PositionedNode.X_DEFAULT,
	 * 			n.getY() == PositionedNode.Y_DEFAULT
	 */
	void addNode();
	
	/**
	 * Crée un nouveau noeud de coordonnées (x,y) et l'ajoute au graphe.
	 * 
	 * @post
	 * 		!isSynchronized()
	 * 		getNodesNb() == oldgetNodesNb() + 1
	 * 		Il existe n appartenant à getNodes() tel que 
	 * 			n.getNumber() == getNodesNb(),
	 * 			n.getX() == x,
	 * 			n.getY() == y
	 */
	void addNode(int x, int y);
	
	/**
	 * Supprime le noeud du graphe, ainsi que ses arcs.
	 * 
	 * @pre
	 * 		n != null
	 * 		getNodes().contains(n)
	 * @post
	 * 		!isSynchronized()
	 * 		getNodesNb() == old getNodesNb() - 1
	 * 		forall v : old getVertexes()
	 * 			v.getNodes().contains(n) ==> !getVertexes().contains(v)
	 * 		
	 * 		forall nd: old getNodes()
	 * 			!nd.equals(n) ==> getNodes().contains(nd)
	 * 		forall n : getNodes()
	 * 			n.getNumber() <= getNodesNb()
	 */
	void removeNode(PositionedNode n);
	
	/**
	 * Supprime un ensemble de noeuds du graphe ansi que leurs arcs.
	 * 
	 * @pre
	 * 		nodes != null
	 * 		nodes.size() > 0
	 * 		forall n: nodes
	 * 			getNodes().contains(n)
	 * @post
	 * 		!isSynchronized()
	 * 		getNodesNb() == old getNodesNb() - nodes.size()
	 * 		!getNodes().contains(nodes)
	 * 		forall v : old getVertexes()
	 * 			forall n : nodes
	 * 				v.getNodes().contains(n) ==>
	 * 					!getVertexes().contains(v)
	 *		forall nd : old getNodes()
	 *			!nodes.contains(nd) ==> getNodes().contains(nd)
	 */
	void removeNodes(Set<PositionedNode> nodes);
	
	/**
	 * Ajoute un arc déjà initialisé au graphe.
	 * @pre
	 * 		v != null
	 * 		getNodes().contains(v.getNodes())
	 * @post
	 * 		!isSynchronized()
	 * 		getVertexes().contains(v)
	 * 		getVertexesNb() == old getVertexesNb() + 1;
	 */
	void addVertex(Edge v);
	
	/**
	 * Supprime un arc du graphe.
	 * 
	 * @pre
	 * 		v != null
	 * 		getVertexes().contains(v)
	 * @post
	 * 		!isSynchronized()
	 * 		!getVertexes().contains(v)
	 * 		getVertexesNb() == old getVertexesNb() - 1
	 * 
	 * 		forall (v' : old getVertexes())
	 * 			!v'.equals(v) ==> getVertexes().contains(v')
	 */
	void removeVertex(Edge v);
	 
	/**
	 * Supprime un ensemble d'arcs du graphe.
	 * 
	 * @pre
	 * 		vertexes != null
	 * 		vertexes.size() > 0
	 * 		forall v : vertexes
	 * 			getVertexes().contains(v)
	 * @post
	 * 		!isSychronized()
	 * 		getVertexesNb() == old getVertexes().vertexes.sizes()
	 * 		!getVertexes().contains(vertexes)
	 * 
	 * 		forall(v' : old getVertexes())
	 * 			forall(v : vertexes)
	 * 				!v.contains(v') ==> getVertexes().contains(v')
	 */ 	
	void removeVertexes(Set<Edge> vertexes);
	
	/**
	 * Colorie le PositionedNode n avec la couleur c.
	 * 
	 * @pre
	 *		!isSYnchronized()
	 * 		n != null
	 * 		getNodes().contains(n)
	 * 		c != null
	 * 		PositionedNode.COLORS.contains(c)
	 */
	void colorizeNode(PositionedNode n, Color c);
	
	/**
	 * Colorie tous les arcs du graphes de manière à utiliser le moins de
	 * couleur possible tout en garantissant que deux arcs adjacents ont une
	 * couleur différente.
	 * @throws NotEnoughColorException 
	 * 
	 * @post
	 * 		!isSynchronized()
	 * 		areAdjacent(v1, v2) ==> !v1.getColor().equals(v2.getColor())
	 * 		forall(Vertex v : getVertexes()) :
	 * 			v.getColor() != null
	 */
	void colorizeVertexes() throws NotEnoughColorsException;
	
	/**
	 * Colorie tous les noeuds du graphes de manière à utiliser le moins de
	 * couleur possible tout en garantissant que deux noeuds adjacents ont une
	 * couleur différente.
	 * @throws NotEnoughColorException 
	 * 
	 *  @post
	 *  	isSynchronized()
	 *  	areAdjacent(Node n1, Node n2)
	 *  		==> !n1.getColor().equals(n2.getColor())
	 *  	forall(Node n : getNodes()) :
	 *  		n.getColor() != null
	 */
	void colorizeNodes() throws NotEnoughColorsException;
	
	/**
	 * Sauvegarde dans un fichier sur le disque dur le graphe.
	 * @throws IOException 
	 * @pre
	 * 		getFile != null
	 */
	void save() throws IOException;
	
	/**
	 * Charge depuis un fichier le graphe.
	 * @throws IOException 
	 * @pre
	 * 		getFile() != null
	 * 		getFile().exists()
	 */
	void load() throws IOException;
}
