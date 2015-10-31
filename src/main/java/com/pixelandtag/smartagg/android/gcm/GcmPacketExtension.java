package com.pixelandtag.smartagg.android.gcm;

import org.jivesoftware.smack.packet.DefaultExtensionElement;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Stanza;
import org.jivesoftware.smack.util.StringUtils;

public class GcmPacketExtension extends DefaultExtensionElement   {
	
	public static final String ELEMENT = "gcm";
    public static final String NAMESPACE = "google:mobile:data";

    private final String json;

    public GcmPacketExtension(String json) {
    	super(ELEMENT, NAMESPACE);
        this.json = json;
    }

    public String getJson() {
        return json;
    }

    @Override
    public String toXML() {
        return String.format("<%s xmlns=\"%s\">%s</%s>",
        		GCMProps.GCM_ELEMENT_NAME, GCMProps.GCM_NAMESPACE,
                StringUtils.escapeForXML(json), GCMProps.GCM_ELEMENT_NAME);
    }

    public Stanza toPacket() {
    	return new Stanza() {
            // Must override toXML() because it includes a <body>
            @Override
            public String toXML() {

                StringBuilder buf = new StringBuilder();
                buf.append("<message");
                //if (getElementName() != null) {
                 //   buf.append(" xmlns=\"").append(getElementName()).append("\"");
                //}
                if (getLanguage() != null) {
                    buf.append(" xml:lang=\"").append(getLanguage()).append("\"");
                }
                if (getStanzaId() != null) {
                    buf.append(" id=\"").append(getStanzaId()).append("\"");
                }
                if (getTo() != null) {
                    buf.append(" to=\"").append(StringUtils.escapeForXML(getTo())).append("\"");
                }
                if (getFrom() != null) {
                    buf.append(" from=\"").append(StringUtils.escapeForXML(getFrom())).append("\"");
                }
                buf.append(">");
                buf.append(GcmPacketExtension.this.toXML());
                buf.append("</message>");
                return buf.toString();
            }
        };
    }
    
    @Override
    public String getNamespace() {
        return NAMESPACE;
    }

    @Override
    public String getElementName() {
        return ELEMENT;
    }
    
    /**
     * Retrieve the GCM packet extension from the packet.
     *
     * @param packet
     * @return the GCM packet extension or null.
     */
    public static GcmPacketExtension from(Stanza packet) {
        return packet.getExtension(ELEMENT, NAMESPACE);
    }
}
