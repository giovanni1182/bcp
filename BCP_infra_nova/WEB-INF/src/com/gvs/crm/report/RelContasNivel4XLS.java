/*     */ package com.gvs.crm.report;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Iterator;

/*     */ import org.apache.poi.hssf.usermodel.HSSFCell;
/*     */ import org.apache.poi.hssf.usermodel.HSSFCellStyle;
/*     */ import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
/*     */ import org.apache.poi.hssf.usermodel.HSSFFont;
/*     */ import org.apache.poi.hssf.usermodel.HSSFRow;
/*     */ import org.apache.poi.hssf.usermodel.HSSFSheet;
/*     */ import org.apache.poi.hssf.usermodel.HSSFWorkbook;
/*     */ import org.apache.poi.hssf.util.Region;
/*     */ import org.apache.poi.util.IOUtils;

import com.gvs.crm.model.ClassificacaoContas;

/*     */ 
/*     */ /*     */ import infra.config.InfraProperties;
/*     */ 
/*     */ 
/*     */ public class RelContasNivel4XLS
/*     */   extends Excel
/*     */ {
/*     */   public RelContasNivel4XLS(Collection<ClassificacaoContas> contas)
/*     */     throws Exception
/*     */   {
/*  31 */     String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
/*     */     
/*  33 */     FileOutputStream stream = new FileOutputStream(caminho);
/*     */     
/*  35 */     setCaminho(caminho);
/*     */     
/*  37 */     HSSFWorkbook wb = new HSSFWorkbook();
/*     */     
/*  39 */     HSSFFont fonteTitulo = wb.createFont();
/*  40 */     fonteTitulo.setFontHeightInPoints((short)10);
/*  41 */     fonteTitulo.setFontName("Arial");
/*  42 */     fonteTitulo.setBoldweight((short)700);
/*     */     
/*  44 */     HSSFFont fonteTexto = wb.createFont();
/*  45 */     fonteTexto.setFontHeightInPoints((short)9);
/*  46 */     fonteTexto.setFontName("Arial");
/*     */     
/*  48 */     HSSFFont fonteTituloTabela = wb.createFont();
/*  49 */     fonteTituloTabela.setFontHeightInPoints((short)10);
/*  50 */     fonteTituloTabela.setFontName("Arial");
/*  51 */     fonteTituloTabela.setColor((short)9);
/*     */     
/*  53 */     HSSFCellStyle estiloTitulo = wb.createCellStyle();
/*  54 */     estiloTitulo.setAlignment((short)2);
/*  55 */     estiloTitulo.setFont(fonteTitulo);
/*     */     
/*  57 */     HSSFCellStyle estiloTexto = wb.createCellStyle();
/*  58 */     estiloTexto.setAlignment((short)2);
/*  59 */     estiloTexto.setFont(fonteTexto);
/*     */     
/*  61 */     HSSFCellStyle estiloTextoD = wb.createCellStyle();
/*  62 */     estiloTextoD.setAlignment((short)3);
/*  63 */     estiloTextoD.setFont(fonteTexto);
/*     */     
/*  65 */     HSSFCellStyle estiloTextoE = wb.createCellStyle();
/*  66 */     estiloTextoE.setAlignment((short)5);
/*  67 */     estiloTextoE.setFont(fonteTexto);
/*     */     
/*  69 */     HSSFCellStyle estiloTituloTabelaC = wb.createCellStyle();
/*  70 */     estiloTituloTabelaC.setAlignment((short)2);
/*  71 */     estiloTituloTabelaC.setFont(fonteTituloTabela);
/*  72 */     estiloTituloTabelaC.setFillForegroundColor((short)63);
/*  73 */     estiloTituloTabelaC.setFillPattern((short)1);
/*     */     
/*  75 */     HSSFSheet planilha = wb.createSheet("Planilla");
/*     */     
/*  77 */     String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
/*  78 */     InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
/*  79 */     byte[] bytes = IOUtils.toByteArray(is);
/*     */     
/*  81 */     int pictureIdx = wb.addPicture(bytes, 5);
/*     */     
/*  83 */     HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0, 0, 0, 0, (short)1, 0, (short)4, 6);
/*  84 */     anchoVivaBem.setAnchorType(3);
/*  85 */     planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
/*  86 */     is.close();
/*     */     
/*  88 */     HSSFRow row = planilha.createRow(1);
/*  89 */     HSSFCell celula = row.createCell(5);
/*     */     
/*  91 */     celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
/*  92 */     celula.setCellStyle(estiloTitulo);
/*  93 */     Region r = new Region(1, (short)5, 1, (short)11);
/*  94 */     planilha.addMergedRegion(r);
/*     */     
/*  96 */     row = planilha.createRow(2);
/*  97 */     celula = row.createCell(5);
/*  98 */     celula.setCellValue("Cuentas hasta cuarto nivel");
/*  99 */     celula.setCellStyle(estiloTitulo);
/* 100 */     r = new Region(2, (short)5, 2, (short)11);
/* 101 */     planilha.addMergedRegion(r);
/*     */     
/* 103 */     int linha = 8;
/*     */     
/* 105 */     row = planilha.createRow(linha);
/* 106 */     celula = row.createCell(0);
/* 107 */     celula.setCellValue("Codigo");
/* 108 */     celula.setCellStyle(estiloTituloTabelaC);
/* 109 */     r = new Region(linha, (short)0, linha, (short)1);
/* 110 */     planilha.addMergedRegion(r);
/*     */     
/* 112 */     celula = row.createCell(2);
/* 113 */     celula.setCellValue("Nombre");
/* 114 */     celula.setCellStyle(estiloTituloTabelaC);
/* 115 */     r = new Region(linha, (short)2, linha, (short)11);
/* 116 */     planilha.addMergedRegion(r);
/* 120 */     for (Iterator<ClassificacaoContas> i = contas.iterator(); i.hasNext();)
/*     */     {
/* 122 */       ClassificacaoContas c = (ClassificacaoContas)i.next();
/*     */       
/* 124 */       linha++;
/*     */       
/* 126 */       String nome = c.obterNome();
/* 127 */       String codigo = c.obterApelido();
/*     */       
/* 129 */       row = planilha.createRow(linha);
/* 130 */       celula = row.createCell(0);
/* 131 */       celula.setCellValue(codigo);
/* 132 */       celula.setCellStyle(estiloTextoE);
/* 133 */       r = new Region(linha, (short)0, linha, (short)1);
/* 134 */       planilha.addMergedRegion(r);
/*     */       
/* 136 */       celula = row.createCell(2);
/* 137 */       celula.setCellValue(nome);
/* 138 */       celula.setCellStyle(estiloTextoE);
/* 139 */       r = new Region(linha, (short)2, linha, (short)11);
/* 140 */       planilha.addMergedRegion(r);
/*     */     }
/* 143 */     wb.write(stream);
/*     */     
/* 145 */     stream.flush();
/*     */     
/* 147 */     stream.close();
/*     */   }
/*     */ }


/* Location:           C:\Users\Giovanni\Desktop\CRM-BCP-11-03-2015.jar
 * Qualified Name:     com.gvs.crm.report.RelContasNivel4XLS
 * JD-Core Version:    0.7.0.1
 */