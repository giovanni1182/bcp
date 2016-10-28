package com.gvs.crm.view;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

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
import com.gvs.crm.model.ClassificacaoContas;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.IndicadoresHome;
import com.gvs.crm.report.Excel;

import infra.config.InfraProperties;

public class CalculoIndicadoresXLS extends Excel
{
	private Map<String, Double> mesAtual;
	private ClassificacaoContas cConta0100,cConta0101,cConta0102,cConta0103,cConta0104,cConta0105,cConta0106,cConta0107,cConta0108,cConta0109,cConta010903,cConta010904,cConta010905,cConta010906,cConta0201,cConta0202,cConta0203,
	cConta0204,cConta0205,cConta0206,cConta0210,cConta0211,cConta0212,cConta0213,cConta0214,cConta0300,cConta0301,cConta0302,cConta0303,cConta0304,cConta0400,cConta0401,cConta0402,cConta0403,cConta0404,cConta0405,cConta0406,
	cConta0407,cConta0408,cConta0409,cConta0410,cConta0411,cConta0413,cConta0412,cConta0414,cConta0415,cConta0426,cConta0500,cConta0501,cConta0502,cConta0503,cConta0504,cConta0505,cConta0506,cConta0507,cConta0508,cConta0509,
	cConta0510,cConta0511,cConta0512,cConta0513,cConta0514,cConta0515,cConta0516,cConta0525,cConta0527;
	
	private Conta cConta0525010401;
	private String calculo7 = "";
	
