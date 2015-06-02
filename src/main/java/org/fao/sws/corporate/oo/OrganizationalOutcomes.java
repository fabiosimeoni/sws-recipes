package org.fao.sws.corporate.oo;

import static org.fao.sws.model.configuration.Dsl.*;
import lombok.experimental.UtilityClass;

import org.fao.sws.automation.Group;
import org.fao.sws.model.Dataset;
import org.fao.sws.model.Dimension;
import org.fao.sws.model.configuration.Configuration;

@UtilityClass
public class OrganizationalOutcomes {

	public final String country_dim_name = "geographicAreaM49";
	public final String year_dim_name = "oo_years";
	public final String indicator__dim_name = "oo_indicator";

	public Dimension country_dim = dimension(country_dim_name).labelKey("oo_country").label("Country")
			.table("REFERENCE_DATA.DIM_GEOGRAPHIC_AREA_M49")
			.selectionTable("OPERATIONAL_DATA.SELECTION_DIM_GEOGRAPHIC_AREA_M49")
			.hierarchyTable("REFERENCE_DATA.DIM_GEOGRAPHIC_AREA_M49_HIERARCHY");

	public Dimension year_dim = timeDimension(year_dim_name).labelKey("ooYears").label("Year");

	public Dimension indicator_dim = measureDimension(indicator__dim_name).length(15).labelKey("ooIndicator").label("Indicator");

	public Dataset dataset = dataset("corporate_oo")
			   .labelKey("corporateOO")
			   .label("Corporate - Organizational Outcomes")
			   .with(
					country_dim.ref().joinColumn("country").roots("1")
				   ,indicator_dim.ref().joinColumn("indicator")
				   ,year_dim.ref().joinColumn("year").roots("28","29")
				);
	
	public Configuration ooindicators = sws()
			.contact("fabio@simeoni.fao.org")
			.with(year_dim,indicator_dim)
			.with(
				domain("corporate")
					.label("Corporate Data")
				 	.with(dataset)
		  );
	
	
	public Group corporate_group = new Group("corporate_oo_users").description("Organizational Outcomes User Group");
	

}
