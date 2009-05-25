package ubadb.tools.scheduleAnalyzer.ternaryLocking;

import ubadb.tools.scheduleAnalyzer.common.Action;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
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
	public ScheduleGraph buildScheduleGraph()
	{
		//TODO: Completar Construir Grafo ternario - Andres
		//Se deben agregar arcos entre T1 -> T2 cuando:
		//- T1 hace Rlock de un ítem A y T2 luego hace WLock de A
		//- T1 hace Wlock de un ítem A y T2 luego hace RLock de A
		//- T1 hace Wlock de un ítem A y T2 luego hace WLock de A
		//OBS1: No hay arcos entre RLocks sobre el mismo ítem 
		//OBS2: No agregar arcos que se deducen por transitividad
		
		return null;
	}
	//[end]

	//[start] analyzeLegality
	@Override
	public LegalResult analyzeLegality()
	{
		//TODO: Completar Legabilidad Ternaria - Fabrizio
		//Un schedule es legal cuando:
		//- Cada transacción T posee como máximo un commit
		//- Si T hace RLOCK A o WLOCK A, luego debe hacer UNLOCK A
		//- Si T hace UNLOCK A, antes debe haber hecho RLOCK A o WLOCK A
		//- Si T hace RLOCK A o WLOCK A, no puede volver a hacer RLOCK A o WLOCK A a menos que antes haya hecho UNLOCK A
		//- Si T hace RLOCK A, ninguna otra transacción T' puede hacer WLOCK A hasta que T libere a A
		//- Si T hace WLOCK A, ninguna otra transacción T' puede hacer RLOCK A o WLOCK A hasta que T libere a A
		return null;
	}
	//[end]
}
