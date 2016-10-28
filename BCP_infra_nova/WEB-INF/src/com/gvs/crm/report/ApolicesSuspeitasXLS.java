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

import com.gvs.crm.model.Apolice;

import infra.config.InfraProperties;

public class ApolicesSuspeitasXLS extends Excel
{
	public ApolicesSuspeitasXLS(Collection apolices, long valor, String textoUsuario) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		DecimalFormat format = new DecimalFormat("#,##0.00");
		
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
	      
	      HSSFCellStyle estiloTextoN = wb.createCellStyle();
	      estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTextoN.setFont(fonteTextoN);
	      
	      HSSFCellStyle estiloTextoE = wb.createCellStyle();
	      estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	      estiloTextoE.setFont(fonteTexto);
	      
	      HSSFCellStyle estiloTextoD = wb.createCellStyle();
	      estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	      estiloTextoD.setFont(fonteTexto);
	      
	      HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
	      estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTituloTabela.setFont(fonteTituloTabela);
	      estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	      estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      
	      HSSFCellStyle estiloTituloTabelaE = wb.createCellStyle();
	      estiloTituloTabelaE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	      estiloTituloTabelaE.setFont(fonteTituloTabela);
	      estiloTituloTabelaE.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	      estiloTituloTabelaE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      
	      String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
	      
	      InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
	      byte [] bytes = IOUtils.toByteArray (is);
	      int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG); 
	      is.close();
	      
	      HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)3 , 5);  
	      anchoVivaBem.setAnchorType(3);  
	      planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
	      
	      HSSFRow row = planilha.createRow(1);
	      HSSFCell celula = row.createCell(5);
	      
	      String complemento = "";
	      if(valor == 0)
	    	  complemento = "Todas";
	      else if(valor == 1)
	    	  complemento = "U$ 10.000 Vigente";
	      else if(valor == 2)
	    	  complemento = "U$ 10.000 Siniestro";
	      else if(valor == 3)
	    	  complemento = "U$ 10.000 Anulación";
	      
	      celula.setCellValue("Operaciones Sospechosas " + complemento);
	      celula.setCellStyle(estiloTitulo);
	    		         
	      Region r = new Region(1, (short)5, 1, (short)12);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(6);
	      celula = row.createCell(0);
	      celula.setCellValue("Aseguradora");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)0, 6, (short)2);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(3);
	      celula.setCellValue("Prima M.E.");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(4);
	      celula.setCellValue("Póliza");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(5);
	      celula.setCellValue("Asegurado");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)5, 6, (short)6);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue("Plan");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)7, 6, (short)8);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(9);
	      celula.setCellValue("Sección");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)9, 6, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue("Vigencia");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)11, 6, (short)12);
	      planilha.addMergedRegion(r);
	      
	      int linha = 7;
	      
	      for (Iterator i = apolices.iterator(); i.hasNext();) 
	      {
				Apolice apolice = (Apolice) i.next();
				
				row = planilha.createRow(linha);
				celula = row.createCell(0);
				celula.setCellValue(apolice.obterOrigem().obterNome());
				celula.setCellStyle(estiloTextoE);
				r = new Region(linha, (short)0, linha, (short)2);
			    planilha.addMergedRegion(r);
				
				celula = row.createCell(3);
				celula.setCellValue(format.format(apolice.obterPrimaMe()));
				celula.setCellStyle(estiloTextoD);
				
				celula = row.createCell(4);
				celula.setCellValue(apolice.obterNumeroApolice());
				celula.setCellStyle(estiloTextoE);
				
				celula = row.createCell(5);
				celula.setCellValue(apolice.obterNomeAsegurado());
				celula.setCellStyle(estiloTextoE);
				r = new Region(linha, (short)5, linha, (short)6);
			    planilha.addMergedRegion(r);
			    
			    celula = row.createCell(7);
				celula.setCellValue(apolice.obterPlano().obterPlano());
				celula.setCellStyle(estiloTextoE);
				r = new Region(linha, (short)7, linha, (short)8);
			    planilha.addMergedRegion(r);
			    
			    celula = row.createCell(9);
				celula.setCellValue(apolice.obterSecao().obterNome());
				celula.setCellStyle(estiloTextoE);
				r = new Region(linha, (short)9, linha, (short)10);
			    planilha.addMergedRegion(r);
			    
			    String data1 = new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio());
				String data2 = new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao());
				
			    celula = row.createCell(11);
				celula.setCellValue(data1 + " - " + data2);
				celula.setCellStyle(estiloTexto);
				r = new Region(linha, (short)11, linha, (short)12);
			    planilha.addMergedRegion(r);
				
				linha++;
	      }
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(textoUsuario);
	      celula.setCellStyle(estiloTitulo);
	      r = new Region(linha, (short)0, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      wb.write(stream);

	      stream.flush();

	      stream.close();
	}
}
