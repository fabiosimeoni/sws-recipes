package org.fao.sws.corporate;

import javax.inject.Inject;

import org.fao.sws.automation.Database;
import org.fao.sws.automation.FileSystem;
import org.fao.sws.automation.Recipe;
import org.junit.After;

public class CorporateRecipe extends Recipe {

	@Inject
	protected Database db;
	
	@Inject
	protected FileSystem fs;
	
	
	@After
	public void cleanup() {
		db.close();
	}
}
