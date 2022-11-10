package ge.ufc.webservices.exception;

import javax.xml.ws.WebFault;

@WebFault
public class Duplicate extends Exception{

    private static final long serialVersionUID = 1L;

    public Duplicate(){
        super("Duplicate");
    }

    public Duplicate(String str){
        super(str);
    }

}
