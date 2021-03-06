package com.sun.corba.ee.impl.transport;
/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012 Oracle and/or its affiliates. All rights reserved.
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
import com.sun.corba.ee.impl.encoding.CDRInputObject;
import com.sun.corba.ee.impl.protocol.RequestIdImpl;
import com.sun.corba.ee.impl.protocol.giopmsgheaders.Message;
import com.sun.corba.ee.spi.protocol.MessageMediator;
import com.sun.corba.ee.spi.threadpool.Work;
import org.junit.After;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;

import static org.junit.Assert.*;

public class ConnectionImplTest extends TransportTestBase {

    private static final byte[] BYTE_DATA = {0,1,2,3,4,5,6,7,8,9,10};

    @After
    public void tearDown() {
        if (getConnection() != null)
            assertNull(getConnection().getDiscardedThrowable());
    }

    @Test
    public void whenRequest1_0_receivedFromSocket_dispatchRequest() throws IOException {   // REG
        final List<Short> params = new ArrayList<Short>();
        defineRequestDispatcher( new RequestDispatcher() {
            public void readParameters(CDRInputObject input) {
                params.add(input.read_short());
            }
        });
        readFromSocketWithoutChannelAndDispatch(new byte[]{'G', 'I', 'O', 'P', 1, 0, Message.FLAG_NO_FRAG_BIG_ENDIAN,
                Message.GIOPRequest, /* size */ 0, 0, 0, 38, /* no service contexts */ 0, 0, 0, 0,
                /* request ID */ 0, 0, 0, 2, /* response expected */ 1, /* padding */ 0, 0, 0,
                /* object key */ 0, 0, 0, 4, 0, 0, 0, 6, /* operation */ 0, 0, 0, 5, 'd', 'o', 'I', 't', 0,
                0, 0, 0, /* principal */ 0, 0, 0, 0, /* short param */ 1, 1});
        getConnection().doWork();
        assertEquals(1, getMediators().size());
        MessageMediator mediator = getMediators().remove(0);
        assertEquals("doIt", mediator.getOperationName());
        assertEquals(2, mediator.getRequestId());
        assertFalse(mediator.isOneWay());
        assertEquals(257, (short) params.get(0));
    }

    @Test
    public void whenRequest1_0_receivedFromNio_dispatchRequest() throws IOException {
        final List<Short> params = new ArrayList<Short>();
        defineRequestDispatcher( new RequestDispatcher() {
            public void readParameters(CDRInputObject input) {
                params.add(input.read_short());
            }
        });
        readFromNio(new byte[]{'G', 'I', 'O', 'P', 1, 0, Message.FLAG_NO_FRAG_BIG_ENDIAN,
                Message.GIOPRequest, /* size */ 0, 0, 0, 38, /* no service contexts */ 0, 0, 0, 0,
                /* request ID */ 0, 0, 0, 2, /* response expected */ 1, /* padding */ 0, 0, 0,
                /* object key */ 0, 0, 0, 4, 0, 0, 0, 6, /* operation */ 0, 0, 0, 5, 'd', 'o', 'I', 't', 0,
                0, 0, 0, /* principal */ 0, 0, 0, 0, /* short param */ 1, 1});
        getConnection().doWork();
        processQueuedWork();

        assertEquals(1, getMediators().size());
        MessageMediator mediator = getMediators().remove(0);
        assertEquals("doIt", mediator.getOperationName());
        assertEquals(2, mediator.getRequestId());
        assertFalse(mediator.isOneWay());
        assertEquals(257, (short) params.get(0));
    }

