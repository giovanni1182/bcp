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

public class CorredoresXLS extends Excel 
{
	public CorredoresXLS(Collection lista, Date data, String obs, String textoUsuario, boolean ci) throws Exception
	{
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
        
        HSSFCellStyle estiloTextoCor = wb.createCellStyle();
        estiloTextoCor.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTextoCor.setFont(fonteTexto);
        estiloTextoCor.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloTextoCor.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        HSSFFont fonteTextoN = wb.createFont();
        fonteTextoN.setFontHeightInPoints((short)8);
        fonteTextoN.setFontName("Arial");
        fonteTextoN.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
        HSSFCellStyle estiloTextoN = wb.createCellStyle();
        estiloTextoN.setFont(fonteTextoN);
        
        HSSFCellStyle estiloTextoE = wb.createCellStyle();
        estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        estiloTextoE.setFont(fonteTexto);
        
        HSSFCellStyle estiloTextoCorE = wb.createCellStyle();
        estiloTextoCorE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        estiloTextoCorE.setFont(fonteTexto);
        estiloTextoCorE.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloTextoCorE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
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
        
        celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
        celula.setCellStyle(estiloTitulo);
        
        Region r = new Region(6, (short)0, 6, (short)9);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(7);
        celula = row.createCell(0);
        
        celula.setCellValue("INTENDENCIA DE CONTROL DE OPERACIONES DE REASEGUROS Y AUXILIARES DEL SEGURO");
        celula.setCellStyle(estiloTitulo);
        
        r = new Region(7, (short)0, 7, (short)9);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(8);
        celula = row.createCell(0);
        
        celula.setCellValue("CORREDORES DE SEGUROS CON MATRICULA VIGENTE - "+mes.toUpperCase()+" - "+ano+"");
        celula.setCellStyle(estiloTitulo);
        
        r = new Region(8, (short)0, 8, (short)9);
        planilha.addMergedRegion(r);
        
        row = planilha.createRow(10);
        celula = row.createCell(0);
        celula.setCellValue("Orden");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(1);
        celula.setCellValue("Matr.");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(2);
        celula.setCellValue("CORREDORES");
        celula.setCellStyle(estiloTituloTabela);
        
        r = new Region(10, (short)2, 10, (short)6);
        planilha.addMergedRegion(r);
        
        celula = row.createCell(7);
        celula.setCellValue("Fecha de Res.");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(8);
        celula.setCellValue("N° de Res.");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(9);
        celula.setCellValue("Vencimiento");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(10);
        celula.setCellValue("Dirección Comercial");
        celula.setCellStyle(estiloTituloTabela);
        
        celula = row.createCell(11);
        celula.setCellValue("Tel. Comercial");
        celula.setCellStyle(estiloTituloTabela);
        if(ci)
        {
        	celula = row.createCell(12);
            celula.setCellValue("CI o RUC");
            celula.setCellStyle(estiloTituloTabela);
        }
        
        int linha = 11;
        int cont = 1;
        boolean cor = false;
        
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
        	String endereco = dados[5];
        	String fone = dados[6];
        	String ruc = dados[7];
        	
        	celula = row.createCell(0);
            celula.setCellValue(cont);
            if(cor)
            	celula.setCellStyle(estiloTextoCor);
            else
            	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(1);
            celula.setCellValue(inscricao);
            if(cor)
            	celula.setCellStyle(estiloTextoCor);
            else
            	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(2);
            celula.setCellValue(nome);
            if(cor)
            	celula.setCellStyle(estiloTextoCorE);
            else
            	celula.setCellStyle(estiloTextoE);
            
            r = new Region(linha, (short)2, linha, (short)6);
            planilha.addMergedRegion(r);
            
            celula = row.createCell(7);
            celula.setCellValue(dataR);
            if(cor)
            	celula.setCellStyle(estiloTextoCor);
            else
            	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(8);
            celula.setCellValue(numeroR);
            if(cor)
            	celula.setCellStyle(estiloTextoCor);
            else
            	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(9);
            celula.setCellValue(dataV);
            if(cor)
            	celula.setCellStyle(estiloTextoCor);
            else
            	celula.setCellStyle(estiloTexto);
            
            celula = row.createCell(10);
            celula.setCellValue(endereco);
            if(cor)
            	celula.setCellStyle(estiloTextoCorE);
            else
            	celula.setCellStyle(estiloTextoE);
            
            celula = row.createCell(11);
            celula.setCellValue(fone);
            if(cor)
            	celula.setCellStyle(estiloTextoCorE);
            else
            	celula.setCellStyle(estiloTextoE);
            
            if(ci)
            {
            	 celula = row.createCell(12);
                 celula.setCellValue(ruc);
                 if(cor)
                 	celula.setCellStyle(estiloTextoCorE);
                 else
                 	celula.setCellStyle(estiloTextoE);
            }
        	
        	linha++;
        	cont++;
        	cor=!cor;
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
        
        linha++;
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
        celula.setCellStyle(estiloTexto);
        r = new Region(linha, (short)0, linha, (short)9);
        planilha.addMergedRegion(r);
        
        linha+=2;
        String dataStr = new SimpleDateFormat("dd/MM/yyyy").format(data);
        row = planilha.createRow(linha);
        celula = row.createCell(0);
        celula.setCellValue("Los Corredores " + dataStr);
        celula.setCellStyle(estiloTexto);
        r = new Region(linha, (short)0, linha, (short)9);
        planilha.addMergedRegion(r);
        
        wb.write(stream);

        stream.flush();

        stream.close();
	}
}
