package gatrimi.model;

import java.awt.Color;
import java.io.Serializable;

/**
 * Classe spécifiant un sommet de graphe repéré par une position dans le plan.
 * Toute classe implémentant PositionedNode doit aussi implémenter Cloneable et
 * redéfinir la méthode clone() afin de pouvoir cloner un PositionedNode.
 * 
 * @inv
 * 		getX() >= 0
 * 		getY() >= 0
 * 		getNumber() > 0
 * 		COLORS.contains(getColor())
 * 		n1.equals(n2) <==> n1.getNumber() == n2.getNumber()
 * 
 * @cons
 * $ARGS$
 * 		int x
 * 		int y
 * 		int number
 * $PRE$
 * 		x >= 0
 * 		y >= 0
 * 		number >= 0
 * $POST$
 * 		getX() == x
 * 		getY() == y
 * 		getNumber == number
 * 		getColor.equals(COLOR_DEFAULT)
 * 
 * @cons
 * $ARGS$
 * 		int number
 * $PRE$
 * 		num >= 0
 * $POST$
 * 		getNumber == number
 * 		getX() == X_DEFAULT
 * 		getY() == Y_DEFAULT
 * 		getColor().equals(COLOR_DEFAULT)
 */
public interface PositionedNode extends ObservableModel,
										Comparable<PositionedNode>,
										Serializable {
	
	//ATTRIBUTS STATIQUES
	
	/**
	 * La position par defaut de l'abcisse.
	 */
	int X_DEFAULT = 0;
	
	/**
	 * La position par defaut de l'ordonée.
	 */
	int Y_DEFAULT = 0;
	
	/**
	 * Le tableau de toutes les couleurs pouvant être prisent par un sommet.
	 */
    Color[] COLORS = {
        Color.BLACK, Color.BLUE, Color.ORANGE, Color.DARK_GRAY,
        Color.GREEN, Color.LIGHT_GRAY, Color.MAGENTA, Color.CYAN,
        Color.PINK, Color.RED, Color.WHITE, Color.YELLOW
    };
    
	/**
	 * La couleur par defaut du sommet.
	 */
	Color COLOR_DEFAULT = COLORS[0];
	
	/**
	 * La première couleur qui n'est pas celle par défaut.
	 */
	int INDEX_COLOR_INIT = 1;
	
	//REQUETES
	
	/**
	 * Retourne la couleur du sommet.
	 */
	Color getColor();
	
	/**
	 * Retourne l'abscisse du sommet.
	 */
	int getX();
	
	/**
	 * Retourne l'ordonée du sommet.
	 */
	int getY();
	
	/**
	 * Retourne le numero du sommet.
	 */
	int getNumber();
	
	//COMMANDES
	
	/**
	 * Donne la couleur c au sommet.
	 * 
	 * @pre
	 * 		c != null
	 * 		COLORS.contains(c)
	 * @post
	 * 		getColor().equals(c)
	 */
	void setColor(Color c);
	
	/**
	 * Donne la position x,y au sommet (x pour abscisse et y pour ordonée).
	 * 
	 * @pre
	 * 		x >= 0
	 * 		y >= 0
	 * 
	 * @post
	 * 		getX() == x
	 * 		getY() == y
	 */
	void setPosition(int x, int y);
	
	/**
	 * Donne le numero n au sommet.
	 * 
	 * @pre
	 * 		n >= 0
	 * 
	 * @post
	 * 		getNumber() == n
	 */
	void setNumber(int n);	
}
