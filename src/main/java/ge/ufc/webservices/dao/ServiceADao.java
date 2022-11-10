package ge.ufc.webservices.dao;

import ge.ufc.webservices.model.Agent;
import ge.ufc.webservices.model.AgentAccess;
import ge.ufc.webservices.model.Transaction;
import ge.ufc.webservices.model.User;


public interface ServiceADao {

    User getUser(int userId);

    Agent getAgent(int agentId);

    AgentAccess getAgentAccess(int agentId);

    Transaction getTransaction(int agentId, String agentTransactionId);

    void updateUser(int userId, User user);

    void addTransaction(Transaction transaction);

}
