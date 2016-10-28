package infra.model.persistence;

import infra.model.ModelManager;

public abstract class PersistentModelManager extends ModelManager {
	public abstract String getDatabaseResourceName();
}