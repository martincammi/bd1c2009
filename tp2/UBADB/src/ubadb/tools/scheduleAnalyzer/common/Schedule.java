package ubadb.tools.scheduleAnalyzer.common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.Set;
import java.util.TreeSet;


import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;
import ubadb.tools.scheduleAnalyzer.common.results.RecoverabilityResult;
import ubadb.tools.scheduleAnalyzer.common.results.RecoverabilityType;
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

	public LegalResult aLoSumoUnCommit_y_esUltimo()
	{
		//- Cada transacción T posee como máximo un commit
		//- Si T tiene COMMIT, éste es el último paso de la transacción.
		Collection transaccionesValidas = new HashSet();
		List<Action> actions = getActions();
		for (int indexAction = 0; indexAction < actions.size(); indexAction++) {
			Action action = actions.get(indexAction);
			if(action.commits())
			{
				//Controla que no haya mas de un commit
				if(transaccionesValidas.contains(action.getTransaction()))
				{
					return new LegalResult(false, action.getTransaction(), "la transaccion "+action.getTransaction()+" hace que la historia sea ilegal por poseer dos commits.");
				}
				//Controla que luego de un commit no haya otra operacion para la misma transaccion.
				else if (tieneOtraOperacion(indexAction + 1, action.getTransaction()))
				{
					return new LegalResult(false, action.getTransaction(), "la transaccion "+action.getTransaction()+" hace que la historia sea ilegal por hacer un lock o unlock luego de haber realizado un commit.");
				}
				else
				{
					transaccionesValidas.add(action.getTransaction());
				}
			}
			
		}
		
		return new LegalResult(true, "", "La historia es legal.");
	}
	
	
	public abstract ScheduleGraph buildScheduleGraph();
	//[end]

	//[start] analyzeSeriality
	public SerialResult analyzeSeriality()
	{
		//TODO: Casos de Test con BinaryLocking y TernaryLocking Martï¿½n Cammi
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
	/**
	 * @author martin.cammi
	 * Analiza la seriabilidad del Grafo de precedencia.
	 * @return Devuelve un objeto SerializabilityResult que indica si:
	 *  - es Serializable
	 *  - si lo es -> muestra las posibles ejecuciones
	 *  - si no lo es -> muestra un ciclo
	 *  - muestra un mensaje.
	 */
	public SerializabilityResult analyzeSerializability()
	{	
		//TERMINADO: Completar Es Serializable por Martin Cammi
		//Usar el grafo para determinar si es o no serializable
		boolean isSerializable = true;
		ScheduleGraph graph = buildScheduleGraph();
		List<List<String>> possibleExcecution = new ArrayList<List<String>>();
		List<String> cycle = new ArrayList<String>();
		String message = "La historia es serializable.";
		isSerializable = !hasCycles(possibleExcecution, graph);
		if (!isSerializable) //Se buscan los ciclos
		{
			getCycle(cycle, graph);
			message = "La historia no es serializable.";
		}
		SerializabilityResult result = new SerializabilityResult(isSerializable, possibleExcecution, cycle, message );
		return result;
	}
	//[end]

	/**
	 * @author martin.cammi
	 * Indica si el grafo tiene ciclos.
	 * @param possibleExcecutions lista de salida de posibles ejecuciones.
	 * @param grafo de precedencia
	 */
	private boolean hasCycles(List<List<String>> possibleExcecutions, ScheduleGraph graph)
	{
		//Inicializaciï¿½n
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
		
	/**
	 * @author martin.cammi
	 * Obtiene las posibles ejecuciones serializables si las hay. (recursivamente)
	 * @param possibleExcecutions lista de salida de posibles ejecuciones.
	 * @param nodos lista de transacciones que se van iterando.
	 * @param oneExecution mantiene la ejecuciï¿½n parcial que se va obteniendo.
	 * @param grafo de precedencia
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
	
	/**
	 * @author martin.cammi
	 * Devuelve los ciclos de un grafo.
	 * Precondiciï¿½n: el grafo no debe ser serializable.
	 * @param cycle lista para devolver el ciclo encontrado.
	 * @param graph grafo de precedencia de donde obtiene el ciclo.
	 */
	private void getCycle(List<String> cycle, ScheduleGraph graph)
	{
		//Inicializaciï¿½n
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
		
		//Todos los nodos estï¿½n deshabilitados y no se pide shutdown.
		if(!isEmpty(nodos) && !shutdownAlgorithm){
			for (int i = 0; i < nodos.size(); i++)
			{
				String tnx = (String) nodos.get(i).getFirst();
				
				//Si la tnx estï¿½ habilitada.
				if(nodos.get(i).getSecond())
				{
					//Si es una dependencia de otro es posible que forme parte de un ciclo.
					if(graph.isDependence(tnx))
					{
						if(cycle.contains(tnx)){ //El nodo ya estaba contenido, se formo un ciclo
							shutdownAlgorithm = true;
						}else{
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
		boolean algunoEsTrue = false;
		for (Par<String, Boolean> par : lista) {
			algunoEsTrue = algunoEsTrue || par.getSecond();
		}
		return !algunoEsTrue; //Si ninguno es true -> estï¿½ vacia.
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
		
		return nodos.size() > 0; //Si quedï¿½ algun nodo en la lista significa que algunos de ellos o todos forman un ciclo
	}*/
	
	/** Muestra el grafo, solo para Debug */
	public void showGraph(){
		//System.out.println(buildScheduleGraph().toString());
		try {
			buildScheduleGraph().mostrar();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	//[start] analyzeRecoverability
	public RecoverabilityResult analyzeRecoverability()
	{
		//TODO: Casos de Testing faltan - Recuperabilidad - Pasa 3 que hay
		//La idea es que usen los mtodos reads, writes & commits de cada accin para analizar recuperabilidad
		//Recuperable si: 
		//	Toda transaccin T hace COMMIT despus de que lo hayan hecho todas las transacciones que escribieron algo que T lee
		//Evita aborts en cascada si:
		//	Toda transaccin lee de tems escritos por transacciones que hicieron COMMIT
		//Escricto si:
		//	Toda transaccin lee y escribe tems escritos por transacciones que hicieron COMMIT
		
		//OBS (importante): Tener en cuenta que en algunos modelos una misma accin puede leer y escribir al mismo tiempo 
		
		Dictionary<String, Set<String> > escritoPor = new Hashtable(); // item - transaccion
		Dictionary<String, Set<String> > leeDe = new Hashtable(); // transacion - lista de transacciones
		Set<String> comiteadas = new TreeSet<String>();

		boolean estricto = true;
		boolean aca = true;
		boolean recuperable = true;
		
		RecoverabilityResult res = new RecoverabilityResult(RecoverabilityType.STRICT,null,null,"La historia es Estricta" );
		
		for (Iterator iterator = actions.iterator(); iterator.hasNext();)
		{
			Action act = (Action) iterator.next();
			/* Action tiene:
			 * public boolean reads();
			 * public boolean writes();
			 * public boolean commits();
			 * public String getTransaction();
			 * public String getItem();
			 */
			String item = act.getItem();
			String transaction = act.getTransaction();
			
			if (act.reads())
			{
				// Si leo un item que alguien escribiÃ³
				if (escritoPor.get(item) != null) 
				{
					boolean tjsComiteadas = comiteadas.containsAll(escritoPor.get(item));
					
					if (aca)
					{
						// Me fijo que cumpla con ACA
						// transaction lee el item de escritoPor.get(item) 
						aca = aca && tjsComiteadas;
						if ( !aca)
						{
							escritoPor.get(item).removeAll(comiteadas);
							String t1 = (String) escritoPor.get(item).iterator().next();
							String t2 = transaction;
							RecoverabilityType type = RecoverabilityType.RECOVERABLE;
							
							res = new RecoverabilityResult(type, t1, t2, "La transaccion "+t2+" lee el item "+item+" de "+t1+" antes de que "+t1+" realize un commit" ) ;
						}
						// Lo pongo adentro del if porque si no es ACA ni vale la pena ver si es estricto
						// Esto es para consistencia, porque
						// el caso del read rompe Estricto sii rompe ACA
						estricto = estricto && tjsComiteadas;	
						
					}
			
					// Lo agrego al diccionario de leeDe
					
					// Defino la entrada en el dic si no estaba
					if ( leeDe.get(transaction) == null )
					{ 
						Set<String> transacs = new TreeSet<String>();
						leeDe.put(transaction, transacs);
					}
					// Agrego a la transaccion que escribiÃ³ el item a la
					// lista de la transacciones que leyeron el item
					leeDe.get(transaction).addAll(escritoPor.get(item));
					
				}
				// Si leo un dato que nadie escribiÃ³ no pasa nada

			}
			//Tengo que hacer primero el reads porque si es un lock, se toma como que escribe 
			// y lee, y si primero hago la escritura piso el que escibiÃ³ antes y no sÃ© de quien leo
			if (act.writes())
			{
				//Me fijo que no rompa con Estricto (Si escribo un item ya escrito por Tj => Tj ya commiteo)
				if (estricto && (escritoPor.get(item) != null) ) 
				{
					// transaction escribe el item previamente escrito por escritoPor.get(item) 
					// Si escritoPor.get(item) no hizo commit, entonces no cumple Estricto
					boolean tjsComiteadas = comiteadas.containsAll( escritoPor.get(item) );
					estricto = estricto && tjsComiteadas;
					
					if (!estricto)
					{
						escritoPor.get(item).removeAll(comiteadas);
						String t1 = (String) escritoPor.get(item).iterator().next();
						String t2 = transaction;
						RecoverabilityType type = RecoverabilityType.AVOIDS_CASCADING_ABORTS;
						
						res = new RecoverabilityResult(type, t1, t2, "La transaccion "+t2+" escribe el item "+item+" previamente escrito por "+t1+" antes de que la misma realize un commit" );
					}					
				}
				
				// Lo agrego al diccionario escritoPor
				
				if ( escritoPor.get(item) == null )
				{ 
					Set<String> transacs = new TreeSet<String>();
					escritoPor.put(item, transacs);
				}
				// Agrego a la transaccion que escribiÃ³ el item a escritoPor
				escritoPor.get(item).add(transaction);
			}
			if (act.commits())
			{
				// Me fijo que no rompa con Recuperabilidad
				// Todas de las que leo estÃ¡n commiteadas
				boolean tjsComiteadas = true;
				if (leeDe.get(transaction) != null) //Si no lee de nadie lo dejo true
					tjsComiteadas = comiteadas.containsAll(leeDe.get(transaction));
										
				recuperable = recuperable && tjsComiteadas;
				
				if (!recuperable)
				{
					leeDe.get(transaction).removeAll(comiteadas);
					String t1 = (String) leeDe.get(transaction).iterator().next();
					String t2 = transaction;
					RecoverabilityType type = RecoverabilityType.NON_RECOVERABLE;
					
					res = new RecoverabilityResult(type, t1, t2, "La transaccion "+t2+" lee de "+t1+" y hace commit antes." );
					break;
				}
				// Y agrego la transaccion a la lista de comiteadas.
				comiteadas.add(transaction);
			}
			
		}
		
		return res;
	}
	//[end]
}
