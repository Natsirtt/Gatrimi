package gatrimi;

import gatrimi.graphics.GraphicGraph;
import gatrimi.graphics.GraphicPositionedNode;
import gatrimi.model.Graph;
import gatrimi.model.ListGraph;
import gatrimi.model.MatrixGraph;
import gatrimi.model.NotEnoughColorsException;
import gatrimi.model.PositionedNode;
import gatrimi.model.StdPositionedNode;
import gatrimi.model.StdEdge;
import gatrimi.model.Edge;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

public class Gatrimi {

	enum State {
		NOMOD, MOD_NOF, MOD_FIL;
	}

	//ATTRIBUTS

	/**
	 * Le modèle de l'application.
	 */
	private Graph model;

	/**
	 * La fenêtre principale de l'application.
	 */
	private JFrame frame;

	/**
	 * Le bouton de menu new.
	 */
	private JMenuItem newMenuItem;

	/**
	 * Le bouton de menu open.
	 */
	private JMenuItem openMenuItem;

	/**
	 * Le bouton de menu save.
	 */
	private JMenuItem saveMenuItem;

	/**
	 * Le bouton de menu save as.
	 */
	private JMenuItem saveAsMenuItem;

	/**
	 * Le bouton de menu reload.
	 */
	private JMenuItem reloadMenuItem;

	/**
	 * Le bouton de menu close.
	 */
	private JMenuItem closeMenuItem;

	/**
	 * Le bouton de menu quit.
	 */
	private JMenuItem quitMenuItem;

	/**
	 * Le bouton de menu undo.
	 */
	private JMenuItem undoMenuItem;

	/**
	 * Le bouton de menu redo.
	 */
	private JMenuItem redoMenuItem;

	/**
	 * Le bouton de menu clear.
	 */
	private JMenuItem clearMenuItem;

	/**
	 * Le bouton de menu use list representation.
	 */
	private JMenuItem useListRepresentationMenuItem;

	/**
	 * Le bouton de menu useMatrixRepresentation.
	 */
	private JMenuItem useMatrixRepresentationMenuItem;

	/**
	 * Le bouton de menu exportPNG.
	 */
	private JMenuItem exportPNGMenuItem;

	/**
	 * Le bouton de menu random Graph.
	 */
	private JMenuItem randomGraphMenuItem;

	/**
	 * Le bouton de menu colorize nodes.
	 */
	private JMenuItem colorizeNodesMenuItem;

	/**
	 * Le bouton de menu colorize vertexes.
	 */
	private JMenuItem colorizeVertexesMenuItem;

	/**
	 * Le bouton de menu contents.
	 */
	private JMenuItem contentsMenuItem;

	/**
	 * Le bouton de menu about.
	 */
	private JMenuItem aboutMenuItem;

	/**
	 * Le text field du nombre de noeuds à ajouter.
	 */
	private JTextField nodesNbToAddTextField;

	/**
	 * Le bouton ajoutant un certain nombre de noeuds au graphe.
	 */
	private JButton addNodesButton;

	/**
	 * Le bouton permettant de relier tous les noeuds du graphe.
	 */
	private JButton linkAll;

	/**
	 * Le bouton permettant de supprimer toutes les arêtes du graphe.
	 */
	private JButton unlinkAll;

	/**
	 * Le bouton lançant le coloriage des noeuds.
	 */
	private JButton colorizeNodesButton;

	/**
	 * Le bouton lançant le coloriage des arcs.
	 */
	private JButton colorizeVertexesButton;

	/**
	 * Le label décrivant l'état du fichier de sauvegarde.
	 */
	private JLabel fileStatusJLabel;

	/**
	 * Un label de notifications diverses.
	 */
	private JLabel notificationsLabel;

	/**
	 * Le composant graphique représentant le graphe.
	 */
	private GraphicGraph graphicGraph;

	/**
	 * L'observateur du modèle mettant à jour la vue de la barre de menu.
	 */
	private Observer menuBarObserver;

	/**
	 * L'observateur du modèle mettant à jour la vue des boutons de
	 * l'application.
	 */
	private Observer toolBarObserver;

	/**
	 * L'observateur du modèle mettant à jour les informations affichées par
	 * l'application.
	 */
	private Observer stateBarObserver;

