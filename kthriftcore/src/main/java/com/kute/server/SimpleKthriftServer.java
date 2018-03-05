package com.kute.server;

import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.ServerSocket;

/**
 * created by kute on 2018/03/04 13:22
 */
public class SimpleKthriftServer extends AbstractKthriftServer {

    private static final Logger logger = LoggerFactory.getLogger(SimpleKthriftServer.class);

    @Override
    public void start(boolean daemon) throws Exception {

        ServerSocket socket = new ServerSocket(port);

        TServerSocket serverTransport = new TServerSocket(socket);

        TServer.Args tArgs = new TServer.Args(serverTransport);
        tArgs.processor(tprocessor);
        tArgs.protocolFactory(new TBinaryProtocol.Factory());

        TServer server = new TSimpleServer(tArgs);

        logger.info("Thrift server started in {}:{}", hostName, port);
        server.serve();

    }

}
