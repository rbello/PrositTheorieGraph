package fr.exia.ue5_2.graph;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.collections15.Transformer;
import org.apache.commons.collections15.TransformerUtils;

import edu.uci.ics.jung.algorithms.layout.Layout;
import edu.uci.ics.jung.algorithms.layout.StaticLayout;
import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.SparseGraph;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.control.AbstractModalGraphMouse;
import edu.uci.ics.jung.visualization.control.DefaultModalGraphMouse;

/**
 *
 * @author vlevorato
 */
public class Prosit2 extends javax.swing.JFrame {

    Graph<String,Number> g;
    VisualizationViewer vv;
    
    HashMap<String,Color> vcolor=new HashMap();
    
    Map<String,Point2D> coordmap = new HashMap<String,Point2D>();
    
    int size=300;
    double oamount=0.3;
    
    class VertexColorTransformer implements Transformer<Object,Color> {

        //palette de couleur
        HashMap<Object,Color> colortable;
        
        public VertexColorTransformer(HashMap ctable){ colortable=ctable; }

        public Color transform(Object v) {
            return colortable.get(v);
        }
        
    }
    
   
    
   
    public Prosit2() throws IOException {
        
        
        
        /** NE PAS MODIFIER LE CODE **/
        initComponents();
        
        //déclaration d'un graphe non-orienté (liste d'adjacence)
        g = new UndirectedSparseGraph();
        
        //déclaration d'une table avec la table des couleurs des noeuds
        vcolor=new HashMap();
     
        /** définition du rendu visuel du graphe **/
        
        //définition de l'algorithme de rendu (Grille 2D)
        Dimension preferredSize = new Dimension(size,size);
        coordmap = new HashMap<String,Point2D>();
        Transformer<String,Point2D> vlf = TransformerUtils.mapTransformer(coordmap);
        g= generateVertexGrid(coordmap, preferredSize, 25);
        Layout<String,Number> gridLayout = new StaticLayout<String,Number>(g, vlf, preferredSize);
        vv = new VisualizationViewer(gridLayout); 
       
        System.out.println("Nombre de noeuds:"+coordmap.size());
        
        //définit la couleur par défaut
        for(String v : g.getVertices())
            vcolor.put(v, Color.white);
        
        //définit le noeud source et cible
        vcolor.put(getSourceNode(), Color.green);
        vcolor.put(getTargetNode(), Color.red);
        
        setObstacles();
        
        //définition de listeners prédéfinis pour la souris
        final AbstractModalGraphMouse graphMouse = new DefaultModalGraphMouse();
        vv.setGraphMouse(graphMouse);
        
        //gestion de la couleur des sommets
        vv.getRenderContext().setVertexFillPaintTransformer(new VertexColorTransformer(vcolor));

        
        //fond en blanc
        vv.setBackground(Color.white);
        	
        
        //ajout de la visualisation du graphe au jFrame principal
        this.getContentPane().add(vv);
        this.pack();
        /** NE PAS MODIFIER LE CODE **/
       
        //Trouve le plus court chemin entre 2 noeuds 
        Astart(g, getSourceNode(),getTargetNode(),coordmap, vcolor);
        
    }
    
