package gatrimi.model;

import java.awt.Color;
import java.io.Serializable;

/**
 * Classe spécifiant un arc coloré entre deux PositionedNode.
 *
 * @inv
 * 		COLORS.contains(getColor())
 * 		getNodes.length == 2
 * 		getNodes()[0].equals(getNodes()[1])
 * 
 * @cons
 * $ARGS$
 * 		PositionedNode n1
 * 		PositionedNode n2
 * $PRE$
 * 		n1 != null
 * 		n2 != null
 * 
 * $POST$
 * 		getNodes().contains(n1)
 * 		getNodes().contains(n2)
 * 		getColor.equals(COLOR_DEFAULT)
 */
public interface Edge extends Serializable, ObservableModel {
	//ATTRIBUTS STATIQUES
	
	/**
	 * Le tableau de toutes les couleurs pouvant être prisent par un arc.
	 */
	Color[] COLORS = {
	        Color.BLACK, Color.BLUE, Color.CYAN, Color.DARK_GRAY, Color.GRAY,
	        Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.ORANGE,
	        Color.PINK, Color.RED, Color.WHITE, Color.YELLOW
	    };
	
	/**
	 * La couleur par défaut prise par un arc.
	 */
	Color COLOR_DEFAULT = COLORS[0];
	
	//REQUETES
	
	/**
	 * Retourne la couleur actuelle de l'arc.
	 */
	Color getColor();
	
	/**
	 * Retourne le tableau des PositionedNodes en relation par cet arc.
	 */
	PositionedNode[] getNodes();
	
	/**
	 * Retourne vrai si le noeud est l'un des noeuds du vertex.
	 */
	boolean containsNode(PositionedNode n);
	
	//COMMANDES
	
	/**
	 * Donne la couleur c à l'arc.
	 * 
	 * @pre
	 * 		c != null
	 * 		COLORS.contains(c)
	 * @post
	 * 		getColor.equals(c)
	 */
	void setColor(Color c);
}
