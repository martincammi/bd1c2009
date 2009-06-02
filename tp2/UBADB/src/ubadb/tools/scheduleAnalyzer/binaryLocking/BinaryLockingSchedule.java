package ubadb.tools.scheduleAnalyzer.binaryLocking;

import java.util.Iterator;

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
		//TODO: TERMINADO Aunque me falta compilarlo :-P
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
				// no pongo más arcos ya que los pongo después
				// (Aunque por como está hecho el addArc lo podría poner igual)
				if(T1 == T2)
				{
					if(item1 == item2 && action2.getType().equals(BinaryLockingActionType.LOCK))
						break;	
				}
				else //Son Transacciones distintas
				{
					// Pongo un arco sólo si la accion es un Lock sobre el mismo item
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
		//TODO: Completar Analizar la legabilidad binario - por Fabrizio
		//Un schedule es legal cuando:
		//- Cada transaccin T posee como mximo un commit
		//- Si T hace LOCK A, luego debe hacer UNLOCK A
		//- Si T hace UNLOCK A, antes debe haber hecho LOCK A
		//- Si T hace LOCK A, no puede volver a hacer LOCK A a menos que antes haya hecho UNLOCK A
		//- Si T hace LOCK A, ninguna otra transaccin T' puede hacer LOCK A hasta que T libere a A
		return null;
	}
	//[end]
}
