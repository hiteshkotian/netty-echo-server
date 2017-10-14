package com.hitesh.netty.echo.handler;

import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.util.CharsetUtil;

import java.nio.charset.Charset;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;

/**
 * 
 * @author hckotian
 *
 */

@Sharable // Indicates that the ChannelHandler can be safely shared by multiple channels.
public class EchoServerHandler extends ChannelInboundHandlerAdapter {
	
	/**
	 * Method called for each incoming request.
	 */
	@Override
	public void channelRead (ChannelHandlerContext ctx, Object msg) {
		if (null == msg) {
			System.out.println("Is null");
			ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
				.addListener(ChannelFutureListener.CLOSE);
		}
		ByteBuf in = (ByteBuf) msg;
		// Log the message to the console.
		System.out.println("Server Received " + in.toString(CharsetUtil.UTF_8));
		// Writes the received message to the sender without
		// flushing the outbound messages.
		ctx.write(in);
	}
	
	/**
	 * Notifies the handler that the last call made to channelRead()
	 * was the last message in the current batch.
	 */
	@Override
	public void channelReadComplete (ChannelHandlerContext ctx) {
		System.out.println("Received last request");
		// Flushes pending messages to the remote peer and closes the
		// channel.
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
			.addListener(ChannelFutureListener.CLOSE);
	}
	
	/**
	 * Called if an exception is caught during the read operation.
	 */
	@Override
	public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) {
		// If there is a class cast Exception then return an error message.
		if (cause instanceof ClassCastException) {
			System.out.println("This is probably in invalid string");
			ctx.writeAndFlush(Unpooled.copiedBuffer("Check the request", Charset.defaultCharset()));
			
		} else {
			cause.printStackTrace();
		}
		ctx.close();
	}
}
