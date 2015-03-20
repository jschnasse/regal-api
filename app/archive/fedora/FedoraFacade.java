/*
 * Copyright 2012 hbz NRW (http://www.hbz-nrw.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package archive.fedora;

import static archive.fedora.FedoraVocabulary.HAS_PART;
import static archive.fedora.FedoraVocabulary.IS_PART_OF;
import static archive.fedora.FedoraVocabulary.REL_HAS_MODEL;
import static archive.fedora.FedoraVocabulary.SIMPLE;
import helper.HttpArchiveException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import models.Link;
import models.Node;
import models.Transformer;

import org.openrdf.model.Statement;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.RepositoryResult;
import org.openrdf.repository.sail.SailRepository;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.sail.memory.MemoryStore;

import com.yourmediashelf.fedora.client.FedoraClient;
import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.FedoraCredentials;
import com.yourmediashelf.fedora.client.request.FedoraRequest;
import com.yourmediashelf.fedora.client.request.GetDatastream;
import com.yourmediashelf.fedora.client.request.GetDatastreamDissemination;
import com.yourmediashelf.fedora.client.request.GetNextPID;
import com.yourmediashelf.fedora.client.request.GetObjectProfile;
import com.yourmediashelf.fedora.client.request.Ingest;
import com.yourmediashelf.fedora.client.request.ListDatastreams;
import com.yourmediashelf.fedora.client.request.ModifyDatastream;
import com.yourmediashelf.fedora.client.request.PurgeObject;
import com.yourmediashelf.fedora.client.request.RiSearch;
import com.yourmediashelf.fedora.client.response.FedoraResponse;
import com.yourmediashelf.fedora.client.response.GetDatastreamResponse;
import com.yourmediashelf.fedora.client.response.GetNextPIDResponse;
import com.yourmediashelf.fedora.client.response.GetObjectProfileResponse;
import com.yourmediashelf.fedora.client.response.ListDatastreamsResponse;
import com.yourmediashelf.fedora.generated.access.DatastreamType;
import com.yourmediashelf.fedora.generated.management.PidList;

/**
 * The FedoraFacade implements all Fedora-Calls as a singleton
 * 
 * @author Jan Schnasse, schnasse@hbz-nrw.de
 */
public class FedoraFacade {
    @SuppressWarnings("serial")
    private class DeleteException extends HttpArchiveException {

	public DeleteException(int httpCode, Exception e) {
	    super(httpCode, e);
	}

	public DeleteException(int httpCode, String msg) {
	    super(httpCode, msg);
	}

    }

    @SuppressWarnings("serial")
    private class ReadNodeException extends HttpArchiveException {

	public ReadNodeException(int httpCode, Exception e) {
	    super(httpCode, e);
	}

	public ReadNodeException(int httpCode, String msg) {
	    super(httpCode, msg);
	}
    }

    @SuppressWarnings("serial")
    private class InitializeFedoraFacadeException extends HttpArchiveException {
	public InitializeFedoraFacadeException(int httpCode, Exception e) {
	    super(httpCode, e);
	}

	public InitializeFedoraFacadeException(int httpCode, String msg) {
	    super(httpCode, msg);
	}

    }

    @SuppressWarnings("serial")
    class UpdateContentModel extends HttpArchiveException {
	public UpdateContentModel(int httpCode, Exception e) {
	    super(httpCode, e);
	}
    }

    @SuppressWarnings("serial")
    class DeleteDatastreamException extends HttpArchiveException {
	public DeleteDatastreamException(int httpCode, Exception e) {
	    super(httpCode, e);
	}
    }

    @SuppressWarnings("serial")
    class GetPidException extends HttpArchiveException {
	public GetPidException(int httpCode, Exception e) {
	    super(httpCode, e);
	}
    }

    @SuppressWarnings("serial")
    class CreateNodeException extends HttpArchiveException {
	public CreateNodeException(int httpCode, Exception e) {
	    super(httpCode, e);
	}

	public CreateNodeException(int httpCode, String msg) {
	    super(httpCode, msg);
	}

    }

    @SuppressWarnings("serial")
    class SearchException extends HttpArchiveException {
	public SearchException(int httpCode, Exception e) {
	    super(httpCode, e);
	}
    }

    @SuppressWarnings("serial")
    class NodeNotFoundException extends HttpArchiveException {
	public NodeNotFoundException(int httpCode, Exception e) {
	    super(httpCode, e);
	}

	public NodeNotFoundException(int httpCode, String msg) {
	    super(httpCode, msg);
	}

    }

