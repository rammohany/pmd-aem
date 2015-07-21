package com.virtusa.aem.pmd;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pmd.lang.java.ast.ASTMemberValuePair;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTNormalAnnotation;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * This rule checks for a @Reference annotation there is a bind method declared
 * in the annotation
 * 
 * @author RammohanY
 * 
 */
public class CheckBindForReferenceRule extends AbstractJavaRule {
	public Object visit(ASTNormalAnnotation annotation, Object data) {
		List<ASTName> names = annotation.findChildrenOfType(ASTName.class);
		boolean bindFound = false;
		for (ASTName name : names) {
			bindFound = false;
			if (StringUtils.equals(name.getImage(),
					AEMPMDConstants.REFERENCE_ANNOTATION_STR)) {
				// check for MemberValuePair whose image property is bind
				List<ASTMemberValuePair> pairs = annotation
						.findDescendantsOfType(ASTMemberValuePair.class);
				for (ASTMemberValuePair pair : pairs) {
					if (StringUtils.equals(pair.getImage(),
							AEMPMDConstants.BIND_STR)) {
						//return super.visit(annotation, data);
						bindFound = true;
					}
				}
				
				if (!bindFound) {
					addViolation(data, annotation);
				}
			}
		}
		return super.visit(annotation, data);
	}
}
