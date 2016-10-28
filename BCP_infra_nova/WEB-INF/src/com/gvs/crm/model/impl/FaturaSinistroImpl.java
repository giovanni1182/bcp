// FrontEnd Plus GUI for JAD
// DeCompiled : FaturaSinistroImpl.class

package com.gvs.crm.model.impl;

import java.util.Date;

import com.gvs.crm.model.Apolice;
import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.FaturaSinistro;
import com.gvs.crm.model.Sinistro;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

// Referenced classes of package com.gio.crm.model:
//            EventoImpl, FaturaSinistro, Aseguradora, Sinistro, 
//            Apolice, EventoHome, Evento

public class FaturaSinistroImpl extends EventoImpl
    implements FaturaSinistro
{

    private Date dataSinistro;
    private String numeroDocumento;
    private String numeroFatura;
    private String rucProvedor;
    private String nomeProvedor;
    private Date dataDocumento;
    private double montanteDocumento;
    private Date dataPagamento;
    private String situacaoFatura;
    private String tipoInstrumento;
    private double numeroEndoso;
    private double certificado;
    private String tipoDocumentoProveedor;
    private String tipoMoedaDocumento;
    private double valorMontanteMe;
    private double valorMontantePago;

    public FaturaSinistroImpl()
    {
    }

    public void atribuirDataSinistro(Date data)
        throws Exception
    {
        dataSinistro = data;
    }

    public void atribuirNumeroDocumento(String numero)
        throws Exception
    {
        numeroDocumento = numero;
    }

    public void atribuirNumeroFatura(String numero)
        throws Exception
    {
        numeroFatura = numero;
    }

    public void atribuirRucProvedor(String ruc)
        throws Exception
    {
        rucProvedor = ruc;
    }

    public void atribuirNomeProvedor(String nome)
        throws Exception
    {
        nomeProvedor = nome;
    }

    public void atribuirDataDocumento(Date data)
        throws Exception
    {
        dataDocumento = data;
    }

    public void atribuirMontanteDocumento(double valor)
        throws Exception
    {
        montanteDocumento = valor;
    }

    public void atribuirDataPagamento(Date data)
        throws Exception
    {
        dataPagamento = data;
    }

    public void atribuirSituacaoFatura(String situacao)
        throws Exception
    {
        situacaoFatura = situacao;
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

    public void atribuirTipoDocumentoProveedor(String tipo)
        throws Exception
    {
        tipoDocumentoProveedor = tipo;
    }

    public void atribuirTipoMoedaDocumento(String tipo)
        throws Exception
    {
        tipoMoedaDocumento = tipo;
    }

    public void atribuirMontanteME(double valor)
        throws Exception
    {
        valorMontanteMe = valor;
    }

    public void atribuirMontantePago(double valor)
        throws Exception
    {
        valorMontantePago = valor;
    }

    public void atualizarDataSinistro(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update fatura_sinistro set data_sinistro = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarNumeroDocumento(String numero)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update fatura_sinistro set numero_documento = ? where id = ?");
        update.addString(numero);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarNumeroFatura(String numero)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update fatura_sinistro set numero_fatura = ? where id = ?");
        update.addString(numero);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarRucProvedor(String ruc)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update fatura_sinistro set ruc_provedor = ? where id = ?");
        update.addString(ruc);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarNomeProvedor(String nome)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update fatura_sinistro set nome_provedor = ? where id = ?");
        update.addString(nome);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataDocumento(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update fatura_sinistro set data_documento = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarMontanteDocumento(double valor)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update fatura_sinistro set montante_documento = ? where id = ?");
        update.addDouble(valor);
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarDataPagamento(Date data)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update fatura_sinistro set data_pagamento = ? where id = ?");
        update.addLong(data.getTime());
        update.addLong(obterId());
        update.execute();
    }

    public void atualizarSituacaoFatura(String situacao)
        throws Exception
    {
        SQLUpdate update = getModelManager().createSQLUpdate("crm", "update fatura_sinistro set situacao_fatura = ? where id = ?");
        update.addString(situacao);
        update.addLong(obterId());
        update.execute();
    }

    public void incluir()
        throws Exception
    {
        super.incluir();
        SQLUpdate insert = getModelManager().createSQLUpdate("crm", "insert into fatura_sinistro values(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
        insert.addLong(obterId());
        if(dataSinistro != null)
            insert.addLong(dataSinistro.getTime());
        else
            insert.addLong(0L);
        insert.addString(numeroDocumento);
        insert.addString(numeroFatura);
        insert.addString(rucProvedor);
        insert.addString(nomeProvedor);
        if(dataDocumento != null)
            insert.addLong(dataDocumento.getTime());
        else
            insert.addLong(0L);
        insert.addDouble(montanteDocumento);
        if(dataPagamento != null)
            insert.addLong(dataPagamento.getTime());
        else
            insert.addLong(0L);
        insert.addString(situacaoFatura);
        insert.addString(tipoInstrumento);
        insert.addDouble(numeroEndoso);
        insert.addDouble(certificado);
        insert.addString(tipoDocumentoProveedor);
        insert.addString(tipoMoedaDocumento);
        insert.addDouble(valorMontanteMe);
        insert.addDouble(valorMontantePago);
        insert.execute();
    }

    public Date obterDataSinistro()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_sinistro from fatura_sinistro where id = ?");
        query.addLong(obterId());
        Date data = null;
        long dataLong = query.executeAndGetFirstRow().getLong("data_sinistro");
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public String obterNumeroDocumento()
        throws Exception
    {
        if(numeroDocumento == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_documento from fatura_sinistro where id = ?");
            query.addLong(obterId());
            numeroDocumento = query.executeAndGetFirstRow().getString("numero_documento");
        }
        return numeroDocumento;
    }

    public String obterNumeroFatura()
        throws Exception
    {
        if(numeroFatura == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_fatura from fatura_sinistro where id = ?");
            query.addLong(obterId());
            numeroFatura = query.executeAndGetFirstRow().getString("numero_fatura");
        }
        return numeroFatura;
    }

    public String obterRucProvedor()
        throws Exception
    {
        if(rucProvedor == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select ruc_provedor from fatura_sinistro where id = ?");
            query.addLong(obterId());
            rucProvedor = query.executeAndGetFirstRow().getString("ruc_provedor");
        }
        return rucProvedor;
    }

    public String obterNomeProvedor()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select nome_provedor from fatura_sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("nome_provedor");
    }

    public Date obterDataDocumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select data_documento from fatura_sinistro where id = ?");
        query.addLong(obterId());
        Date data = null;
        long dataLong = query.executeAndGetFirstRow().getLong("data_documento");
        if(dataLong > 0L)
            data = new Date(dataLong);
        return data;
    }

    public double obterMontanteDocumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select montante_documento from fatura_sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("montante_documento");
    }

    public Date obterDataPagamento()
        throws Exception
    {
        if(dataPagamento == null)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select data_pagamento from fatura_sinistro where id = ?");
            query.addLong(obterId());
            long dataLong = query.executeAndGetFirstRow().getLong("data_pagamento");
            if(dataLong != 0L)
                dataPagamento = new Date(dataLong);
        }
        return dataPagamento;
    }

    public String obterSituacaoFatura()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select situacao_fatura from fatura_sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("situacao_fatura");
    }

    public void verificarDuplicidade(Aseguradora asguradora, Apolice apolice, Sinistro sinistro, String tipoDocumento, String numeroDocumento, String ruc, double numeroEndoso, Date dataPagamento, double certificado, String numerofatura, Date dataSinistro)
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select evento.id from evento,fatura_sinistro,sinistro,apolice where evento.id = fatura_sinistro.id and origem = ? and superior = ? and status_apolice = ? and tipo = ? and numero_documento = ? and ruc_provedor = ? and fatura_sinistro.numero_endoso = ? and fatura_sinistro.data_pagamento = ? and fatura_sinistro.certificado = ? and fatura_sinistro.numero_fatura = ? and sinistro.data_sinistro = ? group by evento.id");
        query.addLong(asguradora.obterId());
        query.addLong(sinistro.obterId());
        query.addString(apolice.obterStatusApolice());
        query.addString(tipoDocumento);
        query.addString(numeroDocumento);
        query.addString(ruc);
        query.addDouble(numeroEndoso);
        query.addLong(dataPagamento.getTime());
        query.addDouble(certificado);
        query.addString(numerofatura);
        if(dataSinistro != null)
            query.addLong(dataSinistro.getTime());
        else
            query.addLong(0L);
        SQLRow rows[] = query.execute();
        for(int i = 0; i < rows.length; i++)
        {
            long id = rows[i].getLong("id");
            EventoHome home = (EventoHome)getModelManager().getHome("EventoHome");
            Evento e = home.obterEventoPorId(id);
            e.excluir();
        }

    }

    public String obterTipoInstrumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_instrumento from fatura_sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_instrumento");
    }

    public double obterNumeroEndoso()
        throws Exception
    {
        if(numeroEndoso == 0.0D)
        {
            SQLQuery query = getModelManager().createSQLQuery("crm", "select numero_endoso from fatura_sinistro where id = ?");
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
            SQLQuery query = getModelManager().createSQLQuery("crm", "select certificado from fatura_sinistro where id = ?");
            query.addLong(obterId());
            certificado = query.executeAndGetFirstRow().getDouble("certificado");
        }
        return certificado;
    }

    public String obterTipoDocumentoProveedor()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_documento_proveedor from fatura_sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_documento_proveedor");
    }

    public String obterTipoMoedaDocumento()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select tipo_moeda_documento from fatura_sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getString("tipo_moeda_documento");
    }

    public double obterMontanteME()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select montante_me from fatura_sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("montante_me");
    }

    public double obterMontantePago()
        throws Exception
    {
        SQLQuery query = getModelManager().createSQLQuery("crm", "select montante_pago from fatura_sinistro where id = ?");
        query.addLong(obterId());
        return query.executeAndGetFirstRow().getDouble("montante_pago");
    }
}