	/**
	 * L'espace (en pixel) entre deux noeuds lors de la génération aléatoire.
	 */
	private static final int RANDOM_GRAPH_NODE_SPACE = 50;
	
	
	// CONSTRUCTEURS

	public Gatrimi() {
		createModel();
		createView();
		placeComponents();
		createController();
	}


	// COMMANDE

	public void display() {
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setVisible(true);
		model.notifyObservers();
	}


	// OUTILS

	private void createModel() {
		model = new ListGraph();
	}

	private void createView() {
		frame = new JFrame("Gatrimi - Les graphes pour tous");

		newMenuItem = new JMenuItem("New");
		openMenuItem = new JMenuItem("Open...");
		saveMenuItem = new JMenuItem("Save");
		saveAsMenuItem = new JMenuItem("Save as...");
		reloadMenuItem = new JMenuItem("Reload");
		closeMenuItem = new JMenuItem("Close");
		quitMenuItem = new JMenuItem("Quit");

		undoMenuItem = new JMenuItem("Undo");
		redoMenuItem = new JMenuItem("Redo");
		clearMenuItem = new JMenuItem("Clear");
		useListRepresentationMenuItem =
				new JMenuItem("Use list representation");
		useMatrixRepresentationMenuItem =
				new JMenuItem("Use matrix representation");

		exportPNGMenuItem = new JMenuItem("Export PNG...");
		randomGraphMenuItem = new JMenuItem("Random graph");
		colorizeNodesMenuItem = new JMenuItem("Colorize nodes");
		colorizeVertexesMenuItem = new JMenuItem("Colorize edges");

		contentsMenuItem = new JMenuItem("Contents");
		aboutMenuItem = new JMenuItem("About");

		nodesNbToAddTextField = new JTextField(2);

		addNodesButton = new JButton("Add");
		linkAll = new JButton("Link All");
		unlinkAll = new JButton("Unlink All");
		colorizeNodesButton = new JButton("Colorize nodes");
		colorizeVertexesButton = new JButton("Colorize edges");

		fileStatusJLabel = new JLabel("File : <none>");
		notificationsLabel = new JLabel("Adjency list representation");

		graphicGraph = new GraphicGraph(model);
	}

	private void placeComponents() {
		//Le menu
		JMenuBar menuBar = new JMenuBar(); {
			JMenu menu = new JMenu("File"); {
				menu.add(newMenuItem);
				menu.add(openMenuItem);
				menu.add(new JSeparator());
				menu.add(saveMenuItem);
				menu.add(saveAsMenuItem);
				menu.add(reloadMenuItem);
				menu.add(new JSeparator());
				menu.add(closeMenuItem);
				menu.add(new JSeparator());
				menu.add(quitMenuItem);
			}
			menuBar.add(menu);

			menu = new JMenu("Edit"); {
				menu.add(undoMenuItem);
				menu.add(redoMenuItem);
				menu.add(new JSeparator());
				menu.add(clearMenuItem);
				menu.add(new JSeparator());

				JMenu pref = new JMenu("Preferences"); {
					pref.add(useListRepresentationMenuItem);
					pref.add(useMatrixRepresentationMenuItem);
				}

				menu.add(pref);
			}
			menuBar.add(menu);

			menu = new JMenu("Tools"); {
				menu.add(exportPNGMenuItem);

				menu.add(new JSeparator());
				menu.add(randomGraphMenuItem);
				menu.add(new JSeparator());

				JMenu colorize = new JMenu("Colorize"); {
					colorize.add(colorizeNodesMenuItem);
					colorize.add(colorizeVertexesMenuItem);
				}
				menu.add(colorize);
			}
			menuBar.add(menu);

			menu = new JMenu("Help"); {
				menu.add(contentsMenuItem);
				menu.add(new JSeparator());
				menu.add(aboutMenuItem);
			}
			menuBar.add(menu);
		}
		frame.setJMenuBar(menuBar);

		//Les JComponents
		JPanel p = new JPanel(new BorderLayout()); {		 
			JPanel q = new JPanel(new GridLayout(0, 2)); {
				JPanel r = new JPanel(new FlowLayout(FlowLayout.RIGHT)); {
					r.add(new JLabel("Nb : "));
					r.add(nodesNbToAddTextField);
				}
				q.add(r);

				r = new JPanel(new FlowLayout(FlowLayout.LEFT)); {
					r.add(addNodesButton);
				}
				q.add(r);

				q.setBorder(BorderFactory.createTitledBorder("Add node"));
			}
			p.add(q, BorderLayout.NORTH);

			q = new JPanel(new GridLayout(2, 0)); {
				JPanel r = new JPanel(new GridLayout(2, 0)); {
					r.add(linkAll);
					r.add(unlinkAll);
				}
				q.add(r);
				r.setBorder(BorderFactory.createTitledBorder("Link Nodes"));

				r = new JPanel(new GridLayout(2, 0)); {
					r.add(colorizeNodesButton);
					r.add(colorizeVertexesButton);	
				}
				q.add(r);

				r.setBorder(BorderFactory.createTitledBorder("Colorize"));
			}
			p.add(q, BorderLayout.SOUTH);

			p.setBorder(BorderFactory.createEtchedBorder());
		}
		frame.add(p, BorderLayout.WEST);

		p = new JPanel(new GridLayout(0, 2)); {
			JPanel q = new JPanel(new FlowLayout(FlowLayout.LEFT)); {
				q.add(fileStatusJLabel);
				q.setBorder(BorderFactory.createLoweredBevelBorder());
			}
			p.add(q);

			q = new JPanel(new FlowLayout(FlowLayout.LEFT)); {
				q.add(notificationsLabel);
				q.setBorder(BorderFactory.createLoweredBevelBorder());
			}
			p.add(q);
		}
		frame.add(p, BorderLayout.SOUTH);

		frame.add(graphicGraph, BorderLayout.CENTER);
	}

