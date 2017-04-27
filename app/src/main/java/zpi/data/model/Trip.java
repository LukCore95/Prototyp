package zpi.data.model;

import java.util.LinkedList;
import java.util.Map;

/**
 * @author Wojciech Michałowski
 * Representation of user trip.
 */
public class Trip {

	private Route route;
	private ControlPoint startPoint;
	private ControlPoint lastVisitedPoint;
	private Point currentTarget;
	private int ID;

	public Trip(Route route, ControlPoint startPoint, int ID) throws DataException {
		setRoute(route);
		setStartPoint(startPoint);
		lastVisitedPoint = null;
		setCurrentTarget(this.startPoint);
		this.ID = ID;
	}

	public Trip(Route route, ControlPoint startPoint, ControlPoint lastVisited, int ID) throws DataException {
		LinkedList<ControlPoint> points = route.getRoutePoints();
		setRoute(route);
		setStartPoint(startPoint);
		lastVisitedPoint = lastVisited;
		setCurrentTarget(points.get(points.indexOf(lastVisitedPoint)+1));
		this.ID = ID;
	}

	public int getID() {
		return this.ID;
	}

	public void setCurrentTarget(Point currentTarget) throws DataException{
		if(currentTarget != null)
			this.currentTarget = currentTarget;
		else throw new DataException("currentTarget", "not null");
	}

	public void setLastVisitedPoint(ControlPoint lastVisitedPoint){
		this.lastVisitedPoint = lastVisitedPoint;
	}

	private void setRoute(Route route) throws DataException{
		if(route != null)
			this.route = route;
		else throw new DataException("route", "not null");
	}

	private void setStartPoint(ControlPoint startPoint) throws DataException{
		if(startPoint != null)
			this.startPoint = startPoint;
		else throw new DataException("startPoint", "not null");
	}

	public Route getRoute(){
		return route;
	}

	public ControlPoint getStartPoint(){
		return startPoint;
	}

	public ControlPoint getLastVisitedPoint(){
		return lastVisitedPoint;
	}

	public Point getCurrentTarget(){
		return currentTarget;
	}
}