	public CalculoIndicadoresXLS(Collection aseguradoras, Date data, IndicadoresHome indicadorHome, EntidadeHome home) throws Exception
	{
		Map<String, Entidade> contas = new TreeMap<String,Entidade>();
		
		cConta0100 = (ClassificacaoContas) home.obterEntidadePorApelido("0100000000");
		contas.put(cConta0100.obterApelido(), cConta0100);
		cConta0101 = (ClassificacaoContas) home.obterEntidadePorApelido("0101000000");
		contas.put(cConta0101.obterApelido(), cConta0101);
		cConta0102 = (ClassificacaoContas) home.obterEntidadePorApelido("0102000000");
		contas.put(cConta0102.obterApelido(), cConta0102);
		cConta0103 = (ClassificacaoContas) home.obterEntidadePorApelido("0103000000");
		contas.put(cConta0103.obterApelido(), cConta0103);
		cConta0104 = (ClassificacaoContas) home.obterEntidadePorApelido("0104000000");
		contas.put(cConta0104.obterApelido(), cConta0104);
		cConta0105 = (ClassificacaoContas) home.obterEntidadePorApelido("0105000000");
		contas.put(cConta0105.obterApelido(), cConta0105);
		cConta0106 = (ClassificacaoContas) home.obterEntidadePorApelido("0106000000");
		contas.put(cConta0106.obterApelido(), cConta0106);
		cConta0107 = (ClassificacaoContas) home.obterEntidadePorApelido("0107000000");
		contas.put(cConta0107.obterApelido(),cConta0107);
		cConta0108 = (ClassificacaoContas) home.obterEntidadePorApelido("0108000000");
		contas.put(cConta0108.obterApelido(), cConta0108);
		cConta0109 = (ClassificacaoContas) home.obterEntidadePorApelido("0109000000");
		contas.put(cConta0109.obterApelido(),cConta0109);
		cConta010903 = (ClassificacaoContas) home.obterEntidadePorApelido("0109030000");
		contas.put(cConta010903.obterApelido(),cConta010903);
		cConta010904 = (ClassificacaoContas) home.obterEntidadePorApelido("0109040000");
		contas.put(cConta010904.obterApelido(),cConta010904);
		cConta010905 = (ClassificacaoContas) home.obterEntidadePorApelido("0109050000");
		contas.put(cConta010905.obterApelido(),cConta010905);
		cConta010906 = (ClassificacaoContas) home.obterEntidadePorApelido("0109060000");
		contas.put(cConta010906.obterApelido(),cConta010906);
		cConta0201 = (ClassificacaoContas) home.obterEntidadePorApelido("0201000000");
		contas.put(cConta0201.obterApelido(),cConta0201);
		cConta0202 = (ClassificacaoContas) home.obterEntidadePorApelido("0202000000");
		contas.put(cConta0202.obterApelido(),cConta0202);
		cConta0203 = (ClassificacaoContas) home.obterEntidadePorApelido("0203000000");
		contas.put(cConta0203.obterApelido(),cConta0203);
		cConta0204 = (ClassificacaoContas) home.obterEntidadePorApelido("0204000000");
		contas.put(cConta0204.obterApelido(),cConta0204);
		cConta0205 = (ClassificacaoContas) home.obterEntidadePorApelido("0205000000");
		contas.put(cConta0205.obterApelido(),cConta0205);
		cConta0206 = (ClassificacaoContas) home.obterEntidadePorApelido("0206000000");
		contas.put(cConta0206.obterApelido(),cConta0206);
		cConta0210 = (ClassificacaoContas) home.obterEntidadePorApelido("0210000000");
		contas.put(cConta0210.obterApelido(),cConta0210);
		cConta0211 = (ClassificacaoContas) home.obterEntidadePorApelido("0211000000");
		contas.put(cConta0211.obterApelido(),cConta0211);
		cConta0212 = (ClassificacaoContas) home.obterEntidadePorApelido("0212000000");
		contas.put(cConta0212.obterApelido(),cConta0212);
		cConta0213 = (ClassificacaoContas) home.obterEntidadePorApelido("0213000000");
		contas.put(cConta0213.obterApelido(),cConta0213);
		cConta0214 = (ClassificacaoContas) home.obterEntidadePorApelido("0214000000");
		contas.put(cConta0214.obterApelido(),cConta0214);
		cConta0300 = (ClassificacaoContas) home.obterEntidadePorApelido("0300000000");
		contas.put(cConta0300.obterApelido(),cConta0300);
		cConta0301 = (ClassificacaoContas) home.obterEntidadePorApelido("0301000000");
		contas.put(cConta0301.obterApelido(),cConta0301);
		cConta0302 = (ClassificacaoContas) home.obterEntidadePorApelido("0302000000");
		contas.put(cConta0302.obterApelido(),cConta0302);
		cConta0303 = (ClassificacaoContas) home.obterEntidadePorApelido("0303000000");
		contas.put(cConta0303.obterApelido(),cConta0303);
		cConta0304 = (ClassificacaoContas) home.obterEntidadePorApelido("0304000000");
		contas.put(cConta0304.obterApelido(),cConta0304);
		cConta0400 = (ClassificacaoContas) home.obterEntidadePorApelido("0400000000");
		contas.put(cConta0400.obterApelido(),cConta0400);
		cConta0401 = (ClassificacaoContas) home.obterEntidadePorApelido("0401000000");
		contas.put(cConta0401.obterApelido(),cConta0401);
		cConta0402 = (ClassificacaoContas) home.obterEntidadePorApelido("0402000000");
		contas.put(cConta0402.obterApelido(),cConta0402);
		cConta0403 = (ClassificacaoContas) home.obterEntidadePorApelido("0403000000");
		contas.put(cConta0403.obterApelido(),cConta0403);
		cConta0404 = (ClassificacaoContas) home.obterEntidadePorApelido("0404000000");
		contas.put(cConta0404.obterApelido(),cConta0404);
		cConta0405 = (ClassificacaoContas) home.obterEntidadePorApelido("0405000000");
		contas.put(cConta0405.obterApelido(),cConta0405);
		cConta0406 = (ClassificacaoContas) home.obterEntidadePorApelido("0406000000");
		contas.put(cConta0406.obterApelido(),cConta0406);
		cConta0407 = (ClassificacaoContas) home.obterEntidadePorApelido("0407000000");
		contas.put(cConta0407.obterApelido(),cConta0407);
		cConta0408 = (ClassificacaoContas) home.obterEntidadePorApelido("0408000000");
		contas.put(cConta0408.obterApelido(),cConta0408);
		cConta0409 = (ClassificacaoContas) home.obterEntidadePorApelido("0409000000");
		contas.put(cConta0409.obterApelido(),cConta0409);
		cConta0410 = (ClassificacaoContas) home.obterEntidadePorApelido("0410000000");
		contas.put(cConta0410.obterApelido(),cConta0410);
		cConta0411 = (ClassificacaoContas) home.obterEntidadePorApelido("0411000000");
		contas.put(cConta0411.obterApelido(),cConta0411);
		cConta0413 = (ClassificacaoContas) home.obterEntidadePorApelido("0413000000");
		contas.put(cConta0413.obterApelido(),cConta0413);
		cConta0412 = (ClassificacaoContas) home.obterEntidadePorApelido("0412000000");
		contas.put(cConta0412.obterApelido(),cConta0412);
		cConta0414 = (ClassificacaoContas) home.obterEntidadePorApelido("0414000000");
		contas.put(cConta0414.obterApelido(),cConta0414);
		cConta0415 = (ClassificacaoContas) home.obterEntidadePorApelido("0415000000");
		contas.put(cConta0415.obterApelido(),cConta0415);
		cConta0426 = (ClassificacaoContas) home.obterEntidadePorApelido("0426000000");
		contas.put(cConta0426.obterApelido(),cConta0426);
		cConta0500 = (ClassificacaoContas) home.obterEntidadePorApelido("0500000000");
		contas.put(cConta0500.obterApelido(),cConta0500);
		cConta0501 = (ClassificacaoContas) home.obterEntidadePorApelido("0501000000");
		contas.put(cConta0501.obterApelido(),cConta0501);
		cConta0502 = (ClassificacaoContas) home.obterEntidadePorApelido("0502000000");
		contas.put(cConta0502.obterApelido(),cConta0502);
		cConta0503 = (ClassificacaoContas) home.obterEntidadePorApelido("0503000000");
		contas.put(cConta0503.obterApelido(),cConta0503);
		cConta0504 = (ClassificacaoContas) home.obterEntidadePorApelido("0504000000");
		contas.put(cConta0504.obterApelido(),cConta0504);
		cConta0505 = (ClassificacaoContas) home.obterEntidadePorApelido("0505000000");
		contas.put(cConta0505.obterApelido(),cConta0505);
		cConta0506 = (ClassificacaoContas) home.obterEntidadePorApelido("0506000000");
		contas.put(cConta0506.obterApelido(),cConta0506);
		cConta0507 = (ClassificacaoContas) home.obterEntidadePorApelido("0507000000");
		contas.put(cConta0507.obterApelido(),cConta0507);
		cConta0508 = (ClassificacaoContas) home.obterEntidadePorApelido("0508000000");
		contas.put(cConta0508.obterApelido(),cConta0508);
		cConta0509 = (ClassificacaoContas) home.obterEntidadePorApelido("0509000000");
		contas.put(cConta0509.obterApelido(),cConta0509);
		cConta0510 = (ClassificacaoContas) home.obterEntidadePorApelido("0510000000");
		contas.put(cConta0510.obterApelido(),cConta0510);
		cConta0511 = (ClassificacaoContas) home.obterEntidadePorApelido("0511000000");
		contas.put(cConta0511.obterApelido(),cConta0511);
		cConta0512 = (ClassificacaoContas) home.obterEntidadePorApelido("0512000000");
		contas.put(cConta0512.obterApelido(),cConta0512);
		cConta0513 = (ClassificacaoContas) home.obterEntidadePorApelido("0513000000");
		contas.put(cConta0513.obterApelido(),cConta0513);
		cConta0514 = (ClassificacaoContas) home.obterEntidadePorApelido("0514000000");
		contas.put(cConta0514.obterApelido(),cConta0514);
		cConta0515 = (ClassificacaoContas) home.obterEntidadePorApelido("0515000000");
		contas.put(cConta0515.obterApelido(),cConta0515);
		cConta0516 = (ClassificacaoContas) home.obterEntidadePorApelido("0516000000");
		contas.put(cConta0516.obterApelido(),cConta0516);
		cConta0525 = (ClassificacaoContas) home.obterEntidadePorApelido("0525000000");
		contas.put(cConta0525.obterApelido(),cConta0525);
		cConta0527 = (ClassificacaoContas) home.obterEntidadePorApelido("0527000000");
		contas.put(cConta0527.obterApelido(),cConta0527);
		cConta0525010401 = (Conta) home.obterEntidadePorApelido("0525010401");
		contas.put(cConta0525010401.obterApelido(),cConta0525010401);
		
		DecimalFormat formataValor = new DecimalFormat("#,##0.00");
		DecimalFormat formataValor6Casas = new DecimalFormat("#,######0.000000");
		//formataValor.setRoundingMode(RoundingMode.HALF_EVEN);
		
		String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
		
		FileOutputStream stream = new FileOutputStream(caminho);
		
		this.setCaminho(caminho);
		
		HSSFWorkbook wb = new HSSFWorkbook();

		HSSFFont fonteTitulo = wb.createFont();
        fonteTitulo.setFontHeightInPoints((short)10);
        fonteTitulo.setFontName("Arial");
        fonteTitulo.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
        
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
        estiloTextoN_E.setAlignment(HSSFCellStyle.ALIGN_LEFT);
        estiloTextoN_E.setFont(fonteTextoN);
        
        HSSFCellStyle estiloTextoN_D = wb.createCellStyle();
        estiloTextoN_D.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
        estiloTextoN_D.setFont(fonteTextoN);
        
        HSSFCellStyle estiloTituloTabela = wb.createCellStyle();
        estiloTituloTabela.setAlignment(HSSFCellStyle.ALIGN_CENTER);
        estiloTituloTabela.setFont(fonteTextoN);
        estiloTituloTabela.setFillForegroundColor(HSSFColor.GREY_25_PERCENT.index);
        estiloTituloTabela.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
        
        String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
        
        Map<String, Double> total12Meses = new TreeMap<String, Double>();
        mesAtual = new TreeMap<String, Double>();
        
        for(Iterator<Aseguradora> i = aseguradoras.iterator() ; i.hasNext() ; )
        {
        	Aseguradora aseguradora = i.next();
        	
        	 //if(aseguradora.obterId() == 5234)
			 //{
        	
        	int linha = 7;
        	
	        HSSFSheet planilha = wb.createSheet(aseguradora.obterNome());
	        
	        InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
	        byte [] bytes = IOUtils.toByteArray (is);
	        int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_JPEG); 
	        is.close();
	        
	        HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0,0,0, 0, (short) 0, 0, (short)3 , 6);  
	        anchoVivaBem.setAnchorType(3);  
	        planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
	        
