package controllers;

import static play.data.Form.form;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import models.Answer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import play.Play;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.game;
import views.html.index;
import views.html.admin;

import com.evrythng.java.wrapper.ApiManager;
import com.evrythng.java.wrapper.exception.EvrythngClientException;
import com.evrythng.java.wrapper.exception.EvrythngException;
import com.evrythng.java.wrapper.service.ThngService;
import com.evrythng.thng.resource.model.store.Thng;

public class Application extends Controller {
	
	private static ApiManager api = new ApiManager(
			Play.application().configuration().getString("evrythgn.apikey"));
	
	private final static Form<Answer> answerForm = form(Answer.class);
	private static List<Thng> things = null; 
	
	
	/*
	 * get the home page. 
	 * 
	 */
    public static Result index() {
    	return ok(index.render("Home"));    
    }

	/*
	 * get the admin page with all the thngs. 
	 * 
	 */
    public static Result admin() throws EvrythngClientException, EvrythngException, JSONException {
        List<Thng> thngs = getThngs();
    	return ok(admin.render(thngs));
        
    }
    
	/*
	 * get the game page and store in cookie the last thngId unlocked.  
	 * 
	 */
    public static Result game(String thngId)  throws EvrythngException,EvrythngClientException {
        if (thngId==null || thngId.isEmpty()){ 
        	if (session("lastThngID")==null || session("lastThngID").isEmpty()){
        		session("lastThngID", getThngs().get(0).getId());
        		return ok(game.render(getThngs().get(0), answerForm));
        	}
        	thngId = session("lastThngID");
        }
    	Thng thng = getThngById(thngId);
    	if (thng==null) {
    		answerForm.reject("nextThngID","The entered ID is not valid. Try again!");
    		return badRequest(game.render(getThngById(session("lastThngID")), answerForm));
    	}
    	if (thng.getCustomFields().get("nextthng")==null)
    		session().remove("lastThngID");
    	else
    		session("lastThngID", thngId);
    	return ok(game.render(thng, answerForm));
        
    }
    
	/*
	 * POST form submission
	 * 
	 */
    public static Result gameSubmit()  throws EvrythngException,EvrythngClientException {
        answerForm.discardErrors();
    	Form<Answer> filledForm = answerForm.bindFromRequest();
    	if(filledForm.hasErrors()) {
    	    return badRequest(game.render(getThngById(session("lastThngID")), filledForm));
    	} else {
    		Answer ans = filledForm.get();
    		Thng prevThing = getThngById(ans.prevThngID);
    		if (!prevThing.getCustomFields().get("nextthng").equals(ans.nextThngID)){
    			filledForm.reject("nextThngID","The entered ID is not valid. Try again!");
    			return badRequest(game.render(getThngById(ans.prevThngID), filledForm));
    		}
    		return game(ans.nextThngID);
    	}
    }
    
    
	/*
	 * Controller that give back a json with all thngs (used for the map). 
	 * 
	 */
    public static Result thngsJson() throws EvrythngClientException, EvrythngException, JSONException {
        List<Thng> thngs = getThngs();
    	response().setContentType("application/json");
    	return ok(thngsToJSON(thngs).toString());   
    }
    

	private static JSONObject thngsToJSON(List<Thng> thngs) throws JSONException {
		JSONArray thngsJSON = new JSONArray();
    	
    	for (Thng thng : thngs) {
			JSONObject thngJSON = new JSONObject();
			thngJSON.put("name", thng.getName());
			if (thng.getLocation()!=null){
				thngJSON.put("longitude", thng.getLocation().getLongitude());
				thngJSON.put("latitude", thng.getLocation().getLatitude());
			}
			thngsJSON.put(thngJSON);	
		}
    	JSONObject mainObj = new JSONObject();
    	mainObj.put("thngs", thngsJSON);
		return mainObj;
	}
	
	
	
	/*
	 * Get all the thngs ordered by ordernumber and cache them. 
	 * 
	 */
	private static List<Thng> getThngs() throws EvrythngException,EvrythngClientException {
		if (things != null) return things;
		
		ThngService thngService = api.thngService();
		List<Thng> thngs = thngService.thngsReader().execute();
		Collections.sort(thngs, new Comparator<Thng>() {
			@Override
			public int compare(Thng t1, Thng t2) {
				Integer ft1 = Integer.parseInt(t1.getCustomFields().get("ordernumber"));
				Integer ft2 = Integer.parseInt(t2.getCustomFields().get("ordernumber"));
				return ft1.compareTo(ft2);
			}
		});
		things = thngs;
		return thngs;
	}

	/*
	 * Get a specific thng from its id. 
	 * 
	 */
	private static Thng getThngById(String thngId){
		ThngService thngService = api.thngService();
		try {
			return thngService.thngReader(thngId).execute();
		} catch (EvrythngException e) {
			e.printStackTrace();
			return null;
		}
	}
}
