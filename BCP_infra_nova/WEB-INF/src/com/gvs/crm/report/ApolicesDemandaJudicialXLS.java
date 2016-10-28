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
import com.gvs.crm.model.AspectosLegais;
import com.gvs.crm.model.ClassificacaoContas;

import infra.config.InfraProperties;

public class ApolicesDemandaJudicialXLS extends Excel
{
	public ApolicesDemandaJudicialXLS(Aseguradora aseguradora, ClassificacaoContas secao, Date dataInicio, Date dataFim, Collection<AspectosLegais> aspectos) throws Exception
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
	      
	      HSSFCellStyle estiloTituloE = wb.createCellStyle();
	      estiloTituloE.setFont(fonteTitulo);
	      
	      HSSFCellStyle estiloTexto = wb.createCellStyle();
	      estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTexto.setFont(fonteTexto);
	      
	      HSSFCellStyle estiloTextoN = wb.createCellStyle();
	      estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTextoN.setFont(fonteTextoN);
	      
	      HSSFCellStyle estiloTextoN_E = wb.createCellStyle();
	      estiloTextoN_E.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	      estiloTextoN_E.setFont(fonteTextoN);
	      
	      HSSFCellStyle estiloTextoN_D = wb.createCellStyle();
	      estiloTextoN_D.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	      estiloTextoN_D.setFont(fonteTextoN);
	      
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
	      
	   	  celula.setCellValue("Pólizas Demanda Judicial " + aseguradora.obterNome());
	      celula.setCellStyle(estiloTituloE);
	      Region r = new Region(1, (short)5, 1, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(2);
	      celula = row.createCell(5);
	      celula.setCellValue("Sección: " + secao.obterNome());
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(2, (short)5, 2, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(3);
	      celula = row.createCell(5);
	      celula.setCellValue("Periodo: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(2, (short)5, 2, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(6);
	      celula = row.createCell(0);
	      celula.setCellValue("Póliza");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(1);
	      celula.setCellValue("Fecha Notificación");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)1, 6, (short)2);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(3);
	      celula.setCellValue("Actor o Demandante");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)3, 6, (short)6);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue("Demandado");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)7, 6, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue("Monto Demandado");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)11, 6, (short)12);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(13);
	      celula.setCellValue("Prov. Siniestro Pendiente");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)13, 6, (short)15);
	      planilha.addMergedRegion(r);
	      
	      int linha = 7;
	      double totalMontante = 0;
	      double totalSinistro = 0;
	      
	      for(Iterator<AspectosLegais> i = aspectos.iterator() ; i.hasNext() ; )
	      {
	    	  AspectosLegais aspecto = i.next();
	    	  
	    	  Apolice apolice = (Apolice) aspecto.obterSuperior();
	    	  
	    	  row = planilha.createRow(linha);
		      celula = row.createCell(0);
		      celula.setCellValue(apolice.obterNumeroIdentificacao());
		      celula.setCellStyle(estiloTexto);
		      
		      celula = row.createCell(1);
		      if(aspecto.obterDataNotificacao()!=null)
		    	  celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(aspecto.obterDataNotificacao()));
		      else
		    	  celula.setCellValue("");
		      
		      celula.setCellStyle(estiloTexto);
		      r = new Region(linha, (short)1, linha, (short)2);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(3);
		      celula.setCellValue(aspecto.obterDemandante());
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)3, linha, (short)6);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(7);
		      celula.setCellValue(aspecto.obterDemandado());
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)7, linha, (short)10);
		      planilha.addMergedRegion(r);
		      
		      double montante = aspecto.obterMontanteDemandado();
		      
		      celula = row.createCell(11);
		      celula.setCellValue(format.format(montante));
		      celula.setCellStyle(estiloTextoD);
		      r = new Region(linha, (short)11, linha, (short)12);
		      planilha.addMergedRegion(r);
		      
		      totalMontante+=montante;
		      
		      double sinistro = aspecto.obterSinistroPendente();
		      celula = row.createCell(13);
		      celula.setCellValue(format.format(sinistro));
		      celula.setCellStyle(estiloTextoD);
		      r = new Region(linha, (short)13, linha, (short)15);
		      planilha.addMergedRegion(r);
		      
		      totalSinistro+=sinistro;
		      
		      linha++;
	      }
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(7);
	      celula.setCellValue("TOTAL");
	      celula.setCellStyle(estiloTextoN);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(totalMontante));
	      celula.setCellStyle(estiloTextoN_D);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(13);
	      celula.setCellValue(format.format(totalSinistro));
	      celula.setCellStyle(estiloTextoN_D);
	      r = new Region(linha, (short)13, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      wb.write(stream);

	      stream.flush();

	      stream.close();
	}
}