	        HSSFRow row = planilha.createRow(1);
	        HSSFCell celula = row.createCell(5);
	        
	        celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
	        celula.setCellStyle(estiloTitulo);
	        Region r = new Region(1, (short)5, 1, (short)10);
	        planilha.addMergedRegion(r);
	        
	        row = planilha.createRow(2);
	        celula = row.createCell(5);
	        celula.setCellValue("CALCULO DE LOS INDICADORES FINANCEIROS");
	        celula.setCellStyle(estiloTitulo);
	        r = new Region(2, (short)5, 2, (short)10);
	        planilha.addMergedRegion(r);
	        
	        row = planilha.createRow(3);
	        celula = row.createCell(5);
	        celula.setCellValue(aseguradora.obterNome());
	        celula.setCellStyle(estiloTitulo);
	        r = new Region(3, (short)5, 3, (short)10);
	        planilha.addMergedRegion(r);
	        
	        row = planilha.createRow(linha);
	        celula = row.createCell(0);
	        celula.setCellValue("Cuentas Contables");
	        celula.setCellStyle(estiloTituloTabela);
	        r = new Region(linha, (short)0, linha, (short)2);
	        planilha.addMergedRegion(r);
	        
	        Calendar c = Calendar.getInstance();
			c.setTime(data);
			
			int coluna = 3;
			
			for(int j = 0 ; j < 12 ; j++)
			{
				String mesAno = new SimpleDateFormat("MM/yyyy").format(c.getTime());
				
				celula = row.createCell(coluna);
		        celula.setCellValue(mesAno);
		        celula.setCellStyle(estiloTituloTabela);
		        r = new Region(linha, (short)coluna, linha, (short)++coluna);
		        planilha.addMergedRegion(r);
		        
		        coluna++;
		        
		        c.add(Calendar.MONTH, -1);
			}
			
			int colunaFinal = coluna + 2;
			
			celula = row.createCell(coluna);
	        celula.setCellValue("Datos acumulados de los últimos 12 meses");
	        celula.setCellStyle(estiloTituloTabela);
	        r = new Region(linha, (short)coluna, linha, (short)colunaFinal);
	        planilha.addMergedRegion(r);
			
			 linha++;
			
			 row = planilha.createRow(linha);
			 celula = row.createCell(0);
			 celula.setCellValue("");
			 celula.setCellStyle(estiloTextoN);
			 r = new Region(linha, (short)0, linha, (short)2);
			 planilha.addMergedRegion(r);
			 
			 c.setTime(data);
			 coluna = 3;
			 
			 for(int j = 0 ; j < 12 ; j++)
			 {
				 celula = row.createCell(coluna);
				 celula.setCellValue("Saldo");
				 celula.setCellStyle(estiloTextoN);
				 r = new Region(linha, (short)coluna, linha, (short)++coluna);
				 planilha.addMergedRegion(r);
				 
				 coluna++;
			        
				 c.add(Calendar.MONTH, -1);
			 }
			 
			 celula = row.createCell(coluna);
			 celula.setCellValue("Saldo");
			 celula.setCellStyle(estiloTextoN);
			 r = new Region(linha, (short)coluna, linha, (short)++coluna);
			 planilha.addMergedRegion(r);
			 
			 linha++;
			 
