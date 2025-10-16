import oracledb from "oracledb"
//
const user = "MALACKATHON";
const password = "Oci.2025_v4m0ssss";
// If you want to connect using your wallet, comment the following line.
const connectString = '(description= (retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=adb.eu-madrid-1.oraclecloud.com))(connect_data=(service_name=g5e089966165be0_malackathon2025_high.adb.oraclecloud.com))(security=(ssl_server_dn_match=yes)))';

const connection = await oracledb.getConnection({
	user,
	password,
	connectString
})

const result = await connection.execute("select * from VISTA_MUY_INTERESANTE")

console.log(result)

/*
* This example is for Node.js version ( 14.6 or later versions )
*
* Follow driver installation and setup instructions here:
* https://www.oracle.com/database/technologies/appdev/quickstartnodejs.html
*/

async function runApp() {
	console.log("executing runApp");
	// Replace USER_NAME, PASSWORD with your username and password
	const user = "malackathon";
	const password = "Oci.2025_v4m0ssss";
	// If you want to connect using your wallet, comment the following line.
	const connectString = '(description= (retry_count=20)(retry_delay=3)(address=(protocol=tcps)(port=1522)(host=adb.eu-madrid-1.oraclecloud.com))(connect_data=(service_name=g5e089966165be0_malackathon2025_high.adb.oraclecloud.com))(security=(ssl_server_dn_match=yes)))';
	/*
	* If you want to connect using your wallet, uncomment the following line.
	* dbname - is the TNS alias present in tnsnames.ora dbname
	*/
	// const connectString ="dbname";
	let connection;
	try {
		connection = await oracledb.getConnection({
			user,
			password,
			connectString,
			// If you want to connect using your wallet, uncomment the following lines.
			// configDir: "/Users/test/wallet_dbname/",
			// walletLocation: "/Users/test/wallet_dbname/",
			// walletPassword: "WALLET_PASSWORD"
		});
		console.log("Successfully connected to Oracle Databas");
		const result = await connection.execute("select * from VISTA_MUY_INTERESANTE");
		result.rows.map((r) => {
			console.log(r[3])
		})
		console.log(result.metaData.map(col => col.name))
		console.log(result.rows);
	} catch (err) {
		console.error(err);
	} finally {
		if (connection) {
			try {
				await connection.close();
			} catch (err) {
				console.error(err);
			}
		}
	}
}

runApp();

