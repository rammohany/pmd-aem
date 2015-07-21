package com.virtusa.aem.pmd.test;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.sling.jcr.api.SlingRepository;

public class TestLoginAdministrative {
    @Reference(referenceInterface=SlingRepository.class, bind="bindRepository", unbind="unbindRepository", cardinality=ReferenceCardinality.MANDATORY_UNARY)
    protected SlingRepository repository;

	public void testMethod() {
		Session adminSession = null;
        try {
        	
            if (adminSession == null) {
                adminSession = repository.loginAdministrative(null);
            }
            Node node = adminSession.getNode("/ab");
            
            String prop1 = node.getProperty("prop1").getString();
            String prop2 = node.getProperty("prop2").getString();
            String prop3 = node.getProperty("prop3").getString();
            String prop4 = node.getProperty("prop4").getString();
            String prop5 = node.getProperty("prop5").getString();
            String prop6 = node.getProperty("prop6").getString();

        } catch (RepositoryException re) {
        	re.printStackTrace();

        }
	}

}