			 for(Iterator<Entidade> j = contas.values().iterator() ; j.hasNext() ; )
			 {
				 Entidade e = j.next();
				 
				 double totalSaldo = 0;
				 
				 row = planilha.createRow(linha);
				 celula = row.createCell(0);
				 celula.setCellValue(e.obterApelido() + " " + e.obterNome());
				 celula.setCellStyle(estiloTextoE);
				 r = new Region(linha, (short)0, linha, (short)2);
				 planilha.addMergedRegion(r);
				 
				 c.setTime(data);
				 
				 coluna = 3;
				 
				 for(int k = 0 ; k < 12 ; k++)
				 {
					 double saldo = 0;
					 
					 String mesAno = new SimpleDateFormat("MM/yyyy").format(c.getTime());
					 
					 String mesStr = new SimpleDateFormat("MM").format(c.getTime());
					 String anoStr = new SimpleDateFormat("yyyy").format(c.getTime());
						
					 int mes = Integer.parseInt(mesStr);
					 int ano  = Integer.parseInt(anoStr);
					 
					 if(aseguradora.existeAgendaNoPeriodo(mes, ano, "Contabil"))
					 {
						 if(e instanceof ClassificacaoContas)
						 {
							 ClassificacaoContas cC = (ClassificacaoContas) e;
							 
							 saldo = cC.obterTotalizacaoExistente(aseguradora, mesAno);
						 }
						 else if(e instanceof Conta)
						 {
							 Conta cC = (Conta) e;
							 
							 saldo = cC.obterTotalizacaoExistente(aseguradora, mesAno);
						 }
					}
					 
					 totalSaldo += saldo;
					 
					 celula = row.createCell(coluna);
				     celula.setCellValue(formataValor.format(saldo));
				     celula.setCellStyle(estiloTextoD);
				     r = new Region(linha, (short)coluna, linha, (short)++coluna);
					 planilha.addMergedRegion(r);
				     
				     coluna++;
				        
				     c.add(Calendar.MONTH, -1);
				     
				     if(k == 0)
				    	 mesAtual.put(e.obterApelido(), saldo);
				 }
				 
				 celula = row.createCell(coluna);
			     celula.setCellValue(formataValor.format(totalSaldo));
			     celula.setCellStyle(estiloTextoN_D);
			     r = new Region(linha, (short)coluna, linha, (short)++coluna);
				 planilha.addMergedRegion(r); 
			    
				 total12Meses.put(e.obterApelido(), totalSaldo);
				 
				 linha++;
			 }
			 
			 linha++;
			 
			 row = planilha.createRow(linha);
			 celula = row.createCell(3);
			 celula.setCellValue("Valores para publicación");
			 celula.setCellStyle(estiloTextoN);
			 r = new Region(linha, (short)3, linha, (short)4);
			 planilha.addMergedRegion(r);
			 
			 celula = row.createCell(5);
			 celula.setCellValue("Valores calculados");
			 celula.setCellStyle(estiloTextoN);
			 r = new Region(linha, (short)5, linha, (short)6);
			 planilha.addMergedRegion(r);
			 
			 linha++;
				
			String calculo1 = "",calculo2 = "",calculo3 = "",calculo4 = "",calculo5 = "",calculo6 = "",calculo8 = "",calculo9 = "",calculo10 = "";
			String mesAno = new SimpleDateFormat("MM/yyyy").format(data);
			 
