package com.hitesh.netty.echo;

import java.net.InetSocketAddress;

import com.hitesh.netty.echo.handler.EchoServerHandler;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

/**
 * Echo Server.
 * 
 * @author hckotian
 *
 */
public class EchoServer {
	private final int port;
	
	public EchoServer (int port) {
		this.port = port;
	}
	
	public void start () throws Exception {
		final EchoServerHandler serverHandler = new EchoServerHandler();
		// Create the EventLoopGroup.
		EventLoopGroup group = new NioEventLoopGroup ();
		try {
			// Create the bootstrap server.
			ServerBootstrap b = new ServerBootstrap();
			b.group(group)
				// Specifies the use of a NIO transport channel.
				.channel(NioServerSocketChannel.class)
				// Sets the socket address using the specified port.
				.localAddress(new InetSocketAddress(port))
				// Adds an EchoServerHandler to the Channel's ChannelPipeline.
				.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						// Since EchoServerHandler is sharable we can always use the same instance.
						ch.pipeline().addLast(serverHandler);
					}
					
				});
			// Binds the server asynchronously. sync () waits for the 
			// bind to complete.
			ChannelFuture f = b.bind().sync();
			// Gets the CloseFuture of the channel and blocks
			// the current thread until it's complete.
			f.channel().closeFuture().sync();
		} finally {
			// Shuts down the EventLoopGroup, release all resources.
			group.shutdownGracefully().sync();
		}
	}
	
	public static void main (String[] args) {
		if (args.length != 1) {
			System.err.println("Usage : " + EchoServer.class.getSimpleName()
					+ " <port>");
		}
		int port = Integer.parseInt(args[0]);
		try {
			new EchoServer(port).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