    @Test
    public void whenRequest1_1_receivedFromNio_dispatchRequest() throws IOException {
        final List<Short> params = new ArrayList<Short>();
        defineRequestDispatcher( new RequestDispatcher() {
            public void readParameters(CDRInputObject input) {
                params.add(input.read_short());
            }
        });
        readFromNio(new byte[]{'G', 'I', 'O', 'P', 1, 1, Message.FLAG_NO_FRAG_BIG_ENDIAN,
                Message.GIOPRequest, /* size */ 0, 0, 0, 38, /* no service contexts */ 0, 0, 0, 0,
                /* request ID */ 0, 0, 0, 2, /* response expected */ 1, /* padding */ 0,0,0,
                /* object key */ 0, 0, 0, 4, 0, 0, 0, 6, /* operation */ 0, 0, 0, 5, 'd', 'o', 'I', 't', 0,
                0, 0, 0, /* principal */ 0, 0, 0, 0, /* short param */ 1, 1});
        getConnection().doWork();
        processQueuedWork();

        assertEquals(1, getMediators().size());
        MessageMediator mediator = getMediators().remove(0);
        assertEquals("doIt", mediator.getOperationName());
        assertEquals(2, mediator.getRequestId());
        assertFalse(mediator.isOneWay());
        assertEquals(257, (short) params.get(0));
    }

    @Test
    public void whenLittleEndianRequest1_2_receivedFromNio_dispatchRequest() throws IOException {
        final List<Short> params = new ArrayList<Short>();
        defineRequestDispatcher( new RequestDispatcher() {
            public void readParameters(CDRInputObject input) {
                params.add(input.read_short());
            }
        });
        readFromNio(new byte[]{'G', 'I', 'O', 'P', 1, 2, Message.LITTLE_ENDIAN_BIT,
                Message.GIOPRequest, /* size */ 38, 0, 0, 0,
                /* request ID */ 2, 0, 0, 0, /* response expected */ 1, /* request reserved */ 0, 0, 0,
                /* use key */ 0, 0, /* padding */ 0, 0, /* object key */ 4, 0, 0, 0, 0, 0, 0, 6,
                /* operation */ 5, 0, 0, 0, 'd', 'o', 'I', 't', 0,
                /* padding */ 0, 0, 0, /* no service contexts */ 0, 0, 0, 0, /* short param */ 1, 1});
        getConnection().doWork();
        processQueuedWork();

        assertEquals(1, getMediators().size());
        MessageMediator mediator = getMediators().remove(0);
        assertEquals("doIt", mediator.getOperationName());
        assertEquals(2, mediator.getRequestId());
        assertFalse(mediator.isOneWay());
        assertEquals(257, (short) params.get(0));
    }

    @Test
    public void whenRequest1_1_receivedFromSocketWithFragments_dispatchRequest() throws IOException, InterruptedException {   // REG
        final List<Short> params = new ArrayList<Short>();
        defineRequestDispatcher( new RequestDispatcher() {
            public void readParameters(CDRInputObject input) {
                params.add(input.read_short());
            }
        });
        readFromSocketWithoutChannelAndDispatch(new byte[]{'G', 'I', 'O', 'P', 1, 1, Message.MORE_FRAGMENTS_BIT,
                Message.GIOPRequest, /* size */ 0, 0, 0, 36, /* no service contexts */ 0, 0, 0, 0,
                /* request ID */ 0, 0, 0, 2, /* response expected */ 1, /* reserved */ 0, 0, 0,
                /* object key */ 0, 0, 0, 4, 0, 0, 0, 6, /* operation */ 0, 0, 0, 5, 'd', 'o', 'I', 't', 0,
                /* padding */ 0, 0, 0, /* principal */ 0, 0, 0, 0,

                'G', 'I', 'O', 'P', 1, 1, Message.FLAG_NO_FRAG_BIG_ENDIAN, Message.GIOPFragment,
                /* size */ 0, 0, 0, 2, /* short param */ 1, 1});
        processSocketMessageWithFragments(1);

        assertEquals(1, getMediators().size());
        MessageMediator mediator = getMediators().remove(0);
        assertEquals("doIt", mediator.getOperationName());
        assertEquals(2, mediator.getRequestId());
        assertFalse(mediator.isOneWay());
        assertEquals(257, (short) params.get(0));
    }

    private void processSocketMessageWithFragments(int numFragments) throws InterruptedException {
        BackgroundProcessor backgroundProcessor = new BackgroundProcessor(numFragments);
        getConnection().doWork();
        backgroundProcessor.waitUntilDone();
    }

