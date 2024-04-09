package icu.hilin.cti.core.entity;

import cn.hutool.json.JSONConfig;
import cn.hutool.json.JSONUtil;
import lombok.Data;

import java.util.*;

@Data
public class Channel {

    private String uuid, direction, created, created_epoch, name, state, cid_name, cid_num, ip_addr, dest, application, application_data, dialplan, context, read_codec, read_rate, read_bit_rate, write_codec, write_rate, write_bit_rate, secure, hostname, presence_id, presence_data, accountcode, callstate, callee_name, callee_num, callee_direction, call_uuid, sent_callee_name, sent_callee_num, initial_cid_name, initial_cid_num, initial_ip_addr, initial_dest, initial_dialplan, initial_context;


    public static void main(String[] args) {
        List<String> list = Arrays.asList("uuid,direction,created,created_epoch,name,state,cid_name,cid_num,ip_addr,dest,application,application_data,dialplan,context,read_codec,read_rate,read_bit_rate,write_codec,write_rate,write_bit_rate,secure,hostname,presence_id,presence_data,accountcode,callstate,callee_name,callee_num,callee_direction,call_uuid,sent_callee_name,sent_callee_num,initial_cid_name,initial_cid_num,initial_ip_addr,initial_dest,initial_dialplan,initial_context",
                "e4bf0c36-af1d-4317-893d-ec57545fc33f,inbound,2024-04-08 16:43:12,1712565792,sofia/internal/1002@192.168.2.20:5060,CS_EXECUTE,1002,1002,192.168.0.61,1003,bridge,user/1003,XML,default,,,,,,,,localhost.localdomain,1002@192.168.2.20,,1002,RINGING,,,,,,,1002,1002,192.168.0.61,1003,XML,default",
                "de32f280-ac63-4c26-8ee8-8ca238962cef,outbound,2024-04-08 16:43:12,1712565792,sofia/internal/1003@192.168.0.218:40161,CS_CONSUME_MEDIA,Extension 1002,1002,192.168.0.61,1003,,,XML,default,,,,,,,,localhost.localdomain,1003@192.168.2.20,,,RINGING,Outbound Call,1003,,e4bf0c36-af1d-4317-893d-ec57545fc33f,,,Extension 1002,1002,192.168.0.61,1003,XML,default");
        List<String> keys = new ArrayList<>();
        List<Channel> channels = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            if (i == 0) {
                keys = Arrays.asList(list.get(i).split(","));
            }else{
                Map<String,String> map = new HashMap<>();
                for (int i1 = 0; i1 < keys.size(); i1++) {
                    map.put(keys.get(i1),list.get(i).split(",")[i1]);
                }
                channels.add(JSONUtil.toBean(JSONUtil.toJsonStr(map), Channel.class));
            }
        }
        System.out.println(JSONUtil.toJsonStr(channels,new JSONConfig().setNatureKeyComparator()));
        System.out.println(new Date());
    }

}
