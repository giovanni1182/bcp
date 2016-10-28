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
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.hssf.util.Region;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.util.IOUtils;

import infra.config.InfraProperties;

public class ReaseguradorasXLS extends Excel
{
	public ReaseguradorasXLS(Collection lista, Date data, String obs, String textoUsuario,boolean ci) throws Exception
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
        
        HSSFFont fonteTextoN = wb.createFont();
        fonteTextoN.setFontHeightInPoints((short)8);
        fonteTextoN.setFontName("Arial");
        fonteTextoN.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFCellStyle estiloTextoN = wb.createCellStyle();
        estiloTextoN.setFont(fonteTextoN);
        
        HSSFFont fonteTexto = wb.createFont();
        fonteTexto.setFontHeightInPoints((short)8);
        fonteTexto.setFontName("Arial");
        
        HSSFCellStyle estiloData = wb.createCellStyle();
        estiloData.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
        
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
        estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
        estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
        
        //String dirImages = "D:/Projetos/BCP_infra_nova/images";
        
        InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
        byte [] bytes = IOUtils.toByteArray (is);
        int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG); 
        is.close();
        
        HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)3 , 5);  
        anchoVivaBem.setAnchorType(3);  
        planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
        
        HSSFRow row = planilha.createRow(6);
        HSSFCell celula = row.createCell(0);
        
        celula.setCellValue("REGISTRO PUBLICO DE EMPRESAS REASEGURADORAS DEL EXTERIOR HABILITADAS A OPERAR EN EL PAIS, AL " + dataStr);
        celula.setCellStyle(estiloTitulo);
        
        Region r = new Region(6, (short)0, 6, (short)16);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(8);
        celula = row.createCell(10);
        celula.setCellValue("Constancia Nº");
        celula.setCellStyle(estiloTituloTabela);
        
        r = new Region(8, (short)10, 8, (short)11);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(13);
        celula.setCellValue("Habilitación");
        celula.setCellStyle(estiloTituloTabela);
        
        row = planilha.createRow(9);
        celula = row.createCell(0);
        celula.setCellValue("Nº");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(1);
        celula.setCellValue("Cód. Entidad");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(2);
        celula.setCellValue("Compañías Reaseguradoras");
        celula.setCellStyle(estiloTituloTabela);
        
        r = new Region(9, (short)2, 9, (short)8);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(9);
        celula.setCellValue("País");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(10);
        celula.setCellValue("y/o Res. N°");
        celula.setCellStyle(estiloTituloTabela);
        
        r = new Region(9, (short)10, 9, (short)11);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(12);
        celula.setCellValue("Fecha");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(13);
        celula.setCellValue("Hasta");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(14);
        celula.setCellValue("Ramo/Grupo");
        celula.setCellStyle(estiloTituloTabela);
        
        r = new Region(9, (short)14, 9, (short)15);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(16);
        celula.setCellValue("CESION HASTA");
        celula.setCellStyle(estiloTituloTabela);
        
        int linha = 10;
        int cont = 1;
        
        for(Iterator i = lista.iterator() ; i.hasNext() ; )
        {
        	row = planilha.createRow(linha);
        	
        	String str = (String) i.next();
        	
        	String[] dados = str.split(";");
        	
        	String inscricao = dados[0];
        	String nome = dados[1];
        	String dataR = dados[2];
        	String numeroR = dados[3];
        	String dataV = dados[4];
        	String pais = dados[5];
        	String ramo = dados[6];
        	String cesion = dados[7] + "%";
        	
        	celula = row.createCell(0);
            celula.setCellValue(cont);
           	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(1);
            celula.setCellValue(inscricao);
           	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(2);
            celula.setCellValue(nome);
           	celula.setCellStyle(estiloTextoE);
           	
           	r = new Region(linha, (short)2, linha, (short)8);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(9);
            celula.setCellValue(pais);
           	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(10);
            celula.setCellValue(numeroR);
           	celula.setCellStyle(estiloTexto);
           	
            r = new Region(linha, (short)10, linha, (short)11);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(12);
            celula.setCellValue(dataR);
           	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(13);
            celula.setCellValue(dataV);
           	celula.setCellStyle(estiloTexto);
           	
           	celula = row.createCell(14);
            celula.setCellValue(ramo);
           	celula.setCellStyle(estiloTexto);
           	
           	r = new Region(linha, (short)14, linha, (short)15);
            planilha.addMergedRegion(r);
           	
           	celula = row.createCell(16);
            celula.setCellValue(cesion);
           	celula.setCellStyle(estiloTexto);
        	
        	linha++;
        	cont++;
        }
        
        if(!obs.equals(""))
        {
	        row = planilha.createRow(linha+1);
	        celula = row.createCell(0);
	        celula.setCellValue(obs);
	        celula.setCellStyle(estiloTextoE);
	        
	        r = new Region(linha+1, (short)0, linha+1, (short)9);
	        planilha.addMergedRegion(r);
	        
	        linha+=3;
        }
        else
        	linha+=1;
        
        row = planilha.createRow(linha);
        celula = row.createCell(0);
        celula.setCellValue("NOTA: A partir de la expiración de la habilitación, las inscripciones y renovaciones se regirán por los procedimientos establecidos por Resolución SS.SG. Nº 284/07 del 26/11/07, la que se encuentra a disposición en la página web de esta SIS.");
        celula.setCellStyle(estiloTextoJ);
        //celula.
        
        r = new Region(linha, (short)0, linha+1, (short)16);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(linha+3);
        celula = row.createCell(0);
        celula.setCellValue("Hecho por:");
        celula.setCellStyle(estiloTextoE_N);
        
        celula = row.createCell(2);
        celula.setCellValue("Vdo. Jefe DCOR");
        celula.setCellStyle(estiloTextoE_N);
        
        celula = row.createCell(4);
        celula.setCellValue("V°.B°. Intendente ICORAS");
        celula.setCellStyle(estiloTextoE_N);
        
        linha+=5;
        row = planilha.createRow(linha);
        celula = row.createCell(0);
        celula.setCellValue("Elaborado por: ICORAS - Intendencia de Control de Operaciones de Reaseguros y Auxiliares del Seguro.");
        celula.setCellStyle(estiloTextoN);
        r = new Region(linha, (short)0, linha, (short)9);
        planilha.addMergedRegion(r);
        
        linha+=2;
        row = planilha.createRow(linha);
        celula = row.createCell(0);
        celula.setCellValue(textoUsuario);
        celula.setCellStyle(estiloTextoN);
        r = new Region(linha, (short)0, linha, (short)9);
        planilha.addMergedRegion(r);
        
        wb.write(stream);

        stream.flush();

        stream.close();
	}
}