    @Test
    public void whenRequest1_1ReceivedFromNioWithFragments_dispatchRequest() throws IOException, InterruptedException {
        final List<Short> params = new ArrayList<Short>();
        defineRequestDispatcher(new RequestDispatcher() {
            public void readParameters(CDRInputObject input) {
                params.add(input.read_short());
            }
        });
        readFromNio(new byte[]{'G', 'I', 'O', 'P', 1, 1, Message.MORE_FRAGMENTS_BIT,
                Message.GIOPRequest, /* size */ 0, 0, 0, 36, /* no service contexts */ 0, 0, 0, 0,
                /* request ID */ 0, 0, 0, 2, /* response expected */ 1, /* reserved */ 0, 0, 0,
                /* object key */ 0, 0, 0, 4, 0, 0, 0, 6, /* operation */ 0, 0, 0, 5, 'd', 'o', 'I', 't', 0,
                /* padding */ 0, 0, 0, /* principal */ 0, 0, 0, 0,

                'G', 'I', 'O', 'P', 1, 1, Message.FLAG_NO_FRAG_BIG_ENDIAN, Message.GIOPFragment,
                /* size */ 0, 0, 0, 2, /* short param */ 1, 1});
        processNioMessageWithFragments(1);

        assertEquals(1, getMediators().size());
        MessageMediator mediator = getMediators().remove(0);
        assertEquals("doIt", mediator.getOperationName());
        assertEquals(2, mediator.getRequestId());
        assertFalse(mediator.isOneWay());
        assertEquals(257, (short) params.get(0));
    }

    @Test
    public void whenRequest1_2ReceivedFromNioWithFragments_dispatchRequest() throws IOException, InterruptedException {
        final List<Short> params = new ArrayList<Short>();
        defineRequestDispatcher(new RequestDispatcher() {
            public void readParameters(CDRInputObject input) {
                params.add(input.read_short());
            }
        });
        readFromNio(new byte[]{'G', 'I', 'O', 'P', 1, 2, Message.MORE_FRAGMENTS_BIT,
                Message.GIOPRequest, /* size */ 0, 0, 0, 36,
                /* request ID */ 0, 0, 0, 2, /* response expected */ 1, /* reserved */ 0, 0, 0,
                /* use key */ 0, 0, /* padding */ 0, 0, /* object key */ 0, 0, 0, 4, 0, 0, 0, 6,
                /* operation */ 0, 0, 0, 8, 'g', 'o', 'A', 'g', 'a', 'i', 'n', 0,
                /* no service contexts */ 0, 0, 0, 0,

                'G', 'I', 'O', 'P', 1, 2, Message.FLAG_NO_FRAG_BIG_ENDIAN, Message.GIOPFragment,
                /* size */ 0, 0, 0, 6, /* request id */ 0, 0, 0, 2, /* short param */ 1, 1});
        processNioMessageWithFragments(1);

        assertEquals(1, getMediators().size());
        MessageMediator mediator = getMediators().remove(0);
        assertEquals("goAgain", mediator.getOperationName());
        assertEquals(2, mediator.getRequestId());
        assertFalse(mediator.isOneWay());
        assertEquals(257, (short) params.get(0));
    }

    private void processNioMessageWithFragments(int numFragments) throws InterruptedException {
        getConnection().doWork();
        Work work = getWorkQueue().remove();
        BackgroundProcessor backgroundProcessor = new BackgroundProcessor(numFragments);
        work.doWork();
        backgroundProcessor.waitUntilDone();
    }

    @Test
    public void whenCloseConnectionReceivedFromSocket_shutdownConnection() {
        readFromSocketWithoutChannelAndDispatch(new byte[]{'G', 'I', 'O', 'P', 1, 1, Message.FLAG_NO_FRAG_BIG_ENDIAN,
                Message.GIOPCloseConnection, /* size */ 0, 0, 0, 0});
        getConnection().doWork();
        assertEquals(1, getNumConnectionsRemoved());
        getConnection().clearDiscardedThrowable();
    }

    @Test(expected = RuntimeException.class)
    public void whenNioConfigureBlockingFails_throwException() throws IOException {
        SocketChannelFake socketChannel = getSocketChannel();
        socketChannel.setFailToConfigureBlocking();
        useNio();
    }

