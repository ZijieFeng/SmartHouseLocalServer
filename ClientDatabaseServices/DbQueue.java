package ClientDatabaseServices;
import java.sql.*;

public class DbQueue {
	
	public UserDb udb = null;
	public AdminDb adb = null;

	public DbQueue(String userOrAdmin) {//
		if (userOrAdmin.equals("user")) {
			System.out.println("isUser");
			udb = new UserDb();
		} else if (userOrAdmin.equals("Admin")) {
			adb = new AdminDb();
		}
	}

	public DbQueue() {
	}
}
