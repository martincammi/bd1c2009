package ubadb.tools.scheduleAnalyzer.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;
import ubadb.tools.scheduleAnalyzer.common.results.RecoverabilityResult;
import ubadb.tools.scheduleAnalyzer.common.results.SerialResult;
import ubadb.tools.scheduleAnalyzer.common.results.SerializabilityResult;
import ubadb.tools.scheduleAnalyzer.exceptions.ScheduleException;


public abstract class Schedule
{
	//[start] Atributos
	private List<String> transactions;
	protected List<String> items;
	protected List<Action> actions;
	
	public List<String> getTransactions()
	{
		return transactions;
	}
	public List<String> getItems()
	{
		return items;
	}
	public List<Action> getActions()
	{
		return actions;
	}
	public abstract void addAction(Action action);
	public abstract ScheduleType getType();
	//[end]
	
	//[start] Constructor
	public Schedule()
	{
		transactions = new ArrayList<String>();
		items		 = new ArrayList<String>();
		actions 	 = new ArrayList<Action>();
	}
	//[end]
	
	private class Par<F,S> {
		
		private F first;
		private S second;
		
		public Par(F first, S second){
			this.first = first;
			this.second = second;
		}
		
		public F getFirst() {
			return first;
		}
		public void setFirst(F first) {
			this.first = first;
		}
		public S getSecond() {
			return second;
		}
		public void setSecond(S second) {
			this.second = second;
		}
	}
	
	//[start] addTransaction
	public void addTransaction(String transaction) throws ScheduleException
	{
		if(getTransactions().contains(transaction))
			throw new ScheduleException("Ya existe una transaccin con ese nombre");
		else
			getTransactions().add(transaction);
	}
	//[end]

	//[start] addItem
	public void addItem(String item) throws ScheduleException
	{
		if(items.contains(item))
			throw new ScheduleException("Ya existe una tem con ese nombre");
		else
			items.add(item);
	}
	//[end]
	
	//[start] actions
	public void editAction(int actionNumber, Action newAction) throws ScheduleException
	{
		if(actionNumber < 0 || actionNumber >= actions.size())
			throw new ScheduleException("La posicin a modificar es invlida");
		
		actions.set(actionNumber, newAction);
	}
	
	public void removeAction(int index) throws ScheduleException
	{
		if(index < 0 || index >= actions.size())
			throw new ScheduleException("La posicin a remover es invlida");
		
		actions.remove(index);
	}

	public void swapAction(int indexA, int indexB) throws ScheduleException
	{
		if(indexA < 0 || indexB < 0  || indexA >= actions.size() || indexB >= actions.size())
			throw new ScheduleException("Las posiciones a swapear son invlidas");
		
		Collections.swap(actions, indexA, indexB);
	}
	//[end]

	//[start] Mtodos Abstractos
	public abstract LegalResult analyzeLegality();
	public abstract ScheduleGraph buildScheduleGraph();
	//[end]

	//[start] analyzeSeriality
	public SerialResult analyzeSeriality()
	{
		//TERMINADO
		//Un schedule es serial si para toda transaccin, todas sus acciones aparecen consecutivas dentro del schedule
		boolean isSerial = true;
		String nonSerialTransaction = "";
		String message = "";
		String transactionActual = "";
		List<String> listTransaction = new ArrayList<String>();
		for (Iterator iterator = actions.iterator(); iterator.hasNext();) 
		{
			Action act = (Action) iterator.next();
		
			if (transactionActual == "")
				transactionActual = act.getTransaction();
			if (transactionActual != act.getTransaction())
			{
				if (listTransaction.contains(act.getTransaction()))
				{
					isSerial = false;
					nonSerialTransaction = act.getTransaction();
					break;
				}
				else
				{
					listTransaction.add(transactionActual);
					transactionActual = act.getTransaction();
				}
			}
			
		}
		
		
		SerialResult result = new SerialResult(isSerial, nonSerialTransaction, message);
		return result;
	}
	//[end]
	
	//[start] analyzeSerializability
	public SerializabilityResult analyzeSerializability()
	{	
		//TODO: Completar Es Serializable por Martin Cammi
		//Usar el grafo para determinar si es o no serializable
		boolean isSerializable = true;
		ScheduleGraph graph = buildScheduleGraph();
		List<List<String>> possibleExcecution = new ArrayList<List<String>>();
		List<String> cycle = new ArrayList<String>();
		String message = "";
		isSerializable = !hasCycles(possibleExcecution, graph);
		if (!isSerializable) //Se buscan los ciclos
		{
			getCycle(cycle, graph);
		}
		SerializabilityResult result = new SerializabilityResult(isSerializable, possibleExcecution, cycle, message );
		return result;
	}
	//[end]

	private boolean hasCycles(List<List<String>> possibleExcecutions, ScheduleGraph graph)
	{
		//Inicialización
		List<String> oneExecution = new ArrayList<String>();
		List<Par<String,Boolean>> nodos = new ArrayList<Par<String,Boolean>>(); //Lista de nodos (nombreTnx,habilitada);
		
		//Carga inicial de tnxs, todas habilitadas.
		for (String nodo : graph.getTransactions()) 
		{
			nodos.add(new Par<String,Boolean>(nodo,true));
		}
		
		//Busca ejecuciones
		findExecutions(possibleExcecutions, nodos, oneExecution, graph);
	
		//Si possibleExcecutions es vacio significa que no se pudo hallar una ejecucion con lo cual no es serializable por lo tanto tiene un ciclo.
		return possibleExcecutions.size() == 0; 
	}
		
