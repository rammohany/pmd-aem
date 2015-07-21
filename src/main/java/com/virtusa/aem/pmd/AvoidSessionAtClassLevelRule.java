package com.virtusa.aem.pmd;

import java.util.List;

import org.apache.commons.lang3.StringUtils;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceBodyDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTFieldDeclaration;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

/**
 * This rule will check for Session variables declared at class level and report
 * them as violation.
 * 
 * @author RammohanY
 *
 */
public class AvoidSessionAtClassLevelRule extends AbstractJavaRule {
	public Object visit(ASTClassOrInterfaceBodyDeclaration body, Object data) {
		List<ASTFieldDeclaration> fields = body
				.findDescendantsOfType(ASTFieldDeclaration.class);
		if (fields != null && fields.size() > 0) {
			for (ASTFieldDeclaration field : fields) {
				List<ASTClassOrInterfaceType> types = field
						.findDescendantsOfType(ASTClassOrInterfaceType.class);
				for (ASTClassOrInterfaceType type : types) {
					if (StringUtils.equals(type.getImage(),
							AEMPMDConstants.SESSION_STRING)) {
						addViolation(data, field);
					}
				}
			}
		}

		return super.visit(body, data);
	}
}
