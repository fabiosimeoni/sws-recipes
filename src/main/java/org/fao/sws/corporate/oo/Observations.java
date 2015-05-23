package org.fao.sws.corporate.oo;

import static org.fao.sws.corporate.oo.Utils.*;
import lombok.experimental.ExtensionMethod;

import org.fao.sws.corporate.CorporateRecipe;
import org.junit.Test;

@ExtensionMethod(Utils.class)
public class Observations extends CorporateRecipe {
	
	@Test
	public void load_so1_baseline() {
		
		//db.dryrun();
		
		read("cba_so1.txt").andPivotItWith("so1_ind_map.txt").forYear("2014").in(db);
		
	}
	
	@Test
	public void so1_countries() {
		
		read("CBA_SO1_results.txt").show("Country");
	}
	
	

}
