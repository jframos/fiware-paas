/**
 * Copyright 2014 Telefonica Investigación y Desarrollo, S.A.U <br>
 * This file is part of FI-WARE project.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License.
 * </p>
 * <p>
 * You may obtain a copy of the License at:<br>
 * <br>
 * http://www.apache.org/licenses/LICENSE-2.0
 * </p>
 * <p>
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * </p>
 * <p>
 * See the License for the specific language governing permissions and limitations under the License.
 * </p>
 * <p>
 * For those usages not covered by the Apache version 2.0 License please contact with opensource@tid.es
 * </p>
 */

package com.acme.handler;

import org.eclipse.jetty.server.handler.ErrorHandler;

/**
 * Created with IntelliJ IDEA. User: jesuspg Date: 28/04/14 Time: 12:44 To change this template use File | Settings |
 * File Templates.
 */
public class MyErrorHandler extends ErrorHandler {

    public void handle(java.lang.String target, org.eclipse.jetty.server.Request baseRequest,
            javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response)
            throws java.io.IOException {

        System.out.println("errorHandler jesus");

    }

}
