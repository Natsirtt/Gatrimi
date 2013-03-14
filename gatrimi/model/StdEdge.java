package gatrimi.model;

import java.awt.Color;
import java.util.Observable;

public class StdEdge extends Observable implements Edge {
	
	//ATTRIBUTS
	/**
	 * La couleur de l'arc.
	 */
	private Color color;
	
	/**
	 * Le tableau des deux PositionedNode en relation par cet arc.
	 */
	private PositionedNode[] nodes;
	
	//CONSTRUCTEURS
	
	public StdEdge(PositionedNode n1, PositionedNode n2) {
		if (n1 == null || n2 == null) {
			throw new IllegalArgumentException();
		}
		nodes = new PositionedNode[2];
		nodes[0] = n1;
		nodes[1] = n2;
		color = COLOR_DEFAULT;
	}
	
	//REQUÊTES
	
	public String toString() {
		return "[" + getNodes()[0].toString() 
				+ " - " + getNodes()[1].toString() + "]";
	}
	
	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public PositionedNode[] getNodes() {
		//Instanciation d'un nouveau tableau pour protéger la variable interne
		PositionedNode[] res = new PositionedNode[2];
		res[0] = nodes[0];
		res[1] = nodes[1];
		return res;
	}
	
	public boolean equals(Object o) {
		if ((o != null) && (o.getClass() == this.getClass())) {
			StdEdge v = (StdEdge) o;
			return (this.nodes[0].equals(v.nodes[0])
					&& this.nodes[1].equals(v.nodes[1]))
					|| (this.nodes[0].equals(v.nodes[1])
					&& this.nodes[1].equals(v.nodes[0]));
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return (nodes[0].hashCode() + nodes[1].hashCode())
						% Integer.MAX_VALUE;
	}
	
	@Override
	public boolean containsNode(PositionedNode n) {
		return nodes[0].equals(n) || nodes[1].equals(n);
	}

	//COMMANDES
	
	@Override
	public void setColor(Color c) {
		if (c == null || !isValidColor(c)) {
			throw new IllegalArgumentException();
		}
		color = c;
		setChanged();
	}

	//METHODES OUTILS
	
	/**
	 * Renvoie true si la couleur peut-être utilisée, false sinon.
	 */
	private boolean isValidColor(Color c) {
		if (c == null) {
			throw new IllegalArgumentException();
		}
		for (Color col : COLORS) {
			if (col.equals(c)) {
				return true;
			}
		}
		return false;
	}
}
