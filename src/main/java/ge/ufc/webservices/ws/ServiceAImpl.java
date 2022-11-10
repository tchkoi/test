package ge.ufc.webservices.ws;

import ge.ufc.webservices.authorization.Agent;
import ge.ufc.webservices.authorization.Password;
import ge.ufc.webservices.dao.DatabaseManager;
import ge.ufc.webservices.dao.ServiceADaoImpl;
import ge.ufc.webservices.exception.InternalError;
import ge.ufc.webservices.exception.*;
import ge.ufc.webservices.model.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import javax.xml.ws.WebServiceContext;
import javax.xml.ws.handler.MessageContext;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@WebService(endpointInterface = "ge.ufc.webservices.ws.ServiceA")
public class ServiceAImpl implements ServiceA{

    WebServiceContext webServiceContext;

    private static ServiceADaoImpl serviceADao;

    private static final Logger logger = LogManager.getLogger(ServiceAImpl.class);

    private String getAddress() {
        return ((HttpServletRequest) webServiceContext.getMessageContext().get(MessageContext.SERVLET_REQUEST)).getRemoteAddr();
    }

    private PayResponse createResponse(PayRequest request,Agent agent, ServiceADaoImpl serviceADao) {
            Transaction transaction = serviceADao.getTransaction(agent.getId(), request.getAgentTransactionId());
            PayResponse response = new PayResponse();
            response.setSystemTransactionId(transaction.getSystemTransactionId());
            return response;

    }

    private static void userUpdate(PayRequest request,Agent agent, ServiceADaoImpl serviceADao) {
            User user = serviceADao.getUser(request.getUserId());
            Transaction transaction = serviceADao.getTransaction(agent.getId(), request.getAgentTransactionId());
            double balance = user.getBalance() + transaction.getAmount();
            user.setBalance(balance);
            serviceADao.updateUser(request.getUserId(), user);
    }

    private static Transaction createTransaction(PayRequest request, Agent agent) {
            Transaction transaction = new Transaction();
            transaction.setAgentId(agent.getId());
            transaction.setAgentTransactionId(request.getAgentTransactionId());
            transaction.setUserId(request.getUserId());
            transaction.setAmount(request.getAmount());
            transaction.setTransactionDate(Timestamp.valueOf(LocalDateTime.now()));
            return transaction;
    }

    private static void agentValidation(Agent agent, Password password,Connection connection) throws AgentAuthFailed, InternalError {
            serviceADao = new ServiceADaoImpl(connection);
            ge.ufc.webservices.model.Agent baseAgent = serviceADao.getAgent(agent.getId());
            String realPassword = baseAgent.getPassword().strip();
            String userPassword = password.getPassword().strip();
            if (!realPassword.equals(userPassword)) {
                throw new AgentAuthFailed();
            }
    }

    private void addressValidation(Agent agent,Connection connection) throws AgentAccessDenied {
        serviceADao = new ServiceADaoImpl(connection);
        AgentAccess agentAccess = serviceADao.getAgentAccess(agent.getId());
        String allowedAddress = agentAccess.getAllowedIP().strip();
        String address = getAddress().strip();
        if(!address.equals(allowedAddress))
            throw new AgentAccessDenied();
    }


    @Override
    public CheckResponse check(CheckRequest request,Agent agent, Password password) throws AgentAuthFailed, AgentAccessDenied, UserNotFound, InternalError {
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();
            serviceADao = new ServiceADaoImpl(connection);
            addressValidation(agent, connection);
            agentValidation(agent, password, connection);
            User user = serviceADao.getUser(request.getUserId());
            CheckResponse response = new CheckResponse();
            String fullName = user.getFirstName().charAt(0) + "." + user.getLastName().charAt(0);
            response.setFullName(fullName);
            response.setBalance(user.getBalance());
            return response;
        } finally {
            DatabaseManager.closeConnection(connection);
        }
    }

    @Override
    public PayResponse pay(PayRequest request, Agent agent, Password password) throws AgentAuthFailed, AgentAccessDenied, UserNotFound, Duplicate, AmountNotPositive, InternalError {
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();
            connection.setAutoCommit(false);
            serviceADao = new ServiceADaoImpl(connection);
            addressValidation(agent, connection);
            agentValidation(agent, password, connection);
            if (request.getAmount() < 0) {
                throw new AmountNotPositive();
            }
            Transaction mainTransaction = serviceADao.getTransaction(agent.getId(), request.getAgentTransactionId());
            double amount = mainTransaction.getAmount();
            int userId = mainTransaction.getUserId();
            if (amount != request.getAmount() || userId != request.getUserId()) {
                throw new Duplicate();
            }
            serviceADao.addTransaction(createTransaction(request,agent));
            userUpdate(request, agent, serviceADao);
            connection.commit();
            return createResponse(request, agent, serviceADao);
        } catch (SQLException | RuntimeException e) {
            throw new InternalError();
        } finally {
            DatabaseManager.closeConnection(connection);
        }
    }

    @Override
    public StatusResponse status(StatusRequest request, Agent agent, Password password) throws AgentAuthFailed, AgentAccessDenied, InternalError, TransactionNotFound {
        Connection connection = null;
        try {
            connection = DatabaseManager.getConnection();
            addressValidation(agent, connection);
            agentValidation(agent, password, connection);
            serviceADao = new ServiceADaoImpl(connection);
            Transaction transaction = serviceADao.getTransaction(agent.getId(), request.getAgentTransactionId());
            StatusResponse statusResponse = new StatusResponse();
            statusResponse.setSystemTransactionId(transaction.getSystemTransactionId());
            return statusResponse;
        }  catch (NullPointerException e) {
            logger.info(e.getMessage());
            throw new RuntimeException();
        }
        finally{
            DatabaseManager.closeConnection(connection);
        }
     }
}
