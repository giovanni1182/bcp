package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import infra.config.InfraProperties;

public class AuditoresXLS extends Excel
{
	public AuditoresXLS(Collection lista,Collection lista2, Date data, String obs, String textoUsuario,boolean ci) throws Exception
	{
		String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
		
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		 String ano = new SimpleDateFormat("yyyy").format(data);
	     String mes = this.getMesExtenso(data);
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		 this.setCaminho(caminho);
		
		HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet planilha = wb.createSheet("Planilha");
        
        HSSFFont fonteTitulo = wb.createFont();
        fonteTitulo.setFontHeightInPoints((short)10);
        fonteTitulo.setFontName("Arial");
        fonteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFFont fonteTituloTabela = wb.createFont();
        fonteTituloTabela.setFontHeightInPoints((short)8);
        fonteTituloTabela.setFontName("Arial");
        fonteTituloTabela.setColor(HSSFColor.WHITE.index);
        
        HSSFFont fonteTituloTabela2 = wb.createFont();
        fonteTituloTabela2.setFontHeightInPoints((short)8);
        fonteTituloTabela2.setFontName("Arial");
        fonteTituloTabela2.setColor(HSSFColor.BLACK.index);
        fonteTituloTabela2.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFFont fonteTexto = wb.createFont();
        fonteTexto.setFontHeightInPoints((short)8);
        fonteTexto.setFontName("Arial");
        
        HSSFFont fonteTextoN = wb.createFont();
        fonteTextoN.setFontHeightInPoints((short)8);
        fonteTextoN.setFontName("Arial");
        fonteTextoN.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFCellStyle estiloTitulo = wb.createCellStyle();
        estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTitulo.setFont(fonteTitulo);
        
        HSSFCellStyle estiloTexto = wb.createCellStyle();
        estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTexto.setFont(fonteTexto);
        
        HSSFCellStyle estiloTextoE = wb.createCellStyle();
        estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        estiloTextoE.setFont(fonteTexto);
        
        HSSFCellStyle estiloTextoE_N = wb.createCellStyle();
        estiloTextoE_N.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        estiloTextoE_N.setFont(fonteTextoN);
        
        HSSFCellStyle estiloTextoJ = wb.createCellStyle();
        estiloTextoJ.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
        estiloTextoJ.setFont(fonteTextoN);
        
        HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
        estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTituloTabela.setFont(fonteTituloTabela);
        estiloTituloTabela.setFillForegroundColor(HSSFColor.BLACK.index);
        estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFCellStyle estiloTituloTabela2 = wb.createCellStyle();
        estiloTituloTabela2.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTituloTabela2.setFont(fonteTituloTabela2);
        estiloTituloTabela2.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloTituloTabela2.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
        
        //String dirImages = "D:/Projetos/BCP_infra_nova/images";
        
        InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
        byte [] bytes = IOUtils.toByteArray (is);
        int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG); 
        is.close();
        
        HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)3 , 5);  
        anchoVivaBem.setAnchorType(3);  
        planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
        
        HSSFRow row = planilha.createRow(1);
        HSSFCell celula = row.createCell(5);
        
        celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
        celula.setCellStyle(estiloTitulo);
        Region r = new Region(1, (short)5, 1, (short)17);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(2);
        celula = row.createCell(5);
        celula.setCellValue("INTENDENCIA DE CONTROL FINANCIERO");
        celula.setCellStyle(estiloTitulo);
        
        r = new Region(2, (short)5, 2, (short)17);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(4);
        celula = row.createCell(5);
        celula.setCellValue("REGISTRO DE AUDITORES EXTERNOS PARA LAS COMPAÑÍAS DE SEGUROS");
        celula.setCellStyle(estiloTitulo);
        
        r = new Region(4, (short)5, 4, (short)17);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(5);
        celula = row.createCell(5);
        celula.setCellValue("FIRMAS DE AUDITORAS EXTERNAS INSCRIPTAS CONFORME LA RESOLUCION SS.SG. Nº 241/04 DEL 30.06.2004");
        celula.setCellStyle(estiloTitulo);
        
        r = new Region(5, (short)5, 5, (short)17);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(7);
        celula = row.createCell(0);
        celula.setCellValue("REGISTRO DE AUDITORES EXTERNOS HABILITADOS");
        celula.setCellStyle(estiloTituloTabela);
        
        r = new Region(7, (short)0, 7, (short)18);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(8);
        celula = row.createCell(0);
        celula.setCellValue("Nº de Orden");
        celula.setCellStyle(estiloTituloTabela2);
        
        celula = row.createCell(1);
        celula.setCellValue("Resolución SS.SG. Nº");
        celula.setCellStyle(estiloTituloTabela2);
        
        celula = row.createCell(2);
        celula.setCellValue("Fecha de Registro");
        celula.setCellStyle(estiloTituloTabela2);
        
        celula = row.createCell(3);
        celula.setCellValue("Vigencia del Registro");
        celula.setCellStyle(estiloTituloTabela2);
        
        celula = row.createCell(4);
        celula.setCellValue("Firma Auditora");
        celula.setCellStyle(estiloTituloTabela2);
        
        r = new Region(8, (short)4, 8, (short)7);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(8);
        celula.setCellValue("Dirección");
        celula.setCellStyle(estiloTituloTabela2);
        
        r = new Region(8, (short)8, 8, (short)12);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(13);
        celula.setCellValue("Teléfono");
        celula.setCellStyle(estiloTituloTabela2);
        
        celula = row.createCell(14);
        celula.setCellValue("Fax");
        celula.setCellStyle(estiloTituloTabela2);
        
        celula = row.createCell(15);
        celula.setCellValue("Email");
        celula.setCellStyle(estiloTituloTabela2);
        r = new Region(8, (short)15, 8, (short)17);
        planilha.addMergedRegion(r);
        
        if(ci)
        {
        	  celula = row.createCell(18);
              celula.setCellValue("CI o RUC");
              celula.setCellStyle(estiloTituloTabela2);
        }
        
        int linha = 9;
        int cont = 1;
        
        for(Iterator i = lista.iterator() ; i.hasNext() ; )
        {
        	row = planilha.createRow(linha);
        	
        	String str = (String) i.next();
        	
        	String[] dados = str.split(";");
        	
        	String resolucao = dados[0];
        	String dataR = dados[1];
        	String dataV = dados[2];
        	String nome = dados[3];
        	String endereco = dados[4];
        	String tel = dados[5];
        	String fax = dados[6];
        	String email = dados[7];
        	String ruc = dados[8];
        	
        	celula = row.createCell(0);
            celula.setCellValue(cont);
           	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(1);
            celula.setCellValue(resolucao);
           	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(2);
            celula.setCellValue(dataR);
           	celula.setCellStyle(estiloTexto);
           	
           	celula = row.createCell(3);
            celula.setCellValue(dataV);
           	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(4);
            celula.setCellValue(nome);
           	celula.setCellStyle(estiloTextoE);
           	
           	r = new Region(linha, (short)4, linha, (short)7);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(8);
            celula.setCellValue(endereco);
           	celula.setCellStyle(estiloTextoE);
           	
           	r = new Region(linha, (short)8, linha, (short)12);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(13);
            celula.setCellValue(tel);
           	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(14);
            celula.setCellValue(fax);
           	celula.setCellStyle(estiloTexto);
           	
           	celula = row.createCell(15);
            celula.setCellValue(email);
           	celula.setCellStyle(estiloTextoE);
            r = new Region(linha, (short)15, linha, (short)17);
            planilha.addMergedRegion(r);
            
            if(ci)
            {
            	celula = row.createCell(18);
                celula.setCellValue(ruc);
               	celula.setCellStyle(estiloTexto);
            }
        	
        	linha++;
        	cont++;
        }
        
        row = planilha.createRow(linha);
        celula = row.createCell(0);
        celula.setCellValue("FECHA DE ACTUALIZACION " + new SimpleDateFormat("dd/MM/yyyy").format(data));
        celula.setCellStyle(estiloTituloTabela);
        
        r = new Region(linha, (short)0, linha, (short)18);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(linha+2);
        celula = row.createCell(0);
        celula.setCellValue("REGISTRO DE AUDITORES EXTERNOS PENDIENTES DE REINSCRIPCIÓN");
        celula.setCellStyle(estiloTituloTabela);
        
        r = new Region(linha+2, (short)0, linha+2, (short)18);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(linha+3);
        celula = row.createCell(0);
        celula.setCellValue("Nº de Orden");
        celula.setCellStyle(estiloTituloTabela2);
        
        celula = row.createCell(1);
        celula.setCellValue("Resolución SS.SG. Nº");
        celula.setCellStyle(estiloTituloTabela2);
        
        celula = row.createCell(2);
        celula.setCellValue("Fecha de Registro");
        celula.setCellStyle(estiloTituloTabela2);
        
        celula = row.createCell(3);
        celula.setCellValue("Vigencia del Registro");
        celula.setCellStyle(estiloTituloTabela2);
        
        celula = row.createCell(4);
        celula.setCellValue("Firma Auditora");
        celula.setCellStyle(estiloTituloTabela2);
        
        r = new Region(linha+3, (short)4, linha+3, (short)7);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(8);
        celula.setCellValue("Dirección");
        celula.setCellStyle(estiloTituloTabela2);
        
        r = new Region(linha+3, (short)8, linha+3, (short)12);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(13);
        celula.setCellValue("Teléfono");
        celula.setCellStyle(estiloTituloTabela2);
        
        celula = row.createCell(14);
        celula.setCellValue("Fax");
        celula.setCellStyle(estiloTituloTabela2);
        
        celula = row.createCell(15);
        celula.setCellValue("Email");
        celula.setCellStyle(estiloTituloTabela2);
        
        r = new Region(linha+3, (short)15, linha+3, (short)17);
        planilha.addMergedRegion(r);
        
        if(ci)
        {
        	  celula = row.createCell(18);
              celula.setCellValue("CI o RUC");
              celula.setCellStyle(estiloTituloTabela2);
        }
        
        linha +=4;
        cont = 1;
        
        for(Iterator i = lista2.iterator() ; i.hasNext() ; )
        {
        	row = planilha.createRow(linha);
        	
        	String str = (String) i.next();
        	
        	String[] dados = str.split(";");
        	
        	String resolucao = dados[0];
        	String dataR = dados[1];
        	String dataV = dados[2];
        	String nome = dados[3];
        	String endereco = dados[4];
        	String tel = dados[5];
        	String fax = dados[6];
        	String email = dados[7];
        	String ruc = dados[8];
        	
        	celula = row.createCell(0);
            celula.setCellValue(cont);
           	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(1);
            celula.setCellValue(resolucao);
           	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(2);
            celula.setCellValue(dataR);
           	celula.setCellStyle(estiloTexto);
           	
           	celula = row.createCell(3);
            celula.setCellValue(dataV);
           	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(4);
            celula.setCellValue(nome);
           	celula.setCellStyle(estiloTextoE);
           	
           	r = new Region(linha, (short)4, linha, (short)7);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(8);
            celula.setCellValue(endereco);
           	celula.setCellStyle(estiloTextoE);
           	
           	r = new Region(linha, (short)8, linha, (short)12);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(13);
            celula.setCellValue(tel);
           	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(14);
            celula.setCellValue(fax);
           	celula.setCellStyle(estiloTexto);
           	
           	celula = row.createCell(15);
            celula.setCellValue(email);
           	celula.setCellStyle(estiloTextoE);
           	
            r = new Region(linha, (short)15, linha, (short)17);
            planilha.addMergedRegion(r);
        	
            if(ci)
            {
            	  celula = row.createCell(18);
                  celula.setCellValue(ruc);
                  celula.setCellStyle(estiloTexto);
            }
            
        	linha++;
        	cont++;
        }
        
        row = planilha.createRow(linha);
        celula = row.createCell(0);
        celula.setCellValue("FECHA DE ACTUALIZACION " + new SimpleDateFormat("dd/MM/yyyy").format(data));
        celula.setCellStyle(estiloTituloTabela);
        
        r = new Region(linha, (short)0, linha, (short)18);
        planilha.addMergedRegion(r);
        
        if(!obs.equals(""))
        {
	        row = planilha.createRow(linha+2);
	        celula = row.createCell(0);
	        celula.setCellValue(obs);
	        celula.setCellStyle(estiloTextoE);
	        
	        r = new Region(linha+2, (short)0, linha+2, (short)18);
	        planilha.addMergedRegion(r);
	        
	        linha+=4;
        }
        else
        	linha+=2;
        
        row = planilha.createRow(linha);
        celula = row.createCell(0);
        celula.setCellValue("AVISO IMPORTANTE: Las firmas que están con reinscripcion pendiente no podrán firmar contrato de prestacion de servicios con las compañías aseguradoras conforme la Resolución SS.SG. Nº 242/04 del 30.06.04.");
        celula.setCellStyle(estiloTextoJ);
        
        r = new Region(linha, (short)0, linha+1, (short)18);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(linha+3);
        celula = row.createCell(0);
        celula.setCellValue("AVISO IMPORTANTE: Considerando la normativa vigente en la cual se expresa que Ninguna Firma de Auditoria Independiente podrá auditar a una misma Entidad de Seguros y/o Reaseguros por más de 5 (cinco) ejercicios consecutivos , debiendo transcurrir por lo " +
"menos 1 (un) año para que proceda a una nueva contratación. Se deben considerar los ejercicios cerrados siguientes: Ejercicio 2005/2006, Ejercicio 2006/2007, Ejercicio 2007/2008, Ejercicio 2008/2009 y Ejercicio 2009/2010, para la contratación de los servicios de " + 
"auditoria independiente.");
        celula.setCellStyle(estiloTextoJ);
        
        r = new Region(linha+3, (short)0, linha+5, (short)18);
        planilha.addMergedRegion(r);
        
        linha+=8;
        row = planilha.createRow(linha);
        celula = row.createCell(0);
        celula.setCellValue("Elaborado por: ICF - Intendencia de Control Financiero.");
        celula.setCellStyle(estiloTextoE_N);
        r = new Region(linha, (short)0, linha, (short)18);
        planilha.addMergedRegion(r);
        
        linha+=2;
        row = planilha.createRow(linha);
        celula = row.createCell(0);
        celula.setCellValue(textoUsuario);
        celula.setCellStyle(estiloTextoE_N);
        r = new Region(linha, (short)0, linha, (short)18);
        planilha.addMergedRegion(r);
        
        wb.write(stream);

        stream.flush();

        stream.close();
	}
}