    static FedoraFacade me = null;
    Utils utils = null;

    /**
     * @param host
     *            The url of the fedora web endpoint
     * @param aUser
     *            A valid fedora user
     * @param aPassword
     *            The password of the fedora user
     */
    private FedoraFacade(String host, String aUser, String aPassword) {
	utils = new Utils(host, aUser);
	try {
	    FedoraCredentials credentials = new FedoraCredentials(host, aUser,
		    aPassword);
	    FedoraClient fedora = new com.yourmediashelf.fedora.client.FedoraClient(
		    credentials);
	    FedoraRequest.setDefaultClient(fedora);

	} catch (MalformedURLException e) {
	    throw new InitializeFedoraFacadeException(500, e);
	}

    }

    /**
     * @param host
     *            The url of the fedora web endpoint
     * @param aUser
     *            A valid fedora user
     * @param aPassword
     *            The password of the fedora user
     * @return a instance of FedoraFacade singleton
     */
    public static FedoraFacade getInstance(String host, String aUser,
	    String aPassword) {
	if (me == null)
	    return new FedoraFacade(host, aUser, aPassword);
	else
	    return me;
    }

    public void createNode(Node node) {
	try {
	    new Ingest(node.getPid()).label(node.getLabel()).execute();
	    DublinCoreHandler.updateDc(node);
	    List<Transformer> cms = node.getTransformer();
	    // utils.createContentModels(cms);
	    utils.linkContentModels(cms, node);
	    if (node.getUploadFile() != null) {
		utils.createManagedStream(node);
	    }
	    if (node.getMetadataFile() != null) {
		utils.createMetadataStream(node);
	    }
	    if (node.getSeqFile() != null) {
		utils.createSeqStream(node);
	    }
	    if (node.getConfFile() != null) {
		utils.createConfStream(node);
	    }
	    // utils.createRelsExt(node);
	    utils.updateRelsExt(node);
	} catch (Exception e) {
	    e.printStackTrace();
	    throw new CreateNodeException(500, e);
	}
    }

    public Node createRootObject(String namespace) {
	Node rootObject = null;
	String pid = getPid(namespace);
	rootObject = new Node();
	rootObject.setPID(pid);
	rootObject.setLabel("Default Object");
	rootObject.setNamespace(namespace);
	createNode(rootObject);
	return rootObject;
    }

    public Node readNode(String pid) {
	if (!nodeExists(pid))
	    throw new NodeNotFoundException(404, pid);
	Node node = new Node();
	node.setPID(pid);
	node.setNamespace(pid.substring(0, pid.indexOf(':')));
	getDublinCoreFromFedora(node);
	getRelsExtFromFedora(node);
	getDatesFromFedora(node);
	getChecksumFromFedora(node);
	getMetadataFromFedora(node);
	getDataFromFedora(pid, node);
	getConfFromFedora(pid, node);
	return node;
    }

    private void getDataFromFedora(String pid, Node node) {
	try {
	    FedoraResponse response = new GetDatastreamDissemination(pid, "seq")
		    .execute();
	    node.setSeq(CopyUtils.copyToString(response.getEntityInputStream(),
		    "utf-8"));
	} catch (Exception e) {
	    // datastream with name metadata is optional
	}
    }

    private void getConfFromFedora(String pid, Node node) {
	try {
	    FedoraResponse response = new GetDatastreamDissemination(pid,
		    "conf").execute();
	    node.setConf(CopyUtils.copyToString(
		    response.getEntityInputStream(), "utf-8"));
	} catch (Exception e) {
	    // datastream with name metadata is optional
	}
    }

    private void getMetadataFromFedora(Node node) {
	try {
	    FedoraResponse response = new GetDatastreamDissemination(
		    node.getPid(), "metadata").execute();
	    node.setMetadata(CopyUtils.copyToString(
		    response.getEntityInputStream(), "utf-8"));
	} catch (Exception e) {
	    // datastream with name metadata is optional
	}
    }

    private void getDublinCoreFromFedora(Node node) {
	try {
	    DublinCoreHandler.readFedoraDcToNode(node);

	} catch (FedoraClientException e) {
	    throw new ReadNodeException(500, e);
	}
    }

    private void getRelsExtFromFedora(Node node) {
	try {
	    utils.readRelsExt(node);
	} catch (FedoraClientException e) {
	    throw new ReadNodeException(500, e);
	}
    }

