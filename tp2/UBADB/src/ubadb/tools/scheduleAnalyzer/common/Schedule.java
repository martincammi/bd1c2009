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
	
	//[start] addTransaction
	public void addTransaction(String transaction) throws ScheduleException
	{
		if(getTransactions().contains(transaction))
			throw new ScheduleException("Ya existe una transacción con ese nombre");
		else
			getTransactions().add(transaction);
	}
	//[end]

	//[start] addItem
	public void addItem(String item) throws ScheduleException
	{
		if(items.contains(item))
			throw new ScheduleException("Ya existe una ítem con ese nombre");
		else
			items.add(item);
	}
	//[end]
	
	//[start] actions
	public void editAction(int actionNumber, Action newAction) throws ScheduleException
	{
		if(actionNumber < 0 || actionNumber >= actions.size())
			throw new ScheduleException("La posición a modificar es inválida");
		
		actions.set(actionNumber, newAction);
	}
	
	public void removeAction(int index) throws ScheduleException
	{
		if(index < 0 || index >= actions.size())
			throw new ScheduleException("La posición a remover es inválida");
		
		actions.remove(index);
	}

	public void swapAction(int indexA, int indexB) throws ScheduleException
	{
		if(indexA < 0 || indexB < 0  || indexA >= actions.size() || indexB >= actions.size())
			throw new ScheduleException("Las posiciones a swapear son inválidas");
		
		Collections.swap(actions, indexA, indexB);
	}
	//[end]

	//[start] Métodos Abstractos
	public abstract LegalResult analyzeLegality();
	public abstract ScheduleGraph buildScheduleGraph();
	//[end]

	//[start] analyzeSeriality
	public SerialResult analyzeSeriality()
	{
		//TERMINADO: Completar
		//Un schedule es serial si para toda transacción, todas sus acciones aparecen consecutivas dentro del schedule
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
		ScheduleGraph graph = buildScheduleGraph();
		
		//TODO: Completar Es Serializable por Martin Cammi
		//Usar el grafo para determinar si es o no serializable
		boolean isSerializable = true;
		List<List<Action>> possibleExcecution = new ArrayList<List<Action>>();
		List<String> cycle = new ArrayList<String>();
		String message = "";
		isSerializable = !tieneCiclos( graph, cycle );
		if (isSerializable)
		{
			//possibleExcecution = ;
		}
		SerializabilityResult result = new SerializabilityResult(isSerializable, possibleExcecution, cycle, message );
		return result;
	}
	//[end]

	private boolean tieneCiclos(ScheduleGraph graph, List<String> cycle)
	{
		return true;
	}
	
	//[start] analyzeRecoverability
	public RecoverabilityResult analyzeRecoverability()
	{
		//TODO: Completar Recuperabilidad - Todos
		//La idea es que usen los métodos reads, writes & commits de cada acción para analizar recuperabilidad
		//Recuperable si: 
		//	Toda transacción T hace COMMIT después de que lo hayan hecho todas las transacciones que escribieron algo que T lee
		//Evita aborts en cascada si:
		//	Toda transacción lee de ítems escritos por transacciones que hicieron COMMIT
		//Escricto si:
		//	Toda transacción lee y escribe ítems escritos por transacciones que hicieron COMMIT
		
		//OBS (importante): Tener en cuenta que en algunos modelos una misma acción puede leer y escribir al mismo tiempo 
		
		return null;
	}
	//[end]
}
