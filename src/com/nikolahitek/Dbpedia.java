package com.nikolahitek;

import org.apache.jena.rdf.model.*;

public class Dbpedia {

    public static void main(String[] args) {

        String dbo = "http://dbpedia.org/ontology/";

        // First model for Kumanovo
        Model model = ModelFactory.createDefaultModel();
        model.read("http://dbpedia.org/data/Kumanovo.ttl", "TTL");

        // Getting the object of the dbo:country property
        Property countryProperty = model.getProperty(dbo + "country");

        Statement countryStatement = model.listStatements(
                new SimpleSelector(null, countryProperty, (RDFNode) null)).toList().get(0);

        Resource countryObject = (Resource) countryStatement.getObject();

        System.out.println("Country resource: " + countryObject);

        // Second model for Macedonia
        Model model2 = ModelFactory.createDefaultModel();
        model2.read(
                countryObject.getURI().replace("resource", "data").concat(".ttl"),
                "TTL");

        // Getting the subject of dbo:headquarter (Macedonia is the object)
        Property propertyHQ = model2.getProperty(dbo + "headquarter");

        Statement statementHQ = model2.listStatements(
                new SimpleSelector(null, propertyHQ, (RDFNode) null)).toList().get(1);

        Resource objectHQ = statementHQ.getSubject();

        System.out.println("HQ resource: " + objectHQ);

        // Third model for Macedonian Academy of Science and Arts
        Model model3 = ModelFactory.createDefaultModel();
        model3.read(
                objectHQ.getURI().replace("resource", "data").concat(".ttl"),
                "TTL");

        // Printing some facts for Macedonian Academy of Science and Arts
        String shortInfo = model3.listStatements(
                new SimpleSelector(null, model3.getProperty(dbo + "abstract"), (RDFNode) null))
                .toList()
                .stream()
                .filter(p -> p.getLanguage().equals("en"))
                .map(Statement::getString)
                .findFirst()
                .orElse("NO INFO");

        String formationDate = model3.listStatements(
                new SimpleSelector(null, model3.getProperty(dbo + "formationDate"), (RDFNode) null))
                .toList()
                .get(0).getString();

        String purpose = model3.listStatements(
                new SimpleSelector(null, model3.getProperty(dbo + "purpose"), (RDFNode) null))
                .toList()
                .get(0).getString();

        System.out.println("Macedonian Academy of Science and Arts");
        System.out.println("Abstract: " + shortInfo);
        System.out.println("Formation date: " + formationDate);
        System.out.println("Purpose: " + purpose);
    }
}
