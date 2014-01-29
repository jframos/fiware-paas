<%@ page import="com.telefonica.euro_iaas.paasmanager.rest.exception.APIException" %>
<%@ page import="com.telefonica.euro_iaas.paasmanager.rest.exception.ErrorCode" %>
<%@ page import="javax.ws.rs.WebApplicationException" %>
<%@ page contentType="application/json" pageEncoding="UTF-8" %>
{"errors":
[
<%
    String message = (String) request.getAttribute("javax.servlet.error.message");
    Integer code = (Integer) request.getAttribute("javax.servlet.error.status_code");
    Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
    if (throwable != null) {

        if (throwable instanceof APIException) {
            message = ((APIException) throwable).getPublicMessage();
            code = ((APIException) throwable).getCode();
            response.setStatus(((APIException) throwable).getHttpCode());
        } else {
            ErrorCode errorCode = ErrorCode.find(throwable.getMessage());
            code = errorCode.getCode();
            message = errorCode.getPublicMessage();

            if (errorCode == ErrorCode.INFRASTRUCTURE) {


                String message2 = throwable.getMessage().replace("\"", "\\\"");

                out.println("{\"message\":\"" + message2 + "\",\"code\":1000},");
            }
            response.setStatus(errorCode.getHttpCode());
        }
    }
    if ((message == null) || message.isEmpty()) {

        ErrorCode errorCode = ErrorCode.DEFAULT;
        code = errorCode.getCode();
        message = errorCode.getPublicMessage();
        response.setStatus(errorCode.getHttpCode());
    }


%>
{"message":"<%= message%>",
"code":<%=code %>
}
]
}
