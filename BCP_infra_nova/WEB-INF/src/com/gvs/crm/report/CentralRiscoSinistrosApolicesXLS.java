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
import com.gvs.crm.model.ClassificacaoContas;

import infra.config.InfraProperties;

public class CentralRiscoSinistrosApolicesXLS extends Excel
{
	public CentralRiscoSinistrosApolicesXLS(Aseguradora aseguradora, Date dataInicio, Date dataFim, ClassificacaoContas cContas, Collection apolices, String situacao, String situacao2) throws Exception
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
      
      HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)3 , 5);  
      anchoVivaBem.setAnchorType(3);  
      planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
      
      HSSFRow row = planilha.createRow(0);
      HSSFCell celula = row.createCell(5);
      
   	  celula.setCellValue("Pólizas de la Aseguradora: " + aseguradora.obterNome());
      celula.setCellStyle(estiloTituloE);
      Region r = new Region(1, (short)5, 1, (short)14);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(1);
      celula = row.createCell(5);
   	  celula.setCellValue("Sección: " + cContas.obterNome() + " " + cContas.obterApelido());
      celula.setCellStyle(estiloTituloE);
      r = new Region(2, (short)5, 2, (short)14);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(2);
      celula = row.createCell(5);
      celula.setCellValue("Periodo: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
      celula.setCellStyle(estiloTituloE);
      r = new Region(3, (short)5, 3, (short)14);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(3);
      celula = row.createCell(5);
      celula.setCellValue("Cantidad: " + apolices.size());
      celula.setCellStyle(estiloTituloE);
      r = new Region(4, (short)5, 4, (short)14);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(4);
      celula = row.createCell(5);
      celula.setCellValue("Situación: " + situacao);
      celula.setCellStyle(estiloTituloE);
      r = new Region(4, (short)5, 4, (short)14);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(6);
      celula = row.createCell(0);
      celula.setCellValue("Vigencia");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(6, (short)0, 6, (short)2);
      planilha.addMergedRegion(r);
      
      celula = row.createCell(3);
      celula.setCellValue("Numero");
      celula.setCellStyle(estiloTituloTabela);
      
      celula = row.createCell(4);
      celula.setCellValue("Plan");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(6, (short)4, 6, (short)7);
      planilha.addMergedRegion(r);
      
      celula = row.createCell(8);
      celula.setCellValue("Asegurado");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(6, (short)8, 6, (short)11);
      planilha.addMergedRegion(r);
      
      celula = row.createCell(12);
      celula.setCellValue("Tipo");
      celula.setCellStyle(estiloTituloTabela);
      
      celula = row.createCell(13);
      celula.setCellValue("Situación");
      celula.setCellStyle(estiloTituloTabela);
      
      celula = row.createCell(14);
      celula.setCellValue("Valor");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(6, (short)14, 6, (short)15);
      planilha.addMergedRegion(r);
      
      celula = row.createCell(16);
      celula.setCellValue("Vlr Pg Aseguradora (Gs)");
      celula.setCellStyle(estiloTituloTabela);
      r = new Region(6, (short)16, 6, (short)19);
      planilha.addMergedRegion(r);
      
      double totalValor = 0;
      double totalRecuperado = 0;
      
      int linha = 7;
      
      for(Iterator i = apolices.iterator() ; i.hasNext() ; )
      {
    	  Apolice apolice = (Apolice) i.next();
    	  
    	  row = planilha.createRow(linha);
          celula = row.createCell(0);
          celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio()) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao()));
          celula.setCellStyle(estiloTexto);
          r = new Region(linha, (short)0, linha, (short)2);
          planilha.addMergedRegion(r);
          
          celula = row.createCell(3);
          celula.setCellValue(apolice.obterNumeroApolice());
          celula.setCellStyle(estiloTexto);
          
          celula = row.createCell(4);
          celula.setCellValue(apolice.obterPlano().obterPlano()  + " " + apolice.obterPlano().obterIdentificador());
          celula.setCellStyle(estiloTextoE);
          r = new Region(linha, (short)4, linha, (short)7);
          planilha.addMergedRegion(r);
          
          celula = row.createCell(8);
          celula.setCellValue(apolice.obterNomeAsegurado());
          celula.setCellStyle(estiloTextoE);
          r = new Region(linha, (short)8, linha, (short)11);
          planilha.addMergedRegion(r);
          
          celula = row.createCell(12);
          celula.setCellValue(apolice.obterTipo());
          celula.setCellStyle(estiloTexto);
          
          celula = row.createCell(13);
          celula.setCellValue(apolice.obterSituacaoSeguro());
          celula.setCellStyle(estiloTexto);
          
          double montante = apolice.obterMontanteGsSinistro(situacao, dataInicio, dataFim);
          double recuperado = apolice.obterRecuperadosSinistro(situacao, dataInicio, dataFim);
          
          celula = row.createCell(14);
          celula.setCellValue(format.format(montante));
          celula.setCellStyle(estiloTextoD);
          r = new Region(linha, (short)14, linha, (short)15);
          planilha.addMergedRegion(r);
          
          celula = row.createCell(16);
          celula.setCellValue(format.format(recuperado));
          celula.setCellStyle(estiloTextoD);
          r = new Region(linha, (short)16, linha, (short)19);
          planilha.addMergedRegion(r);
          
          totalValor+=montante;
          totalRecuperado+=recuperado;
          
          linha++;
      }
      
      row = planilha.createRow(linha);
      celula = row.createCell(0);
      celula.setCellValue("TOTAL");
      celula.setCellStyle(estiloTextoN);
      r = new Region(linha, (short)0, linha, (short)2);
      planilha.addMergedRegion(r);
      
      celula = row.createCell(14);
      celula.setCellValue(format.format(totalValor));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linha, (short)14, linha, (short)15);
      planilha.addMergedRegion(r);
      
      celula = row.createCell(16);
      celula.setCellValue(format.format(totalRecuperado));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linha, (short)16, linha, (short)19);
      planilha.addMergedRegion(r);
      
      wb.write(stream);

      stream.flush();

      stream.close();
	}
}
