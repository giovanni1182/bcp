// Decompiled by DJ v3.12.12.98 Copyright 2014 Atanas Neshkov  Date: 11/02/2015 17:51:44
// Home Page:  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PlanoImpl.java

package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;

import com.gvs.crm.model.DocumentoProduto;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Plano;
import com.gvs.crm.model.Usuario;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gvs.crm.model.impl:
//            EventoImpl

public class PlanoImpl extends EventoImpl
    implements Plano
{

    public PlanoImpl()
    {
    }

    public void atribuirEspecial(int especial)
    {
        this.especial = especial;
    }

    public void atualizarRamo(String ramo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set ramo=? where id=?");
        update.addString(ramo);
        update.addLong(obterId());
        update.execute();
        this.ramo = ramo;
    }

    public void atualizarEspecial(int especial)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set especial=? where id=?");
        update.addInt(especial);
        update.addLong(obterId());
        update.execute();
        this.especial = especial;
    }

    public void atualizarSecao(String secao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set secao=? where id=?");
        update.addString(secao);
        update.addLong(obterId());
        update.execute();
        this.secao = secao;
    }

    public void atualizarPlano(String plano)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set plano=? where id=?");
        update.addString(plano);
        update.addLong(obterId());
        update.execute();
        modalidade = plano;
    }

    public void atualizarIdentificador(String identificador)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set identificador=? where id=?");
        update.addString(identificador);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarResolucao(String resolucao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set resolucao=? where id=?");
        update.addString(resolucao);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataResolucao(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set data_resolucao=? where id=?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSituacao(String situacao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set situacao=? where id=?");
        update.addString(situacao);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDenominacao(String denominacao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set denominacao=? where id=?");
        update.addString(denominacao);
        update.addLong(obterId());
        update.execute();
    }

    public void incluir()
        throws Exception
    {
        super.incluir();
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into plano(id) values(?)");
        insert.addLong(obterId());
        insert.execute();
    }

    public String obterRamo()
        throws Exception
    {
        if(ramo == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select ramo from plano where id=?");
            query.addLong(obterId());
            ramo = query.executeAndGetFirstRow().getString("ramo");
        }
        return ramo;
    }

    public String obterSecao()
        throws Exception
    {
        if(secao == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select secao from plano where id=?");
            query.addLong(obterId());
            secao = query.executeAndGetFirstRow().getString("secao");
        }
        return secao;
    }

    public String obterPlano()
        throws Exception
    {
        if(modalidade == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select plano from plano where id=?");
            query.addLong(obterId());
            modalidade = query.executeAndGetFirstRow().getString("plano");
        }
        return modalidade;
    }

    public int obterEspecial()  throws Exception
    {
        if(especial == 0)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select especial from plano where id=?");
            query.addLong(obterId());
            especial = query.executeAndGetFirstRow().getInt("especial");
        }
        return especial;
    }

    public String obterDenominacao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select denominacao from plano where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("denominacao");
    }

    public String obterIdentificador()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select identificador from plano where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("identificador");
    }

    public String obterResolucao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select resolucao from plano where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("resolucao");
    }

    public Date obterDataResolucao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_resolucao from plano where id=?");
        query.addLong(obterId());
        long dataLong = query.executeAndGetFirstRow().getLong("data_resolucao");
        Date data = null;
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public String obterSituacao()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select situacao from plano where id=?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("situacao");
    }

    public void adicionarNovoRamo(String ramo)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select MAX(seq) as MX from plano_ramos where id=?");
        query.addLong(obterId());
        int id = query.executeAndGetFirstRow().getInt("MX") + 1;
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into plano_ramos(id, seq, nome) values (?, ?, ?)");
        insert.addLong(obterId());
        insert.addInt(id);
        insert.addString(ramo);
        insert.execute();
    }

    public Collection obterNomeRamos()
        throws Exception
    {
        Collection nomeRamos = new ArrayList();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select nome from plano_ramos group by nome");
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
            nomeRamos.add(rows[i].getString("nome"));

        return nomeRamos;
    }

    public void adicionarNovaSecao(String secao)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select MAX(seq) as MX from plano_secao where id=?");
        query.addLong(obterId());
        int id = query.executeAndGetFirstRow().getInt("MX") + 1;
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into plano_secao(id, seq, nome) values (?, ?, ?)");
        insert.addLong(obterId());
        insert.addInt(id);
        insert.addString(secao);
        insert.execute();
    }

    public Collection obterNomeSecoes()
        throws Exception
    {
        Collection secoes = new ArrayList();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select nome from plano_secao group by nome");
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
            secoes.add(rows[i].getString("nome"));

        return secoes;
    }

    public void adicionarNovoPlano(String plano)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select MAX(seq) as MX from plano_planos where id=?");
        query.addLong(obterId());
        int id = query.executeAndGetFirstRow().getInt("MX") + 1;
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into plano_planos(id, seq, nome) values (?, ?, ?)");
        insert.addLong(obterId());
        insert.addInt(id);
        insert.addString(plano);
        insert.execute();
    }

    public Collection obterNomePlanos()
        throws Exception
    {
        Collection modalidades = new ArrayList();
        SQLQuery query = getModelManager().createSQLQuery("crm", "select nome from plano_planos group by nome");
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
            modalidades.add(rows[i].getString("nome"));

        return modalidades;
    }

    public void verificarPlano(String plano) throws Exception
    {
    	String sql = "select count(*) as qtde from plano,evento where evento.id = plano.id and identificador='"+plano+"' and origem = " + obterOrigem().obterId();
    	
    	//System.out.println(sql);
    	
        SQLQuery query = getModelManager().createSQLQuery("crm", sql);
        if(query.executeAndGetFirstRow().getInt("qtde") > 0)
            throw new Exception((new StringBuilder("El Plan N\272 ")).append(plano).append(" ya existe").toString());
    }

    public boolean validarResolucao(String resolucaoStr)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,documento_produto where evento.id = documento_produto.id and numero=?");
        query.addString(resolucaoStr);
        if(query.execute().length == 0)
            return true;
        boolean renovar = true;
        long id = query.executeAndGetFirstRow().getLong("id");
        EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
        DocumentoProduto documento = (DocumentoProduto)home.obterEventoPorId(id);
        if(documento.obterDocumento().obterApelido().equals("resolucion"))
        {
            for(Iterator i = documento.obterFases().iterator(); i.hasNext();)
            {
                com.gvs.crm.model.Evento.Fase fase = (com.gvs.crm.model.Evento.Fase)i.next();
                if(fase.obterCodigo().equals("aprovado"))
                    renovar = false;
            }

            return renovar;
        } else
        {
            return true;
        }
    }

    public boolean permiteAtualizar() throws Exception
    {
        boolean permite = false;
        Usuario usuarioAtual = obterUsuarioAtual();
        if(usuarioAtual.obterId() == 1L)
            return true;
        else if(usuarioAtual.equals(obterResponsavel()))
            permite = true;
        else
        {
        	String nivel = usuarioAtual.obterNivel();
        	
        	if(nivel.equals(Usuario.DIVISAO_ESTUDOS_TECNICOS) || nivel.equals(Usuario.INTENDENTE_IETA))
        		permite = true; 
        	else
        	{
	        	Collection<Entidade> superiores = usuarioAtual.obterSuperiores(); 
	            for(Entidade e : superiores)
	            {
	                if(e.obterApelido() != null && (e.obterApelido().equals("intendenteieta") || e.obterApelido().equals("jefedivisionieta") || e.obterApelido().equals("jefedivisionestudioieta")))
	                {
	                    permite = true;
	                    break;
	                }
	            }
        	}

        }
        return permite;
    }

    public String obterPlanosPorSecao(String secao)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select id from plano where secao = ?");
        query.addString(secao);
        SQLRow rows[] = query.execute();
        String s = "(";
        for(int i = 0; i < rows.length; i++)
        {
            int id = rows[i].getInt("id");
            if(i == 0)
                s = (new StringBuilder(String.valueOf(s))).append("apolice.plano = ").append(id).toString();
            else
                s = (new StringBuilder(String.valueOf(s))).append(" or apolice.plano = ").append(id).toString();
        }

        s = (new StringBuilder(String.valueOf(s))).append(")").toString();
        return s;
    }

    public void atribuirRamo(String ramo)
    {
        this.ramo = ramo;
    }

    public void atribuirSecao(String secao)
    {
        this.secao = secao;
    }

    public void atribuirModalidade(String modalidade)
    {
        this.modalidade = modalidade;
    }

    public void atualizarSegmentoRamo(String ramo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set seg_ramo = ? where id = ?");
        update.addString(ramo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSegmentoSecao(String secao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update plano set seg_secao = ? where id = ?");
        update.addString(secao);
        update.addLong(obterId());
        update.execute();
    }

    private int especial;
    private String ramo;
    private String secao;
    private String modalidade;
}