	        for(int j = 0 ; j < 10 ; j++)
	        {
	        	double valor = 0;
	        	String nomeIndicador = "";
	        	String legenda = "";
	        	
	        	if(j == 0)
	        	{
	        		nomeIndicador = "1) Siniestralidad Bruta (Siniestros Brutos/Primas Devengadas)";
	        		legenda = "Datos acumulados de los últimos 12 meses";
	        		
	        		double numerador = 0;
	        		double denominador = 0;
	        		
	        		numerador += total12Meses.get(cConta0506.obterApelido());
	        		numerador += total12Meses.get(cConta0507.obterApelido());
	        		numerador += total12Meses.get(cConta0508.obterApelido());
	        		numerador += total12Meses.get(cConta0509.obterApelido());
	        		numerador += total12Meses.get(cConta0511.obterApelido());
	        		numerador += total12Meses.get(cConta0513.obterApelido());
	        		numerador += total12Meses.get(cConta0515.obterApelido());
	        		numerador -= total12Meses.get(cConta0407.obterApelido());
	        		
	        		//total12Meses.get(cConta0507.obterApelido()) + total12Meses.get(cConta0508.obterApelido()) + total12Meses.get(cConta0509.obterApelido()) + total12Meses.get(cConta0511.obterApelido()) + total12Meses.get(cConta0513.obterApelido()) + total12Meses.get(cConta0515.obterApelido()) - total12Meses.get(cConta0407.obterApelido());
	        		
	        		
	        		denominador += total12Meses.get(cConta0401.obterApelido());
	        		denominador += total12Meses.get(cConta0402.obterApelido());
	        		denominador += total12Meses.get(cConta0403.obterApelido());
	        		
	        		if(denominador!= 0)
	        			valor = numerador/denominador;
	        		
	        		//System.out.println("numerador " + numerador);
					//System.out.println("denominador " + denominador);
					//System.out.println("valor " + numerador.divide(denominador));
	        		
	        		calculo1 = "1) Siniestralidad Bruta (Siniestros Brutos/Primas Devengadas)\n\n";
	        		calculo1+= cConta0506.obterApelido() + " + " + cConta0507.obterApelido() + " + " + cConta0508.obterApelido() + " + " + cConta0509.obterApelido() + " + " + cConta0511.obterApelido() + " + " + cConta0513.obterApelido() + " + " + cConta0515.obterApelido() + " - " + cConta0407.obterApelido();
	        		calculo1+="\n_______________________________________________________________________________________________";
	        		calculo1+="\n" + cConta0401.obterApelido() + " + " + cConta0402.obterApelido() + " + " + cConta0403.obterApelido();
	        	}
	        	else if(j == 1)
	        	{
	        		nomeIndicador = "2) Siniestralidad Neta (Siniestros Netos de Reaseguros /Primas Devengadas Netas de Reaseguro)";
	        		legenda = "Datos acumulados de los últimos 12 meses";
	        		
	        		double numerador = (total12Meses.get(cConta0506.obterApelido()) + total12Meses.get(cConta0507.obterApelido()) + total12Meses.get(cConta0508.obterApelido()) + total12Meses.get(cConta0509.obterApelido()) + total12Meses.get(cConta0511.obterApelido()) + total12Meses.get(cConta0513.obterApelido()) + total12Meses.get(cConta0515.obterApelido()))
	        				- (total12Meses.get(cConta0407.obterApelido()) + total12Meses.get(cConta0408.obterApelido()) + total12Meses.get(cConta0409.obterApelido()));
	        		
	        		double denominador = (total12Meses.get(cConta0401.obterApelido()) + total12Meses.get(cConta0402.obterApelido()) + total12Meses.get(cConta0403.obterApelido())) - (total12Meses.get(cConta0501.obterApelido()) + total12Meses.get(cConta0502.obterApelido()));
	        		
	        		if(denominador != 0)
	        			valor = numerador/denominador;
	        		
	        		calculo2 = "2) Siniestralidad Neta (Siniestros Netos de Reaseguros /Primas Devengadas Netas de Reaseguro)\n\n";
	        		calculo2+= "( "+cConta0506.obterApelido() + " + " + cConta0507.obterApelido() + " + " + cConta0508.obterApelido() + " + " + cConta0509.obterApelido() + " + " + cConta0511.obterApelido() + " + " + cConta0513.obterApelido() + " + " + cConta0515.obterApelido() + " )";
	        		calculo2+=" - ( " +cConta0407.obterApelido() + " + " + cConta0408.obterApelido() + " + " + cConta0409.obterApelido()+" )";
	        		calculo2+="\n_______________________________________________________________________________________________";
	        		calculo2+="\n(" + cConta0401.obterApelido() + " + " + cConta0402.obterApelido() + " + " + cConta0403.obterApelido() + ") - (" + cConta0501.obterApelido() + " + " + cConta0502.obterApelido() + ")";
	        	}
	        	else if(j == 2)
	        	{
	        		nomeIndicador = "3) Gasto Operativo (Gastos Operativos /Primas Devengadas)";
	        		legenda = "Datos acumulados de los últimos 12 meses";
	        		
	        		double numerador = total12Meses.get(cConta0504.obterApelido()) + total12Meses.get(cConta0525.obterApelido());
	        		
	        		double denominador = total12Meses.get(cConta0401.obterApelido()) + total12Meses.get(cConta0402.obterApelido())  + total12Meses.get(cConta0403.obterApelido());
	        		
	        		if(denominador != 0)
	        			valor = numerador/denominador;
	        		
	        		calculo3 = "3) Gasto Operativo (Gastos Operativos /Primas Devengadas)\n\n";
	        		calculo3+= cConta0504.obterApelido() + " + " + cConta0525.obterApelido();
	        		calculo3+="\n_______________________________________________________________________________________________";
	        		calculo3+="\n" + cConta0401.obterApelido() + " + " + cConta0402.obterApelido() + " + " + cConta0403.obterApelido();
	        	}
	        	else if(j == 3)
	        	{
	        		nomeIndicador = "4) Gasto de Producción (Gastos de Producción /Primas de Seguros Devengadas)";
	        		legenda = "Datos acumulados de los últimos 12 meses";
	        		
	        		double numerador = total12Meses.get(cConta0504.obterApelido());
	        		
	        		double denominador = total12Meses.get(cConta0401.obterApelido());
	        		
	        		if(denominador != 0)
	        			valor = numerador/denominador;
	        		
	        		calculo4 = "4) Gasto de Producción (Gastos de Producción /Primas de Seguros Devengadas)\n\n";
	        		calculo4+= cConta0504.obterApelido();
	        		calculo4+="\n_______________________________________________________________________________________________";
	        		calculo4+="\n" + cConta0401.obterApelido();
	        	}
	        	else if(j == 4)
	        	{
	        		nomeIndicador = "5) Gasto de Explotación (Gastos de Explotación /Primas Devengadas)";
	        		legenda = "Datos acumulados de los últimos 12 meses";
	        		
	        		double numerador = total12Meses.get(cConta0525.obterApelido());
	        		
	        		double denominador = total12Meses.get(cConta0401.obterApelido()) + total12Meses.get(cConta0402.obterApelido())  + total12Meses.get(cConta0403.obterApelido());
	        		
	        		if(denominador != 0)
	        			valor = numerador/denominador;
	        		
	        		calculo5 = "5) Gasto de Explotación (Gastos de Explotación /Primas Devengadas)\n\n";
	        		calculo5+= cConta0525.obterApelido();
	        		calculo5+="\n_______________________________________________________________________________________________";
	        		calculo5+="\n" + cConta0401.obterApelido() + " + " + cConta0402.obterApelido() + " + " + cConta0403.obterApelido();
	        	}
	        	else if(j == 5)
	        	{
	        		nomeIndicador = "6) Representatividad de las Inversiones (Inversiones /Provisiones Técnicas (netas))";
	        		legenda = "Datos " + mesAno;
	        		
	        		double numerador = mesAtual.get(cConta0107.obterApelido());
	        		
	        		double denominador1 = mesAtual.get(cConta0212.obterApelido()) + mesAtual.get(cConta0213.obterApelido());
	        		double denominador2 = mesAtual.get(cConta010903.obterApelido()) + mesAtual.get(cConta010904.obterApelido()) + mesAtual.get(cConta010905.obterApelido()) + mesAtual.get(cConta010906.obterApelido());
	        		
	        		double denominador = denominador1 - denominador2;
	        		
	        		if(denominador != 0)
	        			valor = numerador/denominador;
	        		
	        		calculo6 = "6) Representatividad de las Inversiones (Inversiones /Provisiones Técnicas (netas))\n\n";
	        		calculo6+= cConta0107.obterApelido();
	        		calculo6+="\n_______________________________________________________________________________________________";
	        		calculo6+="\n( "+cConta0212.obterApelido() + " + " + cConta0213.obterApelido()+" ) - ( " + cConta010903.obterApelido() + " + " + cConta010904.obterApelido() + " + " + cConta010905.obterApelido() + " + " + cConta010906.obterApelido() + " )";
	        	}
	        	else if(j == 6)
	        	{
	        		nomeIndicador = "7) Representación del Activo (Patrimonio Neto/Activo)";
	        		legenda = "Datos " + mesAno;
	        		calculo7 = "7) Representación del Activo (Patrimonio Neto/Activo)\n\n";
	        		
	        		double numerador = this.obterNumeradorBG(aseguradora, mesAno);
	        		double denominador = mesAtual.get(cConta0100.obterApelido());
	        		
	        		if(denominador != 0)
	        			valor = numerador/denominador;
	        		
	        		calculo7+="\n_______________________________________________________________________________________________";
	        		calculo7+="\n" + cConta0100.obterApelido();
	        	}
	        	else if(j == 7)
	        	{
	        		nomeIndicador = "8) Índice General de Rendimiento Patrimonial (Retorno/Patrimonio Neto)";
	        		legenda = "Datos " + mesAno;
	        		
	        		double numerador = mesAtual.get(cConta0400.obterApelido()) - mesAtual.get(cConta0500.obterApelido());
	        		double denominador = mesAtual.get(cConta0300.obterApelido());
	        		
	        		if(denominador != 0)
	        			valor = numerador/denominador;
	        		
	        		calculo8 = "8) Índice General de Rendimiento Patrimonial (Retorno/Patrimonio Neto)\n\n";
	        		calculo8+= cConta0400.obterApelido() + " - " + cConta0500.obterApelido();
	        		calculo8+="\n_______________________________________________________________________________________________";
	        		calculo8+="\n" + cConta0300.obterApelido();
	        	}
	        	else if(j == 8)
	        	{
	        		nomeIndicador = "9) Índice Técnico de Rendimiento Patrimonial (Resultado Técnico/Patrimonio Neto)";
	        		legenda = "Datos " + mesAno;
	        		
	        		// Calculo Primas Netas
					double c0401 =  mesAtual.get(cConta0401.obterApelido());
					double c0402 =  mesAtual.get(cConta0402.obterApelido());
					double c0403 =  mesAtual.get(cConta0403.obterApelido());
					double c0404 =  mesAtual.get(cConta0404.obterApelido());
					
					double c0501 =  mesAtual.get(cConta0501.obterApelido());
					double c0502 =  mesAtual.get(cConta0502.obterApelido());
					double c0503 =  mesAtual.get(cConta0503.obterApelido());
					
					double primasNetas = (c0401 + c0402 + c0403 + c0404) - (c0501 + c0502 + c0503);
					
					// Calculo Sinistro Neto 
					double c0506 = mesAtual.get(cConta0506.obterApelido());
					double c0507 = mesAtual.get(cConta0507.obterApelido());
					double c0508 = mesAtual.get(cConta0508.obterApelido());
					double c0509 = mesAtual.get(cConta0509.obterApelido());
					double c0511 = mesAtual.get(cConta0511.obterApelido());
					double c0513 = mesAtual.get(cConta0513.obterApelido());
					double c0515 = mesAtual.get(cConta0515.obterApelido());
					double c0505 = mesAtual.get(cConta0505.obterApelido());
					
					double c0407 = mesAtual.get(cConta0407.obterApelido());
					double c0408 = mesAtual.get(cConta0408.obterApelido());
					double c0409 = mesAtual.get(cConta0409.obterApelido());
					double c0412 = mesAtual.get(cConta0412.obterApelido());
					double c0414 = mesAtual.get(cConta0414.obterApelido());
					double c0406 = mesAtual.get(cConta0406.obterApelido());
					
					double sinistroNeto = (c0506 + c0507 + c0508 + c0509 + c0511 + c0513 + c0515 + c0505) - (c0407 + c0408 + c0409 + c0412 + c0414 + c0406);
					
					double totalUtilidade = primasNetas - sinistroNeto;
					
					// Calculo Ingresso Técnico
					double c0405 = mesAtual.get(cConta0405.obterApelido());
					double c0410 = mesAtual.get(cConta0410.obterApelido());
					double c0411 = mesAtual.get(cConta0411.obterApelido());
					double c0413 = mesAtual.get(cConta0413.obterApelido());
					double c0415 = mesAtual.get(cConta0415.obterApelido());
					double c0426 = mesAtual.get(cConta0426.obterApelido());
					
					double totalIngressoTecnico = c0405 + c0410 + c0411 + c0413 + c0415 + c0426;
					
					// Calculo Egresso Técnico
					double c0504 = mesAtual.get(cConta0504.obterApelido());
					double c0510 = mesAtual.get(cConta0510.obterApelido());
					double c0512 = mesAtual.get(cConta0512.obterApelido());
					double c0514 = mesAtual.get(cConta0514.obterApelido());
					double c0516 = mesAtual.get(cConta0516.obterApelido());
					double c0525 = mesAtual.get(cConta0525.obterApelido());
					double c0525010401 = mesAtual.get(cConta0525010401.obterApelido());
					double c0527 = mesAtual.get(cConta0527.obterApelido());
					
					double totalEgressoTecnico = c0504 + c0510 + c0512 + c0514 + c0516 + c0525 - c0525010401 + c0527;
					
					double totalUtilidadeNeta = (totalUtilidade + totalIngressoTecnico) - totalEgressoTecnico;
					
					double numerador = totalUtilidadeNeta;
					
					double denominador = mesAtual.get(cConta0300.obterApelido());
					
					if(denominador != 0)
	        			valor = numerador/denominador;
					
					calculo9 = "9) Índice Técnico de Rendimiento Patrimonial (Resultado Técnico/Patrimonio Neto)\n\n";
					calculo9 += "v1 = (" + cConta0401.obterApelido() + " + " + cConta0402.obterApelido() + " + " + cConta0403.obterApelido()+ " + " + cConta0404.obterApelido()+ ") - (" + cConta0501.obterApelido()+ " + " + cConta0502.obterApelido()+ " + " + cConta0503.obterApelido() + ")\n";
					calculo9 += "v2 = (" + cConta0506.obterApelido() + " + " + cConta0507.obterApelido() + " + " + cConta0508.obterApelido()+ " + " + cConta0509.obterApelido()+ " + " + cConta0511.obterApelido()+ " + " + cConta0513.obterApelido()+ " + " + cConta0515.obterApelido() + " + " + cConta0505.obterApelido() + ")";
					calculo9 += " - (" + cConta0407.obterApelido() + " + " + cConta0408.obterApelido() + " + " + cConta0409.obterApelido()+ " + " + cConta0412.obterApelido()+ " + " + cConta0414.obterApelido()+ " + " + cConta0406.obterApelido()+ ")\n";
					calculo9+="v3 = v1 - v2\n";
					
					calculo9 += "v4 = "+ cConta0405.obterApelido() + " + " + cConta0410.obterApelido() + " + " + cConta0411.obterApelido()+ " + " + cConta0413.obterApelido()+ " + " + cConta0415.obterApelido()+ " + " + cConta0426.obterApelido()+"\n";
					calculo9 += "v5 = "+ cConta0504.obterApelido() + " + " + cConta0510.obterApelido() + " + " + cConta0512.obterApelido()+ " + " + cConta0514.obterApelido()+ " + " + cConta0516.obterApelido()+ " + " + cConta0525.obterApelido() + " + " + cConta0525010401.obterApelido() + " + " + cConta0527.obterApelido()+"\n";
					calculo9+="v6 = (v3 + v4) - v5\n\n";
					
					calculo9+="v6";
	        		calculo9+="\n_______________________________________________________________________________________________";
	        		calculo9+="\n" + cConta0300.obterApelido();
	        	}
	        	else if(j == 9)
	        	{
	        		nomeIndicador = "10) Rendimiento sobre Volumen de Operación Técnica (Margen de Ganancia/Primas Devengadas)";
	        		legenda = "Datos " + mesAno;
	        		
	        		double numerador = mesAtual.get(cConta0400.obterApelido()) - mesAtual.get(cConta0500.obterApelido());
	        		
	        		double denominador = mesAtual.get(cConta0401.obterApelido()) + mesAtual.get(cConta0402.obterApelido()) + mesAtual.get(cConta0403.obterApelido());
	        		
	        		if(denominador != 0)
	        			valor = numerador/denominador;
	        		
	        		calculo10 = "10) Rendimiento sobre Volumen de Operación Técnica (Margen de Ganancia/Primas Devengadas)\n\n";
	        		calculo10+= cConta0400.obterApelido() + " - " + cConta0500.obterApelido();
	        		calculo10+="\n_______________________________________________________________________________________________";
	        		calculo10+="\n" + cConta0401.obterApelido() + " + " + cConta0402.obterApelido() + " + " + cConta0403.obterApelido();
	        	}
	        	
	        	row = planilha.createRow(linha);
	        	celula = row.createCell(0);
				celula.setCellValue(nomeIndicador);
				celula.setCellStyle(estiloTextoN_E);
				r = new Region(linha, (short)0, linha, (short)2);
				planilha.addMergedRegion(r);
				
				celula = row.createCell(3);
				if(valor>0)
					celula.setCellValue(new Double((valor * 100) + 0.5).intValue());
				else
					celula.setCellValue(new Double((valor * 100) - 0.5).intValue());
				celula.setCellStyle(estiloTextoN_D);
				r = new Region(linha, (short)3, linha, (short)4);
				planilha.addMergedRegion(r);
				
				celula = row.createCell(5);
				celula.setCellValue(formataValor6Casas.format(valor * 100));
				celula.setCellStyle(estiloTextoN_D);
				r = new Region(linha, (short)5, linha, (short)6);
				planilha.addMergedRegion(r);
				
				celula = row.createCell(7);
				celula.setCellValue(legenda);
				celula.setCellStyle(estiloTextoN_E);
				r = new Region(linha, (short)7, linha, (short)13);
				planilha.addMergedRegion(r);
				
				linha++;
	        }
	        
