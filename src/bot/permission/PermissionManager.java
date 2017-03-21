package bot.permission;

import tsxdk.entity.EntityManager;
import api.data.TsClientDTI;

public class PermissionManager {
	private static class SingletonHolder {
		private static final PermissionManager INSTANCE = new PermissionManager();
	}
	
	PermissionManager () {
		
	}
	
//	getSharedInstance().checkHasPermission(emitter,"USE_CLI_THREAD_CMD")
	
	public static PermissionManager getSharedInstance () {
		return SingletonHolder.INSTANCE;
	}
	
	private String lastMissingPermission;
	
	/**
	 * Checks if a client has a certain permission
	 * @param clid the client-id
	 * @param permission the permission to check against
	 * @return true if the given client has got the given permission
	 */
	public boolean checkHasPermission (int clid, String permission) {
		TsClientDTI client = EntityManager.getSharedInstance().getClientList().selectClientClidEquals(clid);
		
		for (int sg : client.getServergroups()) {
			for (ServerGroupPermissions perm : ServerGroupPermissions.values()) {
				if (perm.getSgid() == sg) {
					for (String current : perm.getEntries()) {
						if (current.equals(permission) || perm.equals(ServerGroupPermissions.PERM_I_AM_ROOT))
							return true;
					}
				}
			}
		}

		lastMissingPermission = permission;
		return false;
	}

	public String getLastMissingPermission() {
		return lastMissingPermission;
	}
}
