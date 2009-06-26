package ubadb.tools.scheduleAnalyzer.ternaryLocking;

import java.util.Iterator;
import java.util.List;

import ubadb.tools.scheduleAnalyzer.common.Action;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.common.ScheduleArc;
import ubadb.tools.scheduleAnalyzer.common.ScheduleGraph;
import ubadb.tools.scheduleAnalyzer.common.ScheduleType;
import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;

public class TernaryLockingSchedule extends Schedule
{
	//[start] Add Actions
	public void addAction(Action action)
	{
		actions.add((TernaryLockingAction)action);
	}
	
	public void addRLock(String transaction, String item)
	{
		actions.add(new TernaryLockingAction(TernaryLockingActionType.RLOCK, transaction, item));
	}

	public void addWLock(String transaction, String item)
	{
		actions.add(new TernaryLockingAction(TernaryLockingActionType.WLOCK, transaction, item));
	}

	public void addUnlock(String transaction, String item)
	{
		actions.add(new TernaryLockingAction(TernaryLockingActionType.UNLOCK, transaction, item));
	}

	public void addCommit(String transaction)
	{
		actions.add(new TernaryLockingAction(TernaryLockingActionType.COMMIT, transaction));
	}
	public ScheduleType getType()
	{
		return ScheduleType.TERNARY_LOCKING; 
	}
	//[end]
	
	//[start] buildScheduleGraph
	@Override
	/**
	 * @author andres.melendez
	 * Construye el grafo para el esquema de TernaryLocking.
	 */
	public ScheduleGraph buildScheduleGraph()
	{
		//TODO: Completar Construir Grafo ternario - Andres
		//Se deben agregar arcos entre T1 -> T2 cuando:
		//- T1 hace Rlock de un tem A y T2 luego hace WLock de A
		//- T1 hace Wlock de un tem A y T2 luego hace RLock de A
		//- T1 hace Wlock de un tem A y T2 luego hace WLock de A
		//OBS1: No hay arcos entre RLocks sobre el mismo tem 
		//OBS2: No agregar arcos que se deducen por transitividad
		
		ScheduleGraph graph = new ScheduleGraph();
		
		// Se agregan las transactions al grafo
		for (Iterator iter = getTransactions().iterator(); iter.hasNext();){
			graph.addTransaction((String)iter.next());
		}
		
		//Se agregan las dependencias al grafo
		for (int i = 0; i < getActions().size(); i++) {
			addArcs(graph,i);
		}
				
		return graph;
	}
	//[end]

	/**
	 * @author andres.melendez
	 * A partir de la historia va agregando los arcos correspondientes al grafo.
	 * para el esquema de TernaryLocking.
	 */
	private void addArcs(ScheduleGraph graph, int indexArc)
	{
		TernaryLockingAction action1 = (TernaryLockingAction) getActions().get(indexArc);
		String item1 = action1.getItem();
		String T1 = action1.getTransaction();
		TernaryLockingAction action2;
		String item2;
		String T2;
		if(isRlock(action1)){
			for (int i = indexArc + 1; i < getActions().size(); i++){
				action2 = (TernaryLockingAction) getActions().get(i);
				item2 = action2.getItem();
				T2 = action2.getTransaction();

				// Si encuentro un write sobre el mismo item y de transacciones diferentes agrego un arco
				if(isWlock(action2) && item1.equals(item2) && !T1.equals(T2)){
					ScheduleArc arc = new ScheduleArc(T1,T2,indexArc,i);
					graph.addArc(arc);
				}
				
			}
		}else if(isWlock(action1)){
			for (int i = indexArc + 1; i < getActions().size(); i++){
				action2 = (TernaryLockingAction) getActions().get(i);
				T2 = action2.getTransaction();
				item2 = action2.getItem();
				
				// Si encuentro un action sobre el mismo item y de transacciones diferentes agrego un arco.
				if((isRlock(action2) || isWlock(action2)) && item1.equals(item2) && !T1.equals(T2)){
					ScheduleArc arc = new ScheduleArc(T1,T2,indexArc,i);
					graph.addArc(arc);
				}
			}
		}
	}
	
	private boolean isRlock(TernaryLockingAction action){
		return action.getType().equals(TernaryLockingActionType.RLOCK);
	}

