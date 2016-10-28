/*     */ package com.gvs.crm.report;
/*     */ import java.io.FileInputStream;
/*     */ import java.io.FileOutputStream;
/*     */ import java.io.InputStream;
/*     */ import java.text.DecimalFormat;
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
import com.gvs.crm.model.Conta;
/*     */ import com.gvs.crm.model.EntidadeHome;

/*     */ 
/*     */ /*     */ import infra.config.InfraProperties;
/*     */ 
/*     */ 
/*     */ public class RelFmiBancosXLS
/*     */   extends Excel
/*     */ {
/*     */   public RelFmiBancosXLS(int anoInicial, int anoFinal, Map<String, Conta> contas, Collection<Aseguradora> aseguradoras, EntidadeHome entidadeHome, Map<String, Conta> financeiras)
/*     */     throws Exception
/*     */   {
/*  35 */     String caminho = "C:/tmp/" + new Date().getTime() + ".xls";
/*     */     
/*  37 */     DecimalFormat formataValor = new DecimalFormat("#,##0.00");
/*     */     
/*  39 */     FileOutputStream stream = new FileOutputStream(caminho);
/*     */     
/*  41 */     setCaminho(caminho);
/*     */     
/*  43 */     HSSFWorkbook wb = new HSSFWorkbook();
/*     */     
/*  45 */     HSSFFont fonteTitulo = wb.createFont();
/*  46 */     fonteTitulo.setFontHeightInPoints((short)10);
/*  47 */     fonteTitulo.setFontName("Arial");
/*  48 */     fonteTitulo.setBoldweight((short)700);
/*     */     
/*  50 */     HSSFFont fonteTexto = wb.createFont();
/*  51 */     fonteTexto.setFontHeightInPoints((short)9);
/*  52 */     fonteTexto.setFontName("Arial");
/*     */     
/*  54 */     HSSFFont fonteTituloTabela = wb.createFont();
/*  55 */     fonteTituloTabela.setFontHeightInPoints((short)10);
/*  56 */     fonteTituloTabela.setFontName("Arial");
/*  57 */     fonteTituloTabela.setColor((short)9);
/*     */     
/*  59 */     HSSFCellStyle estiloTitulo = wb.createCellStyle();
/*  60 */     estiloTitulo.setAlignment((short)2);
/*  61 */     estiloTitulo.setFont(fonteTitulo);
/*     */     
/*  63 */     HSSFCellStyle estiloTexto = wb.createCellStyle();
/*  64 */     estiloTexto.setAlignment((short)2);
/*  65 */     estiloTexto.setFont(fonteTexto);
/*     */     
/*  67 */     HSSFCellStyle estiloTextoD = wb.createCellStyle();
/*  68 */     estiloTextoD.setAlignment((short)3);
/*  69 */     estiloTextoD.setFont(fonteTexto);
/*     */     
/*  71 */     HSSFCellStyle estiloTextoE = wb.createCellStyle();
/*  72 */     estiloTextoE.setAlignment((short)5);
/*  73 */     estiloTextoE.setFont(fonteTexto);
/*     */     
/*  75 */     HSSFCellStyle estiloTituloTabelaC = wb.createCellStyle();
/*  76 */     estiloTituloTabelaC.setAlignment((short)2);
/*  77 */     estiloTituloTabelaC.setFont(fonteTituloTabela);
/*  78 */     estiloTituloTabelaC.setFillForegroundColor((short)63);
/*  79 */     estiloTituloTabelaC.setFillPattern((short)1);
/*     */     
/*  81 */     Map<String, String> tipos = new TreeMap();
/*  82 */     tipos.put("Bancos", "Bancos");
/*  83 */     tipos.put("Financieras", "Financieras");
/*     */     
/*  85 */     int difDatas = anoFinal - anoInicial + 1;
///*     */     Iterator<Entidade> j;
/*  87 */     for (Iterator<String> i = tipos.values().iterator(); i.hasNext();)
/*     */     {
/*  89 */       String tipo = (String)i.next();
/*     */       
/*  91 */       HSSFSheet planilha = wb.createSheet(tipo);
/*     */       
/*  93 */       String dirImages = InfraProperties.getInstance().getProperty("report.images.url2");
/*  94 */       InputStream is = new FileInputStream(dirImages + "/bcp.jpg");
/*  95 */       byte[] bytes = IOUtils.toByteArray(is);
/*     */       
/*  97 */       int pictureIdx = wb.addPicture(bytes, 5);
/*     */       
/*  99 */       HSSFClientAnchor anchoVivaBem = new HSSFClientAnchor(0, 0, 0, 0, (short)1, 0, (short)4, 6);
/* 100 */       anchoVivaBem.setAnchorType(3);
/* 101 */       planilha.createDrawingPatriarch().createPicture(anchoVivaBem, pictureIdx);
/* 102 */       is.close();
/*     */       
/* 104 */       HSSFRow row = planilha.createRow(1);
/* 105 */       HSSFCell celula = row.createCell(5);
/*     */       
/* 107 */       celula.setCellValue("SUPERINTENDENCIA DE SEGUROS");
/* 108 */       celula.setCellStyle(estiloTitulo);
/* 109 */       Region r = new Region(1, (short)5, 1, (short)11);
/* 110 */       planilha.addMergedRegion(r);
/*     */       
/* 112 */       row = planilha.createRow(2);
/* 113 */       celula = row.createCell(5);
/* 114 */       celula.setCellValue("Información Bancos y Financieras");
/* 115 */       celula.setCellStyle(estiloTitulo);
/* 116 */       r = new Region(2, (short)5, 2, (short)11);
/* 117 */       planilha.addMergedRegion(r);
/*     */       
/* 119 */       int linha = 8;
/*     */       
				for (Aseguradora aseguradora : aseguradoras)
				{
	/* 125 */       int coluna = 4;
	/*     */       
	/* 127 */       int colunaFim = coluna + difDatas - 1;
	/*     */       
	/* 129 */       row = planilha.createRow(linha);
	/*     */       
	/* 131 */       celula = row.createCell(0);
	/* 132 */       celula.setCellValue("");
	/* 133 */       celula.setCellStyle(estiloTituloTabelaC);
	/* 134 */       r = new Region(linha, (short)0, linha, (short)3);
	/* 135 */       planilha.addMergedRegion(r);
	/*     */       
	/* 137 */       celula = row.createCell(coluna);
	/* 138 */       celula.setCellValue(aseguradora.obterNome());
	/* 139 */       celula.setCellStyle(estiloTituloTabelaC);
	/* 140 */       r = new Region(linha, (short)coluna, linha, (short)colunaFim);
	/* 141 */       planilha.addMergedRegion(r);
	/*     */       
	/* 143 */       linha++;
	/* 144 */       row = planilha.createRow(linha);
	/* 145 */       int contAno = anoInicial;
	/* 146 */       coluna = 4;
	/*     */       
	/* 148 */       celula = row.createCell(0);
	/* 149 */       celula.setCellValue(tipo);
	/* 150 */       celula.setCellStyle(estiloTituloTabelaC);
	/* 151 */       r = new Region(linha, (short)0, linha, (short)3);
	/* 152 */       planilha.addMergedRegion(r);
	/* 154 */       for (int k = 0; k < difDatas; k++)
	/*     */       {
	/* 156 */         celula = row.createCell(coluna);
	/* 157 */         celula.setCellValue(contAno);
	/* 158 */         celula.setCellStyle(estiloTexto);
	/*     */         
	/* 160 */         coluna++;
	/* 161 */         contAno++;
	/*     */       }
	/* 164 */       linha++;
	/* 166 */       if (tipo.equals("Bancos"))
					{
	/* 168 */         for (Iterator<Conta> k = contas.values().iterator(); k.hasNext();)
	/*     */         {
	/* 170 */           Conta c = (Conta)k.next();
	/*     */           
	/* 172 */           String apelido = c.obterApelido();
	/*     */           
	/* 174 */           String apelidoIncompl = apelido.substring(0, 7);
	/* 175 */           String apelidoCod = apelido.substring(8, 10);
	/*     */           
	/* 177 */           String apelidoConta2 = apelidoIncompl + "2" + apelidoCod;
	/* 178 */           String apelidoConta3 = apelidoIncompl + "3" + apelidoCod;
	/* 179 */           String apelidoConta4 = apelidoIncompl + "4" + apelidoCod;
	/*     */           
	/* 181 */           Conta conta2 = (Conta)entidadeHome.obterEntidadePorApelido(apelidoConta2);
	/* 182 */           Conta conta3 = (Conta)entidadeHome.obterEntidadePorApelido(apelidoConta3);
	/* 183 */           Conta conta4 = (Conta)entidadeHome.obterEntidadePorApelido(apelidoConta4);
	/*     */           
	/* 185 */           row = planilha.createRow(linha);
	/* 186 */           celula = row.createCell(0);
	/* 187 */           celula.setCellValue(c.obterNome());
	/* 188 */           celula.setCellStyle(estiloTextoE);
	/* 189 */           r = new Region(linha, (short)0, linha, (short)3);
	/* 190 */           planilha.addMergedRegion(r);
	/*     */           
	/* 192 */           contAno = anoInicial;
	/* 193 */           coluna = 4;
	/* 195 */           for (int w = 0; w < difDatas; w++)
	/*     */           {
	/* 197 */             String mesAno = "06/" + contAno;
	/*     */             
	/* 199 */             celula = row.createCell(coluna);
	/*     */             
	/* 201 */             double valor = c.obterTotalizacaoExistente(aseguradora, mesAno);
	/* 202 */             valor += conta2.obterTotalizacaoExistente(aseguradora, mesAno);
	/* 203 */             valor += conta3.obterTotalizacaoExistente(aseguradora, mesAno);
	/* 204 */             valor += conta4.obterTotalizacaoExistente(aseguradora, mesAno);
	/*     */             
	/* 206 */             celula.setCellValue(formataValor.format(valor));
	/* 207 */             celula.setCellStyle(estiloTextoD);
	/*     */             
	/* 209 */             coluna++;
	/* 210 */             contAno++;
	/*     */           }
	/* 213 */           linha++;
	/*     */         }
	/*     */       }
					else
					{
	/* 219 */         for (Iterator<Conta> k = financeiras.values().iterator(); k.hasNext();)
	/*     */         {
	/* 221 */           Conta c = (Conta)k.next();
	/*     */           
	/* 223 */           String apelido = c.obterApelido();
	/*     */           
	/* 225 */           String apelidoIncompl = apelido.substring(0, 7);
	/* 226 */           String apelidoCod = apelido.substring(8, 10);
	/*     */           
	/* 228 */           String apelidoConta2 = apelidoIncompl + "6" + apelidoCod;
	/*     */           
	/*     */ 
	/*     */ 
	/* 232 */           Conta conta2 = (Conta)entidadeHome.obterEntidadePorApelido(apelidoConta2);
	/*     */           
	/* 234 */           row = planilha.createRow(linha);
	/* 235 */           celula = row.createCell(0);
	/* 236 */           celula.setCellValue(c.obterNome());
	/* 237 */           celula.setCellStyle(estiloTextoE);
	/* 238 */           r = new Region(linha, (short)0, linha, (short)3);
	/* 239 */           planilha.addMergedRegion(r);
	/*     */           
	/* 241 */           contAno = anoInicial;
	/* 242 */           coluna = 4;
	/* 244 */           for (int w = 0; w < difDatas; w++)
	/*     */           {
	/* 246 */             String mesAno = "06/" + contAno;
	/*     */             
	/* 248 */             celula = row.createCell(coluna);
	/*     */             
	/* 250 */             double valor = c.obterTotalizacaoExistente(aseguradora, mesAno);
	/* 251 */             valor += conta2.obterTotalizacaoExistente(aseguradora, mesAno);
	/*     */             
	/* 253 */             celula.setCellValue(formataValor.format(valor));
	/* 254 */             celula.setCellStyle(estiloTextoD);
	/*     */             
	/* 256 */             coluna++;
	/* 257 */             contAno++;
	/*     */           }
	/* 260 */           linha++;
	/*     */         }
/*     */       }
/*     */     }
			}
/* 268 */     wb.write(stream);
/*     */     
/* 270 */     stream.flush();
/*     */     
/* 272 */     stream.close();
/*     */   }
/*     */ }


/* Location:           C:\Users\Giovanni\Desktop\CRM-BCP-11-03-2015.jar
 * Qualified Name:     com.gvs.crm.report.RelFmiBancosXLS
 * JD-Core Version:    0.7.0.1
 */