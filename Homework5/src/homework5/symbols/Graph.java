package homework5.symbols;

import homework5.Kanga;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 *
 * @author labis
 */
public class Graph {
    
    //Metavlhtes (komvoi tou grafou)
    private final Map<Integer, TempInfo> temps;
    
    //Metavlhtes pou zoun tautoxrona (akmes tou grafou)
    private final Map<Integer, List<Integer>> neighbours;

    private final int argc; //orismata pou pairnei mia sunartish
    
    private int maxArgs; //megisto plithos orismatwn gia klhseis pou kanei h sunartish
    
    public Graph(final int argc) {
        temps = new HashMap<Integer, TempInfo>();
        neighbours = new HashMap<Integer, List<Integer>>();
        this.argc = argc;
        maxArgs = 0;
    }
    
    public void addTemp(final int temp, final TempInfo liveRange) {
        temps.put(temp, liveRange);
    }
        
    public TempInfo getTempInfo(final int temp) {
        return temps.get(temp);
    }
    
    public int getMaxArgs() {
    	return maxArgs;
    }
    
    public void setMaxArgs(final int maxArgs) {
    	this.maxArgs = maxArgs;
    }	
    
    public int countSpilledVariables() { //metavlhtes avaftes
        
        int counter = 0;
        
        for(TempInfo tempInfo : temps.values()) {
            if(tempInfo.getColour() == null) {
                counter ++;
            }
        }
        
        return counter;
    }
    
    public int countSpilledArgs() {
        return (argc > Kanga.MAX_ARGS) ? (argc - Kanga.MAX_ARGS) : 0;
    }
    
    public boolean containsTemp(final int temp) {
        return temps.containsKey(temp);
    }
    
    public void chaitinAlgorithm() {
        
        Stack<Integer> stack = new Stack<Integer>();
     
        findNeighbours();
        
        while(true) {
            
            //step 1 (uparxei komvos me ligoterous apo max_colours geitones)
            for (Iterator<Map.Entry<Integer, List<Integer>>> i = neighbours.entrySet().iterator(); i.hasNext();) {
                final Map.Entry<Integer, List<Integer>> entry = i.next();
                //vrhke komvo me ligoterous apo max_colours geitones o opoios borei na xrwmatistei
                if(entry.getValue().size() < Register.values().length) { //osoi einai oi kataxwrhtes
                    //bainei sthn stoiva pros xrwmatismo
                    stack.push(entry.getKey());

                    //gia kathe geitona tou komvou - diagrafh apo tous geitones
                    for(int neighbour : entry.getValue()) {
                        //diagrafh tou komvou apo tous geitones tou geitona (neighbor)
                        neighbours.get(neighbour).remove(entry.getKey());
                    }

                    //petaei ton komvo apo tous geitones
                    i.remove();
                }
            }
            
            if (neighbours.size() == 0)
                break;
            
            //step 2 (den uparxei komvos me ligoterous apo max_colours geitones)
            Map.Entry<Integer, List<Integer>> maxNeighboursNode = neighbours.entrySet().iterator().next();


            //euresh komvou me tous perissoterous geitones gia diagrafh.
            //afou oloi oi geitones tou tha elafrunoun kata ena geitona, sumferei 
            //giati tha uparxoun pio polloi upopsifioi gia to epomeno step 1
            for(Map.Entry<Integer, List<Integer>> entry : neighbours.entrySet()) {

                //vriskei ton komvo me tous perissoterous geitones
                if(maxNeighboursNode.getValue().size() < entry.getValue().size()) {    
                    maxNeighboursNode = entry;
                }
            }

            //gia kathe geitona tou komvou me tous perissoterous geitones, diagrafetai
            //autos o komvos apo geitona twn geitonwn
            for(int neighbour : maxNeighboursNode.getValue()) {
                neighbours.get(neighbour).remove(maxNeighboursNode.getKey());
            }

            neighbours.remove(maxNeighboursNode.getKey());
        }
        
        findNeighbours(); //xana ftiaxnei akmes
        
        //step 3 (xrwmatismos grafou - antistoixhsh kathe proswrinhs metavlhths se kataxwrhth)
        while(stack.size() > 0) {
            int node = stack.pop();
            int maxColour = -1;//avafto
            
            for (int neighbour : neighbours.get(node)) {
                
                //vriskei to megisto xrwma pou xrhsimopoioun oi geitones
                final Register colour = temps.get(neighbour).getColour();
                if(((colour == null) ? -1 : colour.ordinal()) > maxColour) {
                    maxColour = ((colour == null) ? -1 : colour.ordinal());
                }
            }
            
            //epomeno xrwma pou den xrhsimopoieitai apo geitona, to xrhsimopoiei
            //o node.
            temps.get(node).setColour(Register.values()[maxColour + 1]);
        }
        
        //deutero perasma gia tous avaftous kai topothetish tous mesa sti stoiva
        int offset = 0; 
        for(TempInfo tempInfo : temps.values()) {
            if(tempInfo.getColour() == null) {
                tempInfo.setStackOffset(offset++);
            }
        }
    }
    
    private void findNeighbours() {
        for(Map.Entry<Integer, TempInfo> entry1 : temps.entrySet()) {
            final int temp1 = entry1.getKey();
            final TempInfo liveRange1 = entry1.getValue();
            
            for(Map.Entry<Integer, TempInfo> entry2 : temps.entrySet()) {
                final int temp2 = entry2.getKey();
                final TempInfo liveRange2 = entry2.getValue();

                //an oxi (o temp1 genietai kai pethainei prin ton temp2 h o temp2 genietai kai pethainei prin ton temp1)
                if ((temp1 != temp2) && (!(((liveRange1.getBegin() < liveRange2.getBegin()) && liveRange1.getEnd() < liveRange2.getBegin()) || 
                ((liveRange2.getBegin() < liveRange1.getBegin()) && (liveRange2.getEnd() < liveRange1.getBegin()))))) {
                
                    //o temp1 einai geitonas me temp2
                    if(!neighbours.containsKey(temp1))
                        neighbours.put(temp1, new ArrayList<Integer>());
                    
                    if (!neighbours.get(temp1).contains(temp2))
                        neighbours.get(temp1).add(temp2);
                    
                    if(!neighbours.containsKey(temp2))
                        neighbours.put(temp2, new ArrayList<Integer>());
                    
                    //an den einai hdh geitonas, ton kanei geitona
                    if (!neighbours.get(temp2).contains(temp1))
                        neighbours.get(temp2).add(temp1);
                }
            }
        }
    }
}
