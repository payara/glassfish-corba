/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 * 
 * Copyright (c) 1997-2010 Oracle and/or its affiliates. All rights reserved.
 * 
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * https://glassfish.dev.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 * 
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at glassfish/bootstrap/legal/LICENSE.txt.
 * 
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 * 
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 * 
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
/*
 * COMPONENT_NAME: idl.toJava
 *
 * ORIGINS: 27
 *
 * Licensed Materials - Property of IBM
 * 5639-D57 (C) COPYRIGHT International Business Machines Corp. 1997, 1999
 * RMI-IIOP v1.0
 * US Government Users Restricted Rights - Use, duplication or
 * disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.sun.tools.corba.ee.idl.toJavaPortable;

// NOTES:
// -D62023   <klr> New file to implement CORBA 2.4 RTF
// NOTE: The methods in this class should be exact copies of the 
// correspoind methods in MethodGen24. The purpose of this class is 
// to inject the changes made in MethodGen24 between AttributeGen
// and AttributeGen24. When the AttributeGen24 changes are merged, this
// class should be deleted.

import com.sun.tools.corba.ee.idl.MethodEntry;

import java.io.PrintWriter;
import java.util.Hashtable;

/**
 *
 **/
public class MethodGenClone24 extends AttributeGen
{
  /**
   * Public zero-argument constructor.
   **/
  public MethodGenClone24 ()
  {
  } // ctor

  /**
   * write an abstract method definition
   **/
  protected void abstractMethod (Hashtable symbolTable, MethodEntry m, PrintWriter stream)
  {
    this.symbolTable = symbolTable;
    this.m           = m;
    this.stream      = stream;
    if (m.comment () != null)
      m.comment ().generate ("  ", stream);
    stream.print ("  ");
    stream.print ("public abstract ");
    writeMethodSignature ();
    stream.println (";");
    stream.println ();
  } // abstractMethod

  /**
   * delete method templates for valuetypes
   **/
  protected void interfaceMethod (Hashtable symbolTable, MethodEntry m, PrintWriter stream)
  {
    this.symbolTable = symbolTable;
    this.m           = m;
    this.stream      = stream;
    if (m.comment () != null)
      m.comment ().generate ("  ", stream);
    stream.print ("  ");
    writeMethodSignature ();
    stream.println (";");
  } // interfaceMethod
}
