package org.virtual.sr.transforms.mapping;

import static org.fao.fi.comet.mapping.dsl.DataProviderDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingDetailDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingElementDSL.*;
import static org.fao.fi.comet.mapping.dsl.MappingElementIdentifierDSL.*;

import java.net.URI;

import org.fao.fi.comet.mapping.model.MappingData;
import org.sdmxsource.sdmx.api.model.beans.codelist.CodelistBean;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.virtual.sr.transforms.Xml2Rdf;
import org.virtual.sr.transforms.XmlTransform;
import org.virtual.sr.vocabularies.SKOS_CORE;
import org.virtualrepository.Asset;
import org.virtualrepository.RepositoryService;
import org.virtualrepository.comet.CometAsset;
import org.virtualrepository.spi.Transform;

import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.vocabulary.DCTerms;
/**
 * A {@link Transform}s from arbitrary {@link Asset}s in given APIs to RDF. <p>
 * Bridges {@link XmlTransform}s with the {@link Xml2Rdf} transform to adapt the {@link RepositoryService}
 * to given asset types.
 *
 * @author Fabio Simeoni
 *
 */
public class Rdf2CometMapping implements Transform<CometAsset, Model, CodelistBean> {

    private static Logger log = LoggerFactory.getLogger(Rdf2CometMapping.class);

    @Override
        public CodelistBean apply(CometAsset asset, Model m) throws Exception {

        log.info("transforming codelist " + asset.id() + " to comet mapping");

        MappingData mappingData = new MappingData();
        mappingData.
            linking(provider(URI.create("urn:provider:source"), URI.create("urn:source:type"), URI.create("urn:source:data:bar"))).
                 to(provider(URI.create("urn:provider:target"), URI.create("urn:target:type"), URI.create("urn:target:data:bar"))); 
//        Properties props = asset.properties();
//
//        if (props.contains(Constants.ownerName)) {
//            codelist.setAgencyId(props.lookup(Constants.ownerName).value(String.class));
//        } else {
//            codelist.setAgencyId("SDMX");
//        }

//        mapping.setId(asset.name());
//        mapping.setUri(asset.id());
//        mapping.addName("en", asset.name());

        ResIterator codes = m.listSubjectsWithProperty(SKOS_CORE.notation);

        while (codes.hasNext()) {

            Resource code_resource = codes.next();

            mappingData.including(
                map(wrap("over-exploited").with(identifierFor(URI.create("urn:id:33")))).
                    to(
                       target(
                          wrap("overexploited").with(identifierFor(URI.create("urn:id:44")))
                       )
                    )
            );

//            code.setId(adaptId(code_resource.getRequiredProperty(SKOS_CORE.notation).getLiteral().getLexicalForm()));
//            code.setUri(code_resource.getURI());
            ResIterator subjectEntities = m.listSubjectsWithProperty(DCTerms.subject, code_resource);

            while (subjectEntities.hasNext()) {
                Resource subjectEntity = subjectEntities.next();
                NodeIterator labels = m.listObjectsOfProperty(subjectEntity, SKOS_CORE.prefLabel);
                while (labels.hasNext()) {
                    Literal label = labels.next().asLiteral();
//                    code.addName(label.getLanguage(), label.getLexicalForm());
                }
            }

            NodeIterator definitions = m.listObjectsOfProperty(code_resource, SKOS_CORE.definition);
            while (definitions.hasNext()) {
                Literal def = definitions.next().asLiteral();
//                code.addDescription(def.getLanguage(), def.getLexicalForm());
            }

//            mappingData.addItem(code);
        }

//        return mappingData.getImmutableInstance();
        return null;
    }

    @Override
    public Class<Model> inputAPI() {
        return Model.class;
    }

    @Override
    public Class<CodelistBean> outputAPI() {
        return CodelistBean.class;
    }

    //helpers
    private String adaptId(String id) {

        //TODO add to this simple adaptation
        return id.replace(".", "_");
    }
}
