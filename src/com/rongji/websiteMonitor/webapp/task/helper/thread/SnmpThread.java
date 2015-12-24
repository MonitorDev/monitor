package com.rongji.websiteMonitor.webapp.task.helper.thread;

import java.io.IOException;
import java.util.Vector;

import org.snmp4j.CommunityTarget;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.event.ResponseListener;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import com.rongji.websiteMonitor.common.SNMPConstants;
import com.rongji.websiteMonitor.persistence.SnmpModel;
import com.rongji.websiteMonitor.service.ServiceLocator;

public class SnmpThread implements Runnable {

	@Override
	public void run() {
		try {
			Snmp snmp = new Snmp(new DefaultUdpTransportMapping());
			snmp.listen();
			CommunityTarget target = new CommunityTarget();
			target.setCommunity(new OctetString("public"));
			target.setVersion(SnmpConstants.version2c);
			target.setAddress(new UdpAddress(SNMPConstants.getAddress()));
			target.setTimeout(3000); // 3s
			target.setRetries(1);
			PDU pdu = new PDU();
			// 系统cpu使用
			pdu.add(new VariableBinding(new OID(SNMPConstants.CPU_OID)));
			// 系统内存使用
			pdu.add(new VariableBinding(new OID(SNMPConstants.MEMORY_OID)));
			// jvm堆使用
			pdu.add(new VariableBinding(new OID(SNMPConstants.JVMHEAD_OID)));
			// jvm线程数
			pdu.add(new VariableBinding(new OID(SNMPConstants.JVMTHREAD_OID)));
			// 网络流量
			pdu.add(new VariableBinding(new OID(SNMPConstants.IFIN_OID)));
			pdu.setType(PDU.GET);
			snmp.send(pdu, target, null, new ResponseListener() {
				@Override
				public void onResponse(ResponseEvent event) {
					PDU response = event.getResponse();
					if (response == null) {
						System.out.println("TimeOut...");
					} else {
						if (response.getErrorStatus() == PDU.noError) {
							Vector<? extends VariableBinding> vbs = response
									.getVariableBindings();
							SnmpModel sm = new SnmpModel();
							for (VariableBinding vb : vbs) {
								if(SNMPConstants.CPU_OID.equals(vb.getOid().toString())) {
									sm.setCpuUsedRate(vb.toValueString());
								}else if(SNMPConstants.MEMORY_OID.equals(vb.getOid().toString())){
									sm.setMemoryUsedSize(vb.toValueString());
								}else if(SNMPConstants.JVMHEAD_OID.equals(vb.getOid().toString())){
									sm.setJvmHeapUsedSize(vb.toValueString());
								}else if(SNMPConstants.JVMTHREAD_OID.equals(vb.getOid().toString())){
									sm.setJvmTheadSize(vb.toValueString());
								}else if(SNMPConstants.IFIN_OID.equals(vb.getOid().toString())){
									sm.setIoUsedSize(vb.toValueString());
								}
							}
							ServiceLocator.getSnmpService().insertSnmp(sm);
							System.out.println(sm.getCpuUsedRate() + ";memory:" + sm.getMemoryUsedSize() + "; jvmhead:" + sm.getJvmHeapUsedSize() + "; jvmThread: " + sm.getJvmTheadSize() + "; io:" + sm.getIoUsedSize());
						}
					}
				}
			});
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
