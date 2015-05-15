package org.fao.sws;

import static org.fao.sws.model.configuration.Dsl.*;

import javax.inject.Inject;

import org.fao.sws.automation.Recipe;
import org.fao.sws.automation.dsl.Database;
import org.fao.sws.automation.dsl.Deployment;
import org.fao.sws.automation.dsl.FileSystem;
import org.fao.sws.model.Dataset;
import org.fao.sws.model.Dimension;
import org.fao.sws.model.Flag;
import org.fao.sws.model.configuration.Configuration;
import org.junit.Test;

//use junit as the script execution framework
public class CorporateData extends Recipe {

	@Inject @Deployment(config="src/test/resources")
	FileSystem fs;
	
	@Inject
	Database db;
	
	
	//the domain
	Configuration corporate() {
		
		Dimension m49 = dimension("geographicAreaM49");
		Dimension years = timeDimension("timePointYears");
		Dimension score = measureDimension("corporate_ooscore");
		Flag status = flag("corporate_oostatus");
		
		Dataset ooindicators = dataset("ooindicators")
								.with(
									m49.ref().roots("700")
								   ,score.ref()
								   ,years.ref().roots("58","59")
								)
								.with(status.ref());
		
		
		Configuration corporate = sws()
									.contact("fabio@simeoni.fao.org")
									.with(score)
									.with(
										domain("corporate")
										.labelKey("corporate_domain")
										.with(ooindicators)
									);
		
		
		return corporate;
	
	
	}
	
	//stores config
	@Test
	public void save_domain_config() {
		
		fs.store(corporate(),"corporate/C300CorporateOO");
		
	}
	
	//creates schema
	@Test
	public void store_domain() {
		
		db.store(corporate());
	}
	
	
}
