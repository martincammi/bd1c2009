package ubadb.tools.scheduleAnalyzer.common;

import java.awt.Frame;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
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
}
