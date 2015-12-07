package com.pixelandtag.smartagg.android.gcm;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.net.ssl.SSLSocketFactory;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Logger;
import org.jivesoftware.smack.ConnectionConfiguration.SecurityMode;
import org.jivesoftware.smack.ConnectionListener;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.StanzaTypeFilter;
import org.jivesoftware.smack.packet.ExtensionElement;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.provider.ExtensionElementProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.tcp.XMPPTCPConnection;
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration;
import org.json.simple.JSONValue;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

public class CcsClient {
	
	private static final Logger logger = Logger.getLogger(CcsClient.class);

    private static final String GCM_SERVER = "gcm.googleapis.com";
    private static final int GCM_PORT = 5235;

    private static final String GCM_ELEMENT_NAME = "gcm";
    private static final String GCM_NAMESPACE = "google:mobile:data";
    private static final String GCM_PROJECT_NUMBER = "778162576633";
    private static final String GCM_USERNAME = GCM_PROJECT_NUMBER;

    public static final String YOUR_PROJECT_ID = "smartagg-1110";
    public static final String YOUR_API_KEY = "AIzaSyDRdg97fk66fJlSu_St6ic1InW9xLEat70";//"AIzaSyBx_qTrOvK7fUIMuRPUuiJ963u3ZpgOoEk"; // your API Key
    public static final String YOUR_PHONE_REG_ID = "cWzi7Uk4pgU:APA91bE8gmbwZDKySpQOpjMc2I2s70q9SiQg7RMUsZC_VFA4JZmUJBM96ETy_rJYEXG0XAckA7z-gpD-oY1g4vLD3frGpixHfqbDH5FV4y1SSx6jPrWBnwvKE7ABVEwh98WXBv2FH673";//"<your test phone's registration id here>";

    static Random random = new Random();
    XMPPTCPConnection connection;
    XMPPTCPConnectionConfiguration config;

    /// new: some additional instance and class members
    private static CcsClient sInstance = null;
    private String mApiKey = YOUR_API_KEY;
    private String mProjectId = GCM_PROJECT_NUMBER;
    private boolean mDebuggable = true;
    
    
    private CcsClient(String projectId, String apiKey, boolean debuggable) {
        this();
        mApiKey = apiKey;
        mProjectId = projectId;
        mDebuggable = debuggable;
    }
    
    private CcsClient() {
    	 ProviderManager.addExtensionProvider(GCM_ELEMENT_NAME,
                 GCM_NAMESPACE, new  ExtensionElementProvider<ExtensionElement>(){
					@Override
					public ExtensionElement parse(XmlPullParser parser,
							int initialDepth) throws XmlPullParserException,
							IOException, SmackException {
						 String json = parser.nextText();
                         GcmPacketExtension packet = new GcmPacketExtension(json);
                         return packet;
					}
                 });
    	 
    	 System.out.println(" initialized!!! ");
	}

    
    /**
     * Returns a random message id to uniquely identify a message.
     *
     * <p>
     * Note: This is generated by a pseudo random number generator for
     * illustration purpose, and is not guaranteed to be unique.
     *
     */
    public String getRandomMessageId() {
        return "m-" + Long.toString(random.nextLong());
    }
    
    /**
     * Sends a downstream GCM message.	
     * throws NotConnectedException 
     */
    public void send(String jsonRequest) throws NotConnectedException {
    	logger.info(" >>> "+jsonRequest);
    	Stanza request = new GcmPacketExtension(jsonRequest).toPacket();
        connection.sendStanza(request);
    }
    
    
    
    public static CcsClient getInstance() {
        if (sInstance == null) {
            throw new IllegalStateException("You have to prepare the client first");
        }
        return sInstance;
    }
    
  /// new: for sending messages to a list of recipients
    /**
     * Sends a message to multiple recipients. Kind of like the old
     * HTTP message with the list of regIds in the "registration_ids" field.
     */
    public void sendBroadcast(Map<String, String> payload, String collapseKey,
            long timeToLive, Boolean delayWhileIdle, List<String> recipients) {
        Map map = createAttributeMap(null, null, payload, collapseKey,
                    timeToLive, delayWhileIdle);
        for (String toRegId: recipients) {
            String messageId = getRandomMessageId();
            map.put("message_id", messageId);
            map.put("to", toRegId);
            String jsonRequest = createJsonMessage(map);
            try{
            	send(jsonRequest);
            }catch(NotConnectedException nce){
            	logger.error(nce.getMessage(),nce);
            }
        }
    }
    
	public static CcsClient prepareClient(String projectId, String apiKey, boolean debuggable) {
        synchronized(CcsClient.class) {
            if (sInstance == null) {
                sInstance = new CcsClient(projectId, apiKey, debuggable);
            }
        }
        return sInstance;
    }
	
	
	/**
     * Creates a JSON encoded GCM message.
     *
     * @param to RegistrationId of the target device (Required).
     * @param messageId Unique messageId for which CCS will send an "ack/nack"
     * (Required).
     * @param payload Message content intended for the application. (Optional).
     * @param collapseKey GCM collapse_key parameter (Optional).
     * @param timeToLive GCM time_to_live parameter (Optional).
     * @param delayWhileIdle GCM delay_while_idle parameter (Optional).
     * @return JSON encoded GCM message.
     */
    public static String createJsonMessage(String to, String messageId, Map<String, String> payload,
            String collapseKey, Long timeToLive, Boolean delayWhileIdle) {
        return createJsonMessage(createAttributeMap(to, messageId, payload,
                collapseKey, timeToLive, delayWhileIdle));
    }
    
    public static String createJsonMessage(Map map) {
        return JSONValue.toJSONString(map);
    }
    
    
    
