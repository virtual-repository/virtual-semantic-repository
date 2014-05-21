/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.acme;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.graph.NodeFactory;
import com.hp.hpl.jena.graph.Triple;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;
import com.hp.hpl.jena.vocabulary.DCTerms;
import java.net.URI;
import java.util.Date;
import org.apache.jena.web.DatasetGraphAccessorHTTP;
import static org.fao.fi.comet.mapping.dsl.DataProviderDSL.provider;
import static org.fao.fi.comet.mapping.dsl.MappingDSL.map;
import static org.fao.fi.comet.mapping.dsl.MappingDetailDSL.target;
import static org.fao.fi.comet.mapping.dsl.MappingElementDSL.wrap;
import static org.fao.fi.comet.mapping.dsl.MappingElementIdentifierDSL.identifierFor;
import org.fao.fi.comet.mapping.model.Mapping;
import org.fao.fi.comet.mapping.model.MappingData;
import org.fao.fi.comet.mapping.model.MappingElement;
import static org.fao.fi.comet.mapping.model.utils.jaxb.JAXB2DOMUtils.asElement;
import org.fao.fi.comet.mapping.model.utils.jaxb.JAXBDeSerializationUtils;
import org.virtual.sr.RepositoryPlugin;
import org.virtual.sr.vocabularies.SKOS_CORE;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.VirtualRepository;
import org.virtualrepository.comet.CometAsset;
import org.virtualrepository.impl.Repository;

/**
 *
 * @author Claudio Baldassarre <c.baldassarre@me.com>
 */
public class mapping_haspart {

    public static void main(String[] args) {
        MappingData md = makeMapping();
        stageMapping(md);
    }

    private static MappingData makeMapping() {
        DatasetGraphAccessorHTTP accessor = new DatasetGraphAccessorHTTP("http://168.202.3.223:3030/sr_public/data");
        Graph fao_major_areas = accessor.httpGet(NodeFactory.createURI("http://semanticrepository/public/graph/CL_FAO_MAJOR_AREA"));
        Node area21 = fao_major_areas.find(Node.ANY, SKOS_CORE.notation.asNode(), NodeFactory.createLiteral("21")).next().getSubject();
        Graph sub_areas_graph = accessor.httpGet(NodeFactory.createURI("http://semanticrepository/public/graph/CL_SUB_AREA"));
        ExtendedIterator<Triple> sub_areas = sub_areas_graph.find(Node.ANY, DCTerms.subject.asNode(), Node.ANY);

        MappingData mappingData = new MappingData();
        mappingData.
                linking(provider(URI.create("urn:provider:source"), URI.create("urn:source:type"), URI.create("urn:source:data:bar"))).
                to(provider(URI.create("urn:provider:target"), URI.create("urn:target:type"), URI.create("urn:target:data:bar"))).
                version("1.0").
		producedBy("Semantic Repository Plugin").
		on(new Date());

        Mapping mappingEntry;

        MappingElement target, source = wrap(asElement(GenericTerm.describing(area21.getLocalName()))).with(identifierFor(area21.toString()));

        mappingEntry = map(source);
        
        while (sub_areas.hasNext()) {
            Triple triple = sub_areas.next();
            target = wrap(asElement(GenericTerm.describing(triple.getObject().getLocalName()))).with(identifierFor(triple.getSubject().toString()));
            mappingEntry = mappingEntry.to(target(target));

        }
        mappingData.include(mappingEntry);
//        System.out.println(JAXBDeSerializationUtils.toXML(mappingData));

        return mappingData;
    }

    private static void stageMapping(MappingData md) {
        VirtualRepository repo = new Repository();

        RepositoryService service = repo.services().lookup(RepositoryPlugin.name);

        CometAsset asset = new CometAsset("area_haspart_subarea", service);
        
        repo.publish(asset, md);
    }

}
