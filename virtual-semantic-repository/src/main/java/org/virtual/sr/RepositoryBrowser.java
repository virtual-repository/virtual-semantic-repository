package org.virtual.sr;

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

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;

public class RepositoryBrowser implements Browser {

	private final RepositoryConfiguration configuration;

	public RepositoryBrowser(RepositoryConfiguration configuration) {
		this.configuration = configuration;
	}

	@Override
	public Iterable<? extends MutableAsset> discover(Collection<? extends AssetType> types) throws Exception {

		// coding cautiously below: VR should not pass us an unsupported type

		if (types.contains(SdmxCodelist.type))
			return discoverSdmxCodelists();

		if (types.contains(CsvCodelist.type))
			return discoverCsvCodelists();

		throw new IllegalArgumentException("unsupported types " + types);
	}

	@SuppressWarnings("all")
	public Collection<SdmxCodelist> discoverSdmxCodelists() throws Exception {

		List<SdmxCodelist> assets = new ArrayList<SdmxCodelist>();
		String endpoint = configuration.public_endpoint_query().toString();

		Query q = QueryFactory.create(configuration.query_all_sdmx_codelist());
		ResultSet codelists = QueryExecutionFactory.sparqlService(endpoint, q).execSelect();
		System.out.println("");

		while (codelists.hasNext()) {

			QuerySolution next = codelists.next();

			String uri = next.getResource("uri").getURI();
			String name = next.getLiteral("name").getLexicalForm();
			String version = next.getLiteral("version").getLexicalForm();
			String creator = next.getLiteral("creator").getLexicalForm();

			SdmxCodelist asset = new SdmxCodelist(uri, uri, version, name);

			asset.properties().add(Constants.ownerProperty(creator));

			assets.add(asset);
		}
		return assets;
	}

	public Collection<SdmxCodelist> discoverCsvCodelists() throws Exception {

		return Collections.emptyList();
	}
}
