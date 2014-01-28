<%@ page contentType="application/json" pageEncoding="UTF-8" %>

{"errors":
[
{"message":"<%=request.getAttribute("javax.servlet.error.message") %>",
"code":<%=request.getAttribute("javax.servlet.error.status_code") %>
}
]
}
