package icu.hilin.cti.freeswitch.client.outbound;

import org.freeswitch.esl.client.outbound.AbstractOutboundClientHandler;
import org.freeswitch.esl.client.outbound.AbstractOutboundPipelineFactory;
import org.freeswitch.esl.client.outbound.SocketClient;
import org.freeswitch.esl.client.transport.SendMsg;
import org.freeswitch.esl.client.transport.event.EslEvent;
import org.freeswitch.esl.client.transport.message.EslHeaders;
import org.freeswitch.esl.client.transport.message.EslMessage;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

public class OBClient implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        SocketClient obSocket = new SocketClient(8086, new AbstractOutboundPipelineFactory() {
            @Override
            protected AbstractOutboundClientHandler makeHandler() {
                return new AbstractOutboundClientHandler() {
                    @Override
                    protected void handleConnectResponse(ChannelHandlerContext ctx, EslEvent event) {
                        if (event.getEventName().equalsIgnoreCase("CHANNEL_DATA")) {
                            // now bridge the call
                            bridgeCall(ctx.getChannel(), event);
                        }
                    }

                    @Override
                    protected void handleEslEvent(ChannelHandlerContext ctx, EslEvent event) {

                    }

                    private void bridgeCall(Channel channel, EslEvent event) {
                        //随机找1个目标（注：这里只是演示目的，真正分配时，应该考虑到客服的忙闲情况，通常应该分给最空闲的客服）
                        String destNumber = "1003";

                        SendMsg bridgeMsg = new SendMsg();
                        bridgeMsg.addCallCommand("execute");
                        bridgeMsg.addExecuteAppName("bridge");
                        bridgeMsg.addExecuteAppArg("user/" + destNumber);

                        //同步发送bridge命令接通
                        EslMessage response = sendSyncMultiLineCommand(channel, bridgeMsg.getMsgLines());
                        if (response.getHeaderValue(EslHeaders.Name.REPLY_TEXT).startsWith("+OK")) {
                            String originCall = event.getEventHeaders().get("Caller-Destination-Number");
                            System.out.println(originCall + " bridge to " + destNumber + " successful");
                        } else {
                            System.out.println("Call bridge failed: " + response.getHeaderValue(EslHeaders.Name.REPLY_TEXT));
                        }
                    }
                };
            }
        });
        obSocket.start();
    }
}
