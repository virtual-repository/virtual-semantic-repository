package org.virtual.sr.transforms.codelist;

import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.api.model.mutable.codelist.CodelistMutableBean;
import org.sdmxsource.sdmx.sdmxbeans.model.mutable.codelist.CodelistMutableBeanImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.sr.transforms.Xml2Rdf;
import org.virtual.sr.transforms.XmlTransform;
import org.virtualrepository.Asset;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Transform;

import com.hp.hpl.jena.query.ResultSet;

/**
 * A {@link Transform}s from arbitrary {@link Asset}s in given APIs to RDF.
 * <p>
 * Bridges {@link XmlTransform}s with the {@link Xml2Rdf} transform to adapt the {@link RepositoryService} to given asset types.
 * 
 * @author Fabio Simeoni
 *
 */
public class Rdf2SdmxCodelist implements Transform<SdmxCodelist,ResultSet,CodelistBean> {

	private static Logger log = LoggerFactory.getLogger(Rdf2SdmxCodelist.class);
	
	@Override
	public CodelistBean apply(SdmxCodelist asset, ResultSet content) throws Exception {
		
		log.info("transforming codelist "+asset.id()+" to sdmx");

		CodelistMutableBean codelist = new CodelistMutableBeanImpl();
		
		codelist.setAgencyId("SDMX");
		codelist.setId(asset.name());
		codelist.setUri(asset.id());
		codelist.addName("en",asset.name());
		
		//TODO to be continued;
		
		return codelist.getImmutableInstance(); 
	}

	@Override
	public Class<ResultSet> inputAPI() {
		return ResultSet.class;
	}

	@Override
	public Class<CodelistBean> outputAPI() {
		return CodelistBean.class;
	}
}
