package ge.ufc.webservices.exception;

import javax.xml.ws.WebFault;

@WebFault
public class InternalError extends Exception {
    private static final long serialVersionUID = 1L;


    public InternalError(){
        super("Internal error");
    }

    public InternalError(String message) {
        super(message);
    }
}