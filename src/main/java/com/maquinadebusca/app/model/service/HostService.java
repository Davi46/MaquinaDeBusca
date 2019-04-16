package com.maquinadebusca.app.model.service;
 
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Transient;

import org.hibernate.mapping.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.maquinadebusca.app.model.Documento;
import com.maquinadebusca.app.model.Host;
import com.maquinadebusca.app.model.Link;
import com.maquinadebusca.app.model.repository.HostRepository; 

@Service
public class HostService {

	@Autowired
	private HostRepository hr;

	public Host addHost(Host host) {
		return hr.save(host);
	}

	public List<Host> getHosts() {
		return hr.findAll();
	}

	public Host getHostById(long id) {
		Host host = hr.findById(id);
		return host;
	}

	public Host getByHost(String hostUrl) {
		for (Host host : getHosts()) {
			if (host.getHost().equals(hostUrl)) {
				return host;
			}
		}

		return null;
	}
	
	@Transactional(propagation = Propagation.REQUIRED)
	public Host addLink(Documento documento, String hostUrl) {
		Host host = getByHost(hostUrl); 
		
		if(host == null) { 
			host = new Host();
			host.setHost(hostUrl);
			host.getDocumentos().add(documento);
			host.setQtdPaginas(1);
		}else {
			host.getDocumentos().add(documento);
			host.setQtdPaginas(host.getQtdPaginas() + 1);   
		}
		
		host.addDocumento(documento); 
		
		return hr.save(host);
	} 
	
}