    /**
     * Algorithme A* de plus court chemin entre 2 noeuds, de s à t.
     * @param g graphe
     * @param s noeud source
     * @param t noeud cible
     * @param coordmap table des coordonnées
     * @param vmap  table des couleurs
     */
    private void Astart(Graph<String,Number> g, String s, String t, Map<String,Point2D> coordmap, HashMap<String,Color> vmap)
    { 
        /*
         * PARTIE A FAIRE.
         * - Utiliser les coordonées de coordmap pour se déplacer (utiliser la méthode getDistance(String v1, String v2))
         * - Définir les fonctions f,g et h de l'algorithme.
         * - Colorier en bleu le chemin trouvé à l'aide de vmap.
         * - Détecter les obstacles par leurs couleurs (Color.gray) (créer par exemple une methode boolean isObstacle(String v))
         * - Vérifier qu'il y a bien un chemin (et de retourner l'info).
         */
        
        boolean found=false;
        Set<String> closedset =new HashSet();    // The set of nodes already evaluated.
        Set<String> openset = new HashSet();
        openset.add(s);    // The set of tentative nodes to be evaluated, initially containing the start node
        Map<String,String> came_from = new HashMap();    // The map of navigated nodes.
        Map<String,Integer> g_score= new HashMap();  
        Map<String,Integer> f_score= new HashMap(); 
        
        g_score.put(s, 0); // Cost from start along best known path.
        
        // Estimated total cost from start to goal through y.
        f_score.put(s, g_score.get(s) + getDistance(s, t));
 
    while(!openset.isEmpty())
    {
        int low_score=Integer.MAX_VALUE;
        String current="";
        for(String v : openset)
            if(low_score>f_score.get(v))
            {
                low_score=f_score.get(v);
                current=v;
            }
        
        if(current.equals(t))
        {
            found=true;
            while(!current.equals(s))
            {
                current=came_from.get(current);
                vmap.put(current, Color.blue);
            }
            vmap.put(current, Color.green);
            return;
        }
 
        openset.remove(current);
        closedset.add(current);
        vmap.put(current, Color.orange);
 
        Set<String> neigh=new HashSet();
        for(String v:g.getVertices())
            if(getDistance(current,v)<=25)
                neigh.add(v);
        
        
        Set<String> ntoremove=new HashSet();
        
        for(String v:neigh)
            if(vmap.get(v)==Color.gray)
                ntoremove.add(v);
        
        neigh.removeAll(ntoremove);
        
        for(String v : neigh)
        {
            int tentative_g_score=g_score.get(current)+getDistance(current,v);
            
            if((!closedset.contains(v) && !openset.contains(v)) || tentative_g_score < g_score.get(v))
            {
                came_from.put(v, current);
                g_score.put(v,tentative_g_score );
                f_score.put(v, g_score.get(v) + getDistance(v,t));
                openset.add(v);
            }
        }
    }
    
    //failure
    if(!found)
        System.out.println("No path");
        
  
        
    }
    
    
    
    
    //NE PAS MODIFIER APRES CETTE LIGNE
    
    private int getDistance(String v1, String v2)
    {
        int d=0;
        Point2D p1=coordmap.get(v1);
        Point2D p2=coordmap.get(v2);
        d+=Math.abs(p1.getX()-p2.getX());
        d+=Math.abs(p1.getY()-p2.getY());
        
        return d;
    }
    
    
    private String getSourceNode()
    {
        return "v0";
    }
    
    private String getTargetNode()
    {
        String v="v";
        v+=(coordmap.size()-1);
        return v;
    }
    
    private void setObstacles()
    {
        String start=getSourceNode();
        String end=getTargetNode();
        
        for(String v: g.getVertices())
            if(Math.random()<oamount && !v.equals(start) && !v.equals(end))
                vcolor.put(v, Color.gray);
    }

    private Graph<String,Number> generateVertexGrid(Map<String,Point2D> vlf, Dimension d, int interval) {
        int count = d.width/interval * d.height/interval;
        Graph<String,Number> graph = new SparseGraph<String,Number>();
        for(int i=0; i<count; i++) {
            int x = interval*i;
            int y = x / d.width * interval;
            x %= d.width;
            
            Point2D location = new Point2D.Float(x, y);
            String vertex = "v"+i;
            vlf.put(vertex, location);
            graph.addVertex(vertex);
        }
        return graph;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">                          
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("TP Jung");
        setBackground(new java.awt.Color(255, 255, 255));
        getContentPane().setLayout(new java.awt.GridLayout(1, 0));

        pack();
    }// </editor-fold>                        

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(Prosit2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Prosit2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Prosit2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Prosit2.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    Prosit2 frame=new Prosit2();
                    frame.setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(Prosit2.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }
    // Variables declaration - do not modify                     
    // End of variables declaration                   
}
