package models;

import play.data.validation.Constraints.Required;

public class Answer {

	@Required
	public String prevThngID;
    
	@Required
	public String nextThngID;
	
}
