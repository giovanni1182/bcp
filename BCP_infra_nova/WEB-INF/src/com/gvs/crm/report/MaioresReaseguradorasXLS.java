package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
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

public class MaioresReaseguradorasXLS extends Excel
{
	public MaioresReaseguradorasXLS(String tipoValor,Date dataInicio,Date dataFim, String situacao, double dolar, double euro, double real, double pesoArg, double pesoUru, double yen, Collection<String> dados, boolean porPais, String textoUsuario) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		
		 this.setCaminho(caminho);
		
		HSSFWorkbook wb = new HSSFWorkbook();

       HSSFSheet planilha = wb.createSheet("Planilha");
       
       HSSFFont fonteTituloTabela = wb.createFont();
       fonteTituloTabela.setFontHeightInPoints((short)8);
	   fonteTituloTabela.setFontName("Arial");
	   fonteTituloTabela.setColor(HSSFColor.WHITE.index);
	      
       HSSFFont fonteTitulo = wb.createFont();
       fonteTitulo.setFontHeightInPoints((short)10);
       fonteTitulo.setFontName("Arial");
       fonteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
       
       HSSFFont fonteTexto = wb.createFont();
       fonteTexto.setFontHeightInPoints((short)8);
       fonteTexto.setFontName("Arial");
       
       HSSFCellStyle estiloTitulo = wb.createCellStyle();
       estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTitulo.setFont(fonteTitulo);
       
       HSSFCellStyle estiloTituloE = wb.createCellStyle();
       estiloTituloE.setFont(fonteTitulo);
       
       HSSFCellStyle estiloTexto = wb.createCellStyle();
       estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
       estiloTexto.setFont(fonteTexto);
       
       HSSFCellStyle estiloTextoE = wb.createCellStyle();
       estiloTextoE.setFont(fonteTexto);
       
       HSSFCellStyle estiloTextoD = wb.createCellStyle();
       estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
       estiloTextoD.setFont(fonteTexto);
       
       HSSFCellStyle estiloTextoDN = wb.createCellStyle();
       estiloTextoDN.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
       estiloTextoDN.setFont(fonteTitulo);
       
       HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
	   estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	   estiloTituloTabela.setFont(fonteTituloTabela);
	   estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	   estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
       
       String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
       
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
       
       int linha = 7; 
       
       row = planilha.createRow(linha);
       celula = row.createCell(0);
       celula.setCellValue("Mayores Reaseguradoras".toUpperCase());
       celula.setCellStyle(estiloTitulo);
       r = new Region(linha, (short)0, linha, (short)9);
       planilha.addMergedRegion(r);
       
       linha+=2;
       
       if(tipoValor.equals("valorPrima"))
    	   tipoValor = "Prima";
       else if(tipoValor.equals("valorCapital"))
    	   tipoValor = "Capital";
       else if(tipoValor.equals("valorComissao"))
    	   tipoValor = "Comisión";
       
       row = planilha.createRow(linha);
       celula = row.createCell(0);
       celula.setCellValue("Consulta por: " + tipoValor);
       celula.setCellStyle(estiloTituloE);
       r = new Region(linha, (short)0, linha, (short)9);
       planilha.addMergedRegion(r);
       
       linha++;
       
