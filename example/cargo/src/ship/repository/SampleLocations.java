package ship.repository;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ship.domain.Location;


public class SampleLocations {
	

	  public static final Location HONGKONG = new Location("CNHKG", "Hongkong");
	  public static final Location MELBOURNE = new Location("AUMEL", "Melbourne");
	  public static final Location STOCKHOLM = new Location("SESTO", "Stockholm");
	  public static final Location HELSINKI = new Location("FIHEL", "Helsinki");
	  public static final Location CHICAGO = new Location("USCHI", "Chicago");
	  public static final Location TOKYO = new Location("JNTKO", "Tokyo");
	  public static final Location HAMBURG = new Location("DEHAM", "Hamburg");
	  public static final Location SHANGHAI = new Location("CNSHA", "Shanghai");
	  public static final Location ROTTERDAM = new Location("NLRTM", "Rotterdam");
	  public static final Location GOTHENBURG = new Location("SEGOT", "Göteborg");
	  public static final Location HANGZOU = new Location("CNHGH", "Hangzhou");
	  public static final Location NEWYORK = new Location("USNYC", "New York");

	  public static final Map<String, Location> ALL = new HashMap();

	  static {
	    for (Field field : SampleLocations.class.getDeclaredFields()) {
	      if (field.getType().equals(Location.class)) {
	        try {
	          Location location = (Location) field.get(null);
	          ALL.put(location.getUcode(), location);
	        } catch (IllegalAccessException e) {
	          throw new RuntimeException(e);
	        }
	      }
	    }
	  }

	  public static List<Location> getAll() {
	    return new ArrayList(ALL.values());
	  }

	  public static Location lookup(String unLocode) {
	    return ALL.get(unLocode);
	  }


}
