package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
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
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

import infra.config.InfraProperties;

public class RelProdutividadeAgentesCorredoresXLS extends Excel 
{
	private HSSFCellStyle estiloTitulo,estiloTituloJ,estiloTexto,estiloTextoN,estiloTextoCor,estiloTextoE,estiloTextoD,estiloTextoCorE,estiloTituloTabela,estiloTituloTabelaE,estiloTextoN_D;
	private DecimalFormat format = new DecimalFormat("#,##0.00");
	private SimpleDateFormat formataData = new SimpleDateFormat("dd/MM/yyyy");
	private int linha,qtdeGeral;
	private double totalGeralComissaoGs,totalGeralComissaoMe, totalGeralPrimaGs, totalGeralPremioGs, totalGeralCapitalGs, totalGeralCapitalMe;
	
	public RelProdutividadeAgentesCorredoresXLS(Aseguradora aseguradora, Date dataInicio, Date dataFim, String textoUsuario, EntidadeHome entidadeHome, AseguradoraHome aseguradoraHome) throws Exception
	{
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		 this.setCaminho(caminho);
		 
		 Collection<Aseguradora> aseguradoras = new ArrayList<Aseguradora>();
		 if(aseguradora == null)
			 aseguradoras = entidadeHome.obterAseguradoras();
		 else
			 aseguradoras.add(aseguradora);
		
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
	      
	      estiloTitulo = wb.createCellStyle();
	      estiloTitulo.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTitulo.setFont(fonteTitulo);
	      
	      estiloTituloJ = wb.createCellStyle();
	      estiloTituloJ.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
	      estiloTituloJ.setFont(fonteTitulo);
	      
	      estiloTexto = wb.createCellStyle();
	      estiloTexto.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTexto.setFont(fonteTexto);
	      
	      estiloTextoN = wb.createCellStyle();
	      estiloTextoN.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTextoN.setFont(fonteTextoN);
	      
	      estiloTextoCor = wb.createCellStyle();
	      estiloTextoCor.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTextoCor.setFont(fonteTexto);
	      estiloTextoCor.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
	      estiloTextoCor.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      
	      estiloTextoE = wb.createCellStyle();
	      estiloTextoE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	      estiloTextoE.setFont(fonteTexto);
	      
	      estiloTextoD = wb.createCellStyle();
	      estiloTextoD.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	      estiloTextoD.setFont(fonteTexto);
	      
	      estiloTextoN_D = wb.createCellStyle();
	      estiloTextoN_D.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
	      estiloTextoN_D.setFont(fonteTextoN);
	      
	      estiloTextoCorE = wb.createCellStyle();
	      estiloTextoCorE.setAlignment(HSSFCellStyle.ALIGN_LEFT);
	      estiloTextoCorE.setFont(fonteTextoN);
	      estiloTextoCorE.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
	      estiloTextoCorE.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      
	      estiloTituloTabela = wb.createCellStyle();
	      estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
	      estiloTituloTabela.setFont(fonteTituloTabela);
	      estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_80_PERCENT.index);
	      estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
	      
	      estiloTituloTabelaE = wb.createCellStyle();
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
	      
	      celula.setCellValue("Produción Agentes y Corredores");
	      celula.setCellStyle(estiloTitulo);
	    		         
	      Region r = new Region(1, (short)5, 1, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(2);
	      celula = row.createCell(5);
	      celula.setCellValue("Periodo: " + new SimpleDateFormat("dd/MM/yyyy").format(dataInicio) + " hasta " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim));
	      celula.setCellStyle(estiloTitulo);
	      
	      r = new Region(2, (short)5, 2, (short)14);
	      planilha.addMergedRegion(r);
	      
	      linha = 6;
	      
	      Collection<Entidade> agentes;
	      Collection<Entidade> corredores;
	      Collection<Apolice> apolices;
	      Aseguradora aseg;
	      Entidade agente,corredor;
	      
