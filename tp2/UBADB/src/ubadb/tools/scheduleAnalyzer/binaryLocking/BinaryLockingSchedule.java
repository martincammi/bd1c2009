package ubadb.tools.scheduleAnalyzer.binaryLocking;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ubadb.tools.scheduleAnalyzer.common.Action;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
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
	@Override
	public ScheduleGraph buildScheduleGraph()
	{
		//TODO: Completar Armar Grafo binario - por Matias
		//Se deben agregar arcos entre T1 -> T2 cuando:
		//- T1 hace lock de un ítem A y T2 hace lock de A (mcammi: aca creo que debe ser T1 hace UNLOCK y T2 hace LOCK.)
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

	//[start] analyzeLegality
	@Override
	public LegalResult analyzeLegality()
	{
		//TODO: Completar Analizar la legabilidad binario - por Fabrizio
		//Un schedule es legal cuando:
		//- Cada transacción T posee como máximo un commit
		//- Si T hace LOCK A, luego debe hacer UNLOCK A
		//- Si T hace UNLOCK A, antes debe haber hecho LOCK A
		//- Si T hace LOCK A, no puede volver a hacer LOCK A a menos que antes haya hecho UNLOCK A
		//- Si T hace LOCK A, ninguna otra transacción T' puede hacer LOCK A hasta que T libere a A
		return null;
	}
	//[end]
}
