package org.acme;

import static org.fao.fi.comet.mapping.dsl.MappingContributionDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingDataDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingDetailDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingElementDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingElementIdentifierDSL.*;
import static org.fao.fi.comet.mapping.dsl.MatcherConfigurationDSL.*;
import static org.fao.fi.comet.mapping.dsl.MatcherConfigurationPropertyDSL.*;
import static org.sdmx.SdmxServiceFactory.*;

import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Date;

import org.fao.fi.comet.mapping.model.DataProvider;
import org.fao.fi.comet.mapping.model.MappingData;
import org.junit.BeforeClass;
import org.junit.Test;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.sdmxsource.util.io.ReadableDataLocationTmp;
import org.virtual.sr.RepositoryPlugin;
import org.virtual.sr.transforms.Comet2Xml;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.fmf.CometAsset;
import org.virtualrepository.impl.Repository;
import org.virtualrepository.sdmx.SdmxCodelist;

public class PublishIntegrationTests {

    @BeforeClass
    public static void setup() {

        System.setProperty("org.slf4j.simpleLogger.log.org.virtual.sr ", "trace");
    }

    @Test
    public void publishSdmxCodelist() {

        InputStream micro_asfis = getClass().getClassLoader().getResourceAsStream("micro-asfis.xml");
        InputStream micro_asfis_add = getClass().getClassLoader().getResourceAsStream("micro-asfis_add.xml");
        InputStream micro_asfis_remove = getClass().getClassLoader().getResourceAsStream("micro-asfis_remove.xml");

        CodelistBean micro_asfis_list = parser().parseStructures(new ReadableDataLocationTmp(micro_asfis)).
                getStructureBeans(false).getCodelists().iterator().next();

        CodelistBean micro_asfis_add_list = parser().parseStructures(new ReadableDataLocationTmp(micro_asfis_add)).
                getStructureBeans(false).getCodelists().iterator().next();
        
        CodelistBean micro_asfis_remove_list = parser().parseStructures(new ReadableDataLocationTmp(micro_asfis_remove)).
                getStructureBeans(false).getCodelists().iterator().next();

        VirtualRepository repo = new Repository();

        RepositoryService service = repo.services().lookup(RepositoryPlugin.name);

        SdmxCodelist micro_asfis_asset = new SdmxCodelist("micro_asfis", service);
        SdmxCodelist micro_asfis_add_asset = new SdmxCodelist("micro_asfis_add", service);
        SdmxCodelist micro_asfis_remove_asset = new SdmxCodelist("micro_asfis_remove", service);

        repo.publish(micro_asfis_asset, micro_asfis_list);
        repo.publish(micro_asfis_add_asset, micro_asfis_add_list);
        repo.publish(micro_asfis_remove_asset, micro_asfis_remove_list);

    }
    
//    @Test
    public void publishDiff() {

        InputStream micro_asfis_diff = getClass().getClassLoader().getResourceAsStream("micro-asfis_diff.xml");

        CodelistBean list = parser().parseStructures(new ReadableDataLocationTmp(micro_asfis_diff)).
                getStructureBeans(false).getCodelists().iterator().next();

        VirtualRepository repo = new Repository();

        RepositoryService service = repo.services().lookup(RepositoryPlugin.name);

        SdmxCodelist asset = new SdmxCodelist("micro_asfis_diff", service);

        repo.publish(asset, list);

    }


//    @Test
//    public void publishMapping() throws URISyntaxException {
//
//        VirtualRepository repo = new Repository();
//
//        RepositoryService service = repo.services().lookup(RepositoryPlugin.name);
//
//        CometAsset asset = new CometAsset("test", service);
//        
//        MappingData mappingData = this.getFakeMappingData();
//        
//        try {
//        	System.out.println(new Comet2Xml().emitXml(mappingData, asset));
//        } catch(Throwable t) {
//        	t.printStackTrace();
//        }
//
//        repo.publish(asset, mappingData);
//    }

