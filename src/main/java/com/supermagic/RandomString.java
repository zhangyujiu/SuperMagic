package com.supermagic;

import java.util.Arrays;
import java.util.List;

public class RandomString {

	private static String dataType = "boolean,byte,char,double,false,float,int,long,new,short,true,void,instanceof,break,case,catch,continue,default,do,else,for,if,return,switch,try,while,finally,throw,this,super,abstract,final,native,private,protected,public,static,synchronized,transient,volatile,class,extends,implements,interface,package,import,throws,cat,future,generic,innerr,operator,outer,rest,var,rue,false,null";
	private static List<String> senseText = Arrays.asList(dataType.split(","));

	/**
	 * length
	 * @param length
	 * @return
	 */
	public static String randomeString(int length) {
		String lowercase = "abcdefghijklmnopqrstuvwxyz";
		String all = "0123456789abcdefghijklmnopqrstuvwxyz";
		String result = lowercase.charAt((int) (Math.random() * lowercase.length())) + "";
		for (int i = 1; i < length; i++) {
			int last = (int) (Math.random() * (all.length()));
			result += all.charAt(last);
		}
		if(senseText.contains(result)) {
			return randomeString(length);
		}
		
		return result;
	}
}
