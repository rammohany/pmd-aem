package com.virtusa.aem.pmd;

import java.util.ArrayList;
import java.util.List;

import net.sourceforge.pmd.RuleContext;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBody;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTVariableDeclaratorId;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import org.apache.commons.lang3.StringUtils;

/**
 * This rule will suggest to use a utility method to getProperty if there are
 * more than 5 node.getProperty calls across all classes
 * 
 * @author RammohanY
 * 
 */
public class SuggestToUseUtilityMethodForGetPropertyRule extends
		AbstractJavaRule {
	private static final int MAX_ALLOWED = 5;
	private static int total = 0;

	public Object visit(ASTClassOrInterfaceBody clazz, Object data) {
		RuleContext ctx = (RuleContext) data;
		// get all declarations which are of Node type
		List<String> allJcrNodes = getJCRNodes(clazz);

		// for every node check if there is a call to node.getProperty method
		for (String node : allJcrNodes) {
			List<ASTName> methodInvocations = clazz
					.findDescendantsOfType(ASTName.class);
			for (ASTName methodInvocation : methodInvocations) {
				if (StringUtils.equals(methodInvocation.getImage(), node
						+ AEMPMDConstants.DOT + AEMPMDConstants.GET_PROP_STR)) {
					total++;
					if (total > MAX_ALLOWED) {
						addViolation(ctx, methodInvocation);			
					}
				}
			}
		}
		return super.visit(clazz, data);
	}

	private List<String> getJCRNodes(ASTClassOrInterfaceBody clazz) {
		List<String> nodes = new ArrayList<String>();
		List<ASTLocalVariableDeclaration> astNodes = clazz
				.findDescendantsOfType(ASTLocalVariableDeclaration.class);
		for (ASTLocalVariableDeclaration localVar : astNodes) {
			// localVar->Type->ReferenceType->ClassOrInterfaceType
			// contains the type of the variable
			ASTClassOrInterfaceType type = localVar
					.getFirstDescendantOfType(ASTClassOrInterfaceType.class);
			if (type != null) {
				if (StringUtils.equals(type.getImage(),
						AEMPMDConstants.NODE_STR)) {
					ASTVariableDeclaratorId varId = localVar
							.getFirstDescendantOfType(ASTVariableDeclaratorId.class);
					if (!StringUtils.isEmpty(varId.getImage())) {
						nodes.add(varId.getImage());
					}
				}
			}
		}
		return nodes;
	}
}
