package ge.ufc.webservices.exception;

import javax.xml.ws.WebFault;

@WebFault
public class AmountNotPositive extends Exception{

    private static final long serialVersionUID = 1L;

    public AmountNotPositive(){
        super("Amount is not positive");
    }

    public AmountNotPositive(String str){
        super(str);
    }


}
