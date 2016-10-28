package com.gvs.crm.report;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
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
import com.gvs.crm.model.AseguradoraHome;
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Inscricao;

import infra.config.InfraProperties;

public class BalancoGeralXLS extends Excel
{
	public BalancoGeralXLS(Entidade aseguradora, String mes, String ano, EntidadeHome home, boolean acumulado, AseguradoraHome aseguradoraHome) throws Exception
	{
		String mesAno = mes+ano;
		
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
	      
	      if(!acumulado)
	    	  celula.setCellValue("Balance General " + aseguradora.obterNome());
	      else
	    	  celula.setCellValue("Balance General acumulado por Aseguradora");
	      celula.setCellStyle(estiloTitulo);
	    		         
	      Region r = new Region(1, (short)5, 1, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(2);
	      celula = row.createCell(5);
	      celula.setCellValue("Mes/Año: " + mes + "/" + ano);
	      celula.setCellStyle(estiloTitulo);
	      r = new Region(2, (short)5, 2, (short)14);
	      planilha.addMergedRegion(r);
	      
	      row = planilha.createRow(6);
	      celula = row.createCell(0);
	      celula.setCellValue("ACTIVO");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)0, 6, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue("VALOR");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)4, 6, (short)5);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue("PASIVO Y PATRIMONIO NETO");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)7, 6, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue("VALOR");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(6, (short)11, 6, (short)12);
	      planilha.addMergedRegion(r);
	      
	      ClassificacaoContas ativo1 = (ClassificacaoContas) home.obterEntidadePorApelido("0101000000");
	      ClassificacaoContas passivo1 = (ClassificacaoContas) home.obterEntidadePorApelido("0201000000");
	      ClassificacaoContas ativo2 = (ClassificacaoContas) home.obterEntidadePorApelido("0102000000");
	      ClassificacaoContas passivo2 = (ClassificacaoContas) home.obterEntidadePorApelido("0202000000");
	      ClassificacaoContas ativo3 = (ClassificacaoContas) home.obterEntidadePorApelido("0103000000");
	      ClassificacaoContas passivo3 = (ClassificacaoContas) home.obterEntidadePorApelido("0203000000");
	      ClassificacaoContas ativo4 = (ClassificacaoContas) home.obterEntidadePorApelido("0104000000");
	      ClassificacaoContas passivo4 = (ClassificacaoContas) home.obterEntidadePorApelido("0204000000");
	      ClassificacaoContas ativo5 = (ClassificacaoContas) home.obterEntidadePorApelido("0105000000");
	      ClassificacaoContas passivo5 = (ClassificacaoContas) home.obterEntidadePorApelido("0205000000");
	      ClassificacaoContas ativo6 = (ClassificacaoContas) home.obterEntidadePorApelido("0106000000");
	      ClassificacaoContas passivo6 = (ClassificacaoContas) home.obterEntidadePorApelido("0206000000");
	      ClassificacaoContas ativo7 = (ClassificacaoContas) home.obterEntidadePorApelido("0107000000");
	      ClassificacaoContas passivo10 = (ClassificacaoContas) home.obterEntidadePorApelido("0210000000");
	      ClassificacaoContas ativo8 = (ClassificacaoContas) home.obterEntidadePorApelido("0108000000");
	      ClassificacaoContas passivo11 = (ClassificacaoContas) home.obterEntidadePorApelido("0211000000");
	      ClassificacaoContas ativo9 = (ClassificacaoContas) home.obterEntidadePorApelido("0109000000");
	      ClassificacaoContas passivo12 = (ClassificacaoContas) home.obterEntidadePorApelido("0212000000");
	      ClassificacaoContas passivo13 = (ClassificacaoContas) home.obterEntidadePorApelido("0213000000");
	      ClassificacaoContas passivo14 = (ClassificacaoContas) home.obterEntidadePorApelido("0214000000");
	      ClassificacaoContas patrimonio1 = (ClassificacaoContas) home.obterEntidadePorApelido("0301000000");
	      ClassificacaoContas patrimonio2 = (ClassificacaoContas) home.obterEntidadePorApelido("0302000000");
	      ClassificacaoContas patrimonio3 = (ClassificacaoContas) home.obterEntidadePorApelido("0303000000");
	      ClassificacaoContas patrimonio4 = (ClassificacaoContas) home.obterEntidadePorApelido("0304000000");
	      
	      double valorAtivo1 = 0;
	      double valorPassivo1 = 0;
	      double valorAtivo2 = 0;
	      double valorPassivo2 = 0;
	      double valorAtivo3 = 0;
	      double valorPassivo3 = 0;
	      double valorAtivo4 = 0;
	      double valorPassivo4 = 0;
	      double valorAtivo5 = 0;
	      double valorPassivo5 = 0;
	      double valorAtivo6 = 0;
	      double valorPassivo6 = 0;
	      double valorAtivo7 = 0;
	      double valorPassivo10 = 0;
	      double valorAtivo8 = 0;
	      double valorPassivo11 = 0;
	      double valorAtivo9 = 0;
	      double valorPassivo12 = 0;
	      double valorPassivo13 = 0;
	      double valorPassivo14 = 0;
	      double valorPatrimonio1 = 0;
	      double valorPatrimonio2 = 0;
	      double valorPatrimonio3 = 0;
	      double valorPatrimonio4 = 0;
	      
	      if(!acumulado)
	      {
		      valorAtivo1 = ativo1.obterTotalizacaoExistente(aseguradora,mesAno);
		      valorPassivo1 = passivo1.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorAtivo2 = ativo2.obterTotalizacaoExistente(aseguradora,mesAno);
		      valorPassivo2 = passivo2.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorAtivo3 = ativo3.obterTotalizacaoExistente(aseguradora,mesAno);
		      valorPassivo3 = passivo3.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorAtivo4 = ativo4.obterTotalizacaoExistente(aseguradora,mesAno);
		      valorPassivo4 = passivo4.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorAtivo5 = ativo5.obterTotalizacaoExistente(aseguradora,mesAno);
		      valorPassivo5 = passivo5.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorAtivo6 = ativo6.obterTotalizacaoExistente(aseguradora,mesAno);
		      valorPassivo6 = passivo6.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorAtivo7 = ativo7.obterTotalizacaoExistente(aseguradora,mesAno);
		      valorPassivo10 = passivo10.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorAtivo8 = ativo8.obterTotalizacaoExistente(aseguradora,mesAno);
		      valorPassivo11 = passivo11.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorAtivo9 = ativo9.obterTotalizacaoExistente(aseguradora,mesAno);
		      valorPassivo12 = passivo12.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorPassivo13 = passivo13.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorPassivo14 = passivo14.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorPatrimonio1 = patrimonio1.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorPatrimonio2 = patrimonio2.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorPatrimonio3 = patrimonio3.obterTotalizacaoExistente(aseguradora, mesAno);
		      valorPatrimonio4 = patrimonio4.obterTotalizacaoExistente(aseguradora, mesAno);
	      }
	      else
	      {
	    	  for(Iterator i = aseguradoraHome.obterAseguradorasRelatorio(mes+ano).iterator() ; i.hasNext() ; )
	    	  {
	    		  Aseguradora aseguradora2 = (Aseguradora) i.next();
	    		  
	    		  Inscricao inscricao = (Inscricao) aseguradora2.obterInscricaoAtiva();

					if (inscricao != null)
					{
						int numeroInscricao = Integer.parseInt(inscricao.obterInscricao());
	
						if (numeroInscricao <= 80)
						{
	    		    		  valorAtivo1 += ativo1.obterTotalizacaoExistente(aseguradora2,mesAno);
						      valorPassivo1 += passivo1.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorAtivo2 += ativo2.obterTotalizacaoExistente(aseguradora2,mesAno);
						      valorPassivo2 += passivo2.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorAtivo3 += ativo3.obterTotalizacaoExistente(aseguradora2,mesAno);
						      valorPassivo3 += passivo3.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorAtivo4 += ativo4.obterTotalizacaoExistente(aseguradora2,mesAno);
						      valorPassivo4 += passivo4.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorAtivo5 += ativo5.obterTotalizacaoExistente(aseguradora2,mesAno);
						      valorPassivo5 += passivo5.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorAtivo6 += ativo6.obterTotalizacaoExistente(aseguradora2,mesAno);
						      valorPassivo6 += passivo6.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorAtivo7 += ativo7.obterTotalizacaoExistente(aseguradora2,mesAno);
						      valorPassivo10 += passivo10.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorAtivo8 += ativo8.obterTotalizacaoExistente(aseguradora2,mesAno);
						      valorPassivo11 += passivo11.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorAtivo9 += ativo9.obterTotalizacaoExistente(aseguradora2,mesAno);
						      valorPassivo12 += passivo12.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorPassivo13 += passivo13.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorPassivo14 += passivo14.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorPatrimonio1 += patrimonio1.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorPatrimonio2 += patrimonio2.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorPatrimonio3 += patrimonio3.obterTotalizacaoExistente(aseguradora2, mesAno);
						      valorPatrimonio4 += patrimonio4.obterTotalizacaoExistente(aseguradora2, mesAno);
						}
					}
	    	  }
	      }
	      
	      int linha = 7;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(ativo1.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue(format.format(valorAtivo1));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)4, linha, (short)5);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue(passivo1.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPassivo1));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(ativo2.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue(format.format(valorAtivo2));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)4, linha, (short)5);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue(passivo2.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPassivo2));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(ativo3.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue(format.format(valorAtivo3));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)4, linha, (short)5);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue(passivo3.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPassivo3));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(ativo4.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue(format.format(valorAtivo4));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)4, linha, (short)5);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue(passivo4.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPassivo4));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(ativo5.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue(format.format(valorAtivo5));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)4, linha, (short)5);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue(passivo5.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPassivo5));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(ativo6.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue(format.format(valorAtivo6));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)4, linha, (short)5);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue(passivo6.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPassivo6));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(ativo7.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue(format.format(valorAtivo7));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)4, linha, (short)5);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue(passivo10.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPassivo10));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(ativo8.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue(format.format(valorAtivo8));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)4, linha, (short)5);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue(passivo11.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPassivo11));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue(ativo9.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)0, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue(format.format(valorAtivo9));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)4, linha, (short)5);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue(passivo12.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPassivo12));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(7);
	      celula.setCellValue(passivo13.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPassivo13));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(7);
	      celula.setCellValue(passivo14.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPassivo14));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      double totalAtivo = valorAtivo1 + valorAtivo2 + valorAtivo3 + valorAtivo4 + valorAtivo5 + valorAtivo6 + valorAtivo7
			+ valorAtivo8 + valorAtivo9;
	      
	      double totalPassivo = valorPassivo1 + valorPassivo2 + valorPassivo3	+ valorPassivo4 + valorPassivo5 + valorPassivo6
	      + valorPassivo10 + valorPassivo11 + valorPassivo12
	      + valorPassivo13 + valorPassivo14;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(7);
	      celula.setCellValue("PASIVO TOTAL");
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(totalPassivo));
	      celula.setCellStyle(estiloTextoN_D);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha+=2;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(7);
	      celula.setCellValue("PATRIMONIO NETO");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue("VALOR");
	      celula.setCellStyle(estiloTituloTabela);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(7);
	      celula.setCellValue(patrimonio1.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPatrimonio1));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(7);
	      celula.setCellValue(patrimonio2.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPatrimonio2));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(7);
	      celula.setCellValue(patrimonio3.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPatrimonio3));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(7);
	      celula.setCellValue(patrimonio4.obterNome());
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(valorPatrimonio4));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha++;
	      
	      double totalPatrimonio = valorPatrimonio1 + valorPatrimonio2 + valorPatrimonio3 + valorPatrimonio4;
	      double totalExercicio = totalAtivo - (totalPassivo + totalPatrimonio);
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(7);
	      celula.setCellValue("RESULTADO DEL EJERCICIO");
	      celula.setCellStyle(estiloTextoE);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(totalExercicio));
	      celula.setCellStyle(estiloTextoD);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	     /* System.out.println("totalAtivo BG EX: " + format.format(totalAtivo));
			System.out.println("totalPassivo BG EX: " + format.format(totalPassivo));
			System.out.println("totalPatrimonio BG EX: " + format.format(totalPatrimonio));
			System.out.println("totalExercicio BG EX: " + format.format(totalExercicio));*/
	      
	      linha+=2;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(7);
	      celula.setCellValue("TOTAL PATRIMONIO NETO");
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      double totalPatrimonioNeto = totalPatrimonio + totalExercicio;
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(totalPatrimonioNeto));
	      celula.setCellStyle(estiloTextoN_D);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      linha+=2;
	      
	      row = planilha.createRow(linha);
	      celula = row.createCell(0);
	      celula.setCellValue("TOTAL ACTIVO");
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)0, linha, (short)3);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(4);
	      celula.setCellValue(format.format(totalAtivo));
	      celula.setCellStyle(estiloTextoN_D);
	      r = new Region(linha, (short)4, linha, (short)5);
	      planilha.addMergedRegion(r);
	      
	      celula = row.createCell(7);
	      celula.setCellValue("TOTAL PASIVO E PATRIMONIO NETO");
	      celula.setCellStyle(estiloTextoN_E);
	      r = new Region(linha, (short)7, linha, (short)10);
	      planilha.addMergedRegion(r);
	      
	      
	      celula = row.createCell(11);
	      celula.setCellValue(format.format(totalPassivo + totalPatrimonioNeto));
	      celula.setCellStyle(estiloTextoN_D);
	      r = new Region(linha, (short)11, linha, (short)12);
	      planilha.addMergedRegion(r);
	      
	      wb.write(stream);

	      stream.flush();

	      stream.close();
	}
}