	private boolean isWlock(TernaryLockingAction action){
		return action.getType().equals(TernaryLockingActionType.WLOCK);
	}
	
	
	//[start] analyzeLegality
	@Override
	/**
	 * @author pablo.fabrizio
	 * Analiza Legabilidad en el esquema de TernaryLocking.
	 */
	public LegalResult analyzeLegality()
	{
		LegalResult result;
		//TODO: Completar Analizar la legabilidad binario - por Fabrizio
		//Un schedule es legal cuando:
		//- Cada transaccin T posee como mximo un commit
		//- Si T hace RLOCK A o WLOCK A, luego debe hacer UNLOCK A
		result = aLoSumoUnCommit_y_esUltimo();
		if (!result.isLegal())
			return result;

		//- Si T hace UNLOCK A, antes debe haber hecho RLOCK A o WLOCK A
		//- Si T hace RLOCK A o WLOCK A, no puede volver a hacer RLOCK A o WLOCK A a menos que antes haya hecho UNLOCK A
		//- Si T hace RLOCK A, ninguna otra transaccin T' puede hacer WLOCK A hasta que T libere a A
		//- Si T hace WLOCK A, ninguna otra transaccin T' puede hacer RLOCK A o WLOCK A hasta que T libere a A
		result = analyzeReadWriteLockUnlockSeq();
		if (!result.isLegal())
			return result;

		//La historia es legal.
		return new LegalResult(true, "", "La historia es legal.");
	}
	//[end]
	
	//[start] analyzeLockUnlockSeq
	/**
	 * @author pablo.fabrizio
	 * Analiza Legabilidad en el esquema de TernaryLocking.
	 */
	private LegalResult analyzeReadWriteLockUnlockSeq()
	{
		//- Si T hace UNLOCK A, antes debe haber hecho RLOCK A o WLOCK A
		//- Si T hace RLOCK A o WLOCK A, no puede volver a hacer RLOCK A o WLOCK A a menos que antes haya hecho UNLOCK A
		//- Si T hace RLOCK A, ninguna otra transaccin T' puede hacer WLOCK A hasta que T libere a A
		//- Si T hace WLOCK A, ninguna otra transaccin T' puede hacer RLOCK A o WLOCK A hasta que T libere a A
		int indexUnlock;
		//Collection transaccionesValidas = new HashSet();
		List<Action> actions = getActions();
		for (int indexAction = 0; indexAction < actions.size(); indexAction++) {
			TernaryLockingAction action = (TernaryLockingAction) actions.get(indexAction);
			//Guarda en una collection las transacciones que comienzan con un lock para si hay un unlock la transaccion que haga el unlock tiene que estar en esta collection
			if (action.getType().equals(TernaryLockingActionType.RLOCK)  ||  action.getType().equals(TernaryLockingActionType.WLOCK))
			{
				indexUnlock = -1;
				//controla que exista un unlock sobre el item para la misma transaccion 
				for (int indexActionUnLock = (indexAction + 1); indexActionUnLock < actions.size(); indexActionUnLock++) {
					TernaryLockingAction actionUnlock = (TernaryLockingAction)actions.get(indexActionUnLock);
					//Solo controla los lock o unlock sobre el item que se hizo el lock.
					if (action.getItem().equals(actionUnlock.getItem()) )
					{
						//Caso esperado para que la transaccion sea legal.
						if (actionUnlock.getType().equals(TernaryLockingActionType.UNLOCK) && action.getTransaction().equals(actionUnlock.getTransaction()))
						{
							indexUnlock = indexActionUnLock;
							break;
						}
						//Un lock luego de un lock hace que la transaccion sea ilegal.
						if ((actionUnlock.getType().equals(TernaryLockingActionType.RLOCK)  ||  action.getType().equals(TernaryLockingActionType.WLOCK)) && action.getTransaction().equals(actionUnlock.getTransaction()))
						{
							return new LegalResult(false, action.getTransaction(), "la transaccion "+action.getTransaction()+" hace que la historia sea ilegal por no tener un lock luego del Lock sobre el item: " + action.getItem());
						}
						//Controla que otra transaccion no utilice el item antes que se haga el unlock.
						if (!actionUnlock.getTransaction().equals(actionUnlock.getTransaction()) && actionUnlock.getType().equals(TernaryLockingActionType.COMMIT))
						{
							return new LegalResult(false, action.getTransaction(), "la transaccion "+action.getTransaction()+" hace que la historia sea ilegal porque antes de hacer un ulock del item: " + action.getItem() + ", la transaccion " + actionUnlock.getTransaction() + " realizo una operacion sobre el mismo item.");
						}
					}
				}
				//en caso de encontrar el unlock lo remueve para no controlar nada sobre ese unlock que es valido
				if (indexUnlock != -1)
				{
					actions.remove(indexUnlock);
				}
				//Si no se encontro un unlock se devuelve que la transaccion no es legal.
				else
				{
					return new LegalResult(false, action.getTransaction(), "la transaccion "+action.getTransaction()+" hace que la historia sea ilegal por no tener un Unlock del Lock sobre el item: " + action.getItem());
				}
				
			}
			//Verifica que dado un unlock antes tenga un lock
			if (action.getType() == TernaryLockingActionType.UNLOCK)
			{
				return new LegalResult(false, action.getTransaction(), "la transaccion "+action.getTransaction()+" hace que la historia sea ilegal por hacer un unlock sin tener previamente un lock.");
			}
		}
			
		return new LegalResult(true, "", "La historia es legal.");
	}
	//[end]

	
	
}
