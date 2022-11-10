package ge.ufc.webservices.model;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class PayResponse {

    @XmlElement(name = "system_transaction_id", required = true)
    private int systemTransactionId;

    public int getSystemTransactionId() {
        return systemTransactionId;
    }

    public void setSystemTransactionId(int systemTransactionId) {
        this.systemTransactionId = systemTransactionId;
    }
}
