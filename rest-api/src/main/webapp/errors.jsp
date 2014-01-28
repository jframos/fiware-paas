<%@ page import="com.telefonica.euro_iaas.paasmanager.rest.exception.APIException" %>
<%@ page import="com.telefonica.euro_iaas.paasmanager.rest.exception.ErrorCode" %>
<%@ page contentType="application/json" pageEncoding="UTF-8" %>

<%
    String message = (String) request.getAttribute("javax.servlet.error.message");
    Integer code = (Integer) request.getAttribute("javax.servlet.error.status_code");
    Throwable throwable = (Throwable) request.getAttribute("javax.servlet.error.exception");
    if (throwable != null) {

        if (throwable instanceof APIException) {
            message = ((APIException) throwable).getPublicMessage();
            code = ((APIException) throwable).getCode();
        } else {
            ErrorCode errorCode = ErrorCode.find(throwable.getMessage());
            code = errorCode.getCode();
            message = errorCode.getPublicMessage();
        }
    }
    if ((message == null) || message.isEmpty()) {

        ErrorCode errorCode = ErrorCode.DEFAULT;
        code = errorCode.getCode();
        message = errorCode.getPublicMessage();
    }


%>

{"errors":
[
{"message":"<%= message%>",
"code":<%=code %>
}
]
}
