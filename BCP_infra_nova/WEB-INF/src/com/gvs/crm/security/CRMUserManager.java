package com.gvs.crm.security;

import com.gvs.crm.model.CRMModelManager;
import com.gvs.crm.model.Entidade;
import com.gvs.crm.model.EntidadeHome;

import infra.security.AuthenticationException;
import infra.security.User;
import infra.security.UserManager;
import infra.sql.SQLQuery;
import infra.sql.SQLRow;
import infra.sql.SQLUpdate;

public class CRMUserManager extends UserManager {
	public User authenticateUser(String key, String password) throws Exception {
		if (key.equals(""))
			throw new AuthenticationException("La información del Usuario debe ser digitada");
		
		/*if(key.toLowerCase().indexOf("e70") > -1)
		{
			try
			{
				String resu = "";
				String roles = "";
				
				//SQLQuery query = new SQLQuery(null, "crm","EXEC p_seg_check_usr ?, ?, @resu OUTPUT, @roles OUTPUT");
				SQLQuery query = new SQLQuery(null, "crm","EXEC p_seg_check_usr ?, ?, ?, ?");
		    	query.addString(key);
		    	query.addString(password);
		    	query.addString(resu);
		    	query.addString(roles);
		    	
		    	String result = query.executeAndGetFirstRow().getString("auth");
		    	if(result.toLowerCase().equals("n"))
		    		throw new AuthenticationException("Usuario o Contraseña incorrecta");
		    	else
		    	{
		    		query = new SQLQuery(null, "crm","select id from usuario where chave = ?");
		    		query.addString(key);
		    		
		    		long id = query.executeAndGetFirstRow().getLong("id"); 
		    		
		    		CRMModelManager mm = new CRMModelManager(null);
	    			EntidadeHome home = (EntidadeHome) mm.getHome("EntidadeHome");
	    			
		    		if(id == 0)
		    		{
		    			Entidade departamento = home.obterEntidadePorApelido("Usuarios Externos");
		    			
		    			query = new SQLQuery(null, "crm","select max(id) as mx from entidade");
			    		
		    			long novoId = query.executeAndGetFirstRow().getLong("mx")+1;
		    			
		    			SQLUpdate insert = new SQLUpdate(null, "crm", "insert into entidade(id,nome,classe,responsavel,superior) values(?,?,'Usuario',?,?)");
		    			insert.addLong(novoId);
		    			insert.addString(key);
		    			insert.addLong(1);
		    			insert.addLong(departamento.obterId());
		    			insert.execute();
	
		    			String sql = "INSERT INTO usuario(id,chave,visivel,nivel,senhaC) values("+novoId+", '"+key+"', 0, 'Nivel 1', EncryptByKey(Key_GUID('sk_usuarios'), CONVERT(VARBINARY, '"+password+"')))";
		    			
		    			insert = new SQLUpdate(null, "crm", "Open Symmetric key sk_usuarios decryption by certificate usuarios;"+sql);
		    			insert.execute();
		    			
		    			insert = new SQLUpdate(null, "crm", "insert into opcoes(opcao,usuario) values(105,"+novoId+")");
		    			insert.execute();
		    			
		    			insert = new SQLUpdate(null, "crm", "insert into opcoes(opcao,usuario) values(106,"+novoId+")");
		    			insert.execute();
		    		}
		    		else
		    		{
		    			String sql = "update usuario set senhaC=encryptbykey(key_guid('sk_usuarios'),'"+password+"') where id=" + id;
		    			
		    			SQLUpdate insert = new SQLUpdate(null, "crm", "Open Symmetric key sk_usuarios decryption by certificate usuarios;"+sql);
		    			insert.execute();
		    		}
		    	}
			}
	    	catch (Exception e) 
	    	{
	    		throw new AuthenticationException(e.getMessage());
			}
		}
		else
		{*/
			//SQLQuery query = new SQLQuery(null, "crm","select senha2 from usuario where chave=? and visivel = 0");
			SQLQuery query = new SQLQuery(null, "crm","Open Symmetric key sk_usuarios decryption by certificate usuarios select convert(varchar, DECRYPTBYKEY(senhaC)) as senha from usuario where chave=? and visivel = 0");
			query.addString(key);
			SQLRow[] rows = query.execute();
			if (rows.length == 0)
				throw new AuthenticationException("Usuario o Contraseña incorrecta");
			if (rows.length > 1)
				throw new AuthenticationException("Existe mas de un Usuario con la clave " + key);
			if (!rows[0].getString("senha").equals(password))
				throw new AuthenticationException("Usuario o Contraseña incorrecta");
		//}

		CRMUser user = new CRMUser(key);

		/*CRMModelManager mm = new CRMModelManager(user);
		UsuarioHome usuarioHome = (UsuarioHome) mm.getHome("UsuarioHome");
		Usuario usuarioAtual = (Usuario) usuarioHome.obterUsuarioPorUser(user);
		EntidadeHome entidadeHome = (EntidadeHome) mm.getHome("EntidadeHome");

		mm.beginTransaction();
		
		usuarioAtual.atualizarLogPendente();

		Log log = (Log) mm.getEntity("Log");

		Entidade bcp = (Entidade) entidadeHome.obterEntidadePorApelido("bcp");
		Entidade administrador = (Entidade) entidadeHome.obterEntidadePorApelido("admin");
		
		log.atribuirOrigem(usuarioAtual);
		log.atribuirDestino(bcp);
		log.atribuirResponsavel(administrador);
		log.atribuirDataPrevistaInicio(new Date());
		log.atribuirTitulo("Log de Entrada/Salida: "+ new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())+ "\n");
		log.atribuirTipo("Entrada/Salida");
		log.incluir();
		
		mm.commitTransaction();*/

		return user;
	}

	public User getUser(Object credential) throws Exception {
		if (credential instanceof CRMUser) {
			return (User) credential;
		} else {
			return null;
		}
	}

	public boolean isActionEnabledToUser(String actionName, User user)
			throws Exception {
		if (actionName.equals("_logon"))
			return true;

		else
			return user != null;
	}

	public boolean isActionVisibleToUser(String actionName, User user)
			throws Exception {
		return this.isActionEnabledToUser(actionName, user);
	}

	public boolean isInternalLogon() throws Exception {
		return true;
	}

	public boolean needAuthentication(String arg1, User user) {
		return (user == null || user.isAnonymous());

	}
}