       row = planilha.createRow(linha);
       celula = row.createCell(0);
       celula.setCellValue("Pólizas Vigentes desde: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
       celula.setCellStyle(estiloTituloE);
       r = new Region(linha, (short)0, linha, (short)9);
       planilha.addMergedRegion(r);
       
       linha++;
       
       row = planilha.createRow(linha);
       celula = row.createCell(0);
       if(situacao.equals("0"))
    	   celula.setCellValue("Situación: Todas");
       else
    	   celula.setCellValue("Situación: " + situacao);
       celula.setCellStyle(estiloTituloE);
       r = new Region(linha, (short)0, linha, (short)9);
       planilha.addMergedRegion(r);
       
       linha+=2;
       
       row = planilha.createRow(linha);
       celula = row.createCell(0);
   	   celula.setCellValue("Cotización del día en Guaraní");
       celula.setCellStyle(estiloTituloE);
       r = new Region(linha, (short)0, linha, (short)9);
       planilha.addMergedRegion(r);
       
       linha++;
       
       row = planilha.createRow(linha);
       celula = row.createCell(0);
   	   celula.setCellValue("Dólar USA: " + formataValor.format(dolar));
       celula.setCellStyle(estiloTextoE);
       r = new Region(linha, (short)0, linha, (short)1);
       planilha.addMergedRegion(r);
       
       celula = row.createCell(2);
   	   celula.setCellValue("Euro: " + formataValor.format(euro));
       celula.setCellStyle(estiloTextoE);
       r = new Region(linha, (short)2, linha, (short)3);
       planilha.addMergedRegion(r);
       
       celula = row.createCell(4);
   	   celula.setCellValue("Real: " + formataValor.format(real));
       celula.setCellStyle(estiloTextoE);
       r = new Region(linha, (short)4, linha, (short)5);
       planilha.addMergedRegion(r);
       
       celula = row.createCell(6);
   	   celula.setCellValue("Peso Arg.: " + formataValor.format(pesoArg));
       celula.setCellStyle(estiloTextoE);
       r = new Region(linha, (short)6, linha, (short)7);
       planilha.addMergedRegion(r);
       
       celula = row.createCell(8);
   	   celula.setCellValue("Peso Uru.: " + formataValor.format(pesoUru));
       celula.setCellStyle(estiloTextoE);
       r = new Region(linha, (short)8, linha, (short)9);
       planilha.addMergedRegion(r);
       
       celula = row.createCell(10);
   	   celula.setCellValue("Yen: " + formataValor.format(yen));
       celula.setCellStyle(estiloTextoE);
       r = new Region(linha, (short)10, linha, (short)11);
       planilha.addMergedRegion(r);
       
   		double totalPrima = 0;
   		double totalCapital = 0;
   		double totalComissao = 0;
       
       linha+=2;
       
       row = planilha.createRow(linha);
       celula = row.createCell(0);
       if(porPais)
    	   celula.setCellValue("País");
       else
    	   celula.setCellValue("Reaseguradora");
       celula.setCellStyle(estiloTituloTabela);
       r = new Region(linha, (short)0, linha, (short)2);
       planilha.addMergedRegion(r);
       
       if(tipoValor.equals("Prima"))
		{
    	   celula = row.createCell(3);
       	   celula.setCellValue("Prima");
           celula.setCellStyle(estiloTituloTabela);
           r = new Region(linha, (short)3, linha, (short)5);
           planilha.addMergedRegion(r);
           
           celula = row.createCell(6);
       	   celula.setCellValue("Capital");
           celula.setCellStyle(estiloTituloTabela);
           r = new Region(linha, (short)6, linha, (short)8);
           planilha.addMergedRegion(r);
           
           celula = row.createCell(9);
       	   celula.setCellValue("Comisión");
           celula.setCellStyle(estiloTituloTabela);
           r = new Region(linha, (short)9, linha, (short)11);
           planilha.addMergedRegion(r);
		}
       else if(tipoValor.equals("Capital"))
       {
    	   celula = row.createCell(3);
      	   celula.setCellValue("Capital");
      	   celula.setCellStyle(estiloTituloTabela);
      	   r = new Region(linha, (short)3, linha, (short)5);
      	   planilha.addMergedRegion(r);
          
      	   celula = row.createCell(6);
      	   celula.setCellValue("Prima");
      	   celula.setCellStyle(estiloTituloTabela);
      	   r = new Region(linha, (short)6, linha, (short)8);
      	   planilha.addMergedRegion(r);
          
      	   celula = row.createCell(9);
      	   celula.setCellValue("Comisión");
      	   celula.setCellStyle(estiloTituloTabela);
      	   r = new Region(linha, (short)9, linha, (short)11);
      	   planilha.addMergedRegion(r);
		}
       else
       {
    	   celula = row.createCell(3);
      	   celula.setCellValue("Comisión");
      	   celula.setCellStyle(estiloTituloTabela);
      	   r = new Region(linha, (short)3, linha, (short)5);
      	   planilha.addMergedRegion(r);
          
      	   celula = row.createCell(6);
    	   celula.setCellValue("Capital");
    	   celula.setCellStyle(estiloTituloTabela);
    	   r = new Region(linha, (short)6, linha, (short)8);
    	   planilha.addMergedRegion(r);
    	   
      	   celula = row.createCell(9);
      	   celula.setCellValue("Prima");
      	   celula.setCellStyle(estiloTituloTabela);
      	   r = new Region(linha, (short)9, linha, (short)11);
      	   planilha.addMergedRegion(r);
       }
       
       linha++;
       
       for(Iterator<String> i = dados.iterator() ; i.hasNext() ; )
		{
			String linhaSuja = i.next();
			
			String linha2[] = linhaSuja.split(";");
			
			String nome = linha2[0];
			double capital = Double.valueOf(linha2[1]);
			double prima = Double.valueOf(linha2[2]);
			double comissao = Double.valueOf(linha2[3]);
			
			totalCapital+=capital;
			totalPrima+=prima;
			totalComissao+=comissao;
			
			row = planilha.createRow(linha);
		    celula = row.createCell(0);
		   	celula.setCellValue(nome);
		    celula.setCellStyle(estiloTextoE);
		    r = new Region(linha, (short)0, linha, (short)2);
		    planilha.addMergedRegion(r);
		    
		    if(tipoValor.equals("Prima"))
			{
		    	celula = row.createCell(3);
		       	celula.setCellValue(formataValor.format(prima));
		        celula.setCellStyle(estiloTextoD);
		        r = new Region(linha, (short)3, linha, (short)5);
		        planilha.addMergedRegion(r);
		           
		        celula = row.createCell(6);
		        celula.setCellValue(formataValor.format(capital));
		        celula.setCellStyle(estiloTextoD);
		        r = new Region(linha, (short)6, linha, (short)8);
		        planilha.addMergedRegion(r);
		           
		        celula = row.createCell(9);
		        celula.setCellValue(formataValor.format(comissao));
		        celula.setCellStyle(estiloTextoD);
		        r = new Region(linha, (short)9, linha, (short)11);
		        planilha.addMergedRegion(r);
			}
		    else if(tipoValor.equals("Capital"))
		    {
		    	   celula = row.createCell(3);
		    	   celula.setCellValue(formataValor.format(capital));
		      	   celula.setCellStyle(estiloTextoD);
		      	   r = new Region(linha, (short)3, linha, (short)5);
		      	   planilha.addMergedRegion(r);
		          
		      	   celula = row.createCell(6);
		      	   celula.setCellValue(formataValor.format(prima));
		      	   celula.setCellStyle(estiloTextoD);
		      	   r = new Region(linha, (short)6, linha, (short)8);
		      	   planilha.addMergedRegion(r);
		          
		      	   celula = row.createCell(9);
		      	   celula.setCellValue(formataValor.format(comissao));
		      	   celula.setCellStyle(estiloTextoD);
		      	   r = new Region(linha, (short)9, linha, (short)11);
		      	   planilha.addMergedRegion(r);
			}
		    else
		    {
		    	   celula = row.createCell(3);
		    	   celula.setCellValue(formataValor.format(comissao));
		      	   celula.setCellStyle(estiloTextoD);
		      	   r = new Region(linha, (short)3, linha, (short)5);
		      	   planilha.addMergedRegion(r);
		          
		      	   celula = row.createCell(6);
		      	   celula.setCellValue(formataValor.format(capital));
		    	   celula.setCellStyle(estiloTextoD);
		    	   r = new Region(linha, (short)6, linha, (short)8);
		    	   planilha.addMergedRegion(r);
		    	   
		      	   celula = row.createCell(9);
		      	   celula.setCellValue(formataValor.format(prima));
		      	   celula.setCellStyle(estiloTextoD);
		      	   r = new Region(linha, (short)9, linha, (short)11);
		      	   planilha.addMergedRegion(r);
		     }
		    
		    linha++;
		}
       
       row = planilha.createRow(linha);
	    celula = row.createCell(0);
	   	celula.setCellValue("TOTAL");
	    celula.setCellStyle(estiloTitulo);
	    r = new Region(linha, (short)0, linha, (short)2);
	    planilha.addMergedRegion(r);
       
       if(tipoValor.equals("Prima"))
		{
	    	celula = row.createCell(3);
	       	celula.setCellValue(formataValor.format(totalPrima));
	        celula.setCellStyle(estiloTextoDN);
	        r = new Region(linha, (short)3, linha, (short)5);
	        planilha.addMergedRegion(r);
	           
	        celula = row.createCell(6);
	        celula.setCellValue(formataValor.format(totalCapital));
	        celula.setCellStyle(estiloTextoDN);
	        r = new Region(linha, (short)6, linha, (short)8);
	        planilha.addMergedRegion(r);
	           
	        celula = row.createCell(9);
	        celula.setCellValue(formataValor.format(totalComissao));
	        celula.setCellStyle(estiloTextoDN);
	        r = new Region(linha, (short)9, linha, (short)11);
	        planilha.addMergedRegion(r);
		}
	    else if(tipoValor.equals("Capital"))
	    {
	    	   celula = row.createCell(3);
	    	   celula.setCellValue(formataValor.format(totalCapital));
	      	   celula.setCellStyle(estiloTextoDN);
	      	   r = new Region(linha, (short)3, linha, (short)5);
	      	   planilha.addMergedRegion(r);
	          
	      	   celula = row.createCell(6);
	      	   celula.setCellValue(formataValor.format(totalPrima));
	      	   celula.setCellStyle(estiloTextoDN);
	      	   r = new Region(linha, (short)6, linha, (short)8);
	      	   planilha.addMergedRegion(r);
	          
	      	   celula = row.createCell(9);
	      	   celula.setCellValue(formataValor.format(totalComissao));
	      	   celula.setCellStyle(estiloTextoDN);
	      	   r = new Region(linha, (short)9, linha, (short)11);
	      	   planilha.addMergedRegion(r);
		}
	    else
	    {
	    	   celula = row.createCell(3);
	    	   celula.setCellValue(formataValor.format(totalComissao));
	      	   celula.setCellStyle(estiloTextoDN);
	      	   r = new Region(linha, (short)3, linha, (short)5);
	      	   planilha.addMergedRegion(r);
	          
	      	   celula = row.createCell(6);
	      	   celula.setCellValue(formataValor.format(totalCapital));
	    	   celula.setCellStyle(estiloTextoDN);
	    	   r = new Region(linha, (short)6, linha, (short)8);
	    	   planilha.addMergedRegion(r);
	    	   
	      	   celula = row.createCell(9);
	      	   celula.setCellValue(formataValor.format(totalPrima));
	      	   celula.setCellStyle(estiloTextoDN);
	      	   r = new Region(linha, (short)9, linha, (short)11);
	      	   planilha.addMergedRegion(r);
	     }
       
       linha+=2;

       row = planilha.createRow(linha);
	    celula = row.createCell(0);
	   	celula.setCellValue(textoUsuario);
	    celula.setCellStyle(estiloTitulo);
	    r = new Region(linha, (short)0, linha, (short)9);
	    planilha.addMergedRegion(r);
       
       
       wb.write(stream);

       stream.flush();

       stream.close();
       
	}
}
