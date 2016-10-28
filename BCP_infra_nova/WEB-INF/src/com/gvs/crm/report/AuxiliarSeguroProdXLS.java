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
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AuxiliarSeguro;

import infra.config.InfraProperties;

public class AuxiliarSeguroProdXLS extends Excel
{
	public AuxiliarSeguroProdXLS(AuxiliarSeguro auxiliar,Collection aseguradoras, Date dataInicio, Date dataFim) throws Exception
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
	      
	      HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)4 , 5);  
	      anchoVivaBem.setAnchorType(3);  
	      planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
	      
	      HSSFRow row = planilha.createRow(1);
	      HSSFCell celula = row.createCell(5);
	      
	      celula.setCellValue("Produción Agente de Seguros " + auxiliar.obterNome());
	      celula.setCellStyle(estiloTitulo);
	    		         
	      Region r = new Region(1, (short)5, 1, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(2);
	      celula = row.createCell(5);
	      celula.setCellValue("Periodo: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
	      celula.setCellStyle(estiloTitulo);
	      
	      r = new Region(2, (short)5, 2, (short)14);
	      planilha.addMergedRegion(r);
	      
	      int linha = 6;
	      
	      for (Iterator i = aseguradoras.iterator(); i.hasNext();) 
	      {
	    	  Aseguradora aseguradora = (Aseguradora) i.next();
	    	  
	    	  row = planilha.createRow(linha);
		      celula = row.createCell(0);
		      
		      celula.setCellValue("Aseguradora: " + aseguradora.obterNome());
		      celula.setCellStyle(estiloTituloTabelaE);
		      r = new Region(linha, (short)0, linha, (short)14);
		      planilha.addMergedRegion(r);
		      
		      linha++;
		      
		      row = planilha.createRow(linha);
		      celula = row.createCell(0);
		      celula.setCellValue("Nº Póliza");
		      celula.setCellStyle(estiloTextoN);
		      
		      celula = row.createCell(1);
		      celula.setCellValue("Situación");
		      celula.setCellStyle(estiloTextoN);
		      
		      celula = row.createCell(2);
		      celula.setCellValue("Asegurado");
		      celula.setCellStyle(estiloTextoN);
		      r = new Region(linha, (short)2, linha, (short)3);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(4);
		      celula.setCellValue("Emisión");
		      celula.setCellStyle(estiloTextoN);
		      
		      celula = row.createCell(5);
		      celula.setCellValue("Vigencia");
		      celula.setCellStyle(estiloTextoN);
		      r = new Region(linha, (short)5, linha, (short)6);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(7);
		      celula.setCellValue("Tp. Operación");
		      celula.setCellStyle(estiloTextoN);
		      
		      celula = row.createCell(8);
		      celula.setCellValue("Secccón");
		      celula.setCellStyle(estiloTextoN);
		      
		      celula = row.createCell(9);
		      celula.setCellValue("Comisión");
		      celula.setCellStyle(estiloTextoN);
		      r = new Region(linha, (short)9, linha, (short)10);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(11);
		      celula.setCellValue("Prima");
		      celula.setCellStyle(estiloTextoN);
		      
		      celula = row.createCell(12);
		      celula.setCellValue("Premio");
		      celula.setCellStyle(estiloTextoN);
		      
		      celula = row.createCell(13);
		      celula.setCellValue("Cap. Asegurado");
		      celula.setCellStyle(estiloTextoN);
		      r = new Region(linha, (short)13, linha, (short)14);
		      planilha.addMergedRegion(r);
		      
		      linha++;
		      row = planilha.createRow(linha);
		      celula = row.createCell(5);
		      celula.setCellValue("Inicio");
		      celula.setCellStyle(estiloTextoN);
		      
		      celula = row.createCell(6);
		      celula.setCellValue("Fim");
		      celula.setCellStyle(estiloTextoN);
		      
		      celula = row.createCell(9);
		      celula.setCellValue("Gs");
		      celula.setCellStyle(estiloTextoN);
		      
		      celula = row.createCell(10);
		      celula.setCellValue("M.E.");
		      celula.setCellStyle(estiloTextoN);
		      
		      celula = row.createCell(13);
		      celula.setCellValue("Gs");
		      celula.setCellStyle(estiloTextoN);
		      
		      celula = row.createCell(14);
		      celula.setCellValue("M.E.");
		      celula.setCellStyle(estiloTextoN);
		      
		      linha++;
		      
		      for (Iterator j = aseguradora.obterApolices(auxiliar,dataInicio, dataFim).iterator(); j.hasNext();) 
		      {
		    	  Apolice apolice = (Apolice) j.next();
		    	  
		    	  row = planilha.createRow(linha);
		    	  
			      celula = row.createCell(0);
			      celula.setCellValue(apolice.obterNumeroApolice());
			      celula.setCellStyle(estiloTextoE);
			      
			      celula = row.createCell(1);
			      celula.setCellValue(apolice.obterSituacaoSeguro());
			      celula.setCellStyle(estiloTextoE);
			      
			      celula = row.createCell(2);
			      celula.setCellValue(apolice.obterNomeAsegurado());
			      celula.setCellStyle(estiloTextoE);
			      r = new Region(linha, (short)2, linha, (short)3);
			      planilha.addMergedRegion(r);
			      
			      celula = row.createCell(4);
			      celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataEmissao()));
			      celula.setCellStyle(estiloTexto);
			      
			      celula = row.createCell(5);
			      celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio()));
			      celula.setCellStyle(estiloTexto);
			      
			      celula = row.createCell(6);
			      celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao()));
			      celula.setCellStyle(estiloTexto);
			      
			      celula = row.createCell(7);
			      celula.setCellValue(apolice.obterTipo());
			      celula.setCellStyle(estiloTextoE);
			      
			      celula = row.createCell(8);
			      celula.setCellValue(apolice.obterSecao().obterNome());
			      celula.setCellStyle(estiloTextoE);
			      
			      celula = row.createCell(9);
			      celula.setCellValue(format.format(apolice.obterComissaoGs()));
			      celula.setCellStyle(estiloTextoD);
			      
			      celula = row.createCell(10);
			      celula.setCellValue(format.format(apolice.obterComissaoMe()));
			      celula.setCellStyle(estiloTextoD);
			      
			      celula = row.createCell(11);
			      celula.setCellValue(format.format(apolice.obterPrimaGs()));
			      celula.setCellStyle(estiloTextoD);
			      
			      celula = row.createCell(12);
			      celula.setCellValue(format.format(apolice.obterPremiosGs()));
			      celula.setCellStyle(estiloTextoD);
			      
			      celula = row.createCell(13);
			      celula.setCellValue(format.format(apolice.obterCapitalGs()));
			      celula.setCellStyle(estiloTextoD);
			      
			      celula = row.createCell(14);
			      celula.setCellValue(format.format(apolice.obterCapitalMe()));
			      celula.setCellStyle(estiloTextoD);
			      
			      linha++;
		      }
		      
		      linha+=2;
	      }
	      
	      wb.write(stream);

	      stream.flush();

	      stream.close();
	}
}
