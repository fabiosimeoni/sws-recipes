package org.fao.sws.corporate.oo;

import static org.fao.sws.corporate.oo.OrganizationalOutcomes.*;

import org.fao.sws.corporate.CorporateRecipe;
import org.junit.Test;

public class Definitions extends CorporateRecipe {

	@Test
	public void save_domain_config() {
		
		fs.dryrun(true);
		fs.store(ooindicators,"corporate/C300CorporateOO");
		
	}

	@Test
	public void create_schema() {
		
		//db.dryrun(true);
		db.createSchemaFor(ooindicators);
	}
	

}
