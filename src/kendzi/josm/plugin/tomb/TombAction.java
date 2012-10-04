package kendzi.josm.plugin.tomb;

import static org.openstreetmap.josm.tools.I18n.tr;

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JOptionPane;

import org.openstreetmap.josm.Main;
import org.openstreetmap.josm.actions.JosmAction;
import org.openstreetmap.josm.command.AddCommand;
import org.openstreetmap.josm.command.DeleteCommand;
import org.openstreetmap.josm.data.osm.Node;
import org.openstreetmap.josm.data.osm.Way;
import org.openstreetmap.josm.tools.Shortcut;

public class TombAction extends JosmAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    public TombAction() {
        super(tr("Tomb plugin"),
                "tomb_icon.png",
                tr(""),
                Shortcut.registerShortcut("tools:Tomb plugin",
                        tr("Tool: {0}", tr("Tomb")),
                        KeyEvent.VK_C,
                        Shortcut.SHIFT
                        ),
                        true);
    }

    public void actionPerformed(ActionEvent e) {

        //        Collection<Way> selectedWays = Main.main.getCurrentDataSet().getSelectedWays();
        Collection<Node> selectedWays = Main.main.getCurrentDataSet().getSelectedNodes();

        if (selectedWays.size() != 1) {
            JOptionPane.showMessageDialog(null,
                    tr("Chose one node"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        Node node = selectedWays.iterator().next();


        try {
            TombDialogAction dialog = new TombDialogAction();
            //            TombDialog dialog = new TombDialog();
            dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

            dialog.Load(node);

            dialog.setVisible(true);


        } catch (Exception ee) {
            ee.printStackTrace();
        }




    }

    public void actionPerformed2(ActionEvent e) {
        Collection<Way> selectedWays = Main.main.getCurrentDataSet().getSelectedWays();
        Iterator i=selectedWays.iterator();
        Way line=null;
        Way area=null;
        while(i.hasNext()){
            Way w=(Way)i.next();
            if(w.getNode(0)!=w.getNode(w.getNodesCount()-1)){
                if(line==null) {
                    line=w;
                }
            }
            else{
                if(area==null) {
                    area=w;
                }
            }
        }
        if(area==null||line==null) {
            return;
        }

        if (!isAreaConectedToLine(area, line)) {
            JOptionPane.showMessageDialog(null,
                    tr("Error area and way have to be connected!"),
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Node> nodes1=line.getNodes();

        List<Node> nodes2=area.getNodes();
        int first_node=nodes2.indexOf(nodes1.get(0));
        nodes2=moveNodes(nodes2,first_node);



        List <Way> newAreas=new ArrayList<Way>();


        Node lastCrossNode=null;
        Way tmpWay=new Way();
        Node firstN=null;
        //TODO OPT.
        int zzz=0;

        Iterator iter1=nodes2.iterator();
        while(iter1.hasNext()){
            Node n1=null;
            n1=(Node)iter1.next();
            Iterator j=nodes1.iterator();
            ////////////////////////////////
            boolean isIn=false;
            while(j.hasNext()){
                Node n2=(Node)j.next();
                if(n1==n2){
                    isIn=true;
                }
            }
            ////////////////////////////////
            tmpWay.addNode(n1);

            if(isIn){
                System.out.println("IsIn");
                if(lastCrossNode!=null){
                    //make area
                    Way wt=new Way();
                    Iterator p=nodes1.iterator();
                    boolean adding=false;
                    while(p.hasNext()){
                        Node n3=(Node)p.next();

                        //START,STOP adding
                        if(n3==lastCrossNode||n3==n1){
                            adding=!adding;
                            if(!adding) {
                                wt.addNode(n3);
                            }
                        }

                        if(adding) {
                            wt.addNode(n3);
                        }
                    }
                    //CONNECT ways
                    System.out.println("Dodaje nowy");
                    addToWay(tmpWay,wt);
                    newAreas.add(tmpWay);
                    tmpWay=new Way();
                    tmpWay.addNode(n1);
                }

                lastCrossNode=n1;
            }

        }

        //Remove bugs
        //(areas with 3 nodes)
        for(int z=0;z<newAreas.size();){
            if(newAreas.get(z).getNodesCount()<=3) {
                newAreas.remove(z);
            } else {
                z++;
            }
        }



        List<Way> select = new ArrayList<Way>();

        for(int z=0;z<newAreas.size();z++){
            Way w=newAreas.get(z);
            removeDub(w);
            w.setKeys(area.getKeys());
            // ?!@#
            //            Main.main.getCurrentDataSet().addPrimitive(w);

            // add the node
            Main.main.undoRedo.add(new AddCommand(w));
            select.add(w);
        }




        // ?!@#
        //        Main.main.getCurrentDataSet().removePrimitive(area.getPrimitiveId());

        Main.main.undoRedo.add(new DeleteCommand(area));

        if(line.getKeys().size() == 0) {
            // ?!@#
            //            Main.main.getCurrentDataSet().removePrimitive(line.getPrimitiveId());
            Main.main.undoRedo.add(new DeleteCommand(line));
        }

        getCurrentDataSet().setSelected(select);
        Main.map.mapView.repaint();
    }


    private boolean isAreaConectedToLine(Way area, Way line) {
        List<Node> lineNodes = line.getNodes();

        Node begin = lineNodes.get(0);
        Node end = lineNodes.get(lineNodes.size() - 1);

        return  isNodeInList(begin, area.getNodes()) &&
                isNodeInList(end, area.getNodes());

    }

    private boolean isNodeInList(Node begin, List<Node> list) {
        for (Node node : list) {
            if (node.equals(begin)) {
                return true;
            }
        }
        return false;
    }

    private List<Node> moveNodes(List<Node>nodes,int n){
        List<Node> nod=new ArrayList<Node>();
        int p=nodes.size();
        System.out.println(p+":"+n);
        for(int i=n;i<p+n;i++){
            nod.add(nodes.get(i%p));
        }
        nod.add(nod.get(0));
        return nod;
    }

    private void addToWay(Way w1,Way add){
        //TODO OPT.

        if(w1.getNode(0)==add.getNode(0)){
            for(int i=add.getNodesCount()-1;i>=0;i--){
                if(add.getNode(i)!=w1.getNode(0)&&add.getNode(i)!=w1.getNode(w1.getNodesCount()-1)) {
                    w1.addNode(add.getNode(i));
                }
            }
        }else if(w1.getNode(w1.getNodesCount()-1)==add.getNode(0)){
            for(int i=0;i<add.getNodesCount();i++){
                if(add.getNode(i)!=w1.getNode(0)&&add.getNode(i)!=w1.getNode(w1.getNodesCount()-1)) {
                    w1.addNode(add.getNode(i));
                }
            }
        }

        removeDub(w1);
    }


    private void removeDub(Way w1){
        //Remove dubles
        List <Node> ln=new ArrayList<Node>();

        for(int i=0;i<w1.getNodesCount();i++){
            boolean isIn=false;
            for(int j=0;j<ln.size();j++){
                if(ln.get(j)==w1.getNode(i)) {
                    isIn=true;
                }
            }
            if(!isIn) {
                ln.add(w1.getNode(i));
            }
        }
        //Last
        ln.add(ln.get(0));
        w1.setNodes(ln);
    }
    @Override
    protected void updateEnabledState() {
        setEnabled(getCurrentDataSet() != null);
    }
}
