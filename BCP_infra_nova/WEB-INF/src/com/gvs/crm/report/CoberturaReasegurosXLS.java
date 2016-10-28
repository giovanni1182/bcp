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

public class CoberturaReasegurosXLS extends Excel
{
	public CoberturaReasegurosXLS(Aseguradora aseguradora,Date dataInicio,Date dataFim, String situacao,String tipoValor,Collection<String> dados, String textoUsuario) throws Exception
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
	      
	      HSSFFont fonteTituloTabela = wb.createFont();
	      fonteTituloTabela.setFontHeightInPoints((short)8);
	      fonteTituloTabela.setFontName("Arial");
	      fonteTituloTabela.setColor(HSSFColor.WHITE.index);
	      
	      HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
	      estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTituloTabela.setFont(fonteTituloTabela);
	      estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	      estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      
	      HSSFCellStyle estiloTituloD = wb.createCellStyle();
	      estiloTituloD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	      estiloTituloD.setFont(fonteTitulo);
	      
	      HSSFCellStyle estiloTituloE = wb.createCellStyle();
	      estiloTituloE.setFont(fonteTitulo);
	      
	      HSSFCellStyle estiloTexto = wb.createCellStyle();
	      estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTexto.setFont(fonteTexto);
	      
	      HSSFCellStyle estiloTextoD = wb.createCellStyle();
	      estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	      estiloTextoD.setFont(fonteTexto);
	      
	      HSSFCellStyle estiloTextoE = wb.createCellStyle();
	      estiloTextoE.setFont(fonteTexto);
	      
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
	      
	      celula.setCellValue("Cobertura de Reaseguros");
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
	      
	      String consulta = "";
	      if(tipoValor.equals("valorPrima"))
	    	  consulta = "Prima";
			else if(tipoValor.equals("valorCapital"))
				consulta = "Capital";
			else if(tipoValor.equals("valorComissao"))
				consulta = "Comisión";
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
    	  celula.setCellValue("Consulta por: " + consulta);
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
	      
	      linha+=2;
	      
	      row = planilha.createRow(linha);
		  celula = row.createCell(0);
		  celula.setCellValue("Tipo Contrato");
		  celula.setCellStyle(estiloTituloTabela);
		  r = new Region(linha, (short)0, linha, (short)2);
		  planilha.addMergedRegion(r);
		  
		  celula = row.createCell(3);
		  celula.setCellValue("Cantidad");
		  celula.setCellStyle(estiloTituloTabela);
		  r = new Region(linha, (short)3, linha, (short)4);
		  planilha.addMergedRegion(r);
		  
		  celula = row.createCell(5);
		  celula.setCellValue(consulta);
		  celula.setCellStyle(estiloTituloTabela);
		  r = new Region(linha, (short)5, linha, (short)7);
		  planilha.addMergedRegion(r);
		  
		  linha++;
	      double total = 0;
	      double totalQtde = 0;
	      
	      for(Iterator<String> i = dados.iterator() ; i.hasNext() ; )
	      {
				String linhaSuja = i.next();
				
				String[] linhaS = linhaSuja.split(";");
				
				String tipoContrato = linhaS[0];
				double valor = Double.valueOf(linhaS[1]);
				int qtde = Integer.valueOf(linhaS[2]);
				
				row = planilha.createRow(linha);
			    celula = row.createCell(0);
			    celula.setCellValue(tipoContrato);
			    celula.setCellStyle(estiloTextoE);
			    r = new Region(linha, (short)0, linha, (short)2);
			    planilha.addMergedRegion(r);
			    
			    celula = row.createCell(3);
			    celula.setCellValue(qtde);
				celula.setCellStyle(estiloTexto);
				r = new Region(linha, (short)3, linha, (short)4);
				planilha.addMergedRegion(r);
			    
			    celula = row.createCell(5);
			    celula.setCellValue(formataValor.format(valor));
			    celula.setCellStyle(estiloTextoD);
			    r = new Region(linha, (short)5, linha, (short)7);
			    planilha.addMergedRegion(r);
		      
			    linha++;
			    
			    total+=valor;
			    totalQtde+=qtde;
				
	      }
	      
	      row = planilha.createRow(linha);
		  celula = row.createCell(0);
		  celula.setCellValue("TOTAL");
		  celula.setCellStyle(estiloTitulo);
		  r = new Region(linha, (short)0, linha, (short)2);
		  planilha.addMergedRegion(r);
		  
		  celula = row.createCell(3);
		  celula.setCellValue(totalQtde);
		  celula.setCellStyle(estiloTitulo);
		  r = new Region(linha, (short)3, linha, (short)4);
		  planilha.addMergedRegion(r);
		  
		  celula = row.createCell(5);
		  celula.setCellValue(formataValor.format(total));
		  celula.setCellStyle(estiloTituloD);
		  r = new Region(linha, (short)5, linha, (short)7);
		  planilha.addMergedRegion(r);
	      
	      linha+=3;
	      
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
