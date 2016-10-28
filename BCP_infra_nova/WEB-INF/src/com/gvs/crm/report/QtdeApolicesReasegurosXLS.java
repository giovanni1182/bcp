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

import com.gvs.crm.model.Aseguradora;

import infra.config.InfraProperties;

public class QtdeApolicesReasegurosXLS extends Excel
{
	public QtdeApolicesReasegurosXLS(Aseguradora aseguradora,Date dataInicio,Date dataFim,String situacao,double valor,Collection<String> apolices, String textoUsuario, double valorMenor, double valorDolar, double valorMenorDolar) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
	
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.setCaminho(caminho);
		
		HSSFWorkbook wb = new HSSFWorkbook();

		HSSFSheet planilha = wb.createSheet("Planilha");
		
		 HSSFFont fonteTitulo = wb.createFont();
	      fonteTitulo.setFontHeightInPoints((short)10);
	      fonteTitulo.setFontName("Arial");
	      fonteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
	      
	      HSSFFont fonteTexto = wb.createFont();
	      fonteTexto.setFontHeightInPoints((short)9);
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
	      
	      HSSFFont fonteTituloTabela = wb.createFont();
	      fonteTituloTabela.setFontHeightInPoints((short)10);
	      fonteTituloTabela.setFontName("Arial");
	      fonteTituloTabela.setColor(HSSFColor.WHITE.index);
	      
	      HSSFCellStyle estiloTituloTabelaC = wb.createCellStyle();
	      estiloTituloTabelaC.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTituloTabelaC.setFont(fonteTituloTabela);
	      estiloTituloTabelaC.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	      estiloTituloTabelaC.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      
	      String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
	      
	      InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
	      byte [] bytes = IOUtils.toByteArray (is);
	      int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG); 
	      is.close();
	      
	      HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 1, 0, (short)5 , 5);  
	      anchoVivaBem.setAnchorType(3);  
	      planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
	      
	      HSSFRow row = planilha.createRow(1);
	      HSSFCell celula = row.createCell(5);
	      
	      celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
	      celula.setCellStyle(estiloTitulo);
	      Region r = new Region(1, (short)5, 1, (short)11);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(2);
	      celula = row.createCell(5);
	      
	      celula.setCellValue("Cantidad de Pólizas/Reaseguros");
	      celula.setCellStyle(estiloTitulo);
	      r = new Region(2, (short)5, 2, (short)11);
	      planilha.addMergedRegion(r);
	      
	      int linha = 6;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      if(aseguradora!=null)
	    	  celula.setCellValue("Aseguradora: " + aseguradora.obterNome());
	      else
	    	  celula.setCellValue("Aseguradora: Todas");
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(linha, (short)0, linha, (short)11);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
    	  celula.setCellValue("Pólizas desde: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(linha, (short)0, linha, (short)11);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
    	  if(situacao.equals("0"))
    		  celula.setCellValue("Situacion: Todas");
    	  else
    		  celula.setCellValue("Situacion: " + situacao);
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(linha, (short)0, linha, (short)11);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
   		  if(valor > 0)
   			  celula.setCellValue("Capital de las pólizas más que Gs: " + formataValor.format(valor));
   		  else
   			celula.setCellValue("Capital de las pólizas más que Gs: ");
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(linha, (short)0, linha, (short)11);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
   		  if(valorMenor > 0)
   			  celula.setCellValue("Capital de las pólizas menos que Gs: " + formataValor.format(valorMenor));
   		  else
   			celula.setCellValue("Capital de las pólizas menos que Gs: ");
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(linha, (short)0, linha, (short)11);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
   		  if(valorDolar > 0)
   			  celula.setCellValue("Capital de las pólizas más que Dólar USA: " + formataValor.format(valorDolar));
   		  else
   			celula.setCellValue("Capital de las pólizas más que Dólar USA: ");
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(linha, (short)0, linha, (short)11);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
   		  if(valorMenorDolar > 0)
   			  celula.setCellValue("Capital de las pólizas menos que Dólar USA: " + formataValor.format(valorMenorDolar));
   		  else
   			celula.setCellValue("Capital de las pólizas más que Dólar USA: ");
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(linha, (short)0, linha, (short)11);
	      planilha.addMergedRegion(r);
	      
	      linha+=2;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)0, linha, (short)2);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(3);
	      celula.setCellValue("Cantidade");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)3, linha, (short)5);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(6);
	      celula.setCellValue("Prima de Pólizas");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)6, linha, (short)8);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(9);
	      celula.setCellValue("Capital Dólar USA");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)9, linha, (short)11);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      for(Iterator<String> i = apolices.iterator() ; i.hasNext() ; )
	      {
	    	  String linhaN = i.next();
	    	  
	    	  String[] linhaSuja = linhaN.split(";");
	    	  
	    	  String titulo = linhaSuja[0];
	    	  int qtde2 = Integer.valueOf(linhaSuja[1]);
	    	  double primaApolice = Double.valueOf(linhaSuja[2]);
	    	  double capitalDolarApolice = Double.valueOf(linhaSuja[3]);
	    	  
	    	  row = planilha.createRow(linha);
		      celula = row.createCell(0);
		      celula.setCellValue(titulo);
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)0, linha, (short)2);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(3);
		      celula.setCellValue(qtde2);
		      celula.setCellStyle(estiloTexto);
		      r = new Region(linha, (short)3, linha, (short)5);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(6);
		      celula.setCellValue(formataValor.format(primaApolice));
		      celula.setCellStyle(estiloTextoD);
		      r = new Region(linha, (short)6, linha, (short)8);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(9);
		      celula.setCellValue(formataValor.format(capitalDolarApolice));
		      celula.setCellStyle(estiloTextoD);
		      r = new Region(linha, (short)9, linha, (short)11);
		      planilha.addMergedRegion(r);
		      
		      linha++;
	      }
	      
	      linha+=2;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(textoUsuario);
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(linha, (short)0, linha, (short)11);
	      planilha.addMergedRegion(r);
	      
	      wb.write(stream);

	      stream.flush();

	      stream.close();
	}
}
