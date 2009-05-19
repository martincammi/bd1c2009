package ubadb.dbserver;

import java.util.LinkedHashMap;
import java.util.Map;

import ubadb.components.DBComponent;
import ubadb.components.DBComponentsEnum;
import ubadb.components.properties.DBProperties;
import ubadb.services.DBService;
import ubadb.services.DBServicesEnum;
import ubadb.services.recoveryManager.RecoveryManager;

/**
 * Crea por primera vez los componentes y servicios del servidor
 */
public class DBFactory
{
	//[start] getComponents
	public static Map<DBComponentsEnum, DBComponent> getComponents()
	{
		//Notar que usamos la implementaci�n "LinkedHashMap" de "Map" porque es importante el orden de iteraci�n (algunos componentes tienen que inicializarse antes que otros)
		Map<DBComponentsEnum,DBComponent> map = new LinkedHashMap<DBComponentsEnum, DBComponent>();
		
		//Todos los componentes deben estar aqu� (la inclusi�n o no de cada uno NO es configurable)
		
		//Properties
		map.put(DBComponentsEnum.PROPERTIES, new DBProperties());
		
		//Futuros componentes... (van ac�)
		
		return map;
	}
	//[end]

	//[start] getServices
	public static Map<DBServicesEnum, DBService> getServices()
	{
		//Notar que usamos la implementaci�n "LinkedHashMap" de "Map" porque es importante el orden de iteraci�n (algunos servicios tienen que inicializarse antes que otros)
		Map<DBServicesEnum,DBService> map = new LinkedHashMap<DBServicesEnum, DBService>();
		
		//La inclusi�n de los servicios es configurable
		
		//Recovery Manager
		map.put(DBServicesEnum.RECOVERY_MANAGER, new RecoveryManager());
		
		//Futuros servicios... (van ac�)
		
		return map;
	}
	//[end]

}
