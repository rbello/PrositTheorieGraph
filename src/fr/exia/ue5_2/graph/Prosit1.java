package fr.exia.ue5_2.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Paint;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;
import java.util.StringTokenizer;

import javax.swing.JFrame;

import org.apache.commons.collections15.FactoryUtils;
import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.ISOMLayout;
import edu.uci.ics.jung.algorithms.layout.Layout;
// @see http://jung.cvs.sourceforge.net/viewvc/jung/jung2/jung-api/src/main/java/edu/uci/ics/jung/graph/Graph.java?content-type=text%2Fplain
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
// @see http://jung.cvs.sourceforge.net/viewvc/jung/jung2/jung-io/src/main/java/edu/uci/ics/jung/io/PajekNetReader.java?content-type=text%2Fplain
import edu.uci.ics.jung.io.PajekNetReader;
import edu.uci.ics.jung.visualization.VisualizationViewer;

/**
 * 
 * @author rbello
 * @see http://jung.cvs.sourceforge.net/viewvc/jung/jung2/jung-samples/
 */
public class Prosit1 {

	public static final File DATASET = new File("resources/datasets/FB.net");

	private static HashMap<Object, Color> COLORS = new HashMap<Object, Color>();

	private static JFrame FRAME = null;
	
	
	public static void main(String[] args) throws IOException {

		System.out.println("Th�orie des Graphes - Prosit 1 - Exia UE 5.2");

		System.out.print("Chargement du graph...");
		Graph<?, ?> g = readNet(DATASET.getAbsolutePath());
		System.out.println("OK");

		System.out.println("Il y a " + g.getVertexCount()
				+ " sommets (personnes) et " + g.getEdgeCount()
				+ " ar�tes (relations)");

		
		
		System.out.println("Recherche des composants connect�s...");
		Set<?> comps = getConnectedComponent(g);
		System.out.println("Communaut� trouv�e : " + comps.size()
				+ " personnes");
		for (Object o : comps) {
			setNodeColor(o, Color.BLUE);
		}
		
				
		System.out.println("Affichage du graph...");
		afficherGraph(g, new Dimension(800, 600));

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static void afficherGraph(Graph g, Dimension dim) {
		Layout l = new ISOMLayout(g);
		JFrame jf = new JFrame();
		VisualizationViewer vv = new VisualizationViewer(l, dim);
		vv.getRenderContext().setVertexFillPaintTransformer(new Transformer() {
			public Paint transform(Object p) {
				if (COLORS.containsKey(p)) {
					return COLORS.get(p);
				}
				return Color.RED;
			}
		});
		jf.getContentPane().add(vv);
		jf.pack();
		FRAME = jf;
		jf.setVisible(true);
	}

	public static void setNodeColor(Object node, Color color) {
		COLORS.put(node, color);
		if (FRAME != null) FRAME.repaint();
	}

	public static <V, E> Set<V> getConnectedComponent(Graph<V, E> g) {

		// Code source :
		// https://traclifo.univ-orleans.fr/Agape/browser/trunk/AGAPE/src/agape/tools/Components.java

		// Le composant
		Set<V> C = new HashSet<V>();

		// La pile de travail
		Stack<V> stack = new Stack<V>();

		// Le graph est vide
		if (g.getVertices().isEmpty())
			return C;

		// On prend la premi�re ar�te
		V v = g.getVertices().iterator().next();
		C.add(v);
		stack.add(v);

		while (!stack.isEmpty()) {

			V x = stack.pop();

			Set Nx = new HashSet(g.getNeighbors(x));

			Nx.removeAll(C);

			stack.addAll(Nx);

			C.addAll(Nx);

			// // On recup�re l'ar�te � traiter
			// V x = stack.pop();
			//
			// // On recup�re tous les voisins de cette ar�te
			// Set<V> Nx = new HashSet<V>(g.getNeighbors(x));
			//
			// // Non-treated vertices
			// Set<V> nT = new HashSet<V>();
			// Sets.difference(Nx, C).copyInto(nT);
			//
			// // On rajoute tous ces �l�ments dans la pile de travail
			// stack.addAll(nT);
			//
			// // Ainsi que dans le composant
			// C.addAll(nT);

		}

		// On renvoie le composant
		return C;

	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static Graph<Personne, RelationAmitie> getGraph() throws IOException {

		// Le graph cible :
		// - Undirected : non-orient�
		// - Sparse: creux, nombre d'ar�tes �loign� de la maximale
		// Typ� :
		// - Personne : type des sommets (vertex/vertices)
		// - Relation : type des ar�tes (edges)
		Graph<Personne, RelationAmitie> g = new UndirectedSparseGraph<Personne, RelationAmitie>();

		// Pajek reader
		PajekNetReader pnr = new PajekNetReader(
				FactoryUtils.instantiateFactory(Personne.class),
				FactoryUtils.instantiateFactory(RelationAmitie.class));

		// On charge le fichier
		pnr.load(DATASET.getAbsolutePath(), g);

		return g;
	}

	/**
	 * Returns an undirected graph by reading a .net file (pajek format).
	 * 
	 * @param fname
	 *            file path
	 * @return graph
	 */
	public static Graph<String, Integer> readNet(String fname) {

		File f = new File(fname);

		BufferedReader lecteurAvecBuffer = null;

		String ligne;

		Graph<String, Integer> G = new UndirectedSparseGraph<String, Integer>();

		try {
			lecteurAvecBuffer = new BufferedReader(new FileReader(f));
		} catch (FileNotFoundException exc) {
			System.err.println("Error opening file .net\n");
		}

		ArrayList<String> V = new ArrayList<String>();

		try {

			ligne = lecteurAvecBuffer.readLine();

			ligne = lecteurAvecBuffer.readLine();

			// adding vertices
			while (!ligne.equals("*edgeslist")) {
				StringTokenizer tok = new StringTokenizer(ligne, "\"");

				tok.nextToken();
				String v = tok.nextToken();
				// v=v.replaceAll("\"","");
				V.add(v);
				G.addVertex(v);

				ligne = lecteurAvecBuffer.readLine();
			}

			// adding edges
			int ne = 0;
			ligne = lecteurAvecBuffer.readLine();
			while (ligne != null) {
				StringTokenizer tok = new StringTokenizer(ligne, " ");
				int s = Integer.valueOf(tok.nextToken()) - 1;
				while (tok.hasMoreTokens()) {
					int t = Integer.valueOf(tok.nextToken()) - 1;
					if (!V.isEmpty())
						G.addEdge(ne, V.get(s), V.get(t));
					else
						G.addEdge(ne, Integer.toString(s), Integer.toString(t));
					ne++;
				}

				ligne = lecteurAvecBuffer.readLine();
			}

			lecteurAvecBuffer.close();

		} catch (Exception e) {
			System.err.println("Error parsing file: " + e + "\n");
		}

		return G;
	}

}
