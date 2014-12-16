package ClientDatabaseServices;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class UserDb {

	// private String url = "jdbc:mysql://localhost:3306/";
	// private String dbName = "localdatabase";
	// private String driver = "com.mysql.jdbc.Driver";
	// private String userName = "root";
	// private String password = "t02h1844";

	private String ipAdress = "localhost";
	private String port = "3306";
	private String url = "jdbc:mysql://" + ipAdress + ":" + port + "/";
	private String dbName = "localdatabase";
	private String driver = "com.mysql.jdbc.Driver";
	private String userName = "root";
	private String password = "7980066";
	private Connection conn;
	private Statement st;
	private ResultSet res;
	private PreparedStatement ps;
	private String user_ssn;
	private boolean printMsg = true;

	public UserDb() {
		connect();
		// this.user_ssn = user_ssn;
//		testConnection();

	}

	private void testConnection() {
		try {
			// System.out.println(isAuthorized("9310101337", "sw0rdf1sh"));
			// toggleDevice(3, 1);
			// insertDeviceHistory("9310101337", 3, 1);
			// getAllAllowedDevices();
			insertLogInHistory("9310101337", true);

			// getDevicePin(3);
			// checkDevice(3);
			// checkAllDevices();
			// testDevice(3);
		} catch (Exception e) {
			System.out.print("Error in testing connection");
		} finally {
			if (printMsg) {
				System.out.println("Test Connection Succesful");
			}
		}
	}

	public void connect() {
		if (printMsg) {
			System.out.print("Trying to connect to database . . . ");
		}
		try {

			Class.forName(driver).newInstance();
			conn = DriverManager
					.getConnection(url + dbName, userName, password);
			st = conn.createStatement();
			if (printMsg) {
				System.out.println("Connected!!!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void disconnect() {
		try {
			conn.close();
			st.close();
			if (printMsg) {
				System.out.println("Disconnected from database.");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * QUERY + UPDATE created by Mathias
	 */
	public ArrayList toggleDevice(int deviceID, int state) {
		ArrayList toggleList = new ArrayList();
		String query = "SELECT permission.isAllowed\n" //
				+ "FROM permission \n" //
				+ "WHERE permission.userSSN = '9310101337' \n" // Add userId
				+ "AND permission.deviceId = " + deviceID + ";";//
		String update = "UPDATE device SET device.state= ? "
				+ " WHERE device.Id = ? ;";
		if (printMsg) {
			System.out.println("******QUERY****** \n" + query
					+ "\n******************");
			System.out.println("******UPDATE****** \n" + update
					+ "\n******************");
		}
		try {
			res = st.executeQuery(query);
			res.next();
			boolean isAllowed = res.getBoolean("isAllowed");
			if (isAllowed == true) {
				ps = conn.prepareStatement(update);
				ps.setInt(1, state);
				ps.setInt(2, deviceID);
				ps.executeUpdate();
				res = st.executeQuery("SELECT device.name, device.state \n"
						+ "FROM device " + "WHERE device.id = " + deviceID
						+ ";");
				res.next();
				toggleList.add(res.getString("device.name"));
				toggleList.add(res.getInt("device.state"));
				insertDeviceHistory("9310101337", deviceID, state);
			}
			if (printMsg) {
				System.out.println("Update Succesful");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
		return toggleList;
	}

	/*
	 * QUERY created by Mathias
	 */
	public String getDeviceAbr(int deviceID) {
		String query = "SELECT device.abbreviation \n" + "FROM device \n"
				+ "WHERE device.id = " + deviceID + ";";
		if (printMsg) {
			System.out.println("******UPDATE****** \n" + query
					+ "\n*****************");
		}
		try {
			res = st.executeQuery(query);// //////////
			res.next();
			if (printMsg) {
				System.out.println("Query Succesful");
			}
			return res.getString("device.abbreviation");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print(e);
		}
		return "";

	}

	/*
	 * QUERY created by Mathias
	 */
	public ArrayList getRooms() {
		ArrayList roomInfo = new ArrayList<String>();
		String query = "SELECT room.id, room.name \n"//
				+ "FROM room;";
		if (printMsg) {
			System.out.println("******UPDATE****** \n" + query
					+ "\n*****************");
		}
		try {
			res = st.executeQuery(query);
			if (printMsg) {
				System.out.println("Query Succesful");
			}
			while (res.next()) {
				roomInfo.add(String.valueOf(res.getInt("room.id")));
				roomInfo.add(res.getString("room.name"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print(e);
		}
		return roomInfo;

	}

	// /*
	// * QUERY
	// * created by Mathias
	// */
	// public int getDevicePin(int deviceID) {
	// String query = "SELECT device.pin \n" + "FROM device \n"
	// + "WHERE device.id = " + deviceID + ";";
	// if (printMsg) {
	// System.out.println("******UPDATE****** \n" + query
	// + "\n*****************");
	// }
	// try {
	// res = st.executeQuery(query);// //////////
	// res.next();
	// if (printMsg) {
	// System.out.println("Query Succesful");
	// }
	// return res.getInt("device.pin");
	// } catch (Exception e) {
	// e.printStackTrace();
	// System.out.print(e);
	// }
	// return -1;
	// }

	/*
	 * QUERY created by Mathias
	 */
	public ArrayList getDevicesInRoom(int roomId) {
		ArrayList<Object> returnArray = new ArrayList<Object>();
		String query = "SELECT device.name, device.state, image.path \n"
				+ "FROM room, device, permission, image\n"
				+ "WHERE device.id = permission.deviceId\n"
				+ "AND permission.userSSN = '9310101337'\n" // later User.ssn
				+ "AND image.id = device.imageId\n"
				+ "AND room.id =" + roomId + "\n"
				+ "AND permission.isAllowed = true" + ";";
		if (printMsg) {
			System.out.println("******UPDATE****** \n" + query
					+ "\n*****************");
		}
		try {
			res = st.executeQuery(query);
			if (printMsg) {
				System.out.println("Query Succesful");
			}
			while (res.next()) {
				if (printMsg) {
					System.out.println(res.getString("device.name") + " "
							+ res.getInt("device.state") + " "
							+ res.getString("image.path"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print(e);
		}
		return returnArray;
	}

	/*
	 * QUERY created by Mathias
	 */
	public ArrayList getAllCommands() {
		ArrayList<Object> returnArray = new ArrayList<Object>();
		String query = "SELECT *\n" + "FROM command;";
		if (printMsg) {
			System.out.println("******QUERY****** \n" + query
					+ "\n*****************");
		}
		try {
			res = st.executeQuery(query);
			if (printMsg) {
				System.out.println("Query Succesful");
			}
			while (res.next()) {
				if (printMsg) {
					System.out.println(res.getString("command.name") + " "
							+ res.getInt("command.priority"));
				}
				returnArray.add(res.getString("command.name"));
				returnArray.add(String.valueOf(res.getInt("command.priority")));
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print(e);
		}
		return returnArray;
	}

	/*
	 * QUERY created by Mathias
	 */
	public void getAllAllowedDevicesByRoom(int roomId) {
		String query = "SELECT device.name, device.state, device.imagePath\n" //
				+ "FROM device, permission\n" //
				+ "WHERE roomId = " + roomId + "\n" //
				+ "AND device.id = permission.id\n" //
				+ "AND permission.userSSN = '9310101337'\n" // later User.ssn
				+ "AND permission.isAllowed = true" + ";";
		if (printMsg) {
			System.out.println("******UPDATE****** \n" + query
					+ "\n*****************");
		}
		try {
			res = st.executeQuery(query);
			if (printMsg) {
				System.out.println("Query Succesful");
			}
			while (res.next()) {
				if (printMsg) {
					System.out.println(res.getString("device.name") + " "
							+ res.getInt("device.state") + " "
							+ res.getString("device.imagePath"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.print(e);
		}
	}

	/*
	 * QUERY created by Mathias
	 */
	public boolean isAuthorized(String userSSN, String password) {
		String query = "SELECT user.SSN, user.password\n" + "FROM user\n"
				+ "WHERE user.SSN = '" + userSSN + "' AND user.password = '"
				+ password + "';";
		if (printMsg) {
			System.out.println("******UPDATE****** \n" + query
					+ "\n*****************");
		}
		try {
			res = st.executeQuery(query);
			String s = "";
			while (res.next()) {
				s = res.getString("userSSN");
			}
			if (printMsg) {
				System.out.println("Query Succesful");
			}
			if (s != "") {
				return true;
			}
		} catch (Exception e) {
			System.out.print(e);
		}
		return false;
	}

	/*
	 * QUERY created by Mathias
	 */
	public ArrayList getPriority(String command) {
		ArrayList<Integer> returnArray = new ArrayList<Integer>();
		String query = "SELECT command.priority\n" + "FROM command\n"
				+ "WHERE command.name = '" + command + "';";
		if (printMsg) {
			System.out.println("******QUERY****** \n" + query
					+ "\n*****************");
		}
		try {
			res = st.executeQuery(query);
			while (res.next()) {
				returnArray.add(res.getInt("command.priority"));
			}
			if (printMsg) {
				System.out.println("Query Succesful");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
		return returnArray;
	}

	/*
	 * INSERT created by Mathias
	 */
	public void insertLogInHistory(String userSSN, boolean state) {
		String insert = "INSERT INTO logHistory(userSSN,state, ip, timestamp)\n"
				+ "VALUES( ? , ? , ? , null);";
		if (printMsg) {
			System.out.println("******INSERT****** \n" + insert
					+ "\n*****************");
		}
		try {
			PreparedStatement ps = conn.prepareStatement(insert);
			ps.setString(1, userSSN);
			if (state == true) {
				ps.setString(2, "Logged in");
			} else {
				ps.setString(2, "Logged out");

			}
			ps.setString(3, "127.0.0.1"); // /////////////// EDIT IP LATER
			ps.executeUpdate();
			if (printMsg) {
				System.out.println("Insert Succesful");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	/*
	 * INSERT created by Mathias
	 */
	public void insertDeviceHistory(String userSSN, int deviceId, int state) {
		String insert = "INSERT INTO deviceHistory(id,userSSN, deviceId ,state,  timestamp)\n"
				+ "VALUES( null , ? , ? , ? , null);";
		if (printMsg) {
			System.out.println("******INSERT****** \n" + insert
					+ "\n*****************");
		}
		try {
			PreparedStatement ps = conn.prepareStatement(insert);
			ps.setString(1, userSSN); // ////////////////////
			ps.setInt(2, deviceId);
			if (state == 0) {
				ps.setString(3, "DEACTIVATED");
			} else if (state == 1) {
				ps.setString(3, "ACTIVATED");
			} else {
				ps.setString(3, "CHANGED");
			}
			ps.executeUpdate();
			if (printMsg) {
				System.out.println("Insert Succesful");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	// FOR MAKING OF NEW QUERIES/UPDATES/INSERTS
	public void defaultQuery() {
		String query = "SELECT roomName " + "From rooms";
		if (printMsg) {
			System.out.println("******QUERY****** \n" + query
					+ "\n*****************");
		}
		try {
			res = st.executeQuery(query);
			while (res.next()) {
				System.out.println(res.getString("roomName"));
			}
			if (printMsg) {
				System.out.println("Query Succesful");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void defaultUpdate() {
		String update = "UPDATE device\n" + "SET device.state = ? \n"
				+ "WHERE device.id = ?";
		try {

			ps = conn.prepareStatement(update);
			ps.setInt(1, 1);
			ps.setInt(2, 32);
			if (printMsg) {
				System.out.println("******UPDATE****** \n" + update
						+ "\n*****************");
			}
			ps.executeUpdate();
			if (printMsg) {
				System.out.println("Update Succesful");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public void defaultInsert() {
		String insert = "INSERT INTO course(tablename1,tablename2,tablename3)\n"
				+ "VALUES( ? , ? , ? );";
		if (printMsg) {
			System.out.println("******INSERT****** \n" + insert
					+ "\n*****************");
		}
		try {
			ps = conn.prepareStatement(insert);
			ps.setString(1, "Test");
			ps.setString(2, "Test2");
			ps.setString(3, "Test3");
			ps.executeUpdate();
			if (printMsg) {
				System.out.println("Insert Succesful");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
	}

	public ArrayList testDevice(int deviceID) {
		ArrayList<String> returnArray = new ArrayList<String>();
		String query = "SELECT device.name, device.state\n" + "FROM device\n"
				+ "WHERE device.id = " + deviceID + ";";
		String updateOn = "UPDATE device\n" + "SET device.state = true " + "\n"
				+ "WHERE device.id = " + deviceID;
		String updateOff = "UPDATE device\n" + "SET device.state = false "
				+ "\n" + "WHERE device.id = " + deviceID;
		try {

			res = st.executeQuery(query);
			res.next();
			// boolean isAllowed = res.getBoolean("isAllowed");
			String s = res.getString("device.name");
			returnArray.add(s);
			st.executeUpdate(updateOn);
			Thread.sleep(100);
			st.executeUpdate(updateOff);
			returnArray.add("true");
		} catch (Exception e) {
			returnArray.add("false");
			e.printStackTrace();
		}
		return returnArray;
	}

	public ArrayList checkDevice(int deviceID) {

		ArrayList checkList = new ArrayList();
		try {
			res = st.executeQuery("SELECT device.name,device.state FROM device WHERE device.id="
					+ deviceID + ";");
			res.next();
			checkList.add(res.getString("device.name"));
			checkList.add(res.getBoolean("device.state"));
			if (printMsg) {
				System.out.println("Update Succesful");
			}
		} catch (Exception e) {
			System.out.print(e);
		}
		return checkList;
	}

	public ArrayList checkAllDevices() {
		ArrayList checkAllList = new ArrayList();
		try {
			checkAllList.add("checkAllDevices");
			res = st.executeQuery("SELECT device.name, device.state FROM device WHERE device.id>0");
			while (res.next()) {
				checkAllList.add(res.getString("device.name"));
				checkAllList.add(Boolean.toString(res
						.getBoolean("device.state")));
			}
			if (printMsg) {
				System.out.println("Update Succesful");
			}
		} catch (Exception e) {
			System.out.print(e);
			checkAllList = null;
		}
		return checkAllList;
	}

}

// public void setSessionKey(String byteArray) {
//
// String update = "UPDATE user\n" + "SET user.sessionKey = " + byteArray
// + "\n" + "WHERE user.SSN = '" + user_ssn + "';";
// if (printMsg) {
// System.out.println("******UPDATE****** \n" + update
// + "\n*****************");
// }
// try {
// st.executeUpdate(update);
// if (printMsg) {
// System.out.println("Update Succesful");
// }
// } catch (Exception e) {
// System.out.print(e);
// }
// }
//
// public ArrayList getSessionKey() {
//
// ArrayList<String> returnArray = new ArrayList<String>();
// String query = "SELECT user.sessionKey\n" + "From user\n"
// + "Where user.SSN = '" + user_ssn + "';";
// if (printMsg) {
// System.out.println("******QUERY****** \n" + query
// + "\n*****************");
// }
// try {
// res = st.executeQuery(query);
// while (res.next()) {
// returnArray.add(res.getString("user.sessionKey"));
// }
// if (printMsg) {
// System.out.println("Query Succesful");
// }
// } catch (Exception e) {
// System.out.print(e);
// }
// return returnArray;
// }