    private MappingData getFakeMappingData() throws URISyntaxException {
    	DataProvider sourceDataProvider = new DataProvider(new URI("urn:fooResourceStatus"), Term.class.getName());
		DataProvider targetDataProvider = new DataProvider(new URI("urn:barResourceStatus"), Term.class.getName());
		
		MappingData mappingData = new MappingData().
			id(new URI("urn:foo:bar")).
			version("0.01").
			producedBy("Foo Bazzi").
			on(new Date()).
			linking(sourceDataProvider).to(targetDataProvider).
			through(
				configuredMatcher(new URI("urn:matcher:foo")).
					ofType("org.fao.fi.comet.common.matchers.LexicalMatcher").
					weighting(10).
					withMinimumScore(0.1).
					having(
						configurationProperty("stripSymbols", Boolean.FALSE)
					),
				optional(
					configuredMatcher(new URI("urn:matcher:bar")).
						ofType("org.fao.fi.comet.common.matchers.AnotherLexicalMatcher").
						weighting(30).
						withMinimumScore(0.0).
						having(
							configurationProperty("useSoundex", Boolean.TRUE),
							configurationProperty("stripSymbols", Boolean.TRUE)
						)
					),
				optional(
					configuredMatcher(new URI("urn:matcher:baz")).
						ofType("org.fao.fi.comet.common.matchers.YetAnotherLexicalMatcher").
						weighting(20).
						withMinimumScore(0.2).
						having(
							configurationProperty("useSoundex", Boolean.TRUE)
						)
					)
			).
			with(minimumWeightedScore(0.3), maximumCandidates(5)).
			including(
				map(wrap(Term.describing("over-exploited")).with(identifierFor(sourceDataProvider, new URI("urn:1")))).
					to(
						target(wrap(Term.describing("overexploited")).with(identifierFor(targetDataProvider, new URI("urn:69")))).
							asContributedBy(matcher(new URI("urn:matcher:foo")).scoring(0.39), 
											matcher(new URI("urn:matcher:bar")).scoring(0.69),
											matcher(new URI("urn:matcher:baz")).nonPerformed()
							).withWeightedScore(0.59)
					).andTo(
						target(wrap(Term.describing("ov-erexploited")).with(identifierFor(targetDataProvider, new URI("urn:96")))).
							asContributedBy(matcher(new URI("urn:matcher:foo")).scoring(0.79), 
											matcher(new URI("urn:matcher:bar")).nonPerformed(),
											matcher(new URI("urn:matcher:baz")).nonPerformed()
							).withWeightedScore(0.59)
					)
			).including(
				map(wrap(Term.describing("under-exploited")).with(identifierFor(sourceDataProvider, new URI("urn:2")))).
					to(
						target(wrap(Term.describing("underexploited")).with(identifierFor(targetDataProvider, new URI("urn:70")))).
							asContributedBy(matcher(new URI("urn:matcher:foo")).scoring(0.49), 
											matcher(new URI("urn:matcher:bar")).scoring(0.59),
											matcher(new URI("urn:matcher:baz")).nonPerformed()
							).withWeightedScore(0.39)
					).andTo(
						target(wrap(Term.describing("und-erexploited")).with(identifierFor(targetDataProvider, new URI("urn:97")))).
							asContributedBy(matcher(new URI("urn:matcher:foo")).scoring(0.79), 
											matcher(new URI("urn:matcher:bar")).nonPerformed(),
											matcher(new URI("urn:matcher:baz")).nonPerformed()
							).withWeightedScore(0.79)
					).andTo(
						target(wrap(Term.describing("un-derexploited")).with(identifierFor(targetDataProvider, new URI("urn:98")))).
							asContributedBy(matcher(new URI("urn:matcher:foo")).scoring(0.29), 
											matcher(new URI("urn:matcher:bar")).nonPerformed(),
											matcher(new URI("urn:matcher:baz")).scoring(0.39)
							).withWeightedScore(0.35)
					)
		);
        
        return mappingData;
    }
}
