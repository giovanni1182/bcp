/*     */ package com.gvs.crm.control;
/*     */ import java.io.InputStream;
/*     */ import java.util.Collection;
/*     */ import java.util.Date;
/*     */ import java.util.Map;
/*     */ import java.util.TreeMap;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.CRMModelManager;
/*     */ import com.gvs.crm.model.ClassificacaoContas;
/*     */ import com.gvs.crm.model.Conta;
/*     */ import com.gvs.crm.model.Entidade;
/*     */ import com.gvs.crm.model.EntidadeHome;
/*     */ import com.gvs.crm.model.Usuario;
/*     */ import com.gvs.crm.model.UsuarioHome;
import com.gvs.crm.report.RelContasNivel4XLS;
import com.gvs.crm.report.RelFmiBancosXLS;
import com.gvs.crm.report.RelFmiXLS;
/*     */ import com.gvs.crm.view.PaginaInicialView;
import com.gvs.crm.view.RelFmiBancosView;
import com.gvs.crm.view.RelFmiView;

/*     */ 
/*     */ /*     */ import infra.control.Action;
/*     */ import infra.control.Control;
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ 
/*     */ public class FmiControl extends Control
/*     */ {
/*     */   public void relFmi(Action action)
/*     */     throws Exception
/*     */   {
/*  31 */     CRMModelManager mm = new CRMModelManager(getUser());
/*  32 */     EntidadeHome entidadeHome = (EntidadeHome)mm.getHome("EntidadeHome");
/*     */     
/*     */ 
/*  35 */     Date dataInicio = null;
/*  36 */     Date dataFim = null;
/*     */     try
/*     */     {
/*  39 */       if (!action.getBoolean("view"))
/*     */       {
/*  41 */         dataInicio = action.getDate("dataInicio");
/*  42 */         dataFim = action.getDate("dataFim");
/*     */         
/*  44 */         Collection<Aseguradora> aseguradoras = entidadeHome.obterAseguradoras();
/*     */         
/*  46 */         RelFmiXLS xls = new RelFmiXLS(dataInicio, dataFim, aseguradoras, entidadeHome);
/*     */         
/*  48 */         InputStream arquivo = xls.obterArquivo();
/*  49 */         setResponseInputStream(arquivo);
/*  50 */         setResponseFileName("Información requerida FMI.xls");
/*  51 */         setResponseContentType("application/vnd.ms-excel");
/*  52 */         setResponseContentSize(arquivo.available());
/*     */       }
/*  55 */       setResponseView(new RelFmiView(dataInicio, dataFim));
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/*  60 */       setAlert(Util.translateException(exception));
/*  61 */       setResponseView(new RelFmiView(dataInicio, dataFim));
/*     */     }
/*     */   }
/*     */   
/*     */   public void relFmi4Nivel(Action action)
/*     */     throws Exception
/*     */   {
/*  67 */     CRMModelManager mm = new CRMModelManager(getUser());
/*  68 */     EntidadeHome entidadeHome = (EntidadeHome)mm.getHome("EntidadeHome");
/*  69 */     UsuarioHome usuarioHome = (UsuarioHome)mm.getHome("UsuarioHome");
/*  70 */     Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
/*     */     try
/*     */     {
/*  75 */       Collection<ClassificacaoContas> contas = entidadeHome.obterContasNivel4();
/*     */       
/*  77 */       RelContasNivel4XLS xls = new RelContasNivel4XLS(contas);
/*     */       
/*  79 */       InputStream arquivo = xls.obterArquivo();
/*  80 */       setResponseInputStream(arquivo);
/*  81 */       setResponseFileName("Cuentas hasta cuarto nivel.xls");
/*  82 */       setResponseContentType("application/vnd.ms-excel");
/*  83 */       setResponseContentSize(arquivo.available());
/*     */       
/*  85 */       setResponseView(new PaginaInicialView(usuarioAtual, usuarioAtual));
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/*  90 */       setAlert(Util.translateException(exception));
/*  91 */       setResponseView(new PaginaInicialView(usuarioAtual, usuarioAtual));
/*     */     }
/*     */   }
/*     */   
/*     */   public void relFmiBancos(Action action)
/*     */     throws Exception
/*     */   {
/*  97 */     CRMModelManager mm = new CRMModelManager(getUser());
/*  98 */     EntidadeHome entidadeHome = (EntidadeHome)mm.getHome("EntidadeHome");
/*  99 */     UsuarioHome usuarioHome = (UsuarioHome)mm.getHome("UsuarioHome");
/* 100 */     Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(getUser());
/* 101 */     int anoInicial = 0;
/* 102 */     int anoFinal = 0;
/*     */     try
/*     */     {
/* 105 */       if (!action.getBoolean("view"))
/*     */       {
/* 107 */         anoInicial = action.getInt("anoInicial");
/* 108 */         anoFinal = action.getInt("anoFinal");
/* 110 */         if ((new Integer(anoInicial).toString().length() != 4) || (new Integer(anoFinal).toString().length() != 4)) {
/* 111 */           throw new Exception("Formato do Año deve ser Ex.: 2015");
/*     */         }
/* 113 */         if (anoInicial > anoFinal) {
/* 114 */           throw new Exception("Año inicial es maior que ano final");
/*     */         }
/* 116 */         Collection<Aseguradora> aseguradoras = entidadeHome.obterAseguradoras();
/*     */         
/* 118 */         Map<String, Conta> contas = new TreeMap<String, Conta>();
/* 119 */         Map<String, Conta> financeiras = new TreeMap<String, Conta>();
/*     */         
/*     */ 
/* 122 */         ClassificacaoContas cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorId(108L);
/* 123 */         for (Entidade e : cContas.obterInferiores())
/* 126 */           contas.put(e.obterApelido(), (Conta)e);
/* 
 * 130 */         cContas = (ClassificacaoContas) entidadeHome.obterEntidadePorId(116L);
/* 131 */         for (Entidade e : cContas.obterInferiores())
/* 134 */           financeiras.put(e.obterApelido(), (Conta)e);
/* 
 * 137 */         RelFmiBancosXLS xls = new RelFmiBancosXLS(anoInicial, anoFinal, contas, aseguradoras, entidadeHome, financeiras);
/* 138 */         InputStream arquivo = xls.obterArquivo();
/* 139 */         setResponseInputStream(arquivo);
/* 140 */         setResponseFileName("Información Bancos y Financieras.xls");
/* 141 */         setResponseContentType("application/vnd.ms-excel");
/* 142 */         setResponseContentSize(arquivo.available());
/*     */       }
/* 145 */       setResponseView(new RelFmiBancosView(anoInicial, anoFinal));
/*     */     }
/*     */     catch (Exception exception)
/*     */     {
/* 150 */       setAlert(Util.translateException(exception));
/* 151 */       setResponseView(new RelFmiBancosView(anoInicial, anoFinal));
/*     */     }
/*     */   }
/*     */ }


/* Location:           C:\Users\Giovanni\Desktop\CRM-BCP-11-03-2015.jar
 * Qualified Name:     com.gvs.crm.control.FmiControl
 * JD-Core Version:    0.7.0.1
 */