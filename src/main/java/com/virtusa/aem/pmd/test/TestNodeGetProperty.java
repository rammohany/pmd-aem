package com.virtusa.aem.pmd.test;

import javax.jcr.Node;
import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.sling.jcr.api.SlingRepository;

public class TestNodeGetProperty {
    @Reference(referenceInterface=SlingRepository.class, bind="bindRepository", unbind="unbindRepository", cardinality=ReferenceCardinality.MANDATORY_UNARY)
    protected SlingRepository repository;
    
    protected void bindRepository(SlingRepository repo) {
    	this.repository = repo;
    }

	public void testMethod() {
		Session adminSession = null;
		//Session anotherSession = null;
        try {
        	
            if (adminSession == null) {
                adminSession = repository.loginService(null, null);
            }
            Node node = adminSession.getNode("/ab");
            String prop1 = TestUtil.getProperty(node, "prop1");
            String prop2 = TestUtil.getProperty(node, "prop2");
            String prop3 = TestUtil.getProperty(node, "prop3");
            String prop6 = node.getProperty("prop6").getString();

        } catch (RepositoryException re) {
        	re.printStackTrace();
        } finally {
        	adminSession.logout();
        }
	}

}
