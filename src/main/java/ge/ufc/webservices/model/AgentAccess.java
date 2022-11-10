package ge.ufc.webservices.model;

public class AgentAccess {

    private int rowId;
    private String allowedIP;
    private int agentId;

    public int getRowId() {
        return rowId;
    }

    public void setRowId(int rowId) {
        this.rowId = rowId;
    }

    public String getAllowedIP() {
        return allowedIP;
    }

    public void setAllowedIP(String allowedIP) {
        this.allowedIP = allowedIP;
    }

    public int getAgentId() {
        return agentId;
    }

    public void setAgentId(int agentId) {
        this.agentId = agentId;
    }
}
