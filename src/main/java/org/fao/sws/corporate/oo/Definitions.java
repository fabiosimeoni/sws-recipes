package org.fao.sws.corporate.oo;

import static org.fao.sws.corporate.oo.OrganizationalOutcomes.*;
import static org.fao.sws.model.config.DatabaseConfiguration.*;
import static org.fao.sws.model.configuration.Dsl.*;
import static org.junit.Assert.*;

import java.nio.file.Path;

import lombok.SneakyThrows;

import org.fao.sws.corporate.CorporateRecipe;
import org.fao.sws.model.config.DatabaseConfiguration;
import org.fao.sws.model.configuration.Configuration;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class Definitions extends CorporateRecipe {

	@Test
	public void create_domain_config() {
		
		//fs.dryrun(true);
		fs.store(ooindicators,"corporate/C300CorporateOO");
		
	}
	
	
	@Rule
	public TemporaryFolder tempfolder = new TemporaryFolder();
	
	@Ignore @Test @SneakyThrows
	public void ensure_config_can_be_parsed_in_production() {
		
		Configuration baseconfig = sws().with(dimension("geographicAreaM49")).contact("johndoe");
		
		Path location = tempfolder.getRoot().toPath().resolve("config/application/dataset");
		
		fs.store(baseconfig, location.resolve("C000base.xml"));
		fs.store(ooindicators, location.resolve("C300CorporateOO.xml"));
		
		System.setProperty(CONFIG_ROOT_PROPERTY,tempfolder.getRoot().getAbsolutePath());
		
		DatabaseConfiguration config = new DatabaseConfiguration();
		
		assertNull(config.consumeConfigErrors());
		assertNull(config.consumeConfigAlerts());
	}

	@Test
	public void create_corporate_schema() {
		
		db.dryrun(true);
		db.createSchemaFor(ooindicators);
	}
	
	
	@Test
	public void create_corporate_group() {
		
		db.dryrun(true);
		db.createGroup(corporate_group, dataset);
	}
	

}
