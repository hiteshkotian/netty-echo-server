package com.hitesh.test;

import java.nio.charset.Charset;

import org.junit.Test;

import com.hitesh.netty.echo.handler.EchoServerHandler;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.CharsetUtil;
import junit.framework.Assert;

/**
 * Unit test for {@linkplain EchoServerHandler} channel handler.
 * 
 * @author hckotian
 *
 */
public class EchoServerHandlerTest {
	
	/**
	 * Check if the echo server handler will return the test string as is without any case change.
	 */
	@Test
	public void testEchoServerHandlerResponse () {
		String testString = "This is a test string";
		
		EmbeddedChannel ch = new EmbeddedChannel(new EchoServerHandler());
		ch.writeInbound(Unpooled.copiedBuffer(testString, Charset.defaultCharset()));
		ByteBuf val = ch.readOutbound();
		String readValue = val.toString(CharsetUtil.UTF_8);
		System.out.println("read value : " + readValue);
		Assert.assertNotNull(readValue);
		Assert.assertEquals(testString, readValue);
		
		ch.finish();
	}
	
	/**
	 * Check if the echo server handler will return an error message when supplied with an empty string
	 * as input.
	 */
	@Test
	public void testEchoServerHandlerForNull () {
		String testString = "";
		
		EmbeddedChannel ch = new EmbeddedChannel(new EchoServerHandler());
		ch.writeInbound(testString);
		ByteBuf val = ch.readOutbound();
		System.out.println("Val : " + val);
		String readValue = val.toString(CharsetUtil.UTF_8);
		
		Assert.assertNotNull(readValue);
		Assert.assertTrue(!testString.equals(readValue));
		ch.finish();
	}
}