    private void getChecksumFromFedora(Node node) {
	try {
	    GetDatastreamResponse response = new GetDatastream(node.getPid(),
		    "data").execute();
	    node.setMimeType(response.getDatastreamProfile().getDsMIME());
	    node.setFileLabel(response.getDatastreamProfile().getDsLabel());
	    node.setChecksum(response.getDatastreamProfile().getDsChecksum());
	    node.setFileSize(response.getDatastreamProfile().getDsSize());
	} catch (FedoraClientException e) {
	    // datastream with name data is optional
	}
    }

    public void updateNode(Node node) {
	play.Logger.info("Update node in fedora");
	DublinCoreHandler.updateDc(node);
	List<Transformer> models = node.getTransformer();
	// utils.updateContentModels(models);
	node.removeRelations(REL_HAS_MODEL);
	if (node.getUploadFile() != null) {
	    utils.updateManagedStream(node);
	    getChecksumFromFedora(node);
	}
	if (node.getMetadataFile() != null) {
	    utils.updateMetadataStream(node);
	}
	if (node.getSeqFile() != null) {
	    utils.updateSeqStream(node);
	}
	if (node.getConfFile() != null) {
	    play.Logger.info("Write conf file to fedora");
	    utils.updateConfStream(node);
	}
	utils.linkContentModels(models, node);
	utils.updateRelsExt(node);
	getDatesFromFedora(node);
    }

    private void getDatesFromFedora(Node node) {
	try {
	    GetObjectProfileResponse prof = new GetObjectProfile(node.getPid())
		    .execute();
	    node.setLabel(prof.getLabel());
	    node.setLastModified(prof.getLastModifiedDate());
	    node.setCreationDate(prof.getCreateDate());
	} catch (FedoraClientException e) {
	    throw new ReadNodeException(500, e);
	}
    }

    public List<String> findPids(String rdfQuery, String queryFormat) {
	if (queryFormat.compareTo(FedoraVocabulary.SIMPLE) == 0) {
	    return utils.findPidsSimple(rdfQuery);
	} else {
	    return findPidsRdf(rdfQuery, queryFormat);
	}
    }

    public String getPid(String namespace) {
	try {
	    GetNextPIDResponse response = new GetNextPID().namespace(namespace)
		    .execute();
	    return response.getPid();
	} catch (FedoraClientException e) {
	    throw new GetPidException(e.getStatus(), e);
	}
    }

    public String[] getPids(String namespace, int number) {
	try {
	    GetNextPIDResponse response = new GetNextPID().namespace(namespace)
		    .numPIDs(number).execute();
	    PidList list = response.getPids();
	    String[] arr = new String[list.getPid().size()];
	    list.getPid().toArray(arr);
	    return arr;
	} catch (FedoraClientException e) {
	    throw new GetPidException(e.getStatus(), e);
	}
    }

    public void deleteNode(String rootPID) {
	try {
	    unlinkParent(rootPID);
	    new PurgeObject(rootPID).execute();
	} catch (FedoraClientException e) {

	    throw new DeleteException(e.getStatus(), e);
	}
    }

    public void deleteDatastream(String pid, String datastream) {
	try {
	    new ModifyDatastream(pid, datastream).dsState("D").execute();
	} catch (FedoraClientException e) {
	    throw new DeleteDatastreamException(e.getStatus(), e);
	}
    }

    public boolean nodeExists(String pid) {
	return utils.nodeExists(pid);
    }

    public void updateContentModels(List<Transformer> cms) {
	utils.updateContentModels(cms);
    }

    public InputStream findTriples(String query, String queryFormat,
	    String outputformat) {
	try {
	    FedoraResponse response = new RiSearch(query).format(outputformat)
		    .lang(queryFormat).type("triples").execute();
	    return response.getEntityInputStream();
	} catch (Exception e) {
	    throw new SearchException(500, e);
	}
    }

    public String removeUriPrefix(String pred) {
	return utils.removeUriPrefix(pred);
    }

    public String addUriPrefix(String pid) {
	return utils.addUriPrefix(pid);
    }

    public List<String> findNodes(String searchTerm) {
	return findPids(searchTerm, SIMPLE);
    }

    public void readDcToNode(Node node, InputStream in, String dcNamespace) {
	DublinCoreHandler.readDcToNode(node, in, dcNamespace);
    }

    public List<Node> deleteComplexObject(String rootPID) {
	if (!nodeExists(rootPID)) {
	    throw new NodeNotFoundException(404, "Can not find: " + rootPID);
	}
	List<Node> list = listComplexObject(rootPID);
	for (Node n : list) {
	    deleteNode(n.getPid());
	}
	return list;
    }

