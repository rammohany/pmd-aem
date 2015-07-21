package com.virtusa.aem.pmd.test;

import javax.jcr.RepositoryException;
import javax.jcr.Session;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.ReferenceCardinality;
import org.apache.sling.jcr.api.SlingRepository;

public class TestSessionLogout {
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

        } catch (RepositoryException re) {
        	re.printStackTrace();
        } finally {
        	adminSession.logout();
        }
	}

}
