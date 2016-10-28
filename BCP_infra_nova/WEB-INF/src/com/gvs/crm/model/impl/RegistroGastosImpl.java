// FrontEnd Plus GUI for JAD
// DeCompiled : RegistroGastosImpl.class

package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.AuxiliarSeguro;
import com.gvs.crm.model.Conta;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.RegistroGastos;
import com.gvs.crm.model.Sinistro;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, RegistroGastos, AuxiliarSeguro, Conta, 
//            Entidade, EntidadeHome, Aseguradora, Sinistro, 
//            Apolice, EventoHome, Evento

public class RegistroGastosImpl extends EventoImpl
    implements RegistroGastos
{

    private Date dataSinistro;
    private Entidade auxiliar;
    private String nomeTerceiro;
    private double abonoGs;
    private String tipoMoedaAbonoGs;
    private double abonoMe;
    private Date dataPagamento;
    private String numeroCheque;
    private Entidade banco;
    private String situacaoSinistro;
    private String situacaoPagamento;
    private String tipoInstrumento;
    private double numeroEndoso;
    private double certificado;

    public RegistroGastosImpl()
    {
    }

    public void atribuirDataSinistro(Date data)
        throws Exception
    {
        dataSinistro = data;
    }

    public void atribuirAuxiliarSeguro(Entidade auxiliar)
        throws Exception
    {
        this.auxiliar = auxiliar;
    }

    public void atribuirNomeTerceiro(String nome)
        throws Exception
    {
        nomeTerceiro = nome;
    }

    public void atribuirAbonoGs(double valor)
        throws Exception
    {
        abonoGs = valor;
    }

    public void atribuirTipoMoedaAbonoGs(String tipo)
        throws Exception
    {
        tipoMoedaAbonoGs = tipo;
    }

    public void atribuirAbonoMe(double valor)
        throws Exception
    {
        abonoMe = valor;
    }

    public void atribuirDataPagamento(Date data)
        throws Exception
    {
        dataPagamento = data;
    }

    public void atribuirNumeroCheque(String numero)
        throws Exception
    {
        numeroCheque = numero;
    }

    public void atribuirBanco(Entidade conta)
        throws Exception
    {
        banco = conta;
    }

    public void atribuirSituacaoSinistro(String situacao)
        throws Exception
    {
        situacaoSinistro = situacao;
    }

    public void atribuirSituacaoPagamento(String situacao)
        throws Exception
    {
        situacaoPagamento = situacao;
    }

    public void atribuirTipoInstrumento(String tipo)
        throws Exception
    {
        tipoInstrumento = tipo;
    }

    public void atribuirNumeroEndoso(double numeroEndoso)
        throws Exception
    {
        this.numeroEndoso = numeroEndoso;
    }

    public void atribuirCertificado(double certificado)
        throws Exception
    {
        this.certificado = certificado;
    }

    public void atualizarDataSinistro(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_gastos set data_sinistro = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarAuxiliarSeguro(AuxiliarSeguro auxiliar)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_gastos set agente = ? where id = ?");
        update.addLong(auxiliar.obterId());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarNomeTerceiro(String nome)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_gastos set nome_terceiro = ? where id = ?");
        update.addString(nome);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarAbonoGs(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_gastos set abono_gs = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarTipoMoedaAbonoGs(String tipo)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_gastos set tipo_moeda_abono_gs = ? where id = ?");
        update.addString(tipo);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarAbonoMe(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_gastos set abono_me = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataPagamento(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_gastos set data_pagamento = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarNumeroCheque(String numero)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_gastos set numero_cheque = ? where id = ?");
        update.addString(numero);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarBanco(Conta banco)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_gastos set banco = ? where id = ?");
        update.addLong(banco.obterId());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSituacaoSinistro(String situacao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_gastos set situacao_sinistro = ? where id = ?");
        update.addString(situacao);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSituacaoPagamento(String situacao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update registro_gastos set situacao_pagamento = ? where id = ?");
        update.addString(situacao);
        update.addLong(obterId());
        update.execute();
    }

    public void incluir()
        throws Exception
    {
        super.incluir();
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into registro_gastos values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        insert.addLong(obterId());
        if(dataSinistro != null)
            insert.addLong(dataSinistro.getTime());
        else
            insert.addLong(0L);
        if(auxiliar != null)
            insert.addLong(auxiliar.obterId());
        else
            insert.addLong(0L);
        insert.addString(nomeTerceiro);
        insert.addDouble(abonoGs);
        insert.addString(tipoMoedaAbonoGs);
        insert.addDouble(abonoMe);
        if(dataPagamento != null)
            insert.addLong(dataPagamento.getTime());
        else
            insert.addLong(0L);
        insert.addString(numeroCheque);
        if(banco != null)
            insert.addLong(banco.obterId());
        else
            insert.addLong(0L);
        insert.addString(situacaoSinistro);
        insert.addString(situacaoPagamento);
        insert.addString(tipoInstrumento);
        insert.addDouble(numeroEndoso);
        insert.addDouble(certificado);
        insert.execute();
    }

    public Date obterDataSinistro()
        throws Exception
    {
        if(dataSinistro == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select data_sinistro from registro_gastos where id = ?");
            query.addLong(obterId());
            long dataLong = query.executeAndGetFirstRow().getLong("data_sinistro");
            if(dataLong > 0L)
                dataSinistro = new Date(dataLong);
        }
        return dataSinistro;
    }

    public Entidade obterAuxiliarSeguro()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select agente from registro_gastos where id = ?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("agente");
        Entidade auxiliar = null;
        if(id > 0L)
        {
            EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            auxiliar = home.obterEntidadePorId(id);
        }
        return auxiliar;
    }

    public String obterNomeTerceiro()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select nome_terceiro from registro_gastos where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("nome_terceiro");
    }

    public double obterAbonoGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select abono_gs from registro_gastos where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("abono_gs");
    }

    public String obterTipoMoedaAbonoGs()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_abono_gs from registro_gastos where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_abono_gs");
    }

    public double obterAbonoMe()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select abono_me from registro_gastos where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("abono_me");
    }

    public Date obterDataPagamento()
        throws Exception
    {
        if(dataPagamento == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select data_pagamento from registro_gastos where id = ?");
            query.addLong(obterId());
            Date data = null;
            long dataLong = query.executeAndGetFirstRow().getLong("data_pagamento");
            dataPagamento = new Date(dataLong);
        }
        return dataPagamento;
    }

    public String obterNumeroCheque()
        throws Exception
    {
        if(numeroCheque == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_cheque from registro_gastos where id = ?");
            query.addLong(obterId());
            numeroCheque = query.executeAndGetFirstRow().getString("numero_cheque");
        }
        return numeroCheque;
    }

    public Entidade obterBanco()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select banco from registro_gastos where id = ?");
        query.addLong(obterId());
        long id = query.executeAndGetFirstRow().getLong("banco");
        Entidade banco = null;
        if(id > 0L)
        {
            EntidadeHome home = (EntidadeHome)getModelManager().getHome("EntidadeHome");
            banco = home.obterEntidadePorId(id);
        }
        return banco;
    }

    public String obterSituacaoSinistro()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select situacao_sinistro from registro_gastos where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("situacao_sinistro");
    }

    public String obterSituacaoPagamento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select situacao_pagamento from registro_gastos where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("situacao_pagamento");
    }

    public String obterTipoInstrumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_instrumento from registro_gastos where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_instrumento");
    }

    public double obterNumeroEndoso()
        throws Exception
    {
        if(numeroEndoso == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_endoso from registro_gastos where id = ?");
            query.addLong(obterId());
            numeroEndoso = query.executeAndGetFirstRow().getDouble("numero_endoso");
        }
        return numeroEndoso;
    }

    public double obterCertificado()
        throws Exception
    {
        if(certificado == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado from registro_gastos where id = ?");
            query.addLong(obterId());
            certificado = query.executeAndGetFirstRow().getDouble("certificado");
        }
        return certificado;
    }

    public void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, Sinistro sinistro, Date dataPagamento, double numeroEndoso, double certificado, String numeroCheque)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,registro_gastos,sinistro,apolice where evento.id = registro_gastos.id and origem=? and superior = ? and status_apolice = ? and tipo = ? and registro_gastos.data_pagamento = ? and registro_gastos.numero_endoso = ? and registro_gastos.certificado = ? and numero_cheque = ? group by evento.id");
        query.addLong(aseguradora.obterId());
        query.addLong(sinistro.obterId());
        query.addString(apolice.obterStatusApolice());
        query.addString(sinistro.obterTipo());
        query.addLong(dataPagamento.getTime());
        query.addDouble(numeroEndoso);
        query.addDouble(certificado);
        query.addString(numeroCheque);
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            long id = rows[i].getLong("id");
            EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
            Evento e = home.obterEventoPorId(id);
            e.excluir();
        }

    }
}