	        linha++;
	        
	        int linhaFinal = linha + 4;
	        
	        row = planilha.createRow(linha);
        	celula = row.createCell(0);
			celula.setCellValue(calculo1);
			celula.setCellStyle(estiloTextoE);
			r = new Region(linha, (short)0, linhaFinal, (short)10);
			planilha.addMergedRegion(r);
			
			linha = linhaFinal+2;
			linhaFinal = linha + 4;
			
			row = planilha.createRow(linha);
			celula = row.createCell(0);
			celula.setCellValue(calculo2);
			celula.setCellStyle(estiloTextoE);
			r = new Region(linha, (short)0, linhaFinal, (short)10);
			planilha.addMergedRegion(r);
			
			linha = linhaFinal+2;
			linhaFinal = linha + 4;
			
			row = planilha.createRow(linha);
			celula = row.createCell(0);
			celula.setCellValue(calculo3);
			celula.setCellStyle(estiloTextoE);
			r = new Region(linha, (short)0, linhaFinal, (short)10);
			planilha.addMergedRegion(r);
			
			linha = linhaFinal+2;
			linhaFinal = linha + 4;
			
			row = planilha.createRow(linha);
			celula = row.createCell(0);
			celula.setCellValue(calculo4);
			celula.setCellStyle(estiloTextoE);
			r = new Region(linha, (short)0, linhaFinal, (short)10);
			planilha.addMergedRegion(r);
			
