package ge.ufc.webservices.dao;

import ge.ufc.webservices.model.Agent;
import ge.ufc.webservices.model.AgentAccess;
import ge.ufc.webservices.model.Transaction;
import ge.ufc.webservices.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ServiceADaoImpl implements ServiceADao {
    Connection connection;

    public ServiceADaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User getUser(int userId) {
        String getUser = "SELECT * FROM users WHERE user_id = ?";
        User user = new User();
        try (PreparedStatement preparedStatement = connection.prepareStatement(getUser)) {
            preparedStatement.setInt(1, userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()){
                user.setUserId(resultSet.getInt("user_id"));
                user.setFirstName(resultSet.getString("first_name"));
                user.setLastName(resultSet.getString("last_name"));
                user.setIdNumber(resultSet.getString("id_number"));
                user.setBalance(resultSet.getDouble("balance"));
            }
            return user;
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException();
        }
    }

    @Override
    public Agent getAgent(int agentId) {
        String getAgent = "SELECT * FROM agents WHERE agent_id = ?";
        Agent agent = new Agent();
        try(PreparedStatement preparedStatement = connection.prepareStatement(getAgent)){
            preparedStatement.setInt(1, agentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                agent.setAgentId(resultSet.getInt("agent_id"));
                agent.setName(resultSet.getString("name"));
                agent.setPassword(resultSet.getString("password"));
            }
            return agent;
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public AgentAccess getAgentAccess(int agentId) {
        AgentAccess agentAccess = new AgentAccess();
        String getAgentAccess = "SELECT * FROM agent_access WHERE agent_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getAgentAccess)) {
            preparedStatement.setInt(1, agentId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (!resultSet.next()) {
                agentAccess.setRowId(resultSet.getInt("row_id"));
                agentAccess.setAllowedIP(resultSet.getString("allowed_ip"));
                agentAccess.setAgentId(resultSet.getInt("agent_id"));
            }
            return agentAccess;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction getTransaction(int agentId, String agentTransactionId) {
        Transaction transaction = new Transaction();
        String getTransaction = "SELECT * FROM transactions WHERE agent_id = ? AND agent_transaction_id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(getTransaction)) {
            preparedStatement.setInt(1, agentId);
            preparedStatement.setString(2, agentTransactionId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()) {
                transaction.setSystemTransactionId(resultSet.getInt("system_transaction_id"));
                transaction.setAgentId(resultSet.getInt("agent_id"));
                transaction.setAgentTransactionId(resultSet.getString("agent_transaction_id"));
                transaction.setUserId(resultSet.getInt("user_id"));
                transaction.setAmount(resultSet.getDouble("amount"));
                transaction.setTransactionDate(resultSet.getTimestamp("transaction_date"));
            }
            return transaction;
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateUser(int userId, User user) {
        String updateUser = "UPDATE users SET first_name = ?, last_name = ?, id_number= ?, balance= ? WHERE user_id = ?";
        try(PreparedStatement preparedStatement = connection.prepareStatement(updateUser)){
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setString(3, user.getIdNumber());
            preparedStatement.setDouble(4, user.getBalance());
            preparedStatement.setInt(5, userId);
            preparedStatement.executeUpdate();
        } catch (SQLException | RuntimeException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addTransaction(Transaction transaction) {
        String addTransaction = "INSERT INTO transactions (agent_id, agent_transaction_id, user_id, amount, transaction_date) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(addTransaction)) {
            preparedStatement.setInt(1, transaction.getAgentId());
            preparedStatement.setString(2, transaction.getAgentTransactionId());
            preparedStatement.setInt(3, transaction.getUserId());
            preparedStatement.setDouble(4, transaction.getAmount());
            preparedStatement.setTimestamp(5, transaction.getTransactionDate());
            preparedStatement.execute();
        } catch (SQLException | RuntimeException e){
            throw new RuntimeException(e);
        }
    }
}