	/*
	 * Obtiene las posibles ejecuciones serializables si las hay.
	 */
	private void findExecutions(List<List<String>> possibleExcecutions, List<Par<String,Boolean>> nodos, List<String> oneExecution, ScheduleGraph graph){
		
		if(isEmpty(nodos)){
			
			List<String> nuevaExec = new ArrayList<String>();
			for (Iterator iter = oneExecution.iterator(); iter.hasNext();) 
			{
				String element = (String) iter.next();
				nuevaExec.add(element);
			}
			possibleExcecutions.add(nuevaExec);
		}
		
		
		//for (Iterator iter = nodos.iterator(); iter.hasNext();) {
		for (int i = 0; i < nodos.size(); i++)
		{
			String tnx = (String) nodos.get(i).getFirst();
			
			if(nodos.get(i).getSecond())
			{
				if(!graph.isDependenceWithoutNodes(tnx, oneExecution))
				{
					nodos.get(i).setSecond(false);
					oneExecution.add(tnx);
					findExecutions(possibleExcecutions,nodos,oneExecution,graph);
					nodos.get(i).setSecond(true);
					oneExecution.remove(tnx);
				}
			}
		}
	}
	
	private void getCycle(List<String> cycle, ScheduleGraph graph)
	{
		//Inicialización
		List<Par<String,Boolean>> nodos = new ArrayList<Par<String,Boolean>>(); //Lista de nodos (nombreTnx,habilitada);
		boolean shutdownAlgorithm = false;
		
		//Carga inicial de tnxs, todas habilitadas.
		for (String nodo : graph.getTransactions()) 
		{
			nodos.add(new Par<String,Boolean>(nodo,true));
		}
		
		//Busca el ciclo.
		findCycle(cycle, nodos, graph, shutdownAlgorithm);
	}
	
	
	private void findCycle(List<String> cycle, List<Par<String,Boolean>> nodos, ScheduleGraph graph, boolean shutdownAlgorithm){
		
		if(!isEmpty(nodos) && !shutdownAlgorithm){
			for (int i = 0; i < nodos.size(); i++)
			{
				String tnx = (String) nodos.get(i).getFirst();
				
				if(nodos.get(i).getSecond())
				{
					//Si es una dependencia de otro es posible que forme parte de un ciclo.
					if(graph.isDependence(tnx))
					{
						if(cycle.contains(tnx)){ //El nodo ya estaba contenido, se formo un ciclo
							shutdownAlgorithm = true;
						}
						cycle.add(tnx);
						List<Par<String,Boolean>> nodos2 = copyNodosTrue(nodos);
						
						nodos2.get(i).setSecond(false);
						findCycle(cycle,nodos2,graph,shutdownAlgorithm);
						nodos.get(i).setSecond(true);
					}
				}
			}
		}
	}
	
	private List<Par<String,Boolean>> copyNodosTrue(List<Par<String,Boolean>> nodos){
		List<Par<String,Boolean>> nodosNuevos = new ArrayList<Par<String,Boolean>>();
		for (Par<String, Boolean> par : nodos) {
			par.setSecond(true);
			nodosNuevos.add(par);
		}
		return nodosNuevos;
	}
	
	private boolean isEmpty(List<Par<String,Boolean>> lista){
		boolean tieneAlgo = false;
		for (Par<String, Boolean> par : lista) {
			tieneAlgo = tieneAlgo || par.getSecond();
		}
		return !tieneAlgo;
	}
	
	/*
	private boolean tieneCiclosA(List<List<String>> possibleExcecutions, List<String> cycle)
	{
		ScheduleGraph graph = buildScheduleGraph();
		List<String> nodos = graph.getTransactions();
		List<String> oneExec = new ArrayList<String>();
		Iterator iter = nodos.iterator();
		
		for (; iter.hasNext();) {
			String tnx = (String) iter.next();
			if(!graph.isDependence(tnx)){
				nodos.remove(tnx);
				iter = nodos.iterator();
				oneExec.add(tnx);
				graph.removeTransaction(tnx);
			}
		}
		if(nodos.size() == 0){
			possibleExcecutions.add(oneExec);
		}
		
		return nodos.size() > 0; //Si quedó algun nodo en la lista significa que algunos de ellos o todos forman un ciclo
	}*/
	
	/** Muestra el grafo, solo para Debug */
	public void showGraph(){
		//System.out.println(buildScheduleGraph().toString());
		try {
			buildScheduleGraph().mostrar();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//[start] analyzeRecoverability
	public RecoverabilityResult analyzeRecoverability()
	{
		//TODO: Completar Recuperabilidad - Todos
		//La idea es que usen los mtodos reads, writes & commits de cada accin para analizar recuperabilidad
		//Recuperable si: 
		//	Toda transaccin T hace COMMIT despus de que lo hayan hecho todas las transacciones que escribieron algo que T lee
		//Evita aborts en cascada si:
		//	Toda transaccin lee de tems escritos por transacciones que hicieron COMMIT
		//Escricto si:
		//	Toda transaccin lee y escribe tems escritos por transacciones que hicieron COMMIT
		
		//OBS (importante): Tener en cuenta que en algunos modelos una misma accin puede leer y escribir al mismo tiempo 
		
		return null;
	}
	//[end]
}
