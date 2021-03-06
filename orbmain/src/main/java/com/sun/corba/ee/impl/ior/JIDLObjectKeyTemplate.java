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

package com.sun.corba.ee.impl.ior;

import org.omg.CORBA_2_3.portable.InputStream ;
import org.omg.CORBA_2_3.portable.OutputStream ;

import org.omg.CORBA.OctetSeqHolder ;

import com.sun.corba.ee.spi.ior.ObjectId ;
import com.sun.corba.ee.spi.ior.ObjectKeyFactory ;

import com.sun.corba.ee.spi.orb.ORB ;
import com.sun.corba.ee.spi.orb.ORBVersion ;
import com.sun.corba.ee.spi.orb.ORBVersionFactory ;

import com.sun.corba.ee.impl.ior.ObjectKeyFactoryImpl ;

/**
 * @author Ken Cavanaugh
 */
public final class JIDLObjectKeyTemplate extends NewObjectKeyTemplateBase
{
    /** This constructor reads the template ONLY from the stream.
    */
    public JIDLObjectKeyTemplate( ORB orb, int magic, int scid, InputStream is ) 
    {
        super( orb, magic, scid, is.read_long(), JIDL_ORB_ID, JIDL_OAID );

        setORBVersion( is ) ;
    }

    /** This constructor reads a complete ObjectKey (template and Id)
    * from the stream.
    */
    public JIDLObjectKeyTemplate( ORB orb, int magic, int scid, InputStream is,
        OctetSeqHolder osh ) 
    {
        super( orb, magic, scid, is.read_long(), JIDL_ORB_ID, JIDL_OAID );

        osh.value = readObjectKey( is ) ;

        setORBVersion( is ) ;
    }
    
    public JIDLObjectKeyTemplate( ORB orb, int scid, int serverid ) 
    {
        super( orb, ObjectKeyFactoryImpl.JAVAMAGIC_NEWER, scid, serverid, 
            JIDL_ORB_ID, JIDL_OAID ) ; 

        setORBVersion( ORBVersionFactory.getORBVersion() ) ;
    }
   
    protected void writeTemplate( OutputStream os )
    {
        os.write_long( getMagic() ) ;
        os.write_long( getSubcontractId() ) ;
        os.write_long( getServerId() ) ;
    }
}
