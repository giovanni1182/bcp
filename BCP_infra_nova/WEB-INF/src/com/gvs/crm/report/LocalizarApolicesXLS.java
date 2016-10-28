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

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Plano;

import infra.config.InfraProperties;

public class LocalizarApolicesXLS extends Excel
{
	public LocalizarApolicesXLS(Collection<Apolice> apolices, String numeroApolice, String secao2, Aseguradora aseguradora, String nomeAsegurado, String plano2, String situacao, Date dataInicio, Date dataFim, String rucCi, String tomador,String tipoInstrumento, String nomeAsegurado1, String nomeAsegurado2, String textoUsuario) throws Exception
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
      
      HSSFCellStyle estiloTextoN_E = wb.createCellStyle();
      estiloTextoN_E.setFont(fonteTextoN);
      
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
      
      int linha = 6;
      
      if(!numeroApolice.equals(""))
      {
    	  row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Nº Instrumento: " + numeroApolice);
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
      }
      
      if(!tipoInstrumento.equals(""))
      {
    	  row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      if(tipoInstrumento.equals("0"))
	    	  celula.setCellValue("Instrumento: Todas");
	      else
	    	  celula.setCellValue("Instrumento: " + tipoInstrumento);
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
      }
      
      if(!secao2.equals(""))
      {
    	  row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Sección: " + secao2);
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
      }
      
      if(!plano2.equals(""))
      {
    	  row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Modalidad: " + plano2);
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
      }
      
      if(aseguradora!=null)
      {
    	  row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Aseguradora: " + aseguradora.obterNome());
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
      }
      
      if(!nomeAsegurado.equals(""))
      {
    	  row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Nombre Asegurado completo: " + nomeAsegurado);
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
      }
      
      if(!nomeAsegurado1.equals(""))
      {
    	  row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Nombre Asegurado parte 1: " + nomeAsegurado1);
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
      }
      
      if(!nomeAsegurado2.equals(""))
      {
    	  row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Nombre Asegurado parte 2: " + nomeAsegurado2);
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
      }
      
      if(!tomador.equals(""))
      {
    	  row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Tomador: " + tomador);
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
      }
      
      if(!rucCi.equals(""))
      {
    	  row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("RUC/CI: " + rucCi);
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
      }
      
      if(!situacao.equals(""))
      {
    	  row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      if(situacao.equals("0"))
	    	  celula.setCellValue("Situación: Todas");
	      else
	    	  celula.setCellValue("Situación: " + situacao);
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
      }
      
      if(dataInicio!=null && dataFim!=null)
      {
    	  row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Fecha Emisión: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
      }
      
      linha++;
      
