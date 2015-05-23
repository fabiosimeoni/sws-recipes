package org.fao.sws.corporate.oo;

import static org.fao.sws.corporate.oo.OrganizationalOutcomes.*;
import static org.fao.sws.corporate.oo.Utils.*;
import lombok.experimental.ExtensionMethod;

import org.fao.sws.corporate.CorporateRecipe;
import org.junit.Test;

@ExtensionMethod(Utils.class)
public class Dimensions extends CorporateRecipe {
	
	
	@Test
	public void create_indicators() {
		
		db.dryrun();
		
		db.createSchemaFor(indicator_dim);
	}

	@Test
	public void fill_indicators() {
		
		db.dryrun();
		
		read("indicators.txt").addDummyUnit().andFeed(indicator_dim).in(db);
		
	}
	
	
	
	
	@Test
	public void create_years() {
		
		db.dryrun();
		
		db.createSchemaFor(year_dim);
	}
	
	@Test
	public void fill_years() {
		
		db.dryrun();
		
		read("years.txt").andFeed(year_dim).in(db);
		
	}
	

	
}
