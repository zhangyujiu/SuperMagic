package com.supermagic;

import java.util.Hashtable;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;

public class Factory {
	
	public static ASTParser AST_Parser = ASTParser.newParser(AST.JLS8);
	public static String PATH_MATTHER_ALL = "glob:**/*.{java,kt,xml,gradle,pro,txt,c,h}";
	
	public static ASTParser newParser(int level) {
		ASTParser oo = ASTParser.newParser(level);// 锟角筹拷锟斤拷
		Hashtable<String, String> options = JavaCore.getDefaultOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_7);
		options.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_7);
		oo.setCompilerOptions(options);
		oo.setKind(ASTParser.K_COMPILATION_UNIT);
		return oo;
	}
}
