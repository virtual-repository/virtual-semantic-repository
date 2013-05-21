package org.virtual.sr;

import static java.util.Collections.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.virtualrepository.AssetType;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.MutableAsset;

import com.hp.hpl.jena.query.ResultSet;

public class RepositoryBrowser implements Browser {

	@Override
	public Iterable<? extends MutableAsset> discover(Collection<? extends AssetType> types) throws Exception {
		
		List<MutableAsset> assets = new ArrayList<MutableAsset>();
		for (AssetType type : types)
			
			if (type.equals(SdmxCodelist.type))
				assets.addAll(discoverSdmxCodelists());
		
			else if (type.equals(CsvCodelist.type))
				assets.addAll(discoverCsvCodelists());
			
			//else if ....when more types are supported
			//...
			else
				throw new IllegalArgumentException("type "+type+" is not supported by this service");
		
		return assets;
	}

	@SuppressWarnings("all")
	public Collection<SdmxCodelist> discoverSdmxCodelists() throws Exception {
	
		List<SdmxCodelist> assets = new ArrayList<SdmxCodelist>();
		
		ResultSet results = null; //TODO fire query for results
		
		while (results.hasNext()) {

			//TODO build asset from result
			SdmxCodelist asset = null;
			assets.add(asset);
			
		}
		
		return assets;
	}
	
	public Collection<SdmxCodelist> discoverCsvCodelists() throws Exception {
		
		return emptyList();
	}
}
