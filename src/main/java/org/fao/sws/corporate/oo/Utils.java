package org.fao.sws.corporate.oo;

import static java.lang.String.*;
import static java.util.Arrays.*;
import static org.fao.sws.corporate.oo.OrganizationalOutcomes.*;
import static smallgears.api.tabular.dsl.Tables.*;
import static smallgears.api.tabular.operations.TableOperations.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;

import lombok.experimental.ExtensionMethod;
import lombok.experimental.UtilityClass;

import org.fao.sws.automation.Database;
import org.fao.sws.model.Dimension;

import smallgears.api.tabular.Csv;
import smallgears.api.tabular.Row;
import smallgears.api.tabular.Table;
import smallgears.api.tabular.operations.OperationDsl.IndexClause;

@UtilityClass 
@ExtensionMethod(Utils.class) //uses its own extension methods
public class Utils {
	
	
	public IndexClause<Row> andIndex(Table table) {
	
		return index(table);
	}
	
	public DBClause<Table> andFeed(Table table, Dimension dim) {
		
		return db-> {
			
			db.insert(dim,table);
			
			return table;
		};
	}
	
	///////////////////////////////////////////////////////////////////////////////////////
	
	public Table retain(Table table, String ... cols) {
		
		return table.with(row->row.extract(cols));
	}
	
	public Table remove(Table table, String ... cols) {
		
		return table.with(row->row.remove(cols));
	}
	
	
	public Table addDummyUnit(Table table) {
		
		table.columns().add(col("unit_of_measure"));
		
		return table.with(r->r.set("unit_of_measure",1));
	}
	
	
	
	////////////////////////////////////////////////////////////////////////////////// last
	
	final String basepath =  "/corporate/oo/";
	
	public Table read(String path, Csv csv) {
		
		return csv.parse().in(Utils.class.getResourceAsStream(basepath+path));
	}

	public Table read(String path) {
		
		return read(path,Csv.csv());
	}
	
	public DBClause<Table> read(Dimension dim) {
		
		return db->csv().parse().in(db.list(dim.table()).formatCSV());
	}
	
	
	////////////////////////////////////////////////////////////////////////////////// pivot
	
	
	public interface PivotClause {
		
		DBClause<Table> forYear(String year);
		
	}
	
	
	
	public PivotClause andPivotItWith(Table source,String mapping) {
		
		return (year) -> (db) -> {
			
				Resolver resolver = new Resolver(db);
				
				Map<String,String> measure_map = read(mapping).andIndex().over("target").using("source");
				
				BiFunction<Row,String,Row> make_pivoted = (row,column)-> 
				
												new Row()
													.set(country_dim_name, resolver.lookup(row.get("country")))
													.set(indicator__dim_name,measure_map.get(column))
													.set(year_dim_name,year)
													.set("Value",row.getOr(column,"NULL"));
				
												
				BiPredicate<Row,String> can_be_pivoted = (row,column) -> {
					
					return row.has(column) && row.get(column)!=null && !row.get(column).isEmpty();
					
				};
				
				List<Row> empties = new ArrayList<>();
												
												
				Table pivoted = source.unfoldWith(row-> measure_map.keySet()
														.stream()
														.peek(c-> {if (!can_be_pivoted.test(row,c)) empties.add(row);})
														.filter(c->can_be_pivoted.test(row,c))
														.map(col -> make_pivoted.apply(row,col)));
				
				System.out.println(format("discarded %s rows",empties.size()));
				
				pivoted.columns().addAll(asList(col(country_dim_name),
												col(indicator__dim_name),
												col(year_dim_name),
												col("Value")));
				return pivoted;
		};
	}

	
	
	////////////////////////////////////////////////////////////////////////////////// helpers
	
	
	public String[] $(String key, String val) {
		
		return new String[] {key,val};
	}
	
	interface DBClause<T> {
		
		T in(Database db);
	}

}
