package ubadb.tools.scheduleAnalyzer.nonLocking;

import ubadb.tools.scheduleAnalyzer.common.Action;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.common.ScheduleGraph;
import ubadb.tools.scheduleAnalyzer.common.ScheduleType;
import ubadb.tools.scheduleAnalyzer.common.results.LegalResult;

public class NonLockingSchedule extends Schedule
{
	//[start] Add Actions
	public void addAction(Action action)
	{
		actions.add((NonLockingAction)action);
	}
	
	public void addRead(String transaction, String item)
	{
		actions.add(new NonLockingAction(NonLockingActionType.READ, transaction, item));
	}

	public void addWrite(String transaction, String item)
	{
		actions.add(new NonLockingAction(NonLockingActionType.WRITE, transaction, item));
	}

	public void addCommit(String transaction)
	{
		actions.add(new NonLockingAction(NonLockingActionType.COMMIT, transaction));
	}
	public ScheduleType getType()
	{
		return ScheduleType.NON_LOCKING; 
	}
	//[end]
	
	//[start] buildScheduleGraph
	@Override
	public ScheduleGraph buildScheduleGraph()
	{
		//TODO: Completar
		//Se deben agregar arcos entre T1 -> T2 cuando:
		//- T1 lee un ítem A y T2 luego escribe A
		//- T1 escribe un ítem A y T2 luego lee A
		//- T1 escribe un ítem A y T2 luego escribe A
		//OBS: No agregar arcos que se deducen por transitividad
		
		return null;
	}
	//[end]

	//[start] analyzeLegality
	@Override
	public LegalResult analyzeLegality()
	{
		//TODO: Completar
		//Un schedule es legal cuando:
		//- Cada transacción T posee como máximo un commit
		return null;
	}
	//[end]
}
