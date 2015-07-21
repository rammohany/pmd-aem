package com.virtusa.aem.pmd;

import java.util.List;

import net.sourceforge.pmd.lang.java.ast.ASTClassOrInterfaceType;
import net.sourceforge.pmd.lang.java.ast.ASTExpression;
import net.sourceforge.pmd.lang.java.ast.ASTFinallyStatement;
import net.sourceforge.pmd.lang.java.ast.ASTLocalVariableDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTMethodDeclaration;
import net.sourceforge.pmd.lang.java.ast.ASTName;
import net.sourceforge.pmd.lang.java.ast.ASTStatementExpression;
import net.sourceforge.pmd.lang.java.rule.AbstractJavaRule;

import org.apache.commons.lang3.StringUtils;

public class CheckSessionLogoutRule extends AbstractJavaRule {
	public Object visit(ASTMethodDeclaration methodDecl, Object data) {

		List<ASTClassOrInterfaceType> variableDeclarations = methodDecl
				.findDescendantsOfType(ASTClassOrInterfaceType.class);
		if (variableDeclarations != null && variableDeclarations.size() > 0) {
			for (ASTClassOrInterfaceType varDecl : variableDeclarations) {
				if (StringUtils.equals(varDecl.getImage(),
						AEMPMDConstants.SESSION_STRING)) {
					String variableName = getSessionVariableName(varDecl);
					if (isVariableInitializedUsingLoginService(variableName,
							methodDecl)
							&& !isSessionLoggedOut(variableName, methodDecl)) {
						addViolation(data, varDecl);
					}
				}
			}
		}
		return super.visit(methodDecl, data);
	}

	/**
	 * Checks whether sessionVariable is logged out in finally block
	 * 
	 * @param sessionVariable
	 *            - The session variable
	 * @param methodDecl
	 *            - The method in which the session variable is declared
	 * @return - true/false
	 */
	private boolean isSessionLoggedOut(String sessionVariable,
			ASTMethodDeclaration methodDecl) {
		List<ASTFinallyStatement> finallyBlocks = methodDecl
				.findDescendantsOfType(ASTFinallyStatement.class);
		if (finallyBlocks != null) {
			for (ASTFinallyStatement finallyBlock : finallyBlocks) {
				// search for PrimaryPrefix->Name and check if
				// sessionVariable.logout is called
				List<ASTName> prefixNames = finallyBlock
						.findDescendantsOfType(ASTName.class);
				if (prefixNames != null) {
					for (ASTName name : prefixNames) {
						if (StringUtils.equals(name.getImage(), sessionVariable
								+ AEMPMDConstants.DOT
								+ AEMPMDConstants.LOGOUT_STRING)) {
							return true;
						}
					}
				}
			}
		}
		return false;
	}

	/**
	 * This method will check whether the session variable initialized using
	 * loginService call
	 * 
	 * @param variableName
	 *            - The session variable name.
	 * @param methodDecl
	 *            - The method handle.
	 * @return - true/false
	 */
	private boolean isVariableInitializedUsingLoginService(String variableName,
			ASTMethodDeclaration methodDecl) {
		List<ASTStatementExpression> statementExprs = methodDecl
				.findDescendantsOfType(ASTStatementExpression.class);
		for (ASTStatementExpression stmtExpr : statementExprs) {
			// Check if the variable name is same in the statement expression
			// Get stmtExpr->PrimaryExpression->PrimaryPrefix->Name
			try {
				String thisVarName = stmtExpr.jjtGetChild(0).jjtGetChild(0)
						.jjtGetChild(0).getImage();
				if (StringUtils.equals(thisVarName, variableName)) {
					List<ASTExpression> expressions = stmtExpr
							.findChildrenOfType(ASTExpression.class);
					// stmtExpr->Expression->PrimaryExpression->PrimaryPrefix->Name
					for (ASTExpression expr : expressions) {
						String rightSideExpr = expr.jjtGetChild(0).jjtGetChild(0)
								.jjtGetChild(0).getImage();
						if (StringUtils.contains(rightSideExpr, AEMPMDConstants.DOT
								+ AEMPMDConstants.LOGIN_SERVICE_STRING)) {
							return true;
						}
					}
				}
			} catch (Exception e) {
				// do nothing 
			}
		}
		return false;
	}

	private String getSessionVariableName(ASTClassOrInterfaceType classOrInt) {
		List<ASTLocalVariableDeclaration> varDecls = classOrInt
				.getParentsOfType(ASTLocalVariableDeclaration.class);
		String sessionVariableName = null;
		for (ASTLocalVariableDeclaration varDecl : varDecls) {
			sessionVariableName = varDecl.jjtGetChild(1).jjtGetChild(0)
					.getImage();
		}
		return sessionVariableName;
	}
}
