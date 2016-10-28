package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;

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

import com.gvs.crm.model.AgendaMovimentacao;
import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.EntidadeBCP;
import com.gvs.crm.model.Plano;

import infra.config.InfraProperties;

public class LavagemDinheiroXLS extends Excel
{
	public LavagemDinheiroXLS(Collection<Apolice> colecao, String tipoValor, Date dataInicio, Date dataFim, String situacao, String tipoPessoa, String secao, int qtde, String textoUsuario, String modalidade) throws Exception 
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
	      estiloTituloE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	      estiloTituloE.setFont(fonteTitulo);
	      
	      HSSFCellStyle estiloTexto = wb.createCellStyle();
	      estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTexto.setFont(fonteTexto);
	      
	      HSSFCellStyle estiloTextoN = wb.createCellStyle();
	      estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTextoN.setFont(fonteTextoN);
	      
	      HSSFCellStyle estiloTextoCor = wb.createCellStyle();
	      estiloTextoCor.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTextoCor.setFont(fonteTexto);
	      estiloTextoCor.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
	      estiloTextoCor.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      
	      HSSFCellStyle estiloTextoE = wb.createCellStyle();
	      estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	      estiloTextoE.setFont(fonteTexto);
	      
	      HSSFCellStyle estiloTextoD = wb.createCellStyle();
	      estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	      estiloTextoD.setFont(fonteTexto);
	      
	      HSSFCellStyle estiloTextoCorE = wb.createCellStyle();
	      estiloTextoCorE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	      estiloTextoCorE.setFont(fonteTextoN);
	      estiloTextoCorE.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
	      estiloTextoCorE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      
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
	      
