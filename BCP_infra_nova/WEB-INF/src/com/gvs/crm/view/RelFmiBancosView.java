/*    */ package com.gvs.crm.view;
/*    */ import java.util.Locale;
/*    */ import java.util.Properties;

import com.gvs.crm.model.Entidade;

/*    */ 
/*    */ /*    */ import infra.control.Action;
/*    */ import infra.security.User;
/*    */ import infra.view.Button;
/*    */ import infra.view.InputInteger;
/*    */ import infra.view.Label;
/*    */ import infra.view.Table;
/*    */ import infra.view.View;
/*    */ 
/*    */ public class RelFmiBancosView
/*    */   extends PortalView
/*    */ {
/*    */   private int anoInicial;
/*    */   private int anoFinal;
/*    */   
/*    */   public RelFmiBancosView(int anoInicial, int anoFinal)
/*    */   {
/* 22 */     this.anoInicial = anoInicial;
/* 23 */     this.anoFinal = anoFinal;
/*    */   }
/*    */   
/*    */   public View getBody(User user, Locale locale, Properties properties)
/*    */     throws Exception
/*    */   {
/* 28 */     Table table = new Table(4);
/*    */     
/* 30 */     table.addHeader("Año:");
/* 31 */     table.add(new InputInteger("anoInicial", this.anoInicial, 4));
/* 32 */     table.addHeader("hasta:");
/* 33 */     table.add(new InputInteger("anoFinal", this.anoFinal, 4));
/*    */     
/* 35 */     Button consultarButton = new Button("Generar Excel", new Action("relFmiBancos"));
/*    */     
/* 37 */     table.addFooter(consultarButton);
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
/* 54 */     return new Label("Información Bancos y Financieras");
/*    */   }
/*    */   
/*    */   public Entidade getOrigemMenu()
/*    */     throws Exception
/*    */   {
/* 59 */     return null;
/*    */   }
/*    */ }


/* Location:           C:\Users\Giovanni\Desktop\CRM-BCP-11-03-2015.jar
 * Qualified Name:     com.gvs.crm.view.RelFmiBancosView
 * JD-Core Version:    0.7.0.1
 */