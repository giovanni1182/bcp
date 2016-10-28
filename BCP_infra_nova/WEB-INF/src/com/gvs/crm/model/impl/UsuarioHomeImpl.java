package com.gvs.crm.model.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;
import com.gvs.crm.model.EventoHome;
import com.gvs.crm.model.Log;
import com.gvs.crm.model.Usuario;
import com.gvs.crm.model.UsuarioHome;

import infra.model.Home;
import infra.security.User;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;

public class UsuarioHomeImpl extends Home implements UsuarioHome
{
	private HashMap usuariosPorChave = new HashMap();

	public Usuario obterUsuarioPorChave(String chave) throws Exception
	{
		Usuario usuario = (Usuario) this.usuariosPorChave.get(chave);
		if (usuario == null)
		{
			SQLQuery query = this.getModelManager().createSQLQuery("crm","select id from usuario where chave=?");
			query.addString(chave);
			long id = query.executeAndGetFirstRow().getLong("id");
			if (id > 0)
			{
				EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
				usuario = (Usuario) entidadeHome.obterEntidadePorId(id);
				this.usuariosPorChave.put(chave, usuario);
			}
		}

		return usuario;
	}

	public Usuario obterUsuarioPorUser(User user) throws Exception
	{
		return this.obterUsuarioPorChave(user.getName());
	}

	public Collection obterUsuarios() throws Exception {
		EntidadeHomeImpl entidadeHome = (EntidadeHomeImpl) this
				.getModelManager().getHome("EntidadeHome");
		SQLQuery query = this
				.getModelManager()
				.createSQLQuery(
						"crm",
						"select entidade.id,entidade.classe from usuario,entidade where usuario.id=entidade.id order by entidade.nome");
		return entidadeHome.instanciarEntidades(query.execute());
	}

	public boolean possuiResponsabilidades(Usuario usuario) throws Exception {
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select top 1 id from entidade where responsavel=?");
		query.addLong(usuario.obterId());
		
		return query.execute().length > 0;
		
		//SQLRow row = query.executeAndGetFirstRow();
		//int quantidade = row.getInt("id");
		//return quantidade > 0;
	}

	public Collection<Usuario> obterUsuariosCadastrados() throws Exception
	{
		Collection<Usuario> usuarios = new ArrayList<Usuario>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select entidade.id from entidade,usuario where entidade.id = usuario.id and visivel = 0 order by nome");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Usuario usuario = (Usuario) home.obterEntidadePorId(id);
			
			usuarios.add(usuario);
		}
		
		return usuarios;
	}
	
	public Collection<Usuario> obterUsuariosPorLog(Date dataInicio, Date dataFim) throws Exception
	{
		Map<String, Usuario> usuarios = new TreeMap<String,Usuario>();
		EntidadeHome home = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.origem from evento,log,fase where evento.id = log.id and log.id = fase.id and data_prevista_inicio >= ? and data_prevista_conclusao<=? and codigo='concluido' and termino = 0 group by origem");
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("origem");
			
			Usuario usuario = (Usuario) home.obterEntidadePorId(id);
			
			usuarios.put(usuario.obterNome(),usuario);
		}
		
		return usuarios.values();
	}
	
	public Collection<Log> obterLos(Usuario usuario, Date dataInicio, Date dataFim) throws Exception
	{
		EventoHome home = (EventoHome) this.getModelManager().getHome("EventoHome");
		Collection<Log> logs = new ArrayList<Log>();
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select evento.id from evento,log,fase where evento.id = log.id and log.id = fase.id and origem = ? and data_prevista_inicio >= ? and data_prevista_conclusao<=? and codigo='concluido' and termino = 0");
		query.addLong(usuario.obterId());
		query.addLong(dataInicio.getTime());
		query.addLong(dataFim.getTime());
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long id = rows[i].getLong("id");
			
			Log log = (Log) home.obterEventoPorId(id);
			
			logs.add(log);
		}
		
		return logs;
	}
	
	public Collection<Usuario> obterUsuariosDepartamento(Entidade superior) throws Exception
	{
		Collection<Usuario> usuarios = new ArrayList<Usuario>();
		
		for(Iterator<Entidade> i = superior.obterInferiores().iterator() ; i.hasNext() ; )
		{
			Entidade e = i.next();
			
			if(e.obterClasse().equals("departamento"))
				usuarios.addAll(this.obterUsuariosDepartamento(e));
			else if(e instanceof Usuario)
				usuarios.add((Usuario) e);
		}
		
		return usuarios;
	}
	
	public Collection<Usuario> obterUsuariosResEscaneada() throws Exception
	{
		Collection<Usuario> usuarios = new ArrayList<Usuario>();
		EntidadeHome entidadeHome = (EntidadeHome) this.getModelManager().getHome("EntidadeHome");
		
		SQLQuery query = this.getModelManager().createSQLQuery("crm","select usuario_id from opcao_res_escaneada group by usuario_id");
		
		SQLRow[] rows = query.execute();
		
		for(int i = 0 ; i < rows.length ; i++)
		{
			long usuarioId = rows[i].getLong("usuario_id");
			
			Usuario u = (Usuario) entidadeHome.obterEntidadePorId(usuarioId);
			
			usuarios.add(u);
		}
		
		return usuarios;
	}
}