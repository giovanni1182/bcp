// FrontEnd Plus GUI for JAD
// DeCompiled : FaturaSinistro.class

package com.gvs.crm.model;

import java.util.Date;

// Referenced classes of package com.gio.crm.model:
//            Evento, Aseguradora, Apolice, Sinistro

public interface FaturaSinistro
    extends Evento
{

    public abstract void atribuirDataSinistro(Date date)
        throws Exception;

    public abstract void atribuirNumeroDocumento(String s)
        throws Exception;

    public abstract void atribuirNumeroFatura(String s)
        throws Exception;

    public abstract void atribuirRucProvedor(String s)
        throws Exception;

    public abstract void atribuirNomeProvedor(String s)
        throws Exception;

    public abstract void atribuirDataDocumento(Date date)
        throws Exception;

    public abstract void atribuirMontanteDocumento(double d)
        throws Exception;

    public abstract void atribuirDataPagamento(Date date)
        throws Exception;

    public abstract void atribuirSituacaoFatura(String s)
        throws Exception;

    public abstract void atribuirTipoInstrumento(String s)
        throws Exception;

    public abstract void atribuirNumeroEndoso(double d)
        throws Exception;

    public abstract void atribuirCertificado(double d)
        throws Exception;

    public abstract void atribuirTipoDocumentoProveedor(String s)
        throws Exception;

    public abstract void atribuirTipoMoedaDocumento(String s)
        throws Exception;

    public abstract void atribuirMontanteME(double d)
        throws Exception;

    public abstract void atribuirMontantePago(double d)
        throws Exception;

    public abstract void atualizarDataSinistro(Date date)
        throws Exception;

    public abstract void atualizarNumeroDocumento(String s)
        throws Exception;

    public abstract void atualizarNumeroFatura(String s)
        throws Exception;

    public abstract void atualizarRucProvedor(String s)
        throws Exception;

    public abstract void atualizarNomeProvedor(String s)
        throws Exception;

    public abstract void atualizarDataDocumento(Date date)
        throws Exception;

    public abstract void atualizarMontanteDocumento(double d)
        throws Exception;

    public abstract void atualizarDataPagamento(Date date)
        throws Exception;

    public abstract void atualizarSituacaoFatura(String s)
        throws Exception;

    public abstract void incluir()
        throws Exception;

    public abstract Date obterDataSinistro()
        throws Exception;

    public abstract String obterNumeroDocumento()
        throws Exception;

    public abstract String obterNumeroFatura()
        throws Exception;

    public abstract String obterRucProvedor()
        throws Exception;

    public abstract String obterNomeProvedor()
        throws Exception;

    public abstract Date obterDataDocumento()
        throws Exception;

    public abstract double obterMontanteDocumento()
        throws Exception;

    public abstract Date obterDataPagamento()
        throws Exception;

    public abstract String obterSituacaoFatura()
        throws Exception;

    public abstract String obterTipoInstrumento()
        throws Exception;

    public abstract double obterNumeroEndoso()
        throws Exception;

    public abstract double obterCertificado()
        throws Exception;

    public abstract String obterTipoDocumentoProveedor()
        throws Exception;

    public abstract String obterTipoMoedaDocumento()
        throws Exception;

    public abstract double obterMontanteME()
        throws Exception;

    public abstract double obterMontantePago()
        throws Exception;

    public abstract void verificarDuplicidade(Aseguradora aseguradora, Apolice apolice, Sinistro sinistro, String s, String s1, String s2, double d, Date date, double d1, String s3, Date date1)
        throws Exception;
}
