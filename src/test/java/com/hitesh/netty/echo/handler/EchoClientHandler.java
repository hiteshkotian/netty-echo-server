package com.hitesh.netty.echo.handler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

	/**
	 * Called after connection to the server is established.
	 */
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		// When notified sends a request to the server.
		ctx.writeAndFlush(Unpooled.copiedBuffer("Netty is cool", CharsetUtil.UTF_8));
	}
	
	/**
	 * Called when the message is received from the server.
	 */
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in) throws Exception {
		System.out.println("Client received : " + in.toString(CharsetUtil.UTF_8));
	}
	
	/**
	 * On exception log the error and close the channel.
	 */
	@Override
	public void exceptionCaught (ChannelHandlerContext ctx, Throwable cause) {
		cause.printStackTrace();
		ctx.close();
	}

}
