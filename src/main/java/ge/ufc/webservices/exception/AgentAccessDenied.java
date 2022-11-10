package ge.ufc.webservices.exception;

import javax.xml.ws.WebFault;

@WebFault
public class AgentAccessDenied extends Exception {
    private static final long serialVersionUID = 1L;

    public AgentAccessDenied(){
        super("Access access denied");
    }

    public AgentAccessDenied(String message) {
        super(message);
    }
}