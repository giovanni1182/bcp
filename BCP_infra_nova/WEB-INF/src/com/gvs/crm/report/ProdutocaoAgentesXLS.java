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

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Entidade;

import infra.config.InfraProperties;

public class ProdutocaoAgentesXLS extends Excel
{
	public ProdutocaoAgentesXLS(Aseguradora aseguradora, Date dataInicio, Date dataFim) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
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
      
      HSSFFont fonteTextoN = wb.createFont();
      fonteTextoN.setFontHeightInPoints((short)9);
      fonteTextoN.setFontName("Arial");
      fonteTextoN.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
      
      HSSFCellStyle estiloTitulo = wb.createCellStyle();
      estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTitulo.setFont(fonteTitulo);
      
      HSSFCellStyle estiloTexto = wb.createCellStyle();
      estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTexto.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoD = wb.createCellStyle();
      estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      estiloTextoD.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoE = wb.createCellStyle();
      estiloTextoE.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoN = wb.createCellStyle();
      estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTextoN.setFont(fonteTextoN);
      
      HSSFCellStyle estiloTextoN_E = wb.createCellStyle();
      estiloTextoN.setFont(fonteTextoN);
      
      HSSFFont fonteTituloTabela = wb.createFont();
      fonteTituloTabela.setFontHeightInPoints((short)10);
      fonteTituloTabela.setFontName("Arial");
      fonteTituloTabela.setColor(HSSFColor.WHITE.index);
      
      HSSFCellStyle estiloTituloTabelaC = wb.createCellStyle();
      estiloTituloTabelaC.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTituloTabelaC.setFont(fonteTituloTabela);
      estiloTituloTabelaC.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
      estiloTituloTabelaC.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      
      HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
      estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_LEFT);
      estiloTituloTabela.setFont(fonteTituloTabela);
      estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
      estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
      
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
      
   	  celula.setCellValue("Produción Agentes");
      celula.setCellStyle(estiloTitulo);
      r = new Region(2, (short)5, 2, (short)11);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(3);
      celula = row.createCell(5);
      
   	  celula.setCellValue("Aseguradora: " + aseguradora.obterNome());
      celula.setCellStyle(estiloTitulo);
      r = new Region(3, (short)5, 3, (short)11);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(4);
      celula = row.createCell(5);
      
   	  celula.setCellValue("Periodo: " + new SimpleDateFormat("MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("MM/yyyy").format(dataFim));
      celula.setCellStyle(estiloTitulo);
      r = new Region(4, (short)5, 4, (short)11);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(6);
      celula = row.createCell(0);
      celula.setCellValue("Agentes");
      celula.setCellStyle(estiloTituloTabelaC);
      r = new Region(6, (short)0, 6, (short)5);
      planilha.addMergedRegion(r);
      
      int linha = 7;
      
      String dataFimStr = new SimpleDateFormat("dd/MM/yyyy").format(dataFim) + " 23:59:59";
      dataFim = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").parse(dataFimStr);
      
      Collection<Aseguradora> agentes = aseguradora.obterAgentesPorPeridodo(dataInicio, dataFim);
      
      for(Iterator i = agentes.iterator() ; i.hasNext() ; )
      {
    	  Entidade agente = (Entidade) i.next();
    	  
    	  row = planilha.createRow(linha);
          celula = row.createCell(0);
          
          celula.setCellValue(agente.obterNome());
          celula.setCellStyle(estiloTextoE);
          r = new Region(linha, (short)0, linha, (short)5);
          planilha.addMergedRegion(r);
          
          linha++;
      }
      
      wb.write(stream);

      stream.flush();

      stream.close();
	}
}