			linha = linhaFinal+2;
			linhaFinal = linha + 4;
			
			row = planilha.createRow(linha);
			celula = row.createCell(0);
			celula.setCellValue(calculo5);
			celula.setCellStyle(estiloTextoE);
			r = new Region(linha, (short)0, linhaFinal, (short)10);
			planilha.addMergedRegion(r);
			
			linha = linhaFinal+2;
			linhaFinal = linha + 4;
			
			row = planilha.createRow(linha);
			celula = row.createCell(0);
			celula.setCellValue(calculo6);
			celula.setCellStyle(estiloTextoE);
			r = new Region(linha, (short)0, linhaFinal, (short)10);
			planilha.addMergedRegion(r);
			
			linha = linhaFinal+2;
			linhaFinal = linha + 4;
			
			row = planilha.createRow(linha);
			celula = row.createCell(0);
			celula.setCellValue(calculo7);
			celula.setCellStyle(estiloTextoE);
			r = new Region(linha, (short)0, linhaFinal, (short)33);
			planilha.addMergedRegion(r);
			
			linha = linhaFinal+2;
			linhaFinal = linha + 4;
			
			row = planilha.createRow(linha);
			celula = row.createCell(0);
			celula.setCellValue(calculo8);
			celula.setCellStyle(estiloTextoE);
			r = new Region(linha, (short)0, linhaFinal, (short)33);
			planilha.addMergedRegion(r);
			
