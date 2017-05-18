package zpi.data.model;

import java.util.LinkedList;
import java.util.List;
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
	private List<ControlPoint> modifiedRoute = new LinkedList<ControlPoint>();
	private int ID;

	public Trip(Route route, ControlPoint startPoint, int ID) throws DataException {
		setRoute(route);
		setStartPoint(startPoint);
		lastVisitedPoint = null;
		setCurrentTarget(this.startPoint);
		this.ID = ID;
		setModifiedRoute();
	}

	public Trip(Route route, ControlPoint startPoint, ControlPoint lastVisited, int ID) throws DataException {
		LinkedList<ControlPoint> points = route.getRoutePoints();
		setRoute(route);
		setStartPoint(startPoint);
		lastVisitedPoint = lastVisited;
		if(lastVisited != null)
			setCurrentTarget(points.get((indexOf(lastVisitedPoint)+1)%points.size()));
		else
			setCurrentTarget(startPoint);

		this.ID = ID;
		setModifiedRoute();
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

	private void setModifiedRoute(){
		List<ControlPoint> routePoints = route.getRoutePoints();
		modifiedRoute = new LinkedList<ControlPoint>();
		int startId = indexOfStart();
		System.out.println("DŁUGOSĆ WCZYTANEJ TRASY: " + routePoints.size());
		System.out.println("INDEKS STARTU: " + startId);
		modifiedRoute.addAll(routePoints.subList(startId, routePoints.size()));
		if(startId > 0)
			modifiedRoute.addAll(routePoints.subList(0, startId));
	}

	public List<ControlPoint> getModifiedRoute(){
		return modifiedRoute;
	}

	private int indexOfStart(){
		String sName = startPoint.getName();
		int ind = -1;
		int i = 0;
		for(ControlPoint cp: route.getRoutePoints()){
			if(cp.getName().equals(sName))
				ind = i;
			i++;
		}

		return  ind;
	}

	private int indexOf(ControlPoint point){
		String sName = point.getName();
		int ind = -1;
		int i = 0;
		for(ControlPoint cp: route.getRoutePoints()){
			if(cp.getName().equals(sName))
				ind = i;
			i++;
		}

		return  ind;
	}

	@Override
	public boolean equals(Object other){
		return (other instanceof Trip)&&this.getID()==((Trip) other).getID();
	}
}