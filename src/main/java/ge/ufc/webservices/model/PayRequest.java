package ge.ufc.webservices.model;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType
public class PayRequest {

    @XmlElement(name = "agent_transaction_id", required = true)
    private String agentTransactionId;
    @XmlElement(name = "user_id", required = true)
    private int userId;
    @XmlElement(required = true)
    private double amount;

    public String getAgentTransactionId() {
        return agentTransactionId;
    }

    public void setAgentTransactionId(String agentTransactionId) {
        this.agentTransactionId = agentTransactionId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "PayRequest{" +
                "agentTransactionId='" + agentTransactionId + '\'' +
                ", userId=" + userId +
                ", amount=" + amount +
                '}';
    }
}
