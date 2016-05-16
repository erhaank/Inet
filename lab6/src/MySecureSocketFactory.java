

import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.SecureRandom;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

public class MySecureSocketFactory {
	public MySecureSocketFactory() {

	}

	public SSLServerSocket setupListening(int port) throws Exception {
		// Create SecureRandom
		SecureRandom random = new SecureRandom();

		// Setup Keystores
		KeyStore clientKeyStore, serverKeyStore;
		clientKeyStore = KeyStore.getInstance("JKS");
		clientKeyStore.load(new FileInputStream("/tmp/keys/client.public"), "public".toCharArray());
		serverKeyStore = KeyStore.getInstance("JKS");
		serverKeyStore.load(new FileInputStream("/tmp/keys/server.private"), "serverpw".toCharArray());

		// Setup factories
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(clientKeyStore);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(serverKeyStore, "serverpw".toCharArray());

		// Setup SSLContext
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(),
				tmf.getTrustManagers(),
				random);
		
		SSLServerSocketFactory ssf = sslContext.getServerSocketFactory();
		SSLServerSocket serverSocket = (SSLServerSocket)ssf.createServerSocket(port);
		serverSocket.setNeedClientAuth(true);
		return serverSocket;
	}

	public SSLSocket setupConnection(String host, int port) throws Exception {
		// Create SecureRandom
		SecureRandom random = new SecureRandom();

		// Setup Keystores
		KeyStore serverKeyStore, clientKeyStore;
		serverKeyStore = KeyStore.getInstance("JKS");
		serverKeyStore.load(new FileInputStream("/tmp/keys/server.public"), "public".toCharArray());
		clientKeyStore = KeyStore.getInstance("JKS");
		clientKeyStore.load(new FileInputStream("/tmp/keys/client.private"), "clientpw".toCharArray());

		// Setup factories
		TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		tmf.init(serverKeyStore);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
		kmf.init(clientKeyStore, "clientpw".toCharArray());

		// Setup SSLContext
		SSLContext sslContext = SSLContext.getInstance("TLS");
		sslContext.init(kmf.getKeyManagers(),
				tmf.getTrustManagers(),
				random);
		
		SSLSocketFactory sf = sslContext.getSocketFactory();
		SSLSocket clientSocket = (SSLSocket)sf.createSocket(host, port);
		return clientSocket;
	}
}
