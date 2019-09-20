package com.leanit.modulegen;

import com.leanit.codegen.ModuleGenerator;

public class DemoModuleGenerator{
	  

	
	private static String dbUrl = "jdbc:mysql://127.0.0.1:3306/jpress-2rc2-1?useInformationSchema=true";
	    private static String dbUser = "root";
	    private static String dbPassword = "Admin123";


	    private static String moduleName = "product";
	    private static String dbTables = "product";
	    private static String modelPackage = "com.leanit.module.product.model";
	    private static String servicePackage = "com.leanit.module.product.service";

	    public static void main(String[] args) {

	    	
	    	ModuleGenerator moduleGenerator = new ModuleGenerator(moduleName, dbUrl, dbUser, dbPassword, dbTables, modelPackage, servicePackage,true);
	        
	        moduleGenerator.gen();

	    }
	   
	    
}