      if(apolices.size() > 0)
      {
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(apolices.size() + " Instrumentos");
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)2);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Vigencia");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)0, linha, (short)2);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(3);
	      celula.setCellValue("Numero");
	      celula.setCellStyle(estiloTituloTabelaC);
	      
	      /*celula = row.createCell(4);
	      celula.setCellValue("Sección");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)4, linha, (short)6);
	      planilha.addMergedRegion(r);*/
	      
	      celula = row.createCell(4);
	      celula.setCellValue("Plan");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)4, linha, (short)6);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue("Asegurado");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)7, linha, (short)9);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(10);
	      celula.setCellValue("Tomador");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)10, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(13);
	      celula.setCellValue("Tipo Doc.");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)13, linha, (short)14);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(15);
	      celula.setCellValue("Numero Doc.");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)15, linha, (short)16);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(17);
	      celula.setCellValue("Tipo");
	      celula.setCellStyle(estiloTituloTabelaC);
	      
	      celula = row.createCell(18);
	      celula.setCellValue("Aseguradora");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)18, linha, (short)20);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(21);
	      celula.setCellValue("Situación");
	      celula.setCellStyle(estiloTituloTabelaC);
	      
	      celula = row.createCell(22);
	      celula.setCellValue("Capital Asegurado");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)22, linha, (short)23);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(24);
	      celula.setCellValue("Capital Cedido");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)24, linha, (short)25);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(26);
	      celula.setCellValue("Prima Cedida");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)26, linha, (short)27);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(28);
	      celula.setCellValue("Comisión Cedida");
	      celula.setCellStyle(estiloTituloTabelaC);
	      r = new Region(linha, (short)28, linha, (short)29);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      double capitalRiegosGs = 0;
	      double primaGs = 0;
	      double principalGs = 0;
	      double incapacidadeGs = 0;
	      double enfermidadeGs = 0;
	      double acidentesGs = 0;
	      double outrosGs = 0;
	      double iteresGs = 0;
	      double premioGs = 0;
	      double comissaoGs = 0;
	      String identificadorPlano;
	      boolean mostraPlano;
	      Plano plano;
	      
	      for(Apolice apolice : apolices)
	      {
	    	  plano = apolice.obterPlano();
	    	  
	    	  identificadorPlano = "";
	    	  plano = apolice.obterPlano();
				
	    	  if(plano!=null)
	    		  identificadorPlano = plano.obterIdentificador();
				
	    	  mostraPlano = !identificadorPlano.equals("0") & !identificadorPlano.toLowerCase().equals("n") && !identificadorPlano.equals("");
	    	  
	    	  row = planilha.createRow(linha);
	          celula = row.createCell(0);
	          celula.setCellValue(new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaInicio()) + " - "	+ new SimpleDateFormat("dd/MM/yyyy").format(apolice.obterDataPrevistaConclusao()));
	          celula.setCellStyle(estiloTexto);
	          r = new Region(linha, (short)0, linha, (short)2);
	          planilha.addMergedRegion(r);
	          
	          celula = row.createCell(3);
	          celula.setCellValue(apolice.obterNumeroApolice());
	          celula.setCellStyle(estiloTexto);
	          
	         /* celula = row.createCell(4);
	          celula.setCellStyle(estiloTextoE);
	          ClassificacaoContas secao = apolice.obterSecao();
	          if(secao!=null)
	        	  celula.setCellValue(secao.obterApelido());
	          else
	        	  celula.setCellValue("");
	          r = new Region(linha, (short)4, linha, (short)6);
	          planilha.addMergedRegion(r);*/
	          
	          celula = row.createCell(4);
	          if(mostraPlano)
	        	  celula.setCellValue(plano.obterPlano());
	          else
	        	  celula.setCellValue("");
	          celula.setCellStyle(estiloTextoE);
	          r = new Region(linha, (short)4, linha, (short)6);
	          planilha.addMergedRegion(r);
	          
	          celula = row.createCell(7);
	          celula.setCellValue(apolice.obterNomeAsegurado());
	          celula.setCellStyle(estiloTextoE);
	          r = new Region(linha, (short)7, linha, (short)9);
	          planilha.addMergedRegion(r);
	          
	          celula = row.createCell(10);
		      celula.setCellValue(apolice.obterNomeTomador());
		      celula.setCellStyle(estiloTextoE);
		      r = new Region(linha, (short)10, linha, (short)12);
		      planilha.addMergedRegion(r);
	          
	          celula = row.createCell(13);
	          celula.setCellValue(apolice.obterTipoIdentificacao());
	          celula.setCellStyle(estiloTextoE);
	          r = new Region(linha, (short)13, linha, (short)14);
	          planilha.addMergedRegion(r);
	          
	          celula = row.createCell(15);
	          String numeroIden = apolice.obterNumeroIdentificacao();
	          if(numeroIden!=null)
	        	  celula.setCellValue(numeroIden.trim());
	          else
	        	  celula.setCellValue("");
	          celula.setCellStyle(estiloTextoE);
	          r = new Region(linha, (short)15, linha, (short)16);
	          planilha.addMergedRegion(r);
	          
	          celula = row.createCell(17);
	          celula.setCellValue(apolice.obterTipo());
	          celula.setCellStyle(estiloTextoE);
	          
	          celula = row.createCell(18);
	          celula.setCellValue(apolice.obterOrigem().obterNome());
	          celula.setCellStyle(estiloTextoE);
	          r = new Region(linha, (short)18, linha, (short)20);
	          planilha.addMergedRegion(r);
	          
	          celula = row.createCell(21);
	          celula.setCellValue(apolice.obterSituacaoSeguro());
	          celula.setCellStyle(estiloTextoE);
	          
	          double capital = apolice.obterCapitalGs();
	          String[] valoresSujos = apolice.obterValoresBuscaInstrumento().split(";");
	          
	          double totalCapitalCedido = Double.valueOf(valoresSujos[0]);
	          double totalPrimaCedida = Double.valueOf(valoresSujos[1]);
	          double totalComissaoCedida = Double.valueOf(valoresSujos[2]);
	          
	          celula = row.createCell(22);
		      celula.setCellValue(formataValor.format(capital));
		      celula.setCellStyle(estiloTextoD);
		      r = new Region(linha, (short)22, linha, (short)23);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(24);
		      celula.setCellValue(formataValor.format(totalCapitalCedido));
		      celula.setCellStyle(estiloTextoD);
		      r = new Region(linha, (short)24, linha, (short)25);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(26);
		      celula.setCellValue(formataValor.format(totalPrimaCedida));
		      celula.setCellStyle(estiloTextoD);
		      r = new Region(linha, (short)26, linha, (short)27);
		      planilha.addMergedRegion(r);
		      
		      celula = row.createCell(28);
		      celula.setCellValue(formataValor.format(totalComissaoCedida));
		      celula.setCellStyle(estiloTextoD);
		      r = new Region(linha, (short)28, linha, (short)29);
		      planilha.addMergedRegion(r);
	          
	          capitalRiegosGs+=capital;
	          primaGs+=apolice.obterPrimaGs();
	          principalGs+=apolice.obterPrincipalGs();
	          incapacidadeGs+=apolice.obterIncapacidadeGs();
	          enfermidadeGs+=apolice.obterEnfermidadeGs();
	          acidentesGs+=apolice.obterAcidentesGs();
	          outrosGs+=apolice.obterOutrosGs();
	          iteresGs+=apolice.obterFinanciamentoGs();
	          premioGs+=apolice.obterPremiosGs();
	          comissaoGs+=apolice.obterComissaoGs();
	          
	          linha++;
	      }
	      
	      linha++;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Totalización");
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Cantidad de Pólizas:" + apolices.size());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Capital en Riesgo en Gs:" + formataValor.format(capitalRiegosGs));
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Prima en Gs:" + formataValor.format(primaGs));
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Principal en Gs:" + formataValor.format(principalGs));
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Incapacidad en Gs:" + formataValor.format(incapacidadeGs));
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Enfermidad en Gs:" + formataValor.format(enfermidadeGs));
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Accidentes en Gs:" + formataValor.format(acidentesGs));
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Otros en Gs:" + formataValor.format(outrosGs));
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Interés por Financiamento en Gs:" + formataValor.format(iteresGs));
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Premio en Gs:" + formataValor.format(premioGs));
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("Comisión en Gs:" + formataValor.format(comissaoGs));
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
      }
      else
      {
    	  row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("NO FUE ENCONTRADA PÓLIZA PARA LA PESQUISA REALIZADA");
	      celula.setCellStyle(estiloTextoN);
	      r = new Region(linha, (short)0, linha, (short)15);
	      planilha.addMergedRegion(r);
      }
      
      linha+=2;
      
      row = planilha.createRow(linha);
      celula = row.createCell(0);
      celula.setCellValue(textoUsuario);
      celula.setCellStyle(estiloTextoN);
      r = new Region(linha, (short)0, linha, (short)15);
      planilha.addMergedRegion(r);
      
      wb.write(stream);
      stream.flush();
      stream.close();
	}
}
