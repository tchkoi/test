package ge.ufc.webservices.ws;


import ge.ufc.webservices.authorization.Agent;
import ge.ufc.webservices.authorization.Password;
import ge.ufc.webservices.exception.InternalError;
import ge.ufc.webservices.exception.*;
import ge.ufc.webservices.model.*;

import javax.jws.*;
import javax.jws.soap.SOAPBinding;


@WebService
@SOAPBinding(style = SOAPBinding.Style.DOCUMENT, use = SOAPBinding.Use.LITERAL, parameterStyle = SOAPBinding.ParameterStyle.BARE)
public interface ServiceA {
    @WebMethod(operationName = "check")
    @WebResult(name = "CheckResponse")
    CheckResponse check(@WebParam(name = "CheckRequest") final CheckRequest request,
                        @WebParam(name = "agent", header = true) final Agent agent,
                        @WebParam(name = "password", header = true) final Password pass)
            throws AgentAuthFailed, AgentAccessDenied, UserNotFound, InternalError;

    @WebMethod(operationName = "pay")
    @WebResult(name = "PayResponse")
    PayResponse pay(@WebParam(name = "PayRequest") final PayRequest request,
                    @WebParam(name = "agent", header = true) final Agent agent,
                    @WebParam(name = "password", header = true) final Password pass)
            throws AgentAuthFailed, AgentAccessDenied, UserNotFound, Duplicate, AmountNotPositive, InternalError;

    @WebMethod(operationName = "status")
    @WebResult(name = "StatusResponse")
    StatusResponse status(@WebParam(name = "StatusRequest") final StatusRequest request,
                          @WebParam(name = "agent", header = true) final Agent agent,
                          @WebParam(name = "password", header = true) final Password pass)
            throws AgentAuthFailed, AgentAccessDenied, InternalError, TransactionNotFound;
}
