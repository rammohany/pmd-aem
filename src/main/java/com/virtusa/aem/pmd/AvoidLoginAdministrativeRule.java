package com.virtusa.aem.pmd;

import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * 
 * @author RammohanY
 *
 */
public class AvoidLoginAdministrativeRule extends AbstractJavaRule {
	public Object visit(ASTName astName, Object data) {
		String image = astName.getImage();
		if (image != null
				&& image.contains(AEMPMDConstants.DOT
						+ AEMPMDConstants.LOGIN_ADMINISTRATIVE_STRING)) {
			addViolation(data, astName);
		}
		return super.visit(astName, data);
	}
}
