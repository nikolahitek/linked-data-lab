package com.nikolahitek;

import org.apache.jena.rdf.model.*;

import java.util.List;
import java.util.stream.Collectors;

public class Local {

    private static String rdfs = "http://www.w3.org/2000/01/rdf-schema#";
    private static String drugbank = "http://wifo5-04.informatik.uni-mannheim.de/drugbank/resource/drugbank/";

    public static void main(String[] args) {

        Model model = ModelFactory.createDefaultModel();
        model.read("hifm-dataset.ttl", "ttl");

        Resource drug = model.getResource("http://purl.org/net/hifm/data#39195");

        List<Resource> similarDrugs = drug.listProperties(model.getProperty(rdfs + "seeAlso"))
                .toList()
                .stream()
                .map(Statement::getResource)
                .collect(Collectors.toList());

        List<Model> models = similarDrugs.stream()
                .map(d -> {
                    Model m = ModelFactory.createDefaultModel();
                    m.read(d.getURI().replace("resource", "data"));
                    return m;
                })
                .collect(Collectors.toList());

        models.forEach(Local::printDrugInfo);
    }

    private static void printDrugInfo(Model model) {
        String label = model.listStatements(
                new SimpleSelector(null, model.getProperty(rdfs + "label"), (RDFNode) null))
                .toList()
                .get(0).getString();

        String brandName = model.listStatements(
                new SimpleSelector(null, model.getProperty(drugbank + "brandName"), (RDFNode) null))
                .toList()
                .get(0).getString();

        String genericName = model.listStatements(
                new SimpleSelector(null, model.getProperty(drugbank + "genericName"), (RDFNode) null))
                .toList()
                .get(0).getString();

        String pharmacology = model.listStatements(
                new SimpleSelector(null, model.getProperty(drugbank + "pharmacology"), (RDFNode) null))
                .toList()
                .get(0).getString();

        System.out.println("Label: " + label);
        System.out.println("Brand name: " + brandName);
        System.out.println("Generic name: " + genericName);
        System.out.println("Pharmacology: " + pharmacology);
        System.out.println();
    }
}
