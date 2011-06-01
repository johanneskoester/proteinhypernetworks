/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package proteinHypernetworkVisualization.visualization;

import edu.uci.ics.jung.graph.Graph;
import edu.uci.ics.jung.graph.util.EdgeType;
import edu.uci.ics.jung.graph.util.Pair;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.apache.xmlrpc.XmlRpcException;
import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfig;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;

/**
 *
 * @author Johannes Köster <johannes.koester@tu-dortmund.de>
 */
public class CytoscapeVisualization {

  private XmlRpcClient client;
  private String networkId;
  private Graph graph;
  private URL url;

  public CytoscapeVisualization(Graph graph) {
    this(graph, null);
  }

  public CytoscapeVisualization(Graph graph, URL url) {
    this.graph = graph;
    if (url == null) {
      try {
        this.url = new URL("http://localhost:9000/Cytoscape");
      } catch (MalformedURLException ex) {
        // ignore
      }
    } else {
      this.url = url;
    }
  }

  public void visualize() throws XmlRpcException {
    XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
    config.setServerURL(url);
    client = new XmlRpcClient();
    client.setConfig((XmlRpcClientConfig) config);

    networkId = (String) client.execute("Cytoscape.createNetwork",
            new Object[]{"Protein Hypernetwork Visualization"});

    addVertices(networkId);
    addEdges(networkId);

    client.execute("Cytoscape.performDefaultLayout", new Object[]{networkId});
    client.execute("Cytoscape.setVisualStyle", new Object[]{"Solid"});
  }

  private void addVertices(String networkId) throws XmlRpcException {
    List<String> vertexIds = new ArrayList<String>(graph.getVertexCount());
    for (Object v : graph.getVertices()) {
      vertexIds.add(v.toString());
    }

    client.execute("Cytoscape.createNodes", new Object[]{networkId, vertexIds});
  }

  private void addEdges(String networkId) throws XmlRpcException {
    List<String> from = new ArrayList<String>(graph.getEdgeCount());
    List<String> to = new ArrayList<String>(graph.getEdgeCount());
    List<String> type = new ArrayList<String>(graph.getEdgeCount());
    List<Boolean> directed = new ArrayList<Boolean>(graph.getEdgeCount());

    for (Object e : graph.getEdges()) {
      Pair ep = graph.getEndpoints(e);
      from.add(ep.getFirst().toString());
      to.add(ep.getSecond().toString());
      type.add("");
      directed.add(graph.getEdgeType(e) == EdgeType.DIRECTED);
    }

    client.execute("Cytoscape.createEdges", new Object[]{networkId, from, to, type, directed, true});
  }

  public void addVertexAttributes(java.lang.String name,
                                 java.lang.String type,
                                 java.util.Map<java.lang.String,java.lang.Object> values) throws XmlRpcException {

    Boolean ok = (Boolean)client.execute("Cytoscape.addNodeAttributes", new Object[]{name, type, values, true});
    return;
  }
}
