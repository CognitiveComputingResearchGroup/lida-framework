package edu.memphis.ccrg.lida.perception;

public class SpatialLocation{
	private int iLocation = 0;
	private int jLocation = 0;

	public SpatialLocation(int i, int j) {
		iLocation = i;
		jLocation = j;
	}
	
	public boolean equals(Object obj){
		if(!(obj instanceof SpatialLocation))
			return false;
		SpatialLocation other = (SpatialLocation)obj;
		return iLocation == other.iLocation && jLocation == other.jLocation;
	}	
	
	/**
	 * 
	 */
	public int hashCode(){ 
        int hash = 1;
        Integer i = new Integer(iLocation);
        Integer j = new Integer(jLocation);
        
        hash = hash * 31 + i.hashCode();
        hash = hash * 31 + (j == null ? 0 : i.hashCode());
        return hash;
    }   
	
	public int getI(){return iLocation;}
	public void setI(int i){iLocation = i;}
	public int getJ(){return jLocation;}
	public void setJ(int j){jLocation = j;}

	public void print() {
		System.out.println("x: " + jLocation + " y: " + iLocation);		
	}	
	
}