	/*public static void main(String[] args) throws SmackException, IOException, Exception {
		
		BasicConfigurator.configure();
		XMPPTCPConnectionConfiguration config =
    			XMPPTCPConnectionConfiguration.builder()
    			.setServiceName(GCM_SERVER)
    		     .setHost(GCM_SERVER)
    		     .setCompressionEnabled(false)
    		     .setPort(GCM_PORT)
    		     .setConnectTimeout(30000)
    		     .setSecurityMode(SecurityMode.disabled)
    		     .setSendPresence(false)
    		     .setSocketFactory( SSLSocketFactory.getDefault())
    		     .setDebuggerEnabled(true)
    		    .build();
		
		//SASLAuthentication.unBlacklistSASLMechanism("PLAIN");
		///SASLAuthentication.blacklistSASLMechanism("DIGEST-MD5");
		
		logger.info("Success 1! Yay! ");
		XMPPTCPConnection connection = new XMPPTCPConnection(config);
		logger.info("Connecting...");
		connection.connect();
		logger.info("Success 2! Yay! ");
		connection.login(GCM_USERNAME+"@gcm.googleapis.com", YOUR_API_KEY);
		logger.info("Success 3! Yay! ");
		
		 while(true){
	        	Thread.sleep(1000);
	        }
	}*/



	public static Map<String, Object> createAttributeMap(String to, String messageId, Map<String, String> payload,
            String collapseKey, Long timeToLive, Boolean delayWhileIdle) {
        Map<String, Object> message = new HashMap<String, Object>();
        if (to != null) {
            message.put("to", to);
        }
        if (collapseKey != null) {
            message.put("collapse_key", collapseKey);
        }
        if (timeToLive != null) {
            message.put("time_to_live", timeToLive);
        }
        if (delayWhileIdle != null && delayWhileIdle) {
            message.put("delay_while_idle", true);
        }
        if (messageId != null) {
            message.put("message_id", messageId);
        }
        message.put("data", payload);
        return message;
    }
	
	
	public void connect() throws XMPPException, SmackException, IOException {
		
		BasicConfigurator.configure();
		
        config = XMPPTCPConnectionConfiguration.builder()
    			.setServiceName(GCM_SERVER)
   		     .setHost(GCM_SERVER)
   		     .setCompressionEnabled(false)
   		     .setPort(GCM_PORT)
   		     .setConnectTimeout(30000)
   		     .setSecurityMode(SecurityMode.disabled)
   		     .setSendPresence(false)
   		     .setSocketFactory( SSLSocketFactory.getDefault())
   		     .setDebuggerEnabled(mDebuggable)
   		     
   		    .build(); 
        
        connection = new XMPPTCPConnection(config);
        connection.connect();

        connection.addConnectionListener(new ConnectionListener() {

            @Override
            public void reconnectionSuccessful() {
                logger.info("Reconnecting..");
            }

            @Override
            public void reconnectionFailed(Exception e) {
                logger.info("Reconnection failed.. ", e);
            }

            @Override
            public void reconnectingIn(int seconds) {
                logger.info(String.format("Reconnecting in %d secs", seconds));
            }

            @Override
            public void connectionClosedOnError(Exception e) {
                logger.info( "Connection closed on error.");
            }

            @Override
            public void connectionClosed() {
                logger.info("Connection closed.");
            }

			@Override
			public void connected(XMPPConnection connection) {
				logger.info("Connected.");
			}

			@Override
			public void authenticated(XMPPConnection connection, boolean resumed) {
				logger.info("connection : "+connection.isAuthenticated() +" resumed : "+resumed);
			}
        });

        // Handle incoming packets
        //connection.addStanzaAcknowledgedListener(new SmartStanzaListener());
        connection.addAsyncStanzaListener(new SmartStanzaListener(), new StanzaTypeFilter(Stanza.class));
        //connection.addPacketInterceptor(new SmartStanzaListener(), new StanzaTypeFilter(Stanza.class));
        /*// Log all outgoing packets
        connection.addPacketInterceptor(new PacketInterceptor() {
            @Override
            public void interceptPacket(Packet packet) {
                logger.log(Level.INFO, "Sent: {0}", packet.toXML());
            }
        }, new PacketTypeFilter(Message.class));
         */
         

        connection.login(mProjectId + "@gcm.googleapis.com", mApiKey);
        logger.info( "logged in: " + mProjectId);
    }

	
	public static void main(String[] args) throws Exception {
		//BasicConfigurator.configure();
        final String projectId = GCM_PROJECT_NUMBER;
        final String password = YOUR_API_KEY;
        final String toRegId = YOUR_PHONE_REG_ID;

        CcsClient ccsClient = CcsClient.prepareClient(projectId, password, true);

        try {
            ccsClient.connect();
        } catch (XMPPException e) {
            e.printStackTrace();
        }

        // Send a sample hello downstream message to a device.
        String messageId = ccsClient.getRandomMessageId();
        Map<String, String> payload = new HashMap<String, String>();
        String longmsg = "d";
        for(int k = 0; k<2; k++)
        	longmsg+=longmsg;
        System.out.println("\n\n\n\t\tsize:: "+longmsg.length());
        payload.put("message", longmsg);
        payload.put("title", "Smartagg-PixelandContent360");
        String collapseKey = "sample";
        Long timeToLive = 10000L;
        Boolean delayWhileIdle = true;
        String jsonmsg = createJsonMessage(toRegId, messageId, payload, collapseKey,
                timeToLive, delayWhileIdle);
        
        //System.out.println("\n\n \t\t --- jsonmsg : "+jsonmsg);
        ccsClient.send(jsonmsg);
        
        while(true){
        	Thread.sleep(1000);
        	//System.out.println(" connected ? "+ccsClient.connection.isConnected());
        }
    }
}
