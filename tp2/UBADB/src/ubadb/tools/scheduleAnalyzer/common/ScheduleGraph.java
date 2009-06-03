package ubadb.tools.scheduleAnalyzer.common;

import java.awt.Frame;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ScheduleGraph
{
	//[start] Atributos
	private List<ScheduleArc> arcs;
	private List<String> transactions;
	//[end]
	
	//[start] Constructor
	public ScheduleGraph()
	{
		arcs = new ArrayList<ScheduleArc>();
		transactions = new ArrayList<String>();
	}
	//[end]
	
	//[start] addTransaction
	public void addTransaction(String transaction)
	{
		transactions.add(transaction);
	}
	//[end]
	
	//[start] getTransactions
	public List<String> getTransactions()
	{
		return transactions;
	}
	//[end]
	
	//[start] addArc
	public void addArc(ScheduleArc arc)
	{
		if(!arcs.contains(arc))
		{
			arcs.add(arc);
		}
	}
	//[end]
	
	//[start] getArcs
	public List<ScheduleArc> getArcs()
	{
		return arcs;
	}
	//[end]
	
	public void mostrar() throws InterruptedException{
		Frame f = new Frame("prueba");
		
		GrafoApplet applet = new GrafoApplet(this);
		f.add("Center",applet);
		f.pack();
		f.show();
		Thread.sleep(100000);
	}
	
	/**
	 * Obtiene las transacciones dependientes de la transacción pasada como parámetro.
	 */
	
	@Deprecated
	public List<String> getDependencias(String tnx){
		List<String> tnxsHijas = new ArrayList<String>();
		//Itero sobre los arcos. Busco startTransaction = a tnx si es igual guardo EndTransaction
		for (Iterator iter = arcs.iterator(); iter.hasNext();) {
			ScheduleArc arc = (ScheduleArc) iter.next();
			if(arc.getStartTransaction().equals(tnx)){
				tnxsHijas.add(arc.getEndTransaction());
			}
		}
		return tnxsHijas;
	}
	
	/** Indica si hay algun arco que llegue a esa transacción */
	public boolean isDependence(String tnx){
		boolean isDependence = false;
		for (Iterator iter = arcs.iterator(); iter.hasNext() && !isDependence;) {
			ScheduleArc arc = (ScheduleArc) iter.next();
				isDependence = isDependence || arc.getEndTransaction().equals(tnx); //Si es dependencia de algua otra transacción
			}
		return isDependence;
	}
	
	/** Indica si hay algun arco que llegue a esa transacción quitando los nodos de la lista excludeNodes*/
	public boolean isDependenceWithoutNodes(String tnx, List<String> excludedNodes){
		boolean isDependence = false;
		for (Iterator iter = arcs.iterator(); iter.hasNext() && !isDependence;) {
			ScheduleArc arc = (ScheduleArc) iter.next();
				if(!excludedNodes.contains(arc.getStartTransaction())) // Si la dependencia no es con respecto a un nodo exluido
					isDependence = isDependence || arc.getEndTransaction().equals(tnx); //Si es dependencia de algua otra transacción
			}
		return isDependence;
	}
	
	/** Remueve un nodo del grafo junto con todos los arcos relacionados con él*/
	public void removeTransaction(String tnx)
	{
		List<ScheduleArc> quitarArcos = new ArrayList<ScheduleArc>();
		transactions.remove(tnx);
		for (Iterator iter = arcs.iterator(); iter.hasNext();) {
			ScheduleArc arc = (ScheduleArc) iter.next();
			if(arc.getStartTransaction().equals(tnx) || arc.getEndTransaction().equals(tnx)){
				quitarArcos.add(arc);
			}
		}
		
		arcs.removeAll(quitarArcos);
	}
	//[end]
	
	public String toString(){
		StringBuffer sb = new StringBuffer();
		for (Iterator iter = arcs.iterator(); iter.hasNext();) {
			ScheduleArc arc = (ScheduleArc) iter.next();
			sb.append(arc.getStartTransaction() + " --> " + arc.getEndTransaction() + "\n ");
		}
		return sb.toString();
	}
	
}


