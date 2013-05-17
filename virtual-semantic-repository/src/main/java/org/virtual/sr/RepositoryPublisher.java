package org.virtual.sr;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;

import org.sdmxsource.sdmx.api.constants.STRUCTURE_OUTPUT_FORMAT;
import org.sdmxsource.sdmx.api.manager.output.StructureWritingManager;
import org.sdmxsource.sdmx.api.model.beans.SdmxBeans;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.sdmx.structureparser.manager.impl.StructureWritingManagerImpl;
import org.sdmxsource.sdmx.util.beans.container.SdmxBeansImpl;
import org.virtualrepository.Asset;
import org.virtualrepository.AssetType;
import org.virtualrepository.impl.Type;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Publisher;

import com.hp.hpl.jena.rdf.model.Model;

@SuppressWarnings("unused")
public class RepositoryPublisher<A extends Asset> implements Publisher<A,Model> {

	private final RepositoryConfiguration configuration;
	private final Type<A> assetType;
	
	
	public RepositoryPublisher(Type<A> assetType,RepositoryConfiguration configuration) {
		this.assetType=assetType;
		this.configuration=configuration;
	}
	
	@Override
	public Type<A> type() {
		return assetType;
	}

	@Override
	public Class<Model> api() {
		return Model.class;
	}

	@Override
	public void publish(A asset, Model rdf) throws Exception {

		
		System.out.println("publishing to "+configuration.publishURI());
		
	}

	
	//helpers
	
	Source xmlOf(CodelistBean bean) {

		SdmxBeans beans = new SdmxBeansImpl();
		beans.addCodelist(bean);
		
		ByteArrayOutputStream stream = new ByteArrayOutputStream(1024);
		
		STRUCTURE_OUTPUT_FORMAT format = STRUCTURE_OUTPUT_FORMAT.SDMX_V21_STRUCTURE_DOCUMENT;
		
		StructureWritingManager manager = new StructureWritingManagerImpl();
		manager.writeStructures(beans,format, stream);
		
		return new StreamSource(new ByteArrayInputStream(stream.toByteArray()));
	}
}
