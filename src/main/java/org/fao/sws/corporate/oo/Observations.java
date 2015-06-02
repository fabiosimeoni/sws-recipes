package org.fao.sws.corporate.oo;

import static java.lang.String.*;
import static org.fao.sws.corporate.oo.Utils.*;
import static smallgears.api.tabular.dsl.Tables.*;
import lombok.experimental.ExtensionMethod;

import org.fao.sws.corporate.CorporateRecipe;
import org.junit.Test;

import smallgears.api.tabular.Csv;
import smallgears.api.tabular.MaterialisedTable;
import smallgears.api.tabular.Table;
import smallgears.api.tabular.operations.TableOperations;

@ExtensionMethod({TableOperations.class,Utils.class})
public class Observations extends CorporateRecipe {
	
	static final String so1_results = "cba_so1.txt";
	static final String so2_results = "cba_so2.txt";
	static final String so3_results = "cba_so3.txt";
	static final String so5_results = "cba_so5.txt";
	
	@Test
	public void generate_so1_baseline() {
		
		Table pivoted = read_results(so1_results).andPivotItWith("so1_ind_map.txt").forYear("2014").in(db);
		
		pivoted.show();
		
		Csv.csv().serialise(pivoted).at(fs.path("corporate/cba_so1_import.csv"));
		
	}
	
	@Test
	public void genereate_so2_baseline() {
		
		Table pivoted = read_results(so2_results).andPivotItWith("so2_ind_map.txt").forYear("2014").in(db);
		
		pivoted.show();
		
		Csv.csv().serialise(pivoted).at(fs.path("corporate/cba_so2_import.csv"));
		
	}
	
	@Test
	public void genereate_so3_baseline() {
		
		Table pivoted = read_results(so3_results).andPivotItWith("so3_ind_map.txt").forYear("2014").in(db);
		
		pivoted.show();
		
		Csv.csv().serialise(pivoted).at(fs.path("corporate/cba_so3_import.csv"));
		
	}
	
	@Test
	public void genereate_so5_baseline() {
		
		Table pivoted = read_results(so5_results).andPivotItWith("so5_ind_map.txt").forYear("2014").in(db);
		
		pivoted.show();
		
		Csv.csv().serialise(pivoted).at(fs.path("corporate/cba_so5_import.csv"));
		
	}
	
	@Test
	public void generate_full_baseline() {
		 
		Table so1 = read_results(so1_results).andPivotItWith("so1_ind_map.txt").forYear("2014").in(db);
		Table so2 = read_results(so2_results).andPivotItWith("so2_ind_map.txt").forYear("2014").in(db);
		Table so3 = read_results(so3_results).andPivotItWith("so3_ind_map.txt").forYear("2014").in(db);
		Table so5 = read_results(so5_results).andPivotItWith("so5_ind_map.txt").forYear("2014").in(db);
		
		
		Table full = so5.append().to(so3).append().to(so2).append().to(so1);
		
		
		Csv.csv().serialise(full).at(fs.path("corporate/cba_full_import.csv"));
	}
	
	
	///////////////////////////////////////////////////////////////////  inspection
	
	@Test
	public void show_so1_countries_with_mismatches() {
		
		show_countries_with_mismatches(so1_results);
	}
	
	@Test
	public void show_so2_countries_with_mismatches() {

		show_countries_with_mismatches(so2_results);

	}
	
	@Test
	public void show_so3_countries_with_mismatches() {
		
		show_countries_with_mismatches(so3_results);

	}
	
	@Test
	public void show_so5_countries_with_mismatches() {
		
		show_countries_with_mismatches(so5_results);
		
	
	}
	
	//////////////////////////////////////////////////////////////////// helpers
	
	void show_countries_with_mismatches(String resource) {
		
		Table result = read_results(resource).materialise();
		
		result.show("country");
			
		Resolver resolver = new Resolver(db);
		
		MaterialisedTable unmatched = result.stream().filter(r->!resolver.canLookup(r.get("country"))).collect(toTable()).materialise();
		
		if (unmatched.isEmpty())
			
			System.out.println("no unmatched countries.");
		
		else  {
			
			System.out.println(format("there are %s unmatched countries",unmatched.size()));
			
			unmatched.show("country");
		}
	}
	
	Table read_results(String resource) {
		
		return read(resource, csv().delimiter('\t').encoding("UTF-16")).materialise();
		
	}

}
