package evrythng.api.test;

import java.util.ArrayList;
import java.util.List;

import com.evrythng.java.wrapper.ApiManager;
import com.evrythng.java.wrapper.exception.EvrythngClientException;
import com.evrythng.java.wrapper.exception.EvrythngException;
import com.evrythng.java.wrapper.service.ThngService;
import com.evrythng.thng.resource.model.store.LocationWithPlace;
import com.evrythng.thng.resource.model.store.Property;
import com.evrythng.thng.resource.model.store.Thng;

public class TestApi {
	 
	/*
	 * this is just a quick test to understand the API and ckeck if they are working. 
	 * 
	 */
	
	public static void main(String[] args) throws EvrythngClientException, EvrythngException {
		
		ApiManager api = new ApiManager("YOUR API KEY HERE");
	    ThngService thngService = api.thngService();

	    // Create a thng
	    Thng myThng = new Thng();
	    myThng.setName("My First Thng");
	    myThng.setDescription("This is a test .");
	    myThng.addCustomFields("hint", "test hint");
	    // do create!
	    myThng = thngService.thngCreator(myThng).execute();

	    //add location to the thing
	 	LocationWithPlace locationData = new LocationWithPlace();
 		locationData.setLatitude(52.3549);
 		locationData.setLongitude(7.5431);
 		List<LocationWithPlace> lastLocations = thngService.locationUpdater(myThng.getId(), locationData).execute();
	    
	    
	    List<Property> props = new ArrayList<Property>();
	    props.add(new Property("property1", "testP1"));
	    props.add(new Property("property2", "testP2"));
	    props = thngService.propertiesCreator(myThng.getId(), props).execute();

	    myThng = thngService.thngReader(myThng.getId()).execute();
	    System.out.println(myThng.getProperties());

	    
	    thngService.thngDeleter(myThng.getId()).execute();
	    
	    
	}
	
	
}