			linha = linhaFinal+2;
			linhaFinal = linha + 10;
			
			row = planilha.createRow(linha);
			celula = row.createCell(0);
			celula.setCellValue(calculo9);
			celula.setCellStyle(estiloTextoE);
			r = new Region(linha, (short)0, linhaFinal, (short)20);
			planilha.addMergedRegion(r);
			
			linha = linhaFinal+2;
			linhaFinal = linha + 4;
			
			row = planilha.createRow(linha);
			celula = row.createCell(0);
			celula.setCellValue(calculo10);
			celula.setCellStyle(estiloTextoE);
			r = new Region(linha, (short)0, linhaFinal, (short)10);
			planilha.addMergedRegion(r);
       // }
        }
        
        wb.write(stream);

        stream.flush();

        stream.close();
	}
	
	private double obterNumeradorBG(Entidade aseguradora, String mesAno)throws Exception
	{
		double totalPatrimonio = 0;
		double totalPassivo = 0;
		double totalAtivo = 0;
		double totalExercicio = 0;
		double totalPatrimonioNeto = 0;
		
		//Calculo Total Patrimonio
		double c0301 = mesAtual.get(cConta0301.obterApelido());
		double c0302 = mesAtual.get(cConta0302.obterApelido());
		double c0303 = mesAtual.get(cConta0303.obterApelido());
		double c0304 = mesAtual.get(cConta0304.obterApelido());
		
		totalPatrimonio = c0301 + c0302 + c0303 + c0304;
		
		//Calculo Total Passivo
		double c0201 = mesAtual.get(cConta0201.obterApelido());
		double c0202 = mesAtual.get(cConta0202.obterApelido());
		double c0203 = mesAtual.get(cConta0203.obterApelido());
		double c0204 = mesAtual.get(cConta0204.obterApelido());
		double c0205 = mesAtual.get(cConta0205.obterApelido());
		double c0206 = mesAtual.get(cConta0206.obterApelido());
		double c0210 = mesAtual.get(cConta0210.obterApelido());
		double c0211 = mesAtual.get(cConta0211.obterApelido());
		double c0212 = mesAtual.get(cConta0212.obterApelido());
		double c0213 = mesAtual.get(cConta0213.obterApelido());
		double c0214 = mesAtual.get(cConta0214.obterApelido());
		
		totalPassivo = c0201 + c0202 + c0203 + c0204 + c0205 + c0206 + c0210 + c0211 + c0212 + c0213 + c0214;
		
		//Calculo Total Ativo
		double c0101 = mesAtual.get(cConta0101.obterApelido());
		double c0102 = mesAtual.get(cConta0102.obterApelido());
		double c0103 = mesAtual.get(cConta0103.obterApelido());
		double c0104 = mesAtual.get(cConta0104.obterApelido());
		double c0105 = mesAtual.get(cConta0105.obterApelido());
		double c0106 = mesAtual.get(cConta0106.obterApelido());
		double c0107 = mesAtual.get(cConta0107.obterApelido());
		double c0108 = mesAtual.get(cConta0108.obterApelido());
		double c0109 = mesAtual.get(cConta0109.obterApelido());
		
		totalAtivo = c0101 + c0102 + c0103 + c0104 + c0105 + c0106 + c0107 + c0108 + c0109;
		
		totalExercicio = totalAtivo - (totalPassivo + totalPatrimonio);
		
		totalPatrimonioNeto = totalPatrimonio + totalExercicio;
		
		calculo7+="(" + cConta0301.obterApelido() + " + " + cConta0302.obterApelido()+ " + " + cConta0303.obterApelido()+ " + " + cConta0304.obterApelido() + " + ";
		calculo7+=cConta0101.obterApelido() + " + " + cConta0102.obterApelido()+ " + " + cConta0103.obterApelido()+ " + " + cConta0104.obterApelido() +  " + " + cConta0105.obterApelido() + " +  " + cConta0106.obterApelido() + " +  " + cConta0107.obterApelido() + " +  " + cConta0108.obterApelido() + " +  " + cConta0109.obterApelido()+ ")";
		calculo7+=" - (" + cConta0201.obterApelido() + " + " + cConta0202.obterApelido()+ " + " + cConta0203.obterApelido()+ " + " + cConta0204.obterApelido() +  " + " + cConta0205.obterApelido() + " +  " + cConta0206.obterApelido() + " +  " + cConta0210.obterApelido() + " +  " + cConta0211.obterApelido() + " +  " + cConta0212.obterApelido() + " +  " + cConta0213.obterApelido()+ " +  " + cConta0214.obterApelido();
		calculo7+=" + " + cConta0301.obterApelido() + " + " + cConta0302.obterApelido()+ " + " + cConta0303.obterApelido()+ " + " + cConta0304.obterApelido() + ")";
		
		return totalPatrimonioNeto;
	}
}
