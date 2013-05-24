package org.virtual.sr;



import com.hp.hpl.jena.query.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.virtual.sr.utils.Constants;
import org.virtualrepository.AssetType;
import org.virtualrepository.csv.CsvCodelist;
import org.virtualrepository.sdmx.SdmxCodelist;
import org.virtualrepository.spi.Browser;
import org.virtualrepository.spi.MutableAsset;

public class RepositoryBrowser implements Browser {

    private final RepositoryConfiguration configuration;

    public RepositoryBrowser(RepositoryConfiguration configuration) {
        this.configuration = configuration;
    }
    
    
    
    @Override
    public Iterable<? extends MutableAsset> discover(Collection<? extends AssetType> types) throws Exception {
        
        List<MutableAsset> assets = new ArrayList<MutableAsset>();
        for (AssetType type : types) {
            if (type.equals(SdmxCodelist.type)) {
                assets.addAll(discoverSdmxCodelists());
            } else if (type.equals(CsvCodelist.type)) {
                assets.addAll(discoverCsvCodelists());
            } //else if ....when more types are supported
            //...
            else {
                throw new IllegalArgumentException("type " + type + " is not supported by this service");
            }
        }
        
        return assets;
    }
    
    @SuppressWarnings("all")
    public Collection<SdmxCodelist> discoverSdmxCodelists() throws Exception {
    	
    	List<SdmxCodelist> assets = new ArrayList<SdmxCodelist>();        
        String endpoint = configuration.discoveryURI().toString();

        Query q = QueryFactory.create(configuration.sparqlQueryForCodelists());
        ResultSet codelists = QueryExecutionFactory.sparqlService(endpoint, q).execSelect();
        
        
        while (codelists.hasNext()) {
        	
            QuerySolution next = codelists.next();
            
            String uri = next.getResource("uri").getURI();
            String name = next.getLiteral("name").getLexicalForm();

            SdmxCodelist asset = new SdmxCodelist(uri, uri, "unknown", name);
            
            //TODO: pull owner in query and add it as property if it exists
            String owner = next.getLiteral("owner").getLexicalForm();
            asset.properties().add(Constants.ownerProperty(owner));
            
            assets.add(asset);
        } 
        return assets;
    }
    
    public Collection<SdmxCodelist> discoverCsvCodelists() throws Exception {
        
        return Collections.emptyList();
    }
}