	      celula.setCellValue("Mayores Ocurrencias " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
	      celula.setCellStyle(estiloTituloE);
	    		         
	      Region r = new Region(0, (short)5, 0, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(1);
	      celula = row.createCell(5);
	      if(secao.equals(""))
	    	  celula.setCellValue("Sección: Todas");
	      else
	    	  celula.setCellValue("Sección: " + secao);
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(1, (short)5, 1, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(2);
	      celula = row.createCell(5);
	      if(modalidade.equals(""))
	    	  celula.setCellValue("Modalidad: Todas");
	      else
	    	  celula.setCellValue("Modalidad: " + modalidade);
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(1, (short)5, 1, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(3);
	      celula = row.createCell(5);
	      if(tipoPessoa.equals("0"))
	    	  celula.setCellValue("Tipo de Persona: Todas");
	      else
	    	  celula.setCellValue("Tipo de Persona: " + tipoPessoa);
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(2, (short)5, 2, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(4);
	      celula = row.createCell(5);
	      if(situacao.equals("0"))
	    	  celula.setCellValue("Situacion: Todas");
	      else
	    	  celula.setCellValue("Situacion: " + situacao);
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(3, (short)5, 3, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(5);
	      celula = row.createCell(5);
	      celula.setCellValue("Cantidad Solicitada: " + qtde);
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(4, (short)5, 4, (short)14);
	      planilha.addMergedRegion(r);
	      
	      int linha = 7;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Aseguradora");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(linha, (short)0, linha, (short)2);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(3);
	      celula.setCellValue("Instrumento");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(linha, (short)3, linha, (short)4);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(5);
	      celula.setCellValue("Sección");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(linha, (short)5, linha, (short)6);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue("Asegurado");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(linha, (short)7, linha, (short)9);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(10);
	      celula.setCellValue("Tp. Persona");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(11);
	      celula.setCellValue("Tp. Doc");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(12);
	      celula.setCellValue("Identificación");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(13);
	      celula.setCellValue("Tomador");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(linha, (short)13, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(16);
	      celula.setCellValue("Tp. Persona");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(17);
	      celula.setCellValue("Tp. Doc");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(18);
	      celula.setCellValue("Identificación");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(19);
	      if(tipoValor.equals("valorPrima"))
	    	 celula.setCellValue("Vl. Prima Gs");
	      else if(tipoValor.equals("valorSinistro"))
			 celula.setCellValue("Vl. Siniestros Gs");
	      else
			 celula.setCellValue("Vl. Capital en Riesgo Gs");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(20);
	      celula.setCellValue("Situación");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(21);
	      celula.setCellValue("Ini. de Vigencia");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(22);
	      celula.setCellValue("Fin de Vigencia");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      celula = row.createCell(23);
	      celula.setCellValue("Ult. mes grabado");
	      celula.setCellStyle(estiloTituloTabela);
	      
	      linha++;
	      
	      String tipoDoc;
	      String numeroDoc;
	      String tipoPessoa2;
	      Collection<EntidadeBCP> pessoas;
	      Plano plano;
	      
	      for(Apolice apolice : colecao)
	      {
	    	  tipoDoc = "";
		      tipoPessoa2 = "";
		      numeroDoc = "";
	    	  
	    	  int linha2 = linha+1;
				
	    	  row = planilha.createRow(linha);
		      celula = row.createCell(0);
		      celula.setCellValue(apolice.obterOrigem().obterNome());
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)0, linha2, (short)2);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(3);
		      celula.setCellValue(apolice.obterNumeroApolice());
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)3, linha2, (short)4);
		      planilha.addMergedRegion(r);
		      
		      plano = apolice.obterPlano();
		      
		      celula = row.createCell(5);
		      if(plano!=null)
		    	  celula.setCellValue(plano.obterSecao());
		      else
		    	  celula.setCellValue("");
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)5, linha2, (short)6);
		      planilha.addMergedRegion(r);
		      
		      String nomeAsegurado = "";
		      if(apolice.obterNomeAsegurado()!=null)
		    	  nomeAsegurado = apolice.obterNomeAsegurado();
		      
		      if(apolice.obterTipoPessoa()!=null)
		    	  tipoPessoa2 = apolice.obterTipoPessoa();
		      
		      if(apolice.obterTipoIdentificacao()!=null)
		    	  tipoDoc = apolice.obterTipoIdentificacao();
		      
		      if(tipoDoc.indexOf("Cédula de Identidad")>-1)
					tipoDoc = "CI";
				
			  String numeroIdentificacao = apolice.obterNumeroIdentificacao();
			  if(numeroIdentificacao!=null)
				  numeroDoc = numeroIdentificacao.trim();
			  
			  pessoas = apolice.obterAsegurados();
			  for(EntidadeBCP pessoa : pessoas)
			  {
				  nomeAsegurado+="\n"+pessoa.getNome();
				  tipoPessoa2+="\n" + pessoa.getTipoPessoa();
				  tipoDoc+="\n" + pessoa.getTipoDocumento();
				  numeroDoc+="\n"+pessoa.getNumeroDoc();
			  }
			  
			  celula = row.createCell(7);
		      celula.setCellValue(nomeAsegurado.trim());
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)7, linha2, (short)9);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(10);
		      celula.setCellValue(tipoPessoa2.trim());
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)10, linha2, (short)10);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(11);
		      celula.setCellValue(tipoDoc.trim());
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)11, linha2, (short)11);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(12);
		      celula.setCellValue(numeroDoc.trim());
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)12, linha2, (short)12);
		      planilha.addMergedRegion(r);
		      
		      tipoDoc = "";
		      tipoPessoa2 = "";
		      numeroDoc = "";
		      
		      String nomeTomador = "";
		      if(apolice.obterNomeTomador()!=null)
		    	  nomeTomador = apolice.obterNomeTomador();
		      
		      pessoas = apolice.obterTomadores();
			  for(EntidadeBCP pessoa : pessoas)
			  {
				  nomeTomador+="\n"+pessoa.getNome();
				  tipoPessoa2+="\n" + pessoa.getTipoPessoa();
				  tipoDoc+="\n" + pessoa.getTipoDocumento();
				  numeroDoc+="\n"+pessoa.getNumeroDoc();
			  }
		      
		      celula = row.createCell(13);
		      celula.setCellValue(nomeTomador);
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)13, linha2, (short)15);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(16);
		      celula.setCellValue(tipoPessoa2.trim());
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)16, linha2, (short)16);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(17);
		      celula.setCellValue(tipoDoc.trim());
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)17, linha2, (short)17);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(18);
		      celula.setCellValue(numeroDoc.trim());
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)18, linha2, (short)18);
		      planilha.addMergedRegion(r);
		      
		      double valor = 0;
		      
		      celula = row.createCell(19);
		      if(tipoValor.equals("valorPrima"))
		    	  valor = apolice.obterPrimaGs();
		      else if(tipoValor.equals("valorSinistro"))
		    	  valor = apolice.obterValorTotalDosSinistros(dataInicio,dataFim);
		      else
		    	  valor = apolice.obterCapitalGs();
		      celula.setCellValue(format.format(valor));
		      celula.setCellStyle(estiloTextoD);
		      r = new Region(linha, (short)19, linha2, (short)19);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(20);
		      celula.setCellValue(apolice.obterSituacaoSeguro());
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)20, linha2, (short)20);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(21);
		      celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio()));
		      celula.setCellStyle(estiloTexto);
		      r = new Region(linha, (short)21, linha2, (short)21);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(22);
		      celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao()));
		      celula.setCellStyle(estiloTexto);
		      r = new Region(linha, (short)22, linha2, (short)22);
		      planilha.addMergedRegion(r);
		      
		      Aseguradora aseg = (Aseguradora) apolice.obterOrigem();
		      
		      String mesAno = "";
		      AgendaMovimentacao ag = aseg.obterUltimaAgendaMCI();
		      if(ag!=null)
		    	  mesAno = ag.obterMesMovimento() + "/" + ag.obterAnoMovimento();
		     
		      celula = row.createCell(23);
		      celula.setCellValue(mesAno);
		      celula.setCellStyle(estiloTexto);
		      r = new Region(linha, (short)23, linha2, (short)23);
		      planilha.addMergedRegion(r);
		      
		      linha+=2;
	      }
	      
	      linha+=2;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
    	  celula.setCellValue(textoUsuario);
	      celula.setCellStyle(estiloTituloE);
	      r = new Region(linha, (short)0, linha, (short)14);
	      planilha.addMergedRegion(r);
	      
	      wb.write(stream);

	      stream.flush();

	      stream.close();
	}
}
