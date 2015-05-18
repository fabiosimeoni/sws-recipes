package org.fao.sws;

import static org.fao.sws.model.configuration.Dsl.*;

import javax.inject.Inject;

import org.fao.sws.automation.Database;
import org.fao.sws.automation.Deployment;
import org.fao.sws.automation.FileSystem;
import org.fao.sws.automation.Recipe;
import org.fao.sws.model.Dataset;
import org.fao.sws.model.Dimension;
import org.fao.sws.model.Flag;
import org.fao.sws.model.configuration.Binder;
import org.fao.sws.model.configuration.Configuration;
import org.junit.Test;

public class CorporateData extends Recipe {

	@Inject 
	@Deployment(config="src/test/resources")
	FileSystem fs;
	
	@Inject
	Database db;
	
	@Inject
	Binder binder;
	
	//the domain
	Configuration corporate() {
		
		Dimension m49 = dimension("geographicAreaM49");
		Dimension years = timeDimension("timePointYears");
		Dimension score = measureDimension("corporate_ooscore").labelKey("OOScore").label("OO Score");
		
		Flag status = flag("corporate_oostatus");
		
		Dataset ooindicators = dataset("coporate_oo")
							   .label("Corporate - Organizational Outcomes")
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
											.label("Corporate Data")
										 	.with(ooindicators)
								  );
			
		return corporate;
	
	}
	
	@Test
	public void save_domain_config() {
		
		fs.store(corporate(),"corporate/C300CorporateOO");
		
	}
	
	
	@Test
	public void show_config() {

		fs.dryrun(true);
		save_domain_config();
	}
	
	@Test
	public void create_schema() {
		
		//db.clean(true);
		db.dryrun(true);
		db.defineFragment(corporate());
	}
	
	
}
