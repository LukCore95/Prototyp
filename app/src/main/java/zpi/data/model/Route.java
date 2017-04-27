package zpi.data.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Wojciech Michałowski
 * Representation of a route.
 */
public class Route {

	private LinkedList<ControlPoint> routePoints;
	private String name;

	public Route(String name) throws DataException {
		setName(name);
		routePoints = new LinkedList<ControlPoint>();
	}

	public Route(String name, LinkedList<ControlPoint> routePoints) throws DataException {
		setName(name);
		this.routePoints = routePoints;
	}

	public String getName() {
		return this.name;
	}

	private void setName(String name) throws DataException {
		if(name != null)
			this.name = name;
		else
			throw new DataException("name", "not null");
	}

	public LinkedList<ControlPoint> getRoutePoints(){
		return routePoints;
	}

	public void setRoutePoints(LinkedList<ControlPoint> cps){
		this.routePoints = cps;
	}

}