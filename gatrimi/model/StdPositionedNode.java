package gatrimi.model;

import java.awt.Color;
import java.util.Observable;

public class StdPositionedNode extends Observable
							   implements PositionedNode {
	
	//ATTRIBUTS
	
	/**
	 * L'abcisse du noeud.
	 */
	private int x;
	
	/**
	 * L'ordonnée du noeud.
	 */
	private int y;
	
	/**
	 * La couleur de ce sommet.
	 */
	private Color color;
	
	/**
	 * Le numéro de ce sommet.
	 */
	private int number;
	
	//CONSTRUCTEUR
	public StdPositionedNode(int x, int y, int number) {
		if (x < 0 || y < 0 || number < 0) {
			throw new IllegalArgumentException();
		}
		this.x = x;
		this.y = y;
		this.number = number;
		color = COLOR_DEFAULT;
		setChanged();
	}
	public StdPositionedNode(int number) {
		if (number < 0) {
			throw new IllegalArgumentException();
		}
		x = X_DEFAULT;
		y = Y_DEFAULT;
		this.number = number;
		color = COLOR_DEFAULT;
		setChanged();
	}
	
	//REQUETES
	
	public String toString() {
		return "n" + getNumber();
	}
	
	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public int getX() {
		return x;
	}

	@Override
	public int getY() {
		return y;
	}

	@Override
	public int getNumber() {
		return number;
	}
	
	//COMMANDES
	
	@Override
	public void setColor(Color c) {
		if (!isRightColor(c)) {
			throw new IllegalArgumentException();
		}
		color = c;
		setChanged();
	}

	@Override
	public void setPosition(int x, int y) {
		if (x < 0 || y < 0) {
			throw new IllegalArgumentException();
		}
		this.x = x;
		this.y = y;
		setChanged();
	}

	@Override
	public void setNumber(int n) {
		if (n < 0) {
			throw new IllegalArgumentException();
		}
		number = n;
		setChanged();
	}
	
	//OUTILS
	
	@Override
	public boolean equals(Object o) {
		if ((o != null) && (o.getClass() == this.getClass())) {
			StdPositionedNode obj = (StdPositionedNode) o;
			return this.number == obj.number;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.hashCode();
	}
	
	public int compareTo(PositionedNode node) {
		return this.getNumber() - node.getNumber();
	}
	
	static boolean isRightColor(Color c) {
		if (c == null) {
			return false;
		} else {
			for (Color c2 : COLORS) {
				if (c.equals(c2)) {
					return true;
				}
			}
		}
		return false;
	}
}
