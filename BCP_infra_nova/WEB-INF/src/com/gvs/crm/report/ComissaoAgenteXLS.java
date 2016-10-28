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

import com.gvs.crm.model.AuxiliarSeguro;

import infra.config.InfraProperties;

public class ComissaoAgenteXLS extends Excel
{
	private DecimalFormat formataValor = new DecimalFormat("#,##0.00");
	private int linha = 0;
	
	public ComissaoAgenteXLS(AuxiliarSeguro agente,String tipoValor,Date dataInicio,Date dataFim,String situacao, Collection<String> dados,  boolean auxiliar, String textoUsuario) throws Exception
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
      
      HSSFCellStyle estiloTituloE = wb.createCellStyle();
      estiloTituloE.setFont(fonteTitulo);
      
      HSSFCellStyle estiloTexto = wb.createCellStyle();
      estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTexto.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoD = wb.createCellStyle();
      estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      estiloTextoD.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoE = wb.createCellStyle();
      estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
      estiloTextoE.setFont(fonteTexto);
      
      HSSFCellStyle estiloTextoN = wb.createCellStyle();
      estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
      estiloTextoN.setFont(fonteTextoN);
      
      HSSFCellStyle estiloTextoN_D = wb.createCellStyle();
      estiloTextoN_D.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
      estiloTextoN_D.setFont(fonteTextoN);
      
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
      
      HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)3 , 5);  
      anchoVivaBem.setAnchorType(3);  
      planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
      
      HSSFRow row = planilha.createRow(1);
      HSSFCell celula = row.createCell(5);
      
      celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
      celula.setCellStyle(estiloTitulo);
      Region r = new Region(1, (short)5, 1, (short)11);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(3);
      celula = row.createCell(5);
      if(auxiliar)
    	  celula.setCellValue("Comisión Agentes por Sección");
      else
    	  celula.setCellValue("Comisión Corredores de Seguros por Sección");
      celula.setCellStyle(estiloTitulo);
      r = new Region(3, (short)5, 3, (short)11);
      planilha.addMergedRegion(r);
      
      linha = 6;
      
      row = planilha.createRow(linha);
      celula = row.createCell(0);
   	  if(auxiliar)
   	  {
   		  if(agente!=null)
   			celula.setCellValue("Agente: " + agente.obterNome());
   		  else
   			celula.setCellValue("Agente: Todos");
   			  
   	  }
   	  else
   	  {
   		if(agente!=null)
   			celula.setCellValue("Corredor: " + agente.obterNome());
   		  else
   			celula.setCellValue("Corredor: Todos");
   	  }
      celula.setCellStyle(estiloTextoE);
      r = new Region(linha, (short)0, linha, (short)11);
      planilha.addMergedRegion(r);
      
      if(tipoValor.equals("valorPrima"))
    	  tipoValor = "Prima";
      else if(tipoValor.equals("valorCapital"))
    	  tipoValor = "Capital en Riesgo";
      else if(tipoValor.equals("valorComissao"))
    	  tipoValor = "Comisión";
      
      linha++;
      row = planilha.createRow(linha);
      celula = row.createCell(0);
      celula.setCellValue("Consulta por: " + tipoValor);
      celula.setCellStyle(estiloTextoE);
      r = new Region(linha, (short)0, linha, (short)11);
      planilha.addMergedRegion(r);
      
      linha++;
      row = planilha.createRow(linha);
      celula = row.createCell(0);
      celula.setCellValue("Pólizas Vigentes desde: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
      celula.setCellStyle(estiloTextoE);
      r = new Region(linha, (short)0, linha, (short)11);
      planilha.addMergedRegion(r);
      
      linha++;
      row = planilha.createRow(linha);
      celula = row.createCell(0);
      if(situacao.equals("0"))
    	  celula.setCellValue("Situacion: Todas");
      else
    	  celula.setCellValue("Situacion: " + situacao);
      celula.setCellStyle(estiloTextoE);
      r = new Region(linha, (short)0, linha, (short)11);
      planilha.addMergedRegion(r);
      
      linha+=2;
      
      int tamanho = dados.size();
      double totalGs = 0;
      double totalMe = 0;
      int totalQtde = 0;
      int cont = 1;
      long ultimoAgenteId = 0;
      
      for(Iterator<String> i = dados.iterator() ; i.hasNext() ; )
      {
    	  String linhaSuja = i.next();
			
    	  String[] linha2 = linhaSuja.split(";");
		
    	  String nome = linha2[0];
    	  String situacao2 = linha2[1];
    	  int qtde = Integer.valueOf(linha2[2]);
    	  String plano = linha2[3];
    	  double valorGs = Double.parseDouble(linha2[4]);
    	  double valorMe = Double.parseDouble(linha2[5]);
    	  long id = Long.parseLong(linha2[6]);
    	  
    	  if(ultimoAgenteId > 0)
    	  {
    		  if(ultimoAgenteId!=id)
    		  {
    			  //linha++;
    			  this.montaTotal(planilha, totalGs, totalMe, totalQtde, estiloTitulo, estiloTextoN_D);
    			  totalGs = 0;
    			  totalMe = 0;
    			  totalQtde = 0;
    		  }
    	  }
    	  
    	  if(ultimoAgenteId!=id)
    	  {
    		  //linha++;
    		  this.montaCabecalho(planilha, estiloTituloTabelaC, estiloTituloE, nome);
    	  }
    	  
    	  totalGs+=valorGs;
    	  totalMe+=valorMe;
    	  totalQtde+=qtde;
    	  
    	  row = planilha.createRow(linha);
    	  celula = row.createCell(0);
    	  celula.setCellValue(plano);
  	      celula.setCellStyle(estiloTextoE);
  	      r = new Region(linha, (short)0, linha, (short)3);
  	      planilha.addMergedRegion(r);
  	      
  	      celula = row.createCell(4);
  	      celula.setCellValue(situacao2);
  	      celula.setCellStyle(estiloTextoE);
  	      r = new Region(linha, (short)4, linha, (short)5);
  	      planilha.addMergedRegion(r);
  	      
	  	  celula = row.createCell(6);
	  	  celula.setCellValue(qtde);
	  	  celula.setCellStyle(estiloTexto);
  	      
	  	  celula = row.createCell(7);
	  	  celula.setCellValue(formataValor.format(valorGs));
	  	  celula.setCellStyle(estiloTextoD);
	  	  r = new Region(linha, (short)7, linha, (short)9);
	  	  planilha.addMergedRegion(r);
  	      
	  	  celula = row.createCell(10);
	  	  celula.setCellValue(formataValor.format(valorMe));
	  	  celula.setCellStyle(estiloTextoD);
	  	  r = new Region(linha, (short)10, linha, (short)12);
	  	  planilha.addMergedRegion(r);
	  	  
	  	  if(cont == tamanho)
	  	  {
	  		  linha++;
	  		  this.montaTotal(planilha, totalGs, totalMe, totalQtde, estiloTitulo, estiloTextoN_D);
	  	  }
		
	  	  ultimoAgenteId = id;
    	  
    	  cont++;
    	  linha++;
    	  
      }
      
      linha++;
      row = planilha.createRow(linha);
      celula = row.createCell(0);
   	  celula.setCellValue(textoUsuario);
      celula.setCellStyle(estiloTitulo);
      r = new Region(linha, (short)0, linha, (short)11);
      planilha.addMergedRegion(r);
      
      wb.write(stream);

      stream.flush();

      stream.close();
	}
	
	private void montaTotal(HSSFSheet planilha, double totalGs, double totalMe, int totalQtde, HSSFCellStyle estiloTitulo, HSSFCellStyle estiloTextoN_D)
	{
		HSSFRow row = planilha.createRow(linha);
	    HSSFCell celula = row.createCell(0);
	      
	    celula.setCellValue("TOTAL");
	    celula.setCellStyle(estiloTitulo);
	    Region r = new Region(linha, (short)0, linha, (short)3);
	    planilha.addMergedRegion(r);
	    
	    celula = row.createCell(6);
	    celula.setCellValue(totalQtde);
	    celula.setCellStyle(estiloTitulo);
	    
	    celula = row.createCell(7);
	    celula.setCellValue(formataValor.format(totalGs));
	    celula.setCellStyle(estiloTextoN_D);
	    r = new Region(linha, (short)7, linha, (short)9);
	    planilha.addMergedRegion(r);
	    
	    celula = row.createCell(10);
	    celula.setCellValue(formataValor.format(totalMe));
	    celula.setCellStyle(estiloTextoN_D);
	    r = new Region(linha, (short)10, linha, (short)12);
	    planilha.addMergedRegion(r);
	    
	    linha+=2;
	}
	
	private void montaCabecalho(HSSFSheet planilha, HSSFCellStyle estiloTituloTabelaC, HSSFCellStyle estiloTituloE, String nome)
	{
		HSSFRow row = planilha.createRow(linha);
		HSSFCell celula = row.createCell(0);
		
		celula.setCellValue(nome);
	    celula.setCellStyle(estiloTituloE);
	    Region r = new Region(linha, (short)0, linha, (short)11);
	    planilha.addMergedRegion(r);
	    
	    linha++;
		
	    row = planilha.createRow(linha);
	    celula = row.createCell(0);
	    celula.setCellValue("Sección");
	    celula.setCellStyle(estiloTituloTabelaC);
	    r = new Region(linha, (short)0, linha, (short)3);
	    planilha.addMergedRegion(r);
	      
	    celula = row.createCell(4);
	    celula.setCellValue("Situacion");
	    celula.setCellStyle(estiloTituloTabelaC);
	    r = new Region(linha, (short)4, linha, (short)5);
	    planilha.addMergedRegion(r);
	      
	    celula = row.createCell(6);
	    celula.setCellValue("Cantidad");
	    celula.setCellStyle(estiloTituloTabelaC);
	      
	    celula = row.createCell(7);
	    celula.setCellValue("Valor Gs");
	    celula.setCellStyle(estiloTituloTabelaC);
	    r = new Region(linha, (short)7, linha, (short)9);
	    planilha.addMergedRegion(r);
	      
	    celula = row.createCell(10);
	    celula.setCellValue("Valor Me");
	    celula.setCellStyle(estiloTituloTabelaC);
	    r = new Region(linha, (short)10, linha, (short)12);
	    planilha.addMergedRegion(r);
	    
	    linha++;
	}
}
