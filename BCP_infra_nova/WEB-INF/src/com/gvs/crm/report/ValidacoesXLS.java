package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
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
import com.gvs.crm.model.Processamento;
import com.gvs.crm.model.Processamento.Agenda;
import com.gvs.crm.model.ProcessamentoHome;

import infra.config.InfraProperties;

public class ValidacoesXLS extends Excel 
{
	public ValidacoesXLS(Collection<Aseguradora> aseguradoras, int mes, int ano, String tipo, int erro, Date dataInicio, Date dataFim, ProcessamentoHome home) throws Exception
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
	    
	    HSSFFont fonteTituloTabela = wb.createFont();
	      fonteTituloTabela.setFontHeightInPoints((short)10);
	      fonteTituloTabela.setFontName("Arial");
	      fonteTituloTabela.setColor(HSSFColor.WHITE.index);
      
	      HSSFCellStyle estiloTitulo = wb.createCellStyle();
	      estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTitulo.setFont(fonteTitulo);
      
	      HSSFCellStyle estiloTexto = wb.createCellStyle();
	      estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTexto.setFont(fonteTexto);
      
	      HSSFCellStyle estiloTextoE = wb.createCellStyle();
	      estiloTextoE.setFont(fonteTexto);
	      
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
	      
	   	  celula.setCellValue("Validaciones");
	      celula.setCellStyle(estiloTitulo);
	      r = new Region(2, (short)5, 2, (short)11);
	      planilha.addMergedRegion(r);
	      
	      int linha = 7;
	      
	      Aseguradora aseguradora;
	      Agenda agenda;
	      String mesStr;
	      int mes2,ano2;
	      Collection<Agenda> agendas;
	      Processamento processamento;
	      String tipoAgenda;
	      
	      for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
	      {
	    	  aseguradora = i.next();
	    	  
	    	  agendas = home.obterAgendas(aseguradora, mes, ano, tipo, erro, dataInicio, dataFim);
	    	  if(agendas.size() > 0)
	    	  {
		    	  row = planilha.createRow(linha);
		    	  
		    	  celula = row.createCell(0);
			   	  celula.setCellValue(aseguradora.obterNome());
			      celula.setCellStyle(estiloTituloTabelaC);
			      r = new Region(linha, (short)0, linha, (short)15);
			      planilha.addMergedRegion(r);
			      
			      linha++;
			      
			      row = planilha.createRow(linha);
		    	  
			      celula = row.createCell(0);
			   	  celula.setCellValue("Tipo");
			      celula.setCellStyle(estiloTituloTabelaC);
			      
			      celula = row.createCell(1);
			   	  celula.setCellValue("Archivo");
			      celula.setCellStyle(estiloTituloTabelaC);
			      r = new Region(linha, (short)1, linha, (short)3);
			      planilha.addMergedRegion(r);
			      
			      celula = row.createCell(4);
			   	  celula.setCellValue("Mes/Año");
			      celula.setCellStyle(estiloTituloTabelaC);
			      
			      celula = row.createCell(5);
			   	  celula.setCellValue("Mensaje");
			      celula.setCellStyle(estiloTituloTabelaC);
			      r = new Region(linha, (short)5, linha, (short)15);
			      planilha.addMergedRegion(r);
			      
			      linha++;
			      
			      for(Iterator<Agenda> j = agendas.iterator() ; j.hasNext() ; )
			      {
			    	  agenda = j.next();
			    	  
			    	  processamento = agenda.obterProcessamento();
			    	  
			    	  row = planilha.createRow(linha);
			    	  
			    	  tipoAgenda = processamento.obterTipo();
			    	  
			    	  if(tipoAgenda.equals("Contabil"))
							tipoAgenda = "Contable";
						else if(tipoAgenda.equals("Livro"))
							tipoAgenda = "Libro";
			    	  
			    	  celula = row.createCell(0);
				   	  celula.setCellValue(tipoAgenda);
				      celula.setCellStyle(estiloTextoE);
				      
			    	  celula = row.createCell(1);
				   	  celula.setCellValue(agenda.obterNomeArquivo());
				      celula.setCellStyle(estiloTextoE);
				      r = new Region(linha, (short)1, linha, (short)3);
				      planilha.addMergedRegion(r);
				      
				      mes2 = agenda.obterMes();
				      ano2 = agenda.obterAno();
				      
				      mesStr = "";
				      if(new Integer(mes2).toString().length() == 1)
				    	  mesStr = "0"+mes2;
				      else
				    	  mesStr = new Integer(mes2).toString();
						
				      mesStr+="/"+ano2;
				      
				      celula = row.createCell(4);
				   	  celula.setCellValue(mesStr);
				      celula.setCellStyle(estiloTexto);
				      
				      celula = row.createCell(5);
				   	  celula.setCellValue(agenda.obterMensagem());
				      celula.setCellStyle(estiloTextoE);
				      r = new Region(linha, (short)5, linha, (short)15);
				      planilha.addMergedRegion(r);
				      
				      linha++;
			      }
			      
			      linha++;
	    	  }
	      }
	      
	      wb.write(stream);

	      stream.flush();

	      stream.close();
      
	}
}
