package ubadb.tools.scheduleAnalyzer.binaryLocking;

import java.util.Iterator;
import java.util.List;

import ubadb.tools.scheduleAnalyzer.common.Action;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.common.ScheduleArc;
import ubadb.tools.scheduleAnalyzer.common.ScheduleGraph;
import ubadb.tools.scheduleAnalyzer.common.ScheduleType;
import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;

public class BinaryLockingSchedule extends Schedule
{
	//[start] Add Actions
	public void addAction(Action action)
	{
		actions.add((BinaryLockingAction)action);
	}
	
	public void addLock(String transaction, String item)
	{
		actions.add(new BinaryLockingAction(BinaryLockingActionType.LOCK, transaction, item));
	}

	public void addUnlock(String transaction, String item)
	{
		actions.add(new BinaryLockingAction(BinaryLockingActionType.UNLOCK, transaction, item));
	}

	public void addCommit(String transaction)
	{
		actions.add(new BinaryLockingAction(BinaryLockingActionType.COMMIT, transaction));
	}
	public ScheduleType getType()
	{
		return ScheduleType.BINARY_LOCKING; 
	}
	//[end]
	
	//[start] buildScheduleGraph
/*	@Override
	public ScheduleGraph buildScheduleGraph()
	{
		//Se deben agregar arcos entre T1 -> T2 cuando:
		//- T1 hace lock de un tem A y T2 hace lock de A (mcammi: aca creo que debe ser T1 hace UNLOCK y T2 hace LOCK.)
		//OBS: No agregar arcos que se deducen por transitividad

		List<Action> unlocks = new ArrayList<Action>();
		for (Iterator iter = unlocks.iterator(); iter.hasNext();) { //recorro la lista de acciones
			BinaryLockingAction BLAction = (BinaryLockingAction) iter.next();
			
			if (BinaryLockingActionType.UNLOCK.equals(BLAction.getType())){
				unlocks.add(BLAction); // Si es un unlock lo guardo
			}
			
			if (BinaryLockingActionType.LOCK.equals(BLAction.getType())){
				unlocks.add(BLAction); // verifico si hubo un Unlock previo
			}
		}
		

		
		return null;
	}
	//[end]
*/
	//[start] buildScheduleGraph
	@Override
	public ScheduleGraph buildScheduleGraph()
	{
		//Completar Armar Grafo binario - por Matias
		//Se deben agregar arcos entre T1 -> T2 cuando:
		//- T1 hace lock de un tem A y T2 hace lock de A 
		//(mcammi: aca creo que debe ser T1 hace UNLOCK y T2 hace LOCK.)
		//(mperez: eso se ve en legalidad (Que si hace un UNLOCK antes hizo un LOCK))
		//OBS: No agregar arcos que se deducen por transitividad
	
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

	private void addArcs(ScheduleGraph graph, int indexArc)
	{
		BinaryLockingAction action1 = (BinaryLockingAction) getActions().get(indexArc);
		String item1 = action1.getItem();
		String T1 = action1.getTransaction();

		// Me fijo que sea un LOCK
		if(action1.getType().equals(BinaryLockingActionType.LOCK))
		{
			BinaryLockingAction action2;
			String item2;
			String T2;
			int n = getActions().size();
			int i = indexArc + 1;
			while( i < n )
			{
				action2 = (BinaryLockingAction) getActions().get(i);
				item2 = action2.getItem();
				T2 = action2.getTransaction();
				// Si es la misma Transaccion me fijo si hace un Lock sobre el mismo elemento, en ese caso
				// no pongo mÃ¡s arcos ya que los pongo despuÃ©s
				// (Aunque por como estÃ¡ hecho el addArc lo podrÃ­a poner igual)
				if(T1 == T2)
				{
					if(item1 == item2 && action2.getType().equals(BinaryLockingActionType.LOCK))
						break;	
				}
				else //Son Transacciones distintas
				{
					// Pongo un arco sÃ³lo si la accion es un Lock sobre el mismo item
					if (item1 == item2 && action2.getType().equals(BinaryLockingActionType.LOCK))
					{
						ScheduleArc arc = new ScheduleArc(T1,T2,indexArc,i);
						graph.addArc(arc);
						// Y paro de agregar arcos por la optimizacion que piden
						break;
					}
				}
				i++;
			}
		}
		// Si NO soy un Lock no hago nada
	}

	//[start] analyzeLegality
	@Override
	public LegalResult analyzeLegality()
	{
		LegalResult result;
		//TODO: Completar Analizar la legabilidad binario - por Fabrizio
		//Un schedule es legal cuando:
		//- Cada transaccin T posee como mximo un commit
		//- Si T tiene COMMIT, éste es el último paso de la transacción.
		result = aLoSumoUnCommit_y_esUltimo();
		if (!result.isLegal())
			return result;

		//- Si T hace LOCK A, luego debe hacer UNLOCK A
		//- Si T hace UNLOCK A, antes debe haber hecho LOCK A
		//- Si T hace LOCK A, no puede volver a hacer LOCK A a menos que antes haya hecho UNLOCK A
		//- Si T hace LOCK A, ninguna otra transaccin T' puede hacer LOCK A hasta que T libere a A
		result = analyzeLockUnlockSeq();
		if (!result.isLegal())
			return result;

		//La historia es legal.
		return new LegalResult(true, "", "La historia es legal.");

	}
	//[end]
	
	//[start] analyzeLockUnlockSeq
	private LegalResult analyzeLockUnlockSeq()
	{
		//- Si T hace LOCK A, luego debe hacer UNLOCK A
		//- Si T hace UNLOCK A, antes debe haber hecho LOCK A
		//- Si T hace LOCK A, no puede volver a hacer LOCK A a menos que antes haya hecho UNLOCK A
		//- Si T hace LOCK A, ninguna otra transacción T' puede hacer LOCK A hasta que T libere a A
		int indexUnlock;
		//Collection transaccionesValidas = new HashSet();
		List<Action> actions = getActions();
		for (int indexAction = 0; indexAction < actions.size(); indexAction++) {
			BinaryLockingAction action = (BinaryLockingAction) actions.get(indexAction);
			//Guarda en una collection las transacciones que comienzan con un lock para si hay un unlock la transaccion que haga el unlock tiene que estar en esta collection
			if (action.getType().equals(BinaryLockingActionType.LOCK))
			{
				indexUnlock = -1;
				//controla que exista un unlock sobre el item para la misma transaccion 
				for (int indexActionUnLock = (indexAction + 1); indexActionUnLock < actions.size(); indexActionUnLock++) {
					BinaryLockingAction actionUnlock = (BinaryLockingAction)actions.get(indexActionUnLock);
					//Solo controla los lock o unlock sobre el item que se hizo el lock.
					if (action.getItem().equals(actionUnlock.getItem()))
					{
						//Caso esperado para que la transaccion sea legal.
						if (actionUnlock.getType().equals(BinaryLockingActionType.UNLOCK) && action.getTransaction().equals(actionUnlock.getTransaction()) )
						{
							indexUnlock = indexActionUnLock;
							break;
						}
						//Un lock luego de un lock hace que la transaccion sea ilegal.
						if (actionUnlock.getType().equals(BinaryLockingActionType.LOCK) && action.getTransaction().equals(actionUnlock.getTransaction()) )
						{
							return new LegalResult(false, action.getTransaction(), "la transaccion "+action.getTransaction()+" hace que la historia sea ilegal por tener un lock luego del Lock sobre el item: " + action.getItem());
						}
						//Controla que otra transaccion no utilice el item antes que se haga el unlock.
						if (!action.getTransaction().equals(actionUnlock.getTransaction()))
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
			if (action.getType() == BinaryLockingActionType.UNLOCK)
			{
				return new LegalResult(false, action.getTransaction(), "la transaccion "+action.getTransaction()+" hace que la historia sea ilegal por hacer un unlock sin tener previamente un lock.");
			}
		}
			
		return new LegalResult(true, "", "La historia es legal.");
	}
	//[end]

}
