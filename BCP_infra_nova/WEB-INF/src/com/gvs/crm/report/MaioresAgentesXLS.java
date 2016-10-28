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

public class MaioresAgentesXLS extends Excel
{
	public MaioresAgentesXLS(AuxiliarSeguro agente,String tipoValor,Date dataInicio,Date dataFim, String situacao, Collection<String> dados, double monto, int qtde, boolean auxiliar, String textoUsuario) throws Exception
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
      
      row = planilha.createRow(2);
      celula = row.createCell(5);
      
      if(auxiliar)
    	  celula.setCellValue("Mayores Agentes");
      else
    	  celula.setCellValue("Mayores Corredores de Seguros");
      celula.setCellStyle(estiloTitulo);
      r = new Region(2, (short)5, 2, (short)11);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(3);
      celula = row.createCell(5);
      if(agente!=null)
    	  celula.setCellValue("Agente: " + agente.obterNome());
      else
    	  celula.setCellValue("Agente: Todos");
      celula.setCellStyle(estiloTitulo);
      r = new Region(3, (short)5, 3, (short)11);
      planilha.addMergedRegion(r);
      
      String situacao2 = "";
      String consultaPor = "";
		
      if(tipoValor.equals("valorPrima"))
    	  consultaPor = "Prima";
      else if(tipoValor.equals("valorCapital"))
    	  consultaPor = "Capital en Riesgo";
      else if(tipoValor.equals("valorComissao"))
    	  consultaPor = "Comisión";
		
      if(situacao.equals("0"))
    	  situacao2 = "Todas";
      
      row = planilha.createRow(4);
      celula = row.createCell(5);
   	  celula.setCellValue("Consulta por: " + consultaPor + " / Situacion:" + situacao2);
      celula.setCellStyle(estiloTitulo);
      r = new Region(4, (short)5, 4, (short)11);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(5);
      celula = row.createCell(5);
   	  celula.setCellValue("Pólizas Vigentes desde: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta "+ new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
      celula.setCellStyle(estiloTitulo);
      r = new Region(5, (short)5, 5, (short)11);
      planilha.addMergedRegion(r);
      
      row = planilha.createRow(6);
      celula = row.createCell(5);
   	  celula.setCellValue("Monto en Guaraníes: " + formataValor.format(monto) + " / Cantidad Solicitada: " + qtde);
      celula.setCellStyle(estiloTitulo);
      r = new Region(6, (short)5, 6, (short)11);
      planilha.addMergedRegion(r);
      
      int linha = 8;
      
      row = planilha.createRow(linha);
      celula = row.createCell(0);
   	  celula.setCellValue("Agente");
      celula.setCellStyle(estiloTituloTabelaC);
      r = new Region(linha, (short)0, linha, (short)5);
      planilha.addMergedRegion(r);
      
      celula = row.createCell(6);
   	  celula.setCellValue("Situacion");
      celula.setCellStyle(estiloTituloTabelaC);
      r = new Region(linha, (short)6, linha, (short)7);
      planilha.addMergedRegion(r);
      
      celula = row.createCell(8);
   	  celula.setCellValue("Valor Gs");
      celula.setCellStyle(estiloTituloTabelaC);
      r = new Region(linha, (short)8, linha, (short)11);
      planilha.addMergedRegion(r);
      
      double total = 0;
      
      linha++;
      
      for(Iterator<String> i = dados.iterator() ; i.hasNext() ; )
      {
    	  String linhaSuja = i.next();
			
    	  String[] linhaFor = linhaSuja.split(";");
			
    	  String nome = linhaFor[0];
    	  String situacaoT = linhaFor[1];
    	  double valor = Double.parseDouble(linhaFor[2]);
    	  
    	  row = planilha.createRow(linha);
          celula = row.createCell(0);
       	  celula.setCellValue(nome);
          celula.setCellStyle(estiloTextoE);
          r = new Region(linha, (short)0, linha, (short)5);
          planilha.addMergedRegion(r);
          
          celula = row.createCell(6);
       	  celula.setCellValue(situacaoT);
          celula.setCellStyle(estiloTextoE);
          r = new Region(linha, (short)6, linha, (short)7);
          planilha.addMergedRegion(r);
          
          celula = row.createCell(8);
       	  celula.setCellValue(formataValor.format(valor));
          celula.setCellStyle(estiloTextoD);
          r = new Region(linha, (short)8, linha, (short)11);
          planilha.addMergedRegion(r);
          
          linha++;
          
          total+=valor;
      }
      
      row = planilha.createRow(linha);
      celula = row.createCell(0);
   	  celula.setCellValue("TOTAL");
      celula.setCellStyle(estiloTextoN);
      r = new Region(linha, (short)0, linha, (short)5);
      planilha.addMergedRegion(r);
      
      celula = row.createCell(8);
   	  celula.setCellValue(formataValor.format(total));
      celula.setCellStyle(estiloTextoN_D);
      r = new Region(linha, (short)8, linha, (short)11);
      planilha.addMergedRegion(r);
      
      linha+=2;
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
}
