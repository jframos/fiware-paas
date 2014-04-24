<%

    //Get version of application

    java.util.Properties prop = new java.util.Properties();

    prop.load(getServletContext().getResourceAsStream("/META-INF/MANIFEST.MF"));

    String buildNumber = prop.getProperty("Implementation-Build");

%>

<%=applVersion%><br>
<%=buildNumber%>