	      for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
	      {
	    	  aseg = i.next();
	    	  
	    	  row = planilha.createRow(linha);
		      celula = row.createCell(0);
			
		      celula.setCellValue(aseg.obterNome());
		      celula.setCellStyle(estiloTituloTabela);
		      r = new Region(linha, (short)0, linha, (short)14);
		      planilha.addMergedRegion(r);
		      
		      linha++;
		      
		      //agentes = new ArrayList<Entidade>();
		      //corredores = new ArrayList<Entidade>();
		      
	    	  agentes = aseguradoraHome.obterAgentesPorPeridodo(aseg, dataInicio, dataFim);
	    	  corredores = aseguradoraHome.obterCorredoresPorPeridodo(aseg, dataInicio, dataFim);
	    	  
	    	  if(agentes.size() == 0 && corredores.size() == 0)
	    	  {
	    		  row = planilha.createRow(linha);
			      celula = row.createCell(0);
				
			      celula.setCellValue("No hay datos en el período");
			      r = new Region(linha, (short)0, linha, (short)14);
			      planilha.addMergedRegion(r);
			      
			      linha++;
	    	  }
				
	    	  for (Iterator<Entidade> j = agentes.iterator(); j.hasNext();) 
	    	  {
	    		  agente = j.next();

	    		  row = planilha.createRow(linha);
			      celula = row.createCell(0);
			      
			      celula.setCellValue("Agente: " + agente.obterNome());
			      celula.setCellStyle(estiloTituloTabelaE);
			      r = new Region(linha, (short)0, linha, (short)14);
			      planilha.addMergedRegion(r);
			      
			      linha++;
					
	    		  apolices = agente.obterApolicesComoAgentePorPeriodo(dataInicio, dataFim, aseg);
					
	    		  this.montaTabela(planilha, apolices);
	    		  linha++;
	    	  }
			
	    	  for (Iterator<Entidade> j = corredores.iterator(); j.hasNext();) 
	    	  {
	    		  corredor = j.next();

	    		  row = planilha.createRow(linha);
			      celula = row.createCell(0);
			      
			      celula.setCellValue("Corredor: " + corredor.obterNome());
			      celula.setCellStyle(estiloTituloTabelaE);
			      r = new Region(linha, (short)0, linha, (short)14);
			      planilha.addMergedRegion(r);
			      
			      linha++;
					
	    		  apolices = corredor.obterApolicesComoCorredorPorPeriodo(dataInicio, dataFim, aseg);
					
	    		  this.montaTabela(planilha, apolices);
	    	  }
	      }
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("TOTAL GENERAL");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(linha, (short)0, linha, (short)14);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTextoN);
		      
	      celula = row.createCell(1);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTextoN);
		      
	      celula = row.createCell(2);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTextoN);
	      r = new Region(linha, (short)2, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTextoN);
	      
	      celula = row.createCell(5);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTextoN);
	      r = new Region(linha, (short)5, linha, (short)6);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTextoN);
	      
	      celula = row.createCell(8);
	      celula.setCellValue("");
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
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTextoN);
	      
	      celula = row.createCell(6);
	      celula.setCellValue("");
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
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("TOTAL");
	      celula.setCellStyle(estiloTextoN);
	      
	      celula = row.createCell(1);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTextoE);
	      
	      celula = row.createCell(2);
	      celula.setCellValue(qtdeGeral);
	      celula.setCellStyle(estiloTextoN);
	      r = new Region(linha, (short)2, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTexto);
	      
	      celula = row.createCell(5);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTexto);
	      
	      celula = row.createCell(6);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTexto);
	      
	      celula = row.createCell(7);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTextoE);
	      
	      celula = row.createCell(8);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTextoE);
	      
	      celula = row.createCell(9);
	      celula.setCellValue(format.format(totalGeralComissaoGs));
	      celula.setCellStyle(estiloTextoN_D);
	      
	      celula = row.createCell(10);
	      celula.setCellValue(format.format(totalGeralComissaoMe));
	      celula.setCellStyle(estiloTextoN_D);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(totalGeralPrimaGs));
	      celula.setCellStyle(estiloTextoN_D);
	      
	      celula = row.createCell(12);
	      celula.setCellValue(format.format(totalGeralPremioGs));
	      celula.setCellStyle(estiloTextoN_D);
	      
	      celula = row.createCell(13);
	      celula.setCellValue(format.format(totalGeralCapitalGs));
	      celula.setCellStyle(estiloTextoN_D);
	      
	      celula = row.createCell(14);
	      celula.setCellValue(format.format(totalGeralCapitalMe));
	      celula.setCellStyle(estiloTextoN_D);
	      
	      linha+=3;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(textoUsuario);
	      celula.setCellStyle(estiloTitulo);
	      
	      r = new Region(linha, (short)0, linha, (short)14);
	      planilha.addMergedRegion(r);
	      
	      wb.write(stream);

	      stream.flush();

	      stream.close();
	}
	
	private void montaTabela(HSSFSheet planilha, Collection<Apolice> apolices) throws Exception
	{
		  HSSFRow row = planilha.createRow(linha);
		  HSSFCell celula = row.createCell(0);
	      celula.setCellValue("Nº Póliza");
	      celula.setCellStyle(estiloTextoN);
	      
	      celula = row.createCell(1);
	      celula.setCellValue("Situación");
	      celula.setCellStyle(estiloTextoN);
	      
	      celula = row.createCell(2);
	      celula.setCellValue("Asegurado");
	      celula.setCellStyle(estiloTextoN);
	      Region r = new Region(linha, (short)2, linha, (short)3);
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
	      celula.setCellValue("Tipo Operación");
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
	      Apolice apolice;
	      double comissaoGs,comissaoMe, primaGs, premioGs, capitalGs, capitalMe;
	      double totalComissaoGs=0,totalComissaoMe=0, totalPrimaGs=0, totalPremioGs=0, totalCapitalGs=0, totalCapitalMe=0;
	      int qtde = apolices.size();
			
	      for (Iterator<Apolice> j = apolices.iterator(); j.hasNext();) 
	      {
	    	  apolice = j.next();
	    	  
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
		      celula.setCellValue(formataData.format(apolice.obterDataEmissao()));
		      celula.setCellStyle(estiloTexto);
		      
		      celula = row.createCell(5);
		      celula.setCellValue(formataData.format(apolice.obterDataPrevistaInicio()));
		      celula.setCellStyle(estiloTexto);
		      
		      celula = row.createCell(6);
		      celula.setCellValue(formataData.format(apolice.obterDataPrevistaConclusao()));
		      celula.setCellStyle(estiloTexto);
		      
		      celula = row.createCell(7);
		      celula.setCellValue(apolice.obterTipo());
		      celula.setCellStyle(estiloTextoE);
		      
		      celula = row.createCell(8);
		      celula.setCellValue(apolice.obterSecao().obterNome());
		      celula.setCellStyle(estiloTextoE);
		      
		      comissaoGs = apolice.obterComissaoGs();
		      comissaoMe = apolice.obterComissaoMe();
		      primaGs = apolice.obterPrimaGs();
		      premioGs = apolice.obterPremiosGs();
		      capitalGs = apolice.obterCapitalGs();
		      capitalMe = apolice.obterCapitalMe();
		      
		      totalComissaoGs+=comissaoGs;
		      totalComissaoMe+=comissaoMe;
		      totalPrimaGs+=primaGs;
		      totalPremioGs+=premioGs;
		      totalCapitalGs+=capitalGs;
		      totalCapitalMe+=capitalMe;
		      
		      celula = row.createCell(9);
		      celula.setCellValue(format.format(comissaoGs));
		      celula.setCellStyle(estiloTextoD);
		      
		      celula = row.createCell(10);
		      celula.setCellValue(format.format(comissaoMe));
		      celula.setCellStyle(estiloTextoD);
		      
		      celula = row.createCell(11);
		      celula.setCellValue(format.format(primaGs));
		      celula.setCellStyle(estiloTextoD);
		      
		      celula = row.createCell(12);
		      celula.setCellValue(format.format(premioGs));
		      celula.setCellStyle(estiloTextoD);
		      
		      celula = row.createCell(13);
		      celula.setCellValue(format.format(capitalGs));
		      celula.setCellStyle(estiloTextoD);
		      
		      celula = row.createCell(14);
		      celula.setCellValue(format.format(capitalMe));
		      celula.setCellStyle(estiloTextoD);
		      
		      linha++;
	      }
	      
	      row = planilha.createRow(linha);
	      
	      celula = row.createCell(0);
	      celula.setCellValue("TOTAL");
	      celula.setCellStyle(estiloTextoN);
	      
	      celula = row.createCell(1);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTextoE);
	      
	      celula = row.createCell(2);
	      celula.setCellValue(qtde);
	      celula.setCellStyle(estiloTextoN);
	      r = new Region(linha, (short)2, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTexto);
	      
	      celula = row.createCell(5);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTexto);
	      
	      celula = row.createCell(6);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTexto);
	      
	      celula = row.createCell(7);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTextoE);
	      
	      celula = row.createCell(8);
	      celula.setCellValue("");
	      celula.setCellStyle(estiloTextoE);
	      
	      celula = row.createCell(9);
	      celula.setCellValue(format.format(totalComissaoGs));
	      celula.setCellStyle(estiloTextoN_D);
	      
	      celula = row.createCell(10);
	      celula.setCellValue(format.format(totalComissaoMe));
	      celula.setCellStyle(estiloTextoN_D);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(totalPrimaGs));
	      celula.setCellStyle(estiloTextoN_D);
	      
	      celula = row.createCell(12);
	      celula.setCellValue(format.format(totalPremioGs));
	      celula.setCellStyle(estiloTextoN_D);
	      
	      celula = row.createCell(13);
	      celula.setCellValue(format.format(totalCapitalGs));
	      celula.setCellStyle(estiloTextoN_D);
	      
	      celula = row.createCell(14);
	      celula.setCellValue(format.format(totalCapitalMe));
	      celula.setCellStyle(estiloTextoN_D);
	      
	      qtdeGeral+=qtde;
	      totalGeralComissaoGs+=totalComissaoGs;
	      totalGeralComissaoMe+=totalComissaoMe;
	      totalGeralPrimaGs+=totalPrimaGs;
	      totalGeralPremioGs+=totalPremioGs;
	      totalGeralCapitalGs+=totalPrimaGs;
	      totalGeralCapitalMe+=totalCapitalMe;
	      
	      linha++;
	}
}
