package com.gvs.crm.model.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class Zip 
{
	public Zip()
	{
		
	}
	
	 public static void compress(String caminho, Collection arquivos, ZipOutputStream saida) throws IOException
	 {  
       ZipEntry elemento = null;
       
       for(Iterator i = arquivos.iterator() ; i.hasNext() ; )
       {
    	  File arquivo = (File) i.next();
    	   
    	  elemento = new ZipEntry(caminho + "/" + arquivo.getName());  
    	  saida.putNextEntry(elemento);  
    	   
          FileInputStream entrada = new FileInputStream(arquivo);  
          int buffer = 1024;
          byte[] b = new byte[buffer];  
          int r = -1;
          while(( r = entrada.read(b, 0, buffer)) != -1 ) 
          {
        	  saida.write(b, 0, r);
          }
          
          entrada.close();
          arquivo.delete();
       }
       saida.flush();  
	}  
}
