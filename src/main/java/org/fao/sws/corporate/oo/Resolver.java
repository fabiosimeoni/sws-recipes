package org.fao.sws.corporate.oo;

import static java.lang.String.*;
import static org.fao.sws.corporate.oo.OrganizationalOutcomes.*;
import static org.fao.sws.corporate.oo.Utils.*;

import java.util.Map;

import lombok.NonNull;
import lombok.experimental.ExtensionMethod;

import org.fao.sws.automation.Database;

@ExtensionMethod(Utils.class)
public class Resolver {

	
	private final Map<String,String> countries;

	private final Map<String,String> country_mapping;	

	public Resolver(@NonNull Database db) {
		
		countries = read(country_dim).in(db).andIndex().over("code").using("description_en");
		
		country_mapping = read("country_mapping.txt").andIndex().over("M49").using("Country");
	}
	
	
	public String lookup(String name) {
	
		String code = countries.get(country_mapping.getOrDefault(name,name));
		
		if(code==null)
			throw new IllegalStateException(format("country '%s' is not recogized",name));
		
		return code;
	}
	
}
