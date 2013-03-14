package mina.remote.ssl;

import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.Security;
import java.security.cert.CertificateException;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BogusSslContextFactory {

	private static final Logger log = LoggerFactory.getLogger(BogusSslContextFactory.class);

	private static String serverKeys = "serverKeys.bks";
	private static String serverKeysPassword = "123456";
	private static String serverTrust = "serverTrust.bks";
	private static String serverTrustPassword = "123456";

	private static String clientKeys = "clientKeys.jks";
	private static String clientKeysPassword = "123456";
	private static String clientTrust = "clientTrust.jks";
	private static String clientTrustPassword = "123456";

	
	private static final String PROTOCOL = "TLS";
	private static final String KEY_MANAGER_FACTORY_ALGORITHM;	
	private static final String TRUST_MANAGER_FACTORY_ALGORITHM;

	static {
		String algorithm = Security.getProperty("ssl.KeyManagerFactory.algorithm");
		if (algorithm == null) {
			algorithm = KeyManagerFactory.getDefaultAlgorithm();
		}
		KEY_MANAGER_FACTORY_ALGORITHM = algorithm;
		
		algorithm = Security.getProperty("ssl.TrustManagerFactory.algorithm");
		if (algorithm == null) {
			algorithm = TrustManagerFactory.getDefaultAlgorithm();
		}
		TRUST_MANAGER_FACTORY_ALGORITHM = algorithm;
	}

	private static SSLContext serverInstance = null;
	private static SSLContext clientInstance = null;

	/**
	 * Get SSLContext singleton.
	 * 
	 * @return SSLContext
	 * @throws java.security.GeneralSecurityException
	 * 
	 */
	public static SSLContext getInstance(boolean server) throws GeneralSecurityException, IOException {
		SSLContext retInstance = null;
		if (server) {
			synchronized (BogusSslContextFactory.class) {
				if (serverInstance == null) {
					try {
						serverInstance = createBougusServerSslContext();
					} catch (Exception ioe) {
						throw new GeneralSecurityException("Can't create Server SSLContext:" + ioe);
					}
				}
			}
			retInstance = serverInstance;
		} else {
			synchronized (BogusSslContextFactory.class) {
				if (clientInstance == null) {
					clientInstance = createBougusClientSslContext();
				}
			}
			retInstance = clientInstance;
		}
		return retInstance;
	}

	private static SSLContext createBougusServerSslContext() throws GeneralSecurityException, IOException {

		// Initialize the SSLContext to work with our key managers.
		SSLContext sslContext = SSLContext.getInstance(PROTOCOL);
		//sslContext.init(getKeyManagers(serverKeys, serverKeysPassword), getTrustManagers(serverTrust,serverTrustPassword), null);
		sslContext.init(getKeyManagers(serverKeys, serverKeysPassword), trustManagers, null);
		return sslContext;
	}

	private static SSLContext createBougusClientSslContext() throws GeneralSecurityException, IOException {
		SSLContext context = SSLContext.getInstance(PROTOCOL);
		//context.init(getKeyManagers(clientKeys, clientKeysPassword), getTrustManagers(clientTrust,clientTrustPassword), null);
		context.init(getKeyManagers(clientKeys, clientKeysPassword), trustManagers, null);

		return context;
	}

	private static KeyManager[] getKeyManagers(String keysfile, String password) throws GeneralSecurityException,
			IOException {
		// First, get the default KeyManagerFactory.
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(KEY_MANAGER_FACTORY_ALGORITHM);
		
		// Next, set up the TrustStore to use. We need to load the file into
		// a KeyStore instance.	
		KeyStore ks = KeyStore.getInstance("BKS");
		InputStream in = BogusSslContextFactory.class.getResourceAsStream(keysfile);
		ks.load(in, password.toCharArray());
		in.close();

		// Now we initialise the KeyManagerFactory with this KeyStore	
		kmf.init(ks, password.toCharArray());

		// And now get the TrustManagers
		return kmf.getKeyManagers();
	}
	
	protected static TrustManager[] getTrustManagers(String trustfile,String pasword) throws IOException, GeneralSecurityException {
		// First, get the default TrustManagerFactory.
		TrustManagerFactory tmFact = TrustManagerFactory.getInstance(TRUST_MANAGER_FACTORY_ALGORITHM);

		// Next, set up the TrustStore to use. We need to load the file into
		// a KeyStore instance.		
		InputStream in = BogusSslContextFactory.class.getResourceAsStream(trustfile);
		KeyStore ks = KeyStore.getInstance("BKS");
		ks.load(in, pasword.toCharArray());
		in.close();

		// Now we initialise the TrustManagerFactory with this KeyStore
		tmFact.init(ks);

		// And now get the TrustManagers
		TrustManager[] tms = tmFact.getTrustManagers();
		return tms;
	}
	
	
	
	//设置客户端信任任何服务器
    static TrustManager[] trustManagers = new TrustManager[] { new TrustAnyone() };

    private static class TrustAnyone implements X509TrustManager {
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] x509Certificates, String s)
                throws CertificateException {
        }

        public void checkServerTrusted(
                java.security.cert.X509Certificate[] x509Certificates, String s)
                throws CertificateException {
        }

        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[0];
        }
    }

}
