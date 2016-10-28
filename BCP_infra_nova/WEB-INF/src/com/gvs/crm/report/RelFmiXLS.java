/*     */ package com.gvs.crm.report;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.text.DecimalFormat;
/*     */ import java.text.SimpleDateFormat;
/*     */ import java.util.Calendar;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;

/*     */ import org.apache.poi.hssf.usermodel.HSSFCell;
/*     */ import org.apache.poi.hssf.usermodel.HSSFCellStyle;
/*     */ import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
/*     */ import org.apache.poi.hssf.usermodel.HSSFFont;
/*     */ import org.apache.poi.hssf.usermodel.HSSFRow;
/*     */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*     */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*     */ import org.apache.poi.hssf.util.Region;
/*     */ import org.apache.poi.util.IOUtils;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.ClassificacaoContas;
/*     */ import com.gvs.crm.model.Conta;
/*     */ import com.gvs.crm.model.Entidade;
/*     */ import com.gvs.crm.model.EntidadeHome;

/*     */ 
/*     */ /*     */ import infra.config.InfraProperties;
/*     */ 
/*     */ 
/*     */ 
/*     */ public class RelFmiXLS
/*     */   extends Excel
/*     */ {
/*     */   public RelFmiXLS(Date dataInicio, Date dataFim, Collection<Aseguradora> aseguradoras, EntidadeHome entidadeHome) throws Exception
/*     */   {
/*  38 */     String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
/*     */     
/*  40 */     DecimalFormat formataValor = new DecimalFormat("#,##0.00");
/*     */     
/*  42 */     FileOutputStream stream = new FileOutputStream(caminho);
/*     */     
/*  44 */     setCaminho(caminho);
/*     */     
/*  46 */     Map<String, String> planilhas = new TreeMap();
/*  47 */     planilhas.put("Prima Ganada Neta", "4.01.00.00.00");
/*  48 */     planilhas.put("Siniestros", "5.06.00.00.00");
/*  49 */     planilhas.put("Provisiones de Sinestros", "2.13.00.00.00");
/*  50 */     planilhas.put("Prov. Sin. Controvertidos", "2.13.03.00.00");
/*  51 */     planilhas.put("Ingreso Bruto", "4.00.00.00.00");
/*  52 */     planilhas.put("Egreso Bruto", "5.00.00.00.00");
			  planilhas.put("Activo", "1.00.00.00.00");
			  planilhas.put("Pasivo", "2.00.00.00.00");
			  planilhas.put("Provisiones Técnicas", "2.12.00.00.00");
			  planilhas.put("Créditos Técnicos Vigentes", "1.02.00.00.00");
			  planilhas.put("Créditos Técnicos Vencidos", "1.03.00.00.00");
			  planilhas.put("Créditos Administrativos", "1.04.00.00.00");
/*     */     
/*     */ 
/*  55 */     ClassificacaoContas cContas0501000000 = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido("0501000000");
/*  56 */     ClassificacaoContas cContas0502000000 = (ClassificacaoContas)entidadeHome.obterEntidadePorApelido("0502000000");
/*     */     
/*  58 */     HSSFWorkbook wb = new HSSFWorkbook();
/*     */     
/*  60 */     HSSFFont fonteTitulo = wb.createFont();
/*  61 */     fonteTitulo.setFontHeightInPoints((short)10);
/*  62 */     fonteTitulo.setFontName("Arial");
/*  63 */     fonteTitulo.setBoldweight((short)700);
/*     */     
/*  65 */     HSSFFont fonteTexto = wb.createFont();
/*  66 */     fonteTexto.setFontHeightInPoints((short)9);
/*  67 */     fonteTexto.setFontName("Arial");
/*     */     
/*  69 */     HSSFFont fonteTituloTabela = wb.createFont();
/*  70 */     fonteTituloTabela.setFontHeightInPoints((short)10);
/*  71 */     fonteTituloTabela.setFontName("Arial");
/*  72 */     fonteTituloTabela.setColor((short)9);
/*     */     
/*  74 */     HSSFCellStyle estiloTitulo = wb.createCellStyle();
/*  75 */     estiloTitulo.setAlignment((short)2);
/*  76 */     estiloTitulo.setFont(fonteTitulo);
/*     */     
/*  78 */     HSSFCellStyle estiloTexto = wb.createCellStyle();
/*  79 */     estiloTexto.setAlignment((short)2);
/*  80 */     estiloTexto.setFont(fonteTexto);
/*     */     
/*  82 */     HSSFCellStyle estiloTextoD = wb.createCellStyle();
/*  83 */     estiloTextoD.setAlignment((short)3);
/*  84 */     estiloTextoD.setFont(fonteTexto);
/*     */     
/*  86 */     HSSFCellStyle estiloTextoE = wb.createCellStyle();
/*  87 */     estiloTextoE.setAlignment((short)5);
/*  88 */     estiloTextoE.setFont(fonteTexto);
/*     */     
/*  90 */     HSSFCellStyle estiloTituloTabelaC = wb.createCellStyle();
/*  91 */     estiloTituloTabelaC.setAlignment((short)2);
/*  92 */     estiloTituloTabelaC.setFont(fonteTituloTabela);
/*  93 */     estiloTituloTabelaC.setFillForegroundColor((short)63);
/*  94 */     estiloTituloTabelaC.setFillPattern((short)1);
/*     */     
/*  96 */     String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
/*  97 */     InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
/*  98 */     byte[] bytes = IOUtils.toByteArray(is);
/*  99 */     is.close();

/* 101 */     for (Iterator<String> i = planilhas.keySet().iterator(); i.hasNext();)
/*     */     {
/* 103 */       String key = (String)i.next();
/* 104 */       String conta = (String)planilhas.get(key);
/* 105 */       String contaSemPonto = "0" + conta.replace(".", "");
/*     */       
/* 107 */       Entidade entidade = entidadeHome.obterEntidadePorApelido(contaSemPonto);
/*     */       
/* 109 */       HSSFSheet planilha = wb.createSheet(key);
/*     */       
/* 111 */       int pictureIdx = wb.addPicture(bytes, 5);
/*     */       
/* 113 */       HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0, 0, 0, 0, (short)1, 0, (short)4, 6);
/* 114 */       anchoVivaBem.setAnchorType(3);
/* 115 */       planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
/*     */       
/* 117 */       HSSFRow row = planilha.createRow(1);
/* 118 */       HSSFCell celula = row.createCell(5);
/*     */       
/* 120 */       celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
/* 121 */       celula.setCellStyle(estiloTitulo);
/* 122 */       Region r = new Region(1, (short)5, 1, (short)11);
/* 123 */       planilha.addMergedRegion(r);
/*     */       
/* 125 */       row = planilha.createRow(2);
/* 126 */       celula = row.createCell(5);
/* 127 */       celula.setCellValue("Información requerida FMI");
/* 128 */       celula.setCellStyle(estiloTitulo);
/* 129 */       r = new Region(2, (short)5, 2, (short)11);
/* 130 */       planilha.addMergedRegion(r);
/*     */       
/* 132 */       row = planilha.createRow(3);
/* 133 */       celula = row.createCell(5);
/* 134 */       celula.setCellValue("Información requerida: " + key);
/* 135 */       celula.setCellStyle(estiloTitulo);
/* 136 */       r = new Region(3, (short)5, 3, (short)11);
/* 137 */       planilha.addMergedRegion(r);
/*     */       
/* 139 */       row = planilha.createRow(4);
/* 140 */       celula = row.createCell(5);
/* 141 */       celula.setCellValue("Cuenta contable N°.: " + conta);
/* 142 */       celula.setCellStyle(estiloTitulo);
/* 143 */       r = new Region(4, (short)5, 4, (short)11);
/* 144 */       planilha.addMergedRegion(r);
/*     */       
/* 146 */       int linha = 8;
/*     */       
/* 148 */       row = planilha.createRow(linha);
/* 149 */       celula = row.createCell(0);
/* 150 */       celula.setCellValue("Aseguradoras");
/* 151 */       celula.setCellStyle(estiloTituloTabelaC);
/* 152 */       r = new Region(linha, (short)0, linha, (short)2);
/* 153 */       planilha.addMergedRegion(r);
/*     */       
/* 155 */       String dataInicioStr = new SimpleDateFormat("MM/yyyy").format(dataInicio);
/* 156 */       String dataFimStr = new SimpleDateFormat("MM/yyyy").format(dataFim);
/*     */       
/* 158 */       Date mesInicio = new SimpleDateFormat("MM/yyyy").parse(dataInicioStr);
/* 159 */       Date mesFim = new SimpleDateFormat("MM/yyyy").parse(dataFimStr);
/*     */       
/* 161 */       Calendar c = Calendar.getInstance();
/* 162 */       c.setTime(mesInicio);
/*     */       
/* 164 */       int coluna = 3;
/* 166 */       while (c.getTime().compareTo(mesFim) <= 0)
/*     */       {
/* 168 */         String mesAtual = new SimpleDateFormat("MM/yyyy").format(c.getTime());
/*     */         
/* 170 */         celula = row.createCell(coluna);
/* 171 */         celula.setCellValue(mesAtual);
/* 172 */         celula.setCellStyle(estiloTituloTabelaC);
/* 173 */         r = new Region(linha, (short)coluna, linha, (short)++coluna);
/* 174 */         planilha.addMergedRegion(r);
/*     */         
/* 176 */         c.add(2, 1);
/* 177 */         coluna++;
/*     */       }
/* 180 */       linha++;
/*     */       
/* 182 */       for(Aseguradora aseguradora : aseguradoras) 
/*     */       {
	/* 186 */       row = planilha.createRow(linha);
	/* 187 */       celula = row.createCell(0);
	/* 188 */       celula.setCellValue(aseguradora.obterNome());
	/* 189 */       celula.setCellStyle(estiloTextoE);
	/* 190 */       r = new Region(linha, (short)0, linha, (short)2);
	/* 191 */       planilha.addMergedRegion(r);
	/*     */       
	/* 193 */       c.setTime(mesInicio);
	/* 194 */       coluna = 3;
	/* 196 */       while (c.getTime().compareTo(mesFim) <= 0)
	/*     */       {
	/* 198 */         String mesAtual = new SimpleDateFormat("MM/yyyy").format(c.getTime());
	/*     */         
	/* 200 */         double valor = 0.0D;
	/* 201 */         if ((entidade instanceof ClassificacaoContas))
	/*     */         {
	/* 203 */           ClassificacaoContas c2 = (ClassificacaoContas)entidade;
	/* 204 */           valor = c2.obterTotalizacaoExistente(aseguradora, mesAtual);
	/* 206 */           if (key.equals("Prima Ganada Neta"))
	/*     */           {
	/* 208 */             double valor0501 = cContas0501000000.obterTotalizacaoExistente(aseguradora, mesAtual);
	/* 209 */             double valor0502 = cContas0502000000.obterTotalizacaoExistente(aseguradora, mesAtual);
	/*     */             
	/* 211 */             valor -= valor0501;
	/* 212 */             valor -= valor0502;
	/*     */           }
	/*     */         }
	/* 215 */         else if ((entidade instanceof Conta))
	/*     */         {
	/* 217 */           Conta c2 = (Conta)entidade;
	/* 218 */           valor = c2.obterTotalizacaoExistente(aseguradora, mesAtual);
	/*     */         }
	/* 221 */         celula = row.createCell(coluna);
	/* 222 */         celula.setCellValue(formataValor.format(valor));
	/* 223 */         celula.setCellStyle(estiloTextoD);
	/* 224 */         r = new Region(linha, (short)coluna, linha, (short)++coluna);
	/* 225 */         planilha.addMergedRegion(r);
	/*     */         
	/* 227 */         c.add(2, 1);
	/* 228 */         coluna++;
	/*     */       }
					linha++;
				}
/* 231 */       
/*     */     }
/* 235 */     wb.write(stream);
/*     */     
/* 237 */     stream.flush();
/*     */     
/* 239 */     stream.close();
/*     */   }
/*     */ }


/* Location:           C:\Users\Giovanni\Desktop\CRM-BCP-11-03-2015.jar
 * Qualified Name:     com.gvs.crm.report.RelFmiXLS
 * JD-Core Version:    0.7.0.1
 */