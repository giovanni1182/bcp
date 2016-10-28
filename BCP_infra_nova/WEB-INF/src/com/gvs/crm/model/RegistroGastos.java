// FrontEnd Plus GUI for JAD
// DeCompiled : RegistroGastos.class

package com.gvs.crm.model;

import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Evento, Entidade, AuxiliarSeguro, Conta, 
//            Aseguradora, Apolice, Sinistro

public interface RegistroGastos
    extends Evento
{

    public abstract void atribuirDataSinistro(Date date)
        throws Exception;

    public abstract void atribuirAuxiliarSeguro(Entidade entidade)
        throws Exception;

    public abstract void atribuirNomeTerceiro(String s)
        throws Exception;

    public abstract void atribuirAbonoGs(double d)
        throws Exception;

    public abstract void atribuirTipoMoedaAbonoGs(String s)
        throws Exception;

    public abstract void atribuirAbonoMe(double d)
        throws Exception;

    public abstract void atribuirDataPagamento(Date date)
        throws Exception;

    public abstract void atribuirNumeroCheque(String s)
        throws Exception;

    public abstract void atribuirBanco(Entidade entidade)
        throws Exception;

    public abstract void atribuirSituacaoSinistro(String s)
        throws Exception;

    public abstract void atribuirSituacaoPagamento(String s)
        throws Exception;

    public abstract void atribuirTipoInstrumento(String s)
        throws Exception;

    public abstract void atribuirNumeroEndoso(double d)
        throws Exception;

    public abstract void atribuirCertificado(double d)
        throws Exception;

    public abstract void atualizarDataSinistro(Date date)
        throws Exception;

    public abstract void atualizarAuxiliarSeguro(AuxiliarSeguro auxiliarseguro)
        throws Exception;

    public abstract void atualizarNomeTerceiro(String s)
        throws Exception;

    public abstract void atualizarAbonoGs(double d)
        throws Exception;

    public abstract void atualizarTipoMoedaAbonoGs(String s)
        throws Exception;

    public abstract void atualizarAbonoMe(double d)
        throws Exception;

    public abstract void atualizarDataPagamento(Date date)
        throws Exception;

    public abstract void atualizarNumeroCheque(String s)
        throws Exception;

    public abstract void atualizarBanco(Conta conta)
        throws Exception;

    public abstract void atualizarSituacaoSinistro(String s)
        throws Exception;

    public abstract void atualizarSituacaoPagamento(String s)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract Date obterDataSinistro()
        throws Exception;

    public abstract Entidade obterAuxiliarSeguro()
        throws Exception;

    public abstract String obterNomeTerceiro()
        throws Exception;

    public abstract double obterAbonoGs()
        throws Exception;

    public abstract String obterTipoMoedaAbonoGs()
        throws Exception;

    public abstract double obterAbonoMe()
        throws Exception;

    public abstract Date obterDataPagamento()
        throws Exception;

    public abstract String obterNumeroCheque()
        throws Exception;

    public abstract Entidade obterBanco()
        throws Exception;

    public abstract String obterSituacaoSinistro()
        throws Exception;

    public abstract String obterSituacaoPagamento()
        throws Exception;

    public abstract String obterTipoInstrumento()
        throws Exception;

    public abstract double obterNumeroEndoso()
        throws Exception;

    public abstract double obterCertificado()
        throws Exception;

    public abstract void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, Sinistro sinistro, Date date, double d, double d1, String s)
        throws Exception;
}