	private void createController() {
		createObservers();
		createWindowListeners();
		createActionListeners();
	}

	private void createObservers() {
		// Gestion des options disponibles dans la barre de menu
		menuBarObserver =  new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				// Options 'Save' et 'Reload'
				if (getState() == State.MOD_FIL) {
					saveMenuItem.setEnabled(true);
					reloadMenuItem.setEnabled(true);
				} else {
					saveMenuItem.setEnabled(false);
					reloadMenuItem.setEnabled(false);
				}

				// Options 'Save As...' et 'Close'
				if (arg != null && !(Boolean) arg) { //getState() == State.NOMOD
					saveAsMenuItem.setEnabled(false);
					closeMenuItem.setEnabled(false);
				} else {
					saveAsMenuItem.setEnabled(true);
					closeMenuItem.setEnabled(true);
				}

				//TODO Options 'Undo' et 'Redo', implanter undo et redo
				undoMenuItem.setEnabled(false);
				redoMenuItem.setEnabled(false);

				// Options 'Clear', 'Colorize ...' et 'Export'
				if ((arg != null && !(Boolean) arg) //getState() == State.NOMOD 
						|| model.getNodesNb() == 0) {
					clearMenuItem.setEnabled(false);
					linkAll.setEnabled(false);
					unlinkAll.setEnabled(false);
					colorizeNodesMenuItem.setEnabled(false);
					colorizeVertexesMenuItem.setEnabled(false);
					exportPNGMenuItem.setEnabled(false);
				} else {
					clearMenuItem.setEnabled(true);
					linkAll.setEnabled(true);
					colorizeNodesMenuItem.setEnabled(true);
					if (model.getVertexesNb() == 0) {
						unlinkAll.setEnabled(false);
						colorizeVertexesMenuItem.setEnabled(false);
					} else {
						unlinkAll.setEnabled(true);
						colorizeVertexesMenuItem.setEnabled(true);
					}
					exportPNGMenuItem.setEnabled(true);
				}
			}
		};

		// Gestion de la barre d'outils
		toolBarObserver =  new Observer() {
			@Override
			public void update(Observable o, Object arg) {
				nodesNbToAddTextField.setText("");

				if (arg != null && !(Boolean) arg) { //getState() == State.NOMOD
					addNodesButton.setEnabled(false);
					nodesNbToAddTextField.setEditable(false);
					colorizeNodesButton.setEnabled(false);
					colorizeVertexesButton.setEnabled(false);
				} else {
					addNodesButton.setEnabled(true);
					nodesNbToAddTextField.setEditable(true);
					if (model.getNodesNb() > 0) {
						colorizeNodesButton.setEnabled(true);
					} else {
						colorizeNodesButton.setEnabled(false);
					}
					if (model.getVertexesNb() > 0) {
						colorizeVertexesButton.setEnabled(true);
					} else {
						colorizeVertexesButton.setEnabled(false);
					}
				}
			}
		};

		// Gestion de la barre d'état
		stateBarObserver = new Observer() {
			@Override
			public void update(Observable arg0, Object arg1) {
				String state = "File : ";
				if (!noModelOrNoFileOrSynchronized()) {
					state += "* ";
				}
				if (model.getFile() == null) {
					state += "<none>";
				} else {
					state += model.getFile().getAbsolutePath();
				}
				fileStatusJLabel.setText(state);
			}
		};

		addModelObservers();
	}

	private void createWindowListeners() {
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

		frame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if (operationConfirmed()) {
					System.exit(0);
				}	
			}
		});
	}

	private void createActionListeners() {
		// Actions des boutons de la toolbar
		addNodesButton.addActionListener(new ActionListener() {			
			@Override
			public void actionPerformed(ActionEvent e) {
				Integer nodeNbToAdd = 
						putInteger(nodesNbToAddTextField.getText());

				if (nodeNbToAdd == null) {
					showError("Le formulaire doit être "
							+ "rempli avec un nombre entier");
				} else if (nodeNbToAdd <= 0) {
					showError("On ne peut ajouter " 
							+ "qu'un nombre positif de noeuds");
				} else {
					int j = 0;
					int k = 0;
					for (int i = 0; i < nodeNbToAdd; i++) {
						if (PositionedNode.X_DEFAULT 
								+ j * GraphicGraph.SPACE_DEFAULT 
								+ GraphicPositionedNode.DIAMETER
								                    > graphicGraph.getWidth()) {
							j = 0;
							k++;
						}
						model.addNode(
								PositionedNode.X_DEFAULT
									+ j * GraphicGraph.SPACE_DEFAULT,
								PositionedNode.Y_DEFAULT
									+ k * GraphicPositionedNode.DIAMETER);
						j++;
					}
					model.notifyObservers();
				}				
			}
		});

		linkAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				for (PositionedNode n1 : model.getNodes()) {
					for (PositionedNode n2 : model.getNodes()) {
						Edge v = new StdEdge(n1, n2);
						if (!model.getVertexes().contains(v) 
								&& !n1.equals(n2)) {
							model.addVertex(v);
						}
					}
				}
				model.notifyObservers();
			}
		});

		unlinkAll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				model.removeVertexes(model.getVertexes());
				model.notifyObservers();
			}
		});

		//Tous les boutons de coloration
		ActionListener colorizeNodesAction = new ActionListener() {		
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					model.colorizeNodes();
				} catch (NotEnoughColorsException ex) {
					showError("Le graphe est trop grand pour pouvoir être "
							+ "colorié avec le nombre de couleurs "
							+ "dont dispose actuellement l'application");
					for (PositionedNode n : model.getNodes()) {
						model.colorizeNode(n, StdPositionedNode.COLOR_DEFAULT);
					}
				}				
				model.notifyObservers();
			}
		};
		colorizeNodesButton.addActionListener(colorizeNodesAction);
		colorizeNodesMenuItem.addActionListener(colorizeNodesAction);

		ActionListener colorizeVertexesAction = new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					model.colorizeVertexes();
				} catch (NotEnoughColorsException ex) {
					showError("Le graphe est trop grand pour pouvoir être "
							+ "colorié avec le nombre de couleurs "
							+ "dont dispose actuellement l'application");
				}
				model.notifyObservers();
			}
		};
		colorizeVertexesButton.addActionListener(colorizeVertexesAction);
		colorizeVertexesMenuItem.addActionListener(colorizeVertexesAction);

		// Actions des éléments du menu 'File'
		newMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				if (operationConfirmed()) {
					if (getState() != State.NOMOD) {
						model.deleteObservers();
					} else {
						frame.add(graphicGraph, BorderLayout.CENTER);
					}
					model = null;
					if (notificationsLabel.getText().equals("Adjency list "
							+ "representation")) {
						useListRepresentation();
					} else {
						useMatrixRepresentation();
					}
					model.notifyObservers();
				}
				repack();
			}
		});

		openMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				open();
			}
		});

		saveMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (model.getFile() == null) {
					saveAsWithHandledExceptions();
				} else {
					saveWithHandledExceptions();
				}
			}
		});

		saveAsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAsWithHandledExceptions();
			}
		});

		reloadMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (operationConfirmed()) {
					model.removeNodes(model.getNodes());
					try {
						model.load();
					} catch (IOException ioe) {
						showError("Une erreur s'est produite lors du "
								+ "chargement du fichier");
					} finally {
						model.notifyObservers();
					}
				}
			}
		});

		closeMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (operationConfirmed()) {
					model.removeNodes(model.getNodes());
					model.notifyObservers();
					graphicGraph.repaint();
					frame.remove(graphicGraph);
					model.setFile(null);
					//le booléen a false signifie qu'on va enlever le model
					model.notifyObservers(new Boolean(false));
					model.deleteObservers();
					model = null;
					frame.repaint();
				}
			}
		});

		quitMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (operationConfirmed()) {
					System.exit(0);
				}
			}
		});

		// Actions des élément du menu 'Edit'
		undoMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO implanter undo
			}
		});

		redoMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//TODO implanter redo
			}
		});

		clearMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				model.removeNodes(model.getNodes());
				model.notifyObservers();
			}
		});

		useListRepresentationMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				useListRepresentation();
			}
		});

		useMatrixRepresentationMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				useMatrixRepresentation();
			}
		});

		randomGraphMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (operationConfirmed()) {
					String input = JOptionPane.showInputDialog(
							frame, 
							"Entrez le nombre de noeud du graphe", 
							"Séléction", 
							JOptionPane.QUESTION_MESSAGE
							);
					if (input != null) {
						try {
							int nb = Integer.parseInt(input);
							if (nb > 1) {
								if (getState() != State.NOMOD) {
									model.deleteObservers();
								} else {
									frame.add(
											graphicGraph, 
											BorderLayout.CENTER
											);
								}
								model = null;					
								model = randomGraph(nb);
								frame.remove(graphicGraph);
								graphicGraph = new GraphicGraph(model);
								addModelObservers();	
								frame.add(graphicGraph, BorderLayout.CENTER);
							} else {
								showError("La valeur doit être > 1");
							}
						} catch (NumberFormatException ex) {
							showError("La valeur doit être un entier.");
						} finally {
							if (getState() != State.NOMOD) { 
								model.notifyObservers();
							}
							repack();
						}
					}
				}
			}
		});

		// Actions des éléments du menu 'Help'
		contentsMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(frame, 
						"Pour de plus amples informations sur le fonctionnement"
								+ " de cette application, veuillez consulter le"
								+ " rapport fourni en annexe.",
								"Help",
								JOptionPane.INFORMATION_MESSAGE);
			}
		});

		aboutMenuItem.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(
						frame,
						"Une application pour le dessin et la colorisation "
						+ "de graphe.\n"
						+ "Basé sur un sujet de projet par Mme. Selmi.\n\n"
						+ "Par messieurs Smondack, Louet et Ferry.", 
						"GaTriMi - version alpha",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		exportPNGMenuItem.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				Dimension oldDimention = graphicGraph.getSize();
				int translateX = -1 * (int) (graphicGraph.minXandMinY()
						.getWidth()
						- GraphicPositionedNode.DIAMETER);
				int translateY = -1 * (int) (graphicGraph.minXandMinY()
						.getHeight()
						- GraphicPositionedNode.DIAMETER);

				graphicGraph.translate(translateX, translateY);

				graphicGraph.setSize(graphicGraph.idealDrawDimension());
				BufferedImage bi = new BufferedImage(
						graphicGraph.getWidth(), graphicGraph.getHeight(), 
						BufferedImage.TYPE_INT_ARGB
						);

				Graphics g = bi.createGraphics();
				g.setColor(Color.WHITE);
				g.fillRect(0, 0, graphicGraph.getWidth(),
						graphicGraph.getHeight());
				graphicGraph.paint(g);
				g.dispose();

				graphicGraph.translate(-1 * translateX, -1 * translateY);
				graphicGraph.setSize(oldDimention);

				JFileChooser fc = new JFileChooser();
				if (fc.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();

					String[] path = file.getAbsolutePath().split("\\" + ".");
					if (!path[path.length - 1].endsWith("png")) {
						file = new File(file.getAbsolutePath() + ".png");
					}

					if (!file.exists()) {
						try {
							file.createNewFile();
						} catch (IOException e) {
							showError("Impossible de créer le fichier"
									+ "Vérifiez si vous disposez des droits"
									+ " d'écriture pour cet emplacement");
						}
					}
					try {
						ImageIO.write(bi, "png", file);
					} catch (IOException e) { 
						showError("Erreur d'entrée / sortie du fichier"
								+ "\nLe fichier est probablement corrompu");
					}
				}
			}
		});
	}

	private void addModelObservers() {
		model.addObserver(menuBarObserver);
		model.addObserver(toolBarObserver);
		model.addObserver(stateBarObserver);
	}

	private State getState() {
		if (model == null) {
			return State.NOMOD;
		} else if (model.getFile() == null) {
			return State.MOD_NOF;
		} else {
			return State.MOD_FIL;
		}
	}

	private Integer putInteger(String text) {
		Integer res;

		try {
			res = Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return null;
		}
		return res;
	}

	/**
	 * Retourne vrai si le model est null ou si model.isSynchronized() == true.
	 * Ainsi l'info de synchronisation n'est accessible que lorsqu'il y a un
	 * modèle.
	 */
	private boolean noModelOrNoFileOrSynchronized() {
		return (model == null || model.getFile() == null
				|| model.isSynchronized());
	}

	/**
	 * Observe l'état du modèle et demande confirmation si le modèle n'est pas
	 * synchronisé.
	 */
	private boolean operationConfirmed() {
		String message = "Des données n'ont pas été sauvegardées."
				+ "Souhaitez-vous continuer sans sauvegarder ?";

		if (getState() == State.MOD_FIL && !model.isSynchronized()) {
			int answer = JOptionPane.showConfirmDialog(null, message,
					"Attention!", JOptionPane.YES_NO_OPTION); 

			return answer == JOptionPane.YES_OPTION;
		}
		return true;
	}

	/**
	 * Affiche une boite de dialogue informant d'une erreur. La boite de
	 * dialogue affiche le message "message".
	 */
	private void showError(String message) {
		JOptionPane.showMessageDialog(null, message, "Erreur !",
				JOptionPane.ERROR_MESSAGE);
	}

	/**
	 * Réalise une opération de sauvegarde en s'assurant de capturer les
	 * exceptions. Si aucun fichier n'avait était choisi préalablement,
	 * cette action réalise en vériter celle de "Save as".
	 */
	private void saveWithHandledExceptions() {
		try {
			model.save();
		} catch (IOException ioe) {
			showError("Une erreur s'est produite lors de la sauvegarde "
					+ "du fichier\nCelui-ci est probablement corrompu");
		} finally {
			model.notifyObservers();
		}
	}

	/**
	 * Réalise une opération de sauvegarde en demandant préalablement quel
	 * fichier utiliser. Si les choses tournent mal, on remet l'ancien fichier
	 * en place.
	 */
	private void saveAsWithHandledExceptions() {
		File backsave = model.getFile();
		JFileChooser filechooser = new JFileChooser();
		filechooser.setSelectedFile(new File("graph"));

		if (filechooser.showSaveDialog(frame) == JFileChooser.APPROVE_OPTION) {
			model.setFile(filechooser.getSelectedFile());
			try {
				model.save();
			} catch (IOException ioe) {
				if (backsave != null) {
					model.setFile(backsave);
					showError("Une erreur s'est produite lors de la "
							+ "sauvegarde du fichier");
				}
			} finally {
				model.notifyObservers();
			}
		}
	}

	/**
	 * Demande un fichoer à l'utilisateur et s'assure que le model n'est pas
	 * null, puis charge le contenu du fichier dans le model.
	 */
	private void open() {
		JFileChooser fc = new JFileChooser();
		int answer = fc.showOpenDialog(frame);
		if (answer == JFileChooser.APPROVE_OPTION) {
			if (getState() == State.NOMOD) {
				frame.add(graphicGraph, BorderLayout.CENTER);
				useListRepresentation();
			}

			File oldFile = model.getFile();

			model.setFile(fc.getSelectedFile());
			//Le booléen sert à déterminer s'il faut replacer le précédent
			//fichier et faire un reload en cas d'erreur d'entrée sortie.
			boolean retLoad = loadWithHandledExceptions();
			if (!retLoad) {
				if (oldFile != null) {
					model.setFile(oldFile);
					reload();
				}
			}
			model.notifyObservers();
		}
	}

	private void reload() {
		if (getState() == State.MOD_FIL) {
			loadWithHandledExceptions();
		}
	}

	/**
	 * Réalise l'opération de chargement du fichier en s'occupant des
	 * éventuelles exceptions.
	 * Renvoie true si tout s'est bien passé, false sinon.
	 */
	private boolean loadWithHandledExceptions() {
		try {
			model.load();
		} catch (IOException e) {
			showError("Erreur d'entrée / sortie dans le fichier");
			return false;
		}
		model.notifyObservers();
		return true;
	}


	private void useListRepresentation() {
		if (model != null) {
			model.deleteObservers();
			model = new ListGraph(model);
		} else {
			model = new ListGraph();
		}
		frame.remove(graphicGraph);
		graphicGraph = new GraphicGraph(model);
		frame.add(graphicGraph, BorderLayout.CENTER);
		notificationsLabel.setText("Adjency list representation");
		addModelObservers();
		model.notifyObservers();
	}

	private void useMatrixRepresentation() {
		if (model != null) {
			model.deleteObservers();
			model = new MatrixGraph(model);
		} else {
			model = new MatrixGraph();
		}
		frame.remove(graphicGraph);
		graphicGraph = new GraphicGraph(model);
		frame.add(graphicGraph, BorderLayout.CENTER);
		notificationsLabel.setText("Matrix representation");
		addModelObservers();
		model.notifyObservers();
	}
	
	private Graph randomGraph(int n) {
		Graph graph;
		if (notificationsLabel.getText().contains("list")) {
			graph = new ListGraph();
		} else {
			graph = new MatrixGraph();
		}
		int lineNb = (int) Math.floor(Math.sqrt(n));
		int report = graphicGraph.getWidth() / graphicGraph.getHeight();
		int x = RANDOM_GRAPH_NODE_SPACE * report / 2;
		int y = RANDOM_GRAPH_NODE_SPACE / 2;
		int nodeNb = 0;
		while (nodeNb < n) {
			if (nodeNb + lineNb > n) {
				lineNb = n - nodeNb;
			}
			for (int i = 0; i < lineNb; i++) {
				graph.addNode(x, y);
				nodeNb = nodeNb + 1;
				x += RANDOM_GRAPH_NODE_SPACE * report;
			}
			y += RANDOM_GRAPH_NODE_SPACE;
			x = RANDOM_GRAPH_NODE_SPACE * report / 2;
		}
		Random r = new Random();
		int valeurMax = 0;
		for (int i = 0; i < n; i++) {
			valeurMax += i;
		}
		List<PositionedNode> nodes 
			= new ArrayList<PositionedNode>(graph.getNodes());
		int nb = r.nextInt(valeurMax);
		int i = 0;
		while (i < nb) {
			int a = r.nextInt(nodeNb);
			int b = r.nextInt(nodeNb);
			PositionedNode n1 = nodes.get(a);
			PositionedNode n2 = nodes.get(b);
			if (n1 != n2) {
				Edge v = new StdEdge(n1, n2);
				if (!graph.getVertexes().contains(v)) {
					graph.addVertex(v);
				}
				i++;
			}	
		}		
		return graph;
	}

	private void repack() {
		frame.setPreferredSize(frame.getSize());
		frame.pack();
	}
	
	/**
	 * Point d'entrée de l'application.
	 */
	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				new Gatrimi().display();
			}
		});
	}

}