    public List<Node> listComplexObject(String rootPID) {
	List<Node> result = new ArrayList<Node>();
	if (!nodeExists(rootPID)) {
	    throw new NodeNotFoundException(404, "Can not find: " + rootPID);
	}
	Node root = new Node(rootPID);
	try {
	    root = readNode(rootPID);
	} catch (Exception e) {
	    play.Logger.error(rootPID, e);
	}
	result.add(root);
	result.addAll(listChildren(root));
	return result;

    }

    private List<Node> listChildren(Node root) {
	List<Node> result = new ArrayList<Node>();
	try {
	    List<Link> rels = root.getRelsExt();
	    for (Link r : rels) {
		if (HAS_PART.equals(r.getPredicate())) {
		    result.addAll(listComplexObject(r.getObject()));
		}
	    }
	} catch (Exception e) {
	    play.Logger.error(root.getPid(), e);
	}
	return result;
    }

    public String getNodeParent(Node node) {
	List<Link> links = node.getRelsExt();
	for (Link link : links) {
	    if (link.getPredicate().compareTo(IS_PART_OF) == 0) {
		return link.getObject();
	    }
	}
	return null;
    }

    void unlinkParent(String pid) {
	try {
	    Node node = readNode(pid);
	    if (node.getParentPid() == null)
		return;
	    Node parent = readNode(node.getParentPid());
	    parent.removeRelation(HAS_PART, node.getPid());
	    updateNode(parent);
	} catch (NodeNotFoundException e) {
	    play.Logger.debug("", e);
	} catch (ReadNodeException e) {
	    play.Logger.debug("", e);
	}
    }

    public void unlinkParent(Node node) {
	Node parent = readNode(node.getParentPid());
	parent.removeRelation(HAS_PART, node.getPid());
	updateNode(parent);
    }

    public void linkToParent(Node node, String parentPid) {
	node.removeRelations(IS_PART_OF);
	Link link = new Link();
	link.setPredicate(IS_PART_OF);
	link.setObject(parentPid, false);
	node.addRelation(link);
	node.setParentPid(parentPid);
	updateNode(node);
    }

    public void linkParentToNode(String parentPid, String pid) {
	try {
	    Node parent = readNode(parentPid);
	    Link link = new Link();
	    link.setPredicate(HAS_PART);
	    link.setObject(pid, false);
	    parent.addRelation(link);
	    updateNode(parent);
	} catch (NodeNotFoundException e) {
	    // Nothing to do
	    // logger.debug(pid +
	    // " has no parent! ParentPid: "+parentPid+" is not a valid pid.");
	}
    }

    public boolean dataStreamExists(String pid, String datastreamId) {
	try {
	    ListDatastreamsResponse response = new ListDatastreams(pid)
		    .execute();
	    for (DatastreamType ds : response.getDatastreams()) {
		if (ds.getDsid().compareTo(datastreamId) == 0) {
		    GetDatastreamResponse r = new GetDatastream(pid,
			    datastreamId).execute();
		    if (r.getDatastreamProfile().getDsState().equals("D"))
			return false;
		    else
			return true;
		}
	    }
	} catch (FedoraClientException e) {
	    return false;
	}
	return false;
    }

    private List<String> findPidsRdf(String rdfQuery, String queryFormat) {
	InputStream stream = findTriples(rdfQuery, FedoraVocabulary.SPO,
		FedoraVocabulary.N3);
	List<String> resultVector = new Vector<String>();
	RepositoryConnection con = null;
	Repository myRepository = new SailRepository(new MemoryStore());
	try {
	    myRepository.initialize();
	    con = myRepository.getConnection();
	    String baseURI = "";
	    con.add(stream, baseURI, RDFFormat.N3);
	    RepositoryResult<Statement> statements = con.getStatements(null,
		    null, null, true);
	    while (statements.hasNext()) {
		Statement st = statements.next();
		String str = removeUriPrefix(st.getSubject().stringValue());
		resultVector.add(str);
	    }
	    return resultVector;
	} catch (RepositoryException e) {
	    throw new RdfException(rdfQuery, e);
	} catch (RDFParseException e) {
	    throw new RdfException(rdfQuery, e);
	} catch (IOException e) {
	    throw new RdfException(rdfQuery, e);
	} finally {
	    if (con != null) {
		try {
		    con.close();
		} catch (RepositoryException e) {
		    throw new RdfException(
			    rdfQuery + ". Can not close stream.", e);
		}
	    }
	}
    }
}