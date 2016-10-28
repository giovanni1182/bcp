package com.gvs.crm.model.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;

import org.joda.time.DateTime;
import org.joda.time.Seconds;

import com.gvs.crm.model.Aseguradora;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.Evento;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Log;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class UsuarioImpl extends EntidadeImpl implements Usuario {
	private String chave;

	private Boolean possuiSenha;

	private double alcada;

	public void atribuirAlcada(double alcada) throws Exception {
		this.alcada = alcada;
	}

	public void atribuirChave(String chave) throws Exception {
		this.chave = chave;
	}

	public void atualizar() throws Exception {
		super.atualizar();
		if (this.chave != null) {
			UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager()
					.getHome("UsuarioHome");
			Usuario usuario = usuarioHome.obterUsuarioPorChave(this.chave);
			if (usuario != null && usuario.obterId() != this.obterId())
				throw new Exception("A chave " + this.chave
						+ " já está sendo utilizada pelo usuário "
						+ usuario.obterNome());
			SQLUpdate update = this.getModelManager().createSQLUpdate(
					"update usuario set chave=? where id=?");
			update.addString(this.chave);
			update.addLong(this.obterId());
			update.execute();
		}
	}

	public void atualizarAlcada(double alcada) throws Exception {
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"update usuario set alcada=? where id=?");
		update.addDouble(alcada);
		update.addLong(this.obterId());
		update.execute();
	}

	public void atualizarSenha(String senhaAtual, String novaSenha1, String novaSenha2) throws Exception
	{
		if (!this.verificarSenha(senhaAtual))
			throw new Exception("A senha atual não confere");
		if (!novaSenha1.equals(novaSenha2))
			throw new Exception("As duas novas senhas não conferem");
		/*SQLUpdate update = this.getModelManager().createSQLUpdate("update usuario set senha=? where id=?");
		update.addString(novaSenha1);
		update.addLong(this.obterId());*/
		
		SQLUpdate update = this.getModelManager().createSQLUpdate("update usuario set senhaC=encryptbykey(key_guid('sk_usuarios'),'"+novaSenha1+"') where id=" + this.obterId());
		
		update.execute();
	}

	public void converterEmPessoa() throws Exception {
		this.atualizarClasse("Pessoa");
		SQLUpdate update = this.getModelManager().createSQLUpdate(
				"delete from usuario where id=?");
		update.addLong(this.obterId());
		update.execute();
	}

	public void excluir() throws Exception {
		super.excluir();
		SQLUpdate update1 = this.getModelManager().createSQLUpdate(
				"delete from usuario where id=?");
		update1.addLong(this.obterId());
		update1.execute();
		//		SQLUpdate update2 = this.getModelManager().createSQLUpdate("delete
		// from grupo where chave=?");
		//		update2.addString(this.obterChave());
		//		update2.execute();
	}

	public void incluir() throws Exception
	{
		super.incluir();
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome("UsuarioHome");
		Usuario usuario = usuarioHome.obterUsuarioPorChave(this.chave);
		if (usuario != null && usuario.obterId() != this.obterId())
			throw new Exception("A chave '" + this.chave + "' já está sendo utilizada pelo usuário '"	+ usuario.obterNome() + "'");
		
		/*SQLUpdate update1 = this.getModelManager().createSQLUpdate("insert into usuario (id, chave, senha,visivel) values (?, ?, '',?)");
		update1.addLong(this.obterId());
		update1.addString(this.chave);
		update1.addInt(0);*/
		
		SQLUpdate update1 = this.getModelManager().createSQLUpdate("Open Symmetric key sk_usuarios decryption by certificate usuarios; INSERT INTO usuario(id,chave,visivel,senhaC) values("+this.obterId()+", '"+this.chave+"', 0, EncryptByKey(Key_GUID('sk_usuarios'), CONVERT(VARBINARY, 'bcp2013')))");
		update1.execute();
	}

	public double obterAlcada() throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm",
				"select alcada from usuario where id=?");
		query.addLong(this.obterId());
		this.alcada = query.executeAndGetFirstRow().getDouble("alcada");

		return this.alcada;
	}

	public String obterChave() throws Exception {
		if (this.chave == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select chave from usuario where id=?");
			query.addLong(this.obterId());
			String chave = query.executeAndGetFirstRow().getString("chave");
			if (chave != null)
				this.chave = chave.toLowerCase();
		}
		return this.chave;
	}
	
	public String obterSenha() throws Exception
	{
		//SQLQuery query = this.getModelManager().createSQLQuery("crm", "select senha from usuario where id=?");
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "Open Symmetric key sk_usuarios decryption by certificate usuarios select convert(varchar, DECRYPTBYKEY(senhaC)) as senha from usuario where id=?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("senha");
	}

	public Collection<Evento> obterCompromissos(Date data) throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterCompromissos(this, data);
	}

	public Collection obterEntidadeDeResponsabilidade() throws Exception {
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome(
				"EntidadeHome");
		return home.obterEntidadesPorResponsavel(this);
	}

	public Collection obterEventosCriados() throws Exception {
		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return home.obterEventosPorCriador(this);
	}

	public Collection obterEventosCriadosHistorico(int pagina) throws Exception {
		EventoHome home = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return home.obterEventosPorCriadorHistorico(this, pagina);
	}

	public Collection obterEventosPendentes() throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterPendencias(this);
	}

	public Collection obterTarefasAtrasadas(Date dataLimite) throws Exception {
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
				.getModelManager().getUser());
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterTarefasAtrasadas(usuarioAtual, dataLimite);
	}

	public Collection obterTarefasPendentes() throws Exception {
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
				.getModelManager().getUser());
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterTarefasPendentes(usuarioAtual);
	}

	public Collection obterTarefasPendentes(Date inicio, Date termino)
			throws Exception {
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
				.getModelManager().getUser());
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterTarefasPendentes(usuarioAtual, inicio, termino);
	}

	public boolean permiteAtualizarSenha() throws Exception {
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
				.getModelManager().getUser());
		return usuarioAtual.obterId() == this.obterId();
	}

	public boolean permiteConverterParaPessoa() throws Exception {
		UsuarioHome usuarioHome = (UsuarioHome) this.getModelManager().getHome(
				"UsuarioHome");
		Usuario usuarioAtual = usuarioHome.obterUsuarioPorUser(this
				.getModelManager().getUser());
		return usuarioAtual.obterId() == this.obterResponsavel().obterId()
				&& this.obterId() != usuarioAtual.obterId();
	}

	//public boolean permiteExcluir() throws Exception {
		//return super.permiteExcluir() && !this.possuiResponsabilidades();
	//}

	public boolean possuiResponsabilidades() throws Exception {
		UsuarioHomeImpl home = (UsuarioHomeImpl) this.getModelManager()
				.getHome("UsuarioHome");
		return home.possuiResponsabilidades(this);
	}

	public boolean possuiSenha() throws Exception {
		if (this.possuiSenha == null) {
			SQLQuery query = this.getModelManager().createSQLQuery("crm",
					"select senha from usuario where id=?");
			query.addLong(this.obterId());
			String senha = query.executeAndGetFirstRow().getString("senha");
			this.possuiSenha = new Boolean(senha != null && !senha.equals(""));
		}
		return this.possuiSenha.booleanValue();
	}

	public boolean verificarSenha(String senha) throws Exception
	{
		/*SQLQuery query = this.getModelManager().createSQLQuery("crm", "select senha from usuario where id=?");
		query.addLong(this.obterId());*/
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm", "Open Symmetric key sk_usuarios decryption by certificate usuarios select convert(varchar, DECRYPTBYKEY(senhaC)) as senha from usuario where id=?");
		query.addLong(this.obterId());
		
		String s = query.executeAndGetFirstRow().getString("senha");
		return senha.equals(s);
	}

	public Collection verificarAgendas() throws Exception {
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome(
				"EventoHome");
		return eventoHome.obterAgendas(true);
	}

	public Collection obterEventosVinculador() throws Exception
	{
		EventoHome eventoHome = (EventoHome) this.getModelManager().getHome("EventoHome");
		Collection eventos = new ArrayList();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select TOP 5 id from evento where origem = ? or criador = ? or responsavel = ?");
		query.addLong(this.obterId());
		query.addLong(this.obterId());
		query.addLong(this.obterId());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Evento e = eventoHome.obterEventoPorId(id);
			
			eventos.add(e);
		}
		
		
		return eventos;
	}

	public boolean estaVisivel() throws Exception
	{
		SQLQuery query =  this.getModelManager().createSQLQuery("crm","select visivel from usuario where id = ?");
		query.addLong(this.obterId());
		
		int visivel = query.executeAndGetFirstRow().getInt("visivel");
		
		if(visivel == 0)
			return true;
		else
			return false;
	}

	public void excluirLogicamente() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select id from usuario where id = ?");
		query.addLong(this.obterId());
		
		if(query.execute().length == 0)
		{
			SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into usuario values(?,?,?,?,?,?)");
			insert.addLong(this.obterId());
			insert.addString("Excluido");
			insert.addString("Excluido");
			insert.addDouble(0);
			insert.addInt(1);
			insert.addString("Nivel 1");
			
			insert.execute();
		}
		else
		{
			SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update usuario set visivel = 1 where id = ?");
			update.addLong(this.obterId());
			
			update.execute();
		}
	}

	public void atualizarNivel(String nivel) throws Exception
	{
		SQLUpdate update = this.getModelManager().createSQLUpdate("crm","update usuario set nivel = ? where id = ?");
		update.addString(nivel);
		update.addLong(this.obterId());
		
		update.execute();
	}

	public String obterNivel() throws Exception
	{
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select nivel from usuario where id = ?");
		query.addLong(this.obterId());
		
		return query.executeAndGetFirstRow().getString("nivel");
	}
	
	public boolean permiteExcluir() throws Exception 
	{
		Usuario usuarioAtual = this.obterUsuarioAtual();
		
		if(this.obterChave() == null && usuarioAtual.obterNivel().equals(Usuario.ADMINISTRADOR))
			return true;
		else
		{
			Usuario responsavel = this.obterResponsavel();
			
			/*System.out.println(this.obterId() != usuarioAtual.obterId());
			System.out.println(this.obterNumeroEntidadeInferiores() == 0);
			System.out.println(usuarioAtual.equals(responsavel));*/
			
			return this.obterId() != usuarioAtual.obterId() && this.obterNumeroEntidadeInferiores() == 0 && usuarioAtual.equals(responsavel);
		}

	}
	
	public void atualizarLogPendente() throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,log,fase where evento.id = log.id and log.id = fase.id and origem = ? and codigo='pendente' and termino = 0");
		query.addLong(this.obterId());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Log log = (Log) home.obterEventoPorId(id);
			
			log.atualizarDataPrevistaConclusao(new Date());
			log.atualizarFase(Evento.EVENTO_CONCLUIDO);
		}
	}
	
	public String obterTempoConectado(Date dataInicio, Date dataFim) throws Exception
	{
		String tempo = "";
		
		this.obterInferiores();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id,data_prevista_inicio,data_prevista_conclusao from evento,log,fase where evento.id = log.id and log.id = fase.id and origem = ? and codigo='concluido' and termino = 0 and data_prevista_inicio>=? and data_prevista_conclusao<=?");
		query.addLong(this.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		int totalHoras = 0;
		int totalMinutos = 0;
		int totalSegundos = 0;
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			Date dataInicio2 = new Date(rows[i].getLong("data_prevista_inicio"));
			Date dataFim2 = new Date(rows[i].getLong("data_prevista_conclusao"));
			
			//System.out.println(new SimpleDateFormat("dd/MM/yyyy").format(dataInicio2) + " - " + new SimpleDateFormat("dd/MM/yyyy").format(dataFim2));
			
			int ano = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataInicio2));
			int mes = Integer.parseInt(new SimpleDateFormat("MM").format(dataInicio2));
			int dia = Integer.parseInt(new SimpleDateFormat("dd").format(dataInicio2));
			int hora = Integer.parseInt(new SimpleDateFormat("HH").format(dataInicio2));
			int minuto = Integer.parseInt(new SimpleDateFormat("mm").format(dataInicio2));
			int segundo = Integer.parseInt(new SimpleDateFormat("ss").format(dataInicio2));
			
			int ano2 = Integer.parseInt(new SimpleDateFormat("yyyy").format(dataFim2));
			int mes2 = Integer.parseInt(new SimpleDateFormat("MM").format(dataFim2));
			int dia2 = Integer.parseInt(new SimpleDateFormat("dd").format(dataFim2));
			int hora2 = Integer.parseInt(new SimpleDateFormat("HH").format(dataFim2));
			int minuto2 = Integer.parseInt(new SimpleDateFormat("mm").format(dataFim2));
			int segundo2 = Integer.parseInt(new SimpleDateFormat("ss").format(dataFim2));
			
			DateTime inicio2 = new DateTime(ano,mes,dia,hora,minuto,segundo,0);
			DateTime fim2 = new DateTime(ano2,mes2,dia2,hora2,minuto2,segundo2,0);
			
			int segundos = Seconds.secondsBetween(inicio2, fim2).getSeconds();
			
			int horaT = (int)(segundos / (60 * 60)); 
			int minT = (int)((segundos - (horaT * 60 * 60)) / 60);
			int segT = (int)(segundos - (horaT * 60 * 60) - (minT * 60));
			
			totalHoras+=horaT;
			totalMinutos+=minT;
			totalSegundos+= segT;
		}
		
		totalSegundos+=(totalMinutos * 60) + (totalHoras * 3600);
		
		int hora = (int)(totalSegundos / (60 * 60)); 
		int min = (int)((totalSegundos - (hora * 60 * 60)) / 60);
		int seg = (int)(totalSegundos - (hora * 60 * 60) - (min * 60));
		
		if(hora > 0 || min > 0 || seg > 0)
			tempo = hora + "Hr "+ min+"min " + seg +"sec";
		
		return tempo;
	}
	
	public static final Map<String, String> obterNiveisInspecao()
	{
		Map<String, String> niveis = new TreeMap<String, String>();
		
		niveis.put(Usuario.ADMINISTRADOR, Usuario.ADMINISTRADOR);
		niveis.put(Usuario.INTENDENTE_ICORAS, Usuario.INTENDENTE_ICORAS);
		niveis.put(Usuario.INTENDENTE_IETA, Usuario.INTENDENTE_IETA);
		niveis.put(Usuario.DIVISAO_CONTROL_AUXILIARES, Usuario.DIVISAO_CONTROL_AUXILIARES);
		niveis.put(Usuario.DIVISAO_CONTROL_REASEGUROS, Usuario.DIVISAO_CONTROL_REASEGUROS);
		niveis.put(Usuario.SUPERINTENDENTE, Usuario.SUPERINTENDENTE);
		niveis.put(Usuario.INTENDENDE_IAL, Usuario.INTENDENDE_IAL);
		niveis.put(Usuario.INTENDENTE_ICF, Usuario.INTENDENTE_ICF);
		niveis.put(Usuario.COORDENACAO_ADSCRITORIA, Usuario.COORDENACAO_ADSCRITORIA);
		niveis.put(Usuario.DIVISAO_ADMINISTRATIVA, Usuario.DIVISAO_ADMINISTRATIVA);
		niveis.put(Usuario.DIVISAO_LAVAGEM_DINHEIRO, Usuario.DIVISAO_LAVAGEM_DINHEIRO);
		niveis.put(Usuario.DIVISAO_DEFESA_USUARIO, Usuario.DIVISAO_DEFESA_USUARIO);
		niveis.put(Usuario.DIVISAO_INFORMATICA, Usuario.DIVISAO_INFORMATICA);
		niveis.put(Usuario.DIVISAO_SERVICOS_ADMIN, Usuario.DIVISAO_SERVICOS_ADMIN);
		niveis.put(Usuario.DIVISAO_PRECESSOS, Usuario.DIVISAO_PRECESSOS);
		niveis.put(Usuario.DIVISAO_ANALISE_FINANCEIRA, Usuario.DIVISAO_ANALISE_FINANCEIRA);
		niveis.put(Usuario.DIVISAO_AUDITORIA, Usuario.DIVISAO_AUDITORIA);
		niveis.put(Usuario.DIVISAO_ESTUDOS_ATUAIS, Usuario.DIVISAO_ESTUDOS_ATUAIS);
		niveis.put(Usuario.DIVISAO_ESTUDOS_TECNICOS, Usuario.DIVISAO_ESTUDOS_TECNICOS);
		
		/*niveis.put(Usuario.ANALISTA_AUXILIARES, Usuario.ANALISTA_AUXILIARES);
		niveis.put(Usuario.ANALISTA_REASEGUROS, Usuario.ANALISTA_REASEGUROS);*/
		
		
		return niveis;
	}
	
	public static final Map<String, String> obterTodosOsNiveis()
	{
		Map<String, String> niveis = new TreeMap<String, String>();
		
		niveis.put(Usuario.ADMINISTRADOR, Usuario.ADMINISTRADOR);
		niveis.put(Usuario.INTENDENTE_ICORAS, Usuario.INTENDENTE_ICORAS);
		niveis.put(Usuario.INTENDENTE_IETA, Usuario.INTENDENTE_IETA);
		niveis.put(Usuario.DIVISAO_CONTROL_AUXILIARES, Usuario.DIVISAO_CONTROL_AUXILIARES);
		niveis.put(Usuario.DIVISAO_CONTROL_REASEGUROS, Usuario.DIVISAO_CONTROL_REASEGUROS);
		niveis.put(Usuario.SUPERINTENDENTE, Usuario.SUPERINTENDENTE);
		niveis.put(Usuario.INTENDENDE_IAL, Usuario.INTENDENDE_IAL);
		niveis.put(Usuario.INTENDENTE_ICF, Usuario.INTENDENTE_ICF);
		niveis.put(Usuario.COORDENACAO_ADSCRITORIA, Usuario.COORDENACAO_ADSCRITORIA);
		niveis.put(Usuario.DIVISAO_ADMINISTRATIVA, Usuario.DIVISAO_ADMINISTRATIVA);
		niveis.put(Usuario.DIVISAO_LAVAGEM_DINHEIRO, Usuario.DIVISAO_LAVAGEM_DINHEIRO);
		niveis.put(Usuario.DIVISAO_DEFESA_USUARIO, Usuario.DIVISAO_DEFESA_USUARIO);
		niveis.put(Usuario.DIVISAO_INFORMATICA, Usuario.DIVISAO_INFORMATICA);
		niveis.put(Usuario.DIVISAO_SERVICOS_ADMIN, Usuario.DIVISAO_SERVICOS_ADMIN);
		niveis.put(Usuario.DIVISAO_PRECESSOS, Usuario.DIVISAO_PRECESSOS);
		niveis.put(Usuario.DIVISAO_ANALISE_FINANCEIRA, Usuario.DIVISAO_ANALISE_FINANCEIRA);
		niveis.put(Usuario.DIVISAO_AUDITORIA, Usuario.DIVISAO_AUDITORIA);
		niveis.put(Usuario.DIVISAO_ESTUDOS_ATUAIS, Usuario.DIVISAO_ESTUDOS_ATUAIS);
		niveis.put(Usuario.DIVISAO_ESTUDOS_TECNICOS, Usuario.DIVISAO_ESTUDOS_TECNICOS);
		niveis.put(Usuario.ANALISTA_AUXILIARES, Usuario.ANALISTA_AUXILIARES);
		niveis.put(Usuario.ANALISTA_REASEGUROS, Usuario.ANALISTA_REASEGUROS);
		niveis.put(Usuario.BANCO_DE_DADOS, Usuario.BANCO_DE_DADOS);
		
		return niveis;
	}
	
	public Map<Long,Aseguradora> obterAseguradorasResEscaneada() throws Exception
	{
		Map<Long,Aseguradora> aseguradoras = new TreeMap<Long,Aseguradora>();
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select aseguradora_id from opcao_res_escaneada where usuario_id = ?");
		query.addLong(this.obterId());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("aseguradora_id");
			
			Aseguradora aseg = (Aseguradora) entidadeHome.obterEntidadePorId(id);
			
			aseguradoras.put(aseg.obterId(),aseg);
		}
		
		return aseguradoras;
	}
	
	public void addResEscaneada(Aseguradora aseg) throws Exception
	{
		SQLUpdate insert = this.getModelManager().createSQLUpdate("crm","insert into opcao_res_escaneada(usuario_id,aseguradora_id) values(?,?)");
		insert.addLong(this.obterId());
		insert.addLong(aseg.obterId());
		
		insert.execute();
	}
}