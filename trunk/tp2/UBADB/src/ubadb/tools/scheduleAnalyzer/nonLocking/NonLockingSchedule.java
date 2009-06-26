package ubadb.tools.scheduleAnalyzer.nonLocking;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import ubadb.tools.scheduleAnalyzer.common.Action;
import ubadb.tools.scheduleAnalyzer.common.Schedule;
import ubadb.tools.scheduleAnalyzer.common.ScheduleArc;
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
		//TERMINADO: Completar
		//Se deben agregar arcos entre T1 -> T2 cuando:
		//- T1 lee un ítem A y T2 luego escribe A
		//- T1 escribe un ítem A y T2 luego lee A
		//- T1 escribe un ítem A y T2 luego escribe A
		//OBS: No agregar arcos que se deducen por transitividad
		
		ScheduleGraph graph = new ScheduleGraph();
		
		// Se agregan las transactions al graph
		for (Iterator iter = getTransactions().iterator(); iter.hasNext();) 
		{
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
	 * @author Grupo4 (todos)
	 * A partir de la historia va agregando los arcos correspondientes al grafo
	 * para el esquema de NonLocking.
	 */
	private void addArcs(ScheduleGraph graph, int indexArc)
	{
		NonLockingAction actionAux = (NonLockingAction) getActions().get(indexArc);
		String item1 = actionAux.getItem();
		String T1 = actionAux.getTransaction();
		if(actionAux.reads())
		{
			for (int i = indexArc + 1; i < getActions().size(); i++) 
			{
				NonLockingAction action = (NonLockingAction) getActions().get(i);
				String T2 = action.getTransaction();
				String item2 = action.getItem();
				if(action.writes() && item1.equals(item2) && !T1.equals(T2)) // Si encuentro un write sobre el mismo item y de transacciones diferentes agrego un arco
				{
					ScheduleArc arc = new ScheduleArc(T1,T2,indexArc,i);
					graph.addArc(arc);
					break;
				}
				
			}
		}else if(actionAux.writes())
		{
			for (int i = indexArc + 1; i < getActions().size(); i++) 
			{
				NonLockingAction action = (NonLockingAction) getActions().get(i);
				String T2 = action.getTransaction();
				String item2 = action.getItem();
				if(item1.equals(item2) && !T1.equals(T2)) // Si encuentro un action sobre el mismo item y de transacciones diferentes agrego un arco.
				{
					ScheduleArc arc = new ScheduleArc(T1,T2,indexArc,i);
					graph.addArc(arc);
					if(action.writes()) //Si el action es un write termino de buscar
						break;
				}
				
			}
		}
	}
	
	//TERMINADO
	//[start] analyzeLegality
	@Override
	/**
	 * @author pablo.fabrizio
	 * Analiza Legabilidad en el esquema de NonLocking.
	 */
	public LegalResult analyzeLegality()
	{
		//TODO: TERMINADO
		return aLoSumoUnCommit_y_esUltimo();
	}
	//[end]
}
