/*    */ package com.gvs.crm.view;
/*    */ import java.util.Date;
/*    */ import java.util.Locale;
/*    */ import java.util.Properties;

import com.gvs.crm.model.Entidade;

/*    */ 
/*    */ /*    */ import infra.control.Action;
/*    */ import infra.security.User;
/*    */ import infra.view.Button;
/*    */ import infra.view.InputDate;
/*    */ import infra.view.Label;
/*    */ import infra.view.Table;
/*    */ import infra.view.View;
/*    */ 
/*    */ public class RelFmiView
/*    */   extends PortalView
/*    */ {
/*    */   private Date dataInicio;
/*    */   private Date dataFim;
/*    */   
/*    */   public RelFmiView(Date dataInicio, Date dataFim)
/*    */   {
/* 23 */     this.dataInicio = dataInicio;
/* 24 */     this.dataFim = dataFim;
/*    */   }
/*    */   
/*    */   public View getBody(User user, Locale locale, Properties properties)
/*    */     throws Exception
/*    */   {
/* 29 */     Table table = new Table(4);
/*    */     
/* 31 */     table.addHeader("Periodo:");
/* 32 */     table.add(new InputDate("dataInicio", this.dataInicio));
/* 33 */     table.addHeader("hasta");
/* 34 */     table.add(new InputDate("dataFim", this.dataFim));
/*    */     
/* 36 */     Button consultarBUtton = new Button("Generar Excel", new Action("relFmi"));
/* 37 */     table.addFooter(consultarBUtton);
/*    */     
/* 39 */     return table;
/*    */   }
/*    */   
/*    */   public String getSelectedGroup()
/*    */     throws Exception
/*    */   {
/* 44 */     return null;
/*    */   }
/*    */   
/*    */   public String getSelectedOption()
/*    */     throws Exception
/*    */   {
/* 49 */     return null;
/*    */   }
/*    */   
/*    */   public View getTitle()
/*    */     throws Exception
/*    */   {
/* 54 */     return new Label("Información requerida FMI");
/*    */   }
/*    */   
/*    */   public Entidade getOrigemMenu()
/*    */     throws Exception
/*    */   {
/* 59 */     return null;
/*    */   }
/*    */ }


/* Location:           C:\Users\Giovanni\Desktop\CRM-BCP-11-03-2015.jar
 * Qualified Name:     com.gvs.crm.view.RelFmiView
 * JD-Core Version:    0.7.0.1
 */