    @Test
    public void whenNioFullBufferWritable_allDataIsWritten() throws IOException {
        useNio();
        getConnection().write(ByteBuffer.wrap(BYTE_DATA));

        assertArrayEquals(BYTE_DATA, getSocketChannel().getDataWritten());
    }


    @Test
    public void whenNioChannelMomentarilyBusy_allDataIsWritten() throws IOException {
        useNio();
        getSocketChannel().setNumBytesToWrite(0);
        getConnection().write(ByteBuffer.wrap(BYTE_DATA));

        assertArrayEquals(BYTE_DATA, getSocketChannel().getDataWritten());
    }

    @Test
    public void whenNioWholeMessageReceived_queueSingleEntry() throws IOException {
        useNio();
        getSocketChannel().enqueData(new byte[]{'G', 'I', 'O', 'P', 1, 0, Message.FLAG_NO_FRAG_BIG_ENDIAN, Message.GIOPRequest, 0, 0, 0, 6, 1, 2, 3, 4, 5, 6});
        getConnection().doWork();
        assertEquals(1, getWorkQueue().size());
        assertTrue(getWorkQueue().remove() instanceof MessageMediator);
    }

    @Test
    public void whenNioMessageReceivedInTwoReads_queueSingleEntryAfterSecond() throws IOException {
        useNio();
        getSocketChannel().enqueData(new byte[]{'G', 'I', 'O', 'P', 1, 0, Message.FLAG_NO_FRAG_BIG_ENDIAN, Message.GIOPRequest, 0, 0, 0, 6, 1, 2, 3, 4, 5, 6});
        getSocketChannel().setNumBytesToRead(8, 0);
        getConnection().doWork();
        getConnection().doWork();
        assertEquals(1, getWorkQueue().size());
        assertTrue(getWorkQueue().remove() instanceof MessageMediator);
    }

    @Test
    public void whenNioFragmentsIncluded_queueFirstMessageAndAddFragmentsToFragmentList() throws IOException {
        useNio();
        byte[] messages = {'G', 'I', 'O', 'P', 1, 2, Message.MORE_FRAGMENTS_BIT, Message.GIOPRequest, 0, 0, 0, 6, 0, 0, 0, 3, 5, 6,
                          'G', 'I', 'O', 'P', 1, 2, Message.MORE_FRAGMENTS_BIT, Message.GIOPFragment, 0, 0, 0, 6, 0, 0, 0, 3, 5, 6,
                          'G', 'I', 'O', 'P', 1, 2, Message.FLAG_NO_FRAG_BIG_ENDIAN, Message.GIOPFragment, 0, 0, 0, 4, 0, 0, 0, 3};
        getSocketChannel().enqueData(messages);
        getConnection().doWork();
        assertEquals(1, getWorkQueue().size());
        Work workItem = getWorkQueue().remove();
        assertTrue(workItem instanceof MessageMediator);
        Queue<MessageMediator> fragmentList = getConnection().getFragmentList(new RequestIdImpl(3));
        assertEquals(2, fragmentList.size());
    }

    @Test
    public void whenMessageWithFragmentsReceivedFromSocket_dispatchEachPart() throws IOException {
        byte[] messages = {'G', 'I', 'O', 'P', 1, 2, Message.MORE_FRAGMENTS_BIT, Message.GIOPRequest, 0, 0, 0, 6, 0, 0, 0, 3, 5, 6,
                          'G', 'I', 'O', 'P', 1, 2, Message.MORE_FRAGMENTS_BIT, Message.GIOPFragment, 0, 0, 0, 6, 0, 0, 0, 3, 5, 6,
                          'G', 'I', 'O', 'P', 1, 2, Message.FLAG_NO_FRAG_BIG_ENDIAN, Message.GIOPFragment, 0, 0, 0, 4, 0, 0, 0, 3};
        readFromSocketWithoutChannelAndDispatch(messages);
        collectMediatorsAsDispatched();
        getConnection().doWork();
        assertEquals(1, getMediators().size());
        getConnection().doWork();
        getConnection().doWork();
        assertEquals(3, getMediators().size());
    }



}
