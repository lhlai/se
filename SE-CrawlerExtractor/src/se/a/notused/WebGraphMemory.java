package se.a.notused;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
//�ڴ�洢
public class WebGraphMemory {
	// ��ÿ��URLӳ��Ϊһ���������洢��webͼ��
	private Map<Integer, String> IdentifyerToURL;

	/**
	 * A Map storing relationships from URLs to numeric identifiers, usefull for
	 * storing Web graphs
	 */
	private Map<String, Map<String, Integer>> URLToIdentifyer;

	/**
	 * �洢��ȣ�����������һ��������URL��ID���ڶ��������Ǵ��ָ�����URL���ӵ�Map��Double��ʾȨ��
	 */
	private Map<Integer, Map<Integer, Double>> InLinks;

	/**
	 * �洢���ȣ����е�һ��������URL��ID���ڶ��������Ǵ����ҳ�еĳ����ӣ�Double��ʾȨ��
	 */
	private Map<Integer, Map<Integer, Double>> OutLinks;
	/** ͼ�нڵ����Ŀ */
	private int nodeCount;

	/**
	 * ���캯����0���ڵ�Ĺ��캯��
	 */
	public WebGraphMemory() {
		IdentifyerToURL = new HashMap<Integer, String>();
		URLToIdentifyer = new HashMap<String, Map<String, Integer>>();
		InLinks = new HashMap<Integer, Map<Integer, Double>>();
		OutLinks = new HashMap<Integer, Map<Integer, Double>>();
		nodeCount = 0;
	}

	/**
	 * ��һ���ı��ļ���ȡ�ýڵ�Ĺ��캯���� ÿ�а���һ��ָ���ϵ�����磺 http://url1.com -> http://url2.com 1.0
	 * ��ʾ "http://url1.com" ����һ�������� "http://url2.com", ������������ӵ�Ȩ����1.0
	 */
	public WebGraphMemory(File file) throws IOException, FileNotFoundException {
		this();
		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		while ((line = reader.readLine()) != null) {
			int index1 = line.indexOf("->");
			if (index1 == -1)
				addLink(line.trim());
			else {
				String url1 = line.substring(0, index1).trim();
				String url2 = line.substring(index1 + 2).trim();
				Double strength = new Double(1.0);
				index1 = url2.indexOf(" ");
				if (index1 != -1)
					try {
						strength = new Double(url2.substring(index1 + 1).trim());
						url2 = url2.substring(0, index1).trim();
					} catch (Exception e) {
					}
				addLink(url1, url2, strength);
			}
		}
	}

	/**
	 * ����URL�ƶ�����ID
	 */
	public Integer URLToIdentifyer(String URL) {
		String host;
		String name;
		int index = 0, index2 = 0;
		if (URL.startsWith("http://"))
			index = 7;
		else if (URL.startsWith("ftp://"))
			index = 6;
		index2 = URL.substring(index).indexOf("/");
		if (index2 != -1) {
			name = URL.substring(index + index2 + 1);
			host = URL.substring(0, index + index2);
		} else {
			host = URL;
			name = "";
		}
		// System.out.println("host:"+host + " name:"+name);
		Map<String, Integer> map = (URLToIdentifyer.get(host));
		if (map == null) {
			// System.out.println("will return null");
			return null;
		}
		// System.out.println("return:"+map.get(""));
		return (map.get(name));
	}

	/**
	 * ����ID���URL
	 */
	public String IdentifyerToURL(Integer id) {
		return (IdentifyerToURL.get(id));
	}

	/**
	 * ��ͼ������һ���ڵ�
	 */
	public Integer addLink(String link) {
		Integer id = URLToIdentifyer(link);
		if (id == null) {
			id = new Integer(++nodeCount);
			String host;
			String name;
			int index = 0, index2 = 0;
			if (link.startsWith("http://"))
				index = 7;
			else if (link.startsWith("ftp://"))
				index = 6;
			index2 = link.substring(index).indexOf("/");
			if (index2 != -1) {
				name = link.substring(index + index2 + 1);
				host = link.substring(0, index + index2);
			} else {
				host = link;
				name = "";
			}
			// System.out.println("HOST:"+host + " name:"+name);
			Map<String, Integer> map = (URLToIdentifyer.get(host));
			if (map == null) {
				map = new HashMap<String, Integer>();
				URLToIdentifyer.put(host, map);
			}
			map.put(name, id);

			// error here
			// URLToIdentifyer.put(link,map);
			IdentifyerToURL.put(id, link);
			InLinks.put(id, new HashMap<Integer, Double>());
			OutLinks.put(id, new HashMap<Integer, Double>());
			// System.out.println("id:"+id);
		}
		return id;
	}

	/**
	 * �������ڵ�������һ����Ӧ��ϵ������ڵ㲻���ڣ����´����ڵ�
	 */
	public Double addLink(String fromLink, String toLink, Double weight) {
		Integer id1 = addLink(fromLink);
		Integer id2 = addLink(toLink);
		return addLink(id1, id2, weight);
	}

	/**
	 * �������ڵ�������һ����Ӧ��ϵ������ڵ㲻���ڣ����´����ڵ�
	 */
	private Double addLink(Integer fromLink, Integer toLink, Double weight) {
		// System.out.println("from "+fromLink+" to "+toLink);
		Double aux;
		Map<Integer, Double> map1 = (InLinks.get(toLink));
		Map<Integer, Double> map2 = (OutLinks.get(fromLink));
		aux = (Double) (map1.get(fromLink));
		if (aux == null)
			map1.put(fromLink, weight);
		else if (aux.doubleValue() < weight.doubleValue())
			map1.put(fromLink, weight);
		else
			weight = new Double(aux.doubleValue());

		aux = (map2.get(toLink));
		if (aux == null)
			map2.put(toLink, weight);
		else if (aux.doubleValue() < weight.doubleValue())
			map2.put(toLink, weight);
		else {
			weight = new Double(aux.doubleValue());
			map1.put(fromLink, weight);
		}
		InLinks.put(toLink, map1);
		OutLinks.put(fromLink, map2);
		return weight;
	}

	/**
	 * ���ָ����URL���ذ���������ȵ����ӵ�Map
	 */
	public Map inLinks(String URL) {
		Integer id = URLToIdentifyer(URL);
		return inLinks(id);
	}

	/**
	 * ���ָ����URL���ذ���������ȵ����ӵ�Map
	 */
	public Map<Integer, Double> inLinks(Integer link) {
		if (link == null)
			return null;
		Map<Integer, Double> aux = (InLinks.get(link));
		return aux;
	}

	/**
	 * ���ָ����URL���ذ������ĳ��ȵ����ӵ�Map
	 */
	public Map<Integer, Double> outLinks(String URL) {
		Integer id = URLToIdentifyer(URL);
		return outLinks(id);
	}

	/**
	 * ���ָ����URL���ذ������ĳ��ȵ����ӵ�Map
	 */
	public Map<Integer, Double> outLinks(Integer link) {
		if (link == null)
			return null;
		Map<Integer, Double> aux = OutLinks.get(link);
		return aux;
	}

	/**
	 * ���������ڵ�֮���Ȩ�أ�����ڵ�û�����ӣ��ͷ���0
	 */
	public Double inLink(String fromLink, String toLink) {
		Integer id1 = URLToIdentifyer(fromLink);
		Integer id2 = URLToIdentifyer(toLink);
		return inLink(id1, id2);
	}

	/**
	 * ���������ڵ�֮���Ȩ�أ�����ڵ�û�����ӣ��ͷ���0
	 */
	public Double outLink(String fromLink, String toLink) {
		Integer id1 = URLToIdentifyer(fromLink);
		Integer id2 = URLToIdentifyer(toLink);
		return outLink(id1, id2);
	}

	/**
	 * ���������ڵ�֮���Ȩ�أ�����ڵ�û�����ӣ��ͷ���0
	 */
	public Double inLink(Integer fromLink, Integer toLink) {
		Map<Integer, Double> aux = inLinks(toLink);
		if (aux == null)
			return new Double(0);
		Double weight = (aux.get(fromLink));
		return (weight == null) ? new Double(0) : weight;
	}

	/**
	 * ���������ڵ�֮���Ȩ�أ�����ڵ�û�����ӣ��ͷ���0
	 */
	public Double outLink(Integer fromLink, Integer toLink) {
		Map<Integer, Double> aux = outLinks(fromLink);
		if (aux == null)
			return new Double(0);
		Double weight = (aux.get(toLink));
		return (weight == null) ? new Double(0) : weight;
	}

	/**
	 * ������ͼ��Ϊ����ͼ��
	 */
	public void transformUnidirectional() {
		Iterator it = OutLinks.keySet().iterator();
		while (it.hasNext()) {
			Integer link1 = (Integer) (it.next());
			Map auxMap = (Map) (OutLinks.get(link1));
			Iterator it2 = auxMap.keySet().iterator();
			while (it2.hasNext()) {
				Integer link2 = (Integer) (it.next());
				Double weight = (Double) (auxMap.get(link2));
				addLink(link2, link1, weight);
			}
		}
	}

	/**
	 * ɾ���ڲ����ӣ��ڲ����Ӿ���ָ��ͬһ�����ϵ�����
	 */
	public void removeInternalLinks() {
		int index1;
		Iterator it = OutLinks.keySet().iterator();
		while (it.hasNext()) {
			Integer link1 = (Integer) (it.next());
			Map<Integer, Double> auxMap = (OutLinks.get(link1));
			Iterator it2 = auxMap.keySet().iterator();
			if (it2.hasNext()) {
				String URL1 = (String) (IdentifyerToURL.get(link1));
				index1 = URL1.indexOf("://");
				if (index1 != -1)
					URL1 = URL1.substring(index1 + 3);
				index1 = URL1.indexOf("/");
				if (index1 != -1)
					URL1 = URL1.substring(0, index1);
				while (it2.hasNext()) {
					Integer link2 = (Integer) (it.next());
					String URL2 = (String) (IdentifyerToURL.get(link2));
					index1 = URL2.indexOf("://");
					if (index1 != -1)
						URL2 = URL1.substring(index1 + 3);
					index1 = URL2.indexOf("/");
					if (index1 != -1)
						URL2 = URL1.substring(0, index1);
					if (URL1.equals(URL2)) {
						auxMap.remove(link2);
						OutLinks.put(link1, auxMap);
						auxMap = (InLinks.get(link2));
						auxMap.remove(link1);
						InLinks.put(link2, auxMap);
					}
				}
			}
		}
	}

	/**
	 * ɾ���ڲ��������ӡ�
	 */
	public void removeNepotistic() {
		removeInternalLinks();
	}

	/**
	 * ɾ�� stop URLs.��
	 */
	public void removeStopLinks(String stopURLs[]) {
		HashMap aux = new HashMap();
		for (int i = 0; i < stopURLs.length; i++)
			aux.put(stopURLs[i], null);
		removeStopLinks(aux);
	}

	/**
	 * ɾ�� stop URLs��
	 */
	public void removeStopLinks(Map stopURLs) {
		int index1;
		Iterator it = OutLinks.keySet().iterator();
		while (it.hasNext()) {
			Integer link1 = (Integer) (it.next());
			String URL1 = (String) (IdentifyerToURL.get(link1));
			index1 = URL1.indexOf("://");
			if (index1 != -1)
				URL1 = URL1.substring(index1 + 3);
			index1 = URL1.indexOf("/");
			if (index1 != -1)
				URL1 = URL1.substring(0, index1);
			if (stopURLs.containsKey(URL1)) {
				OutLinks.put(link1, new HashMap());
				InLinks.put(link1, new HashMap());
			}
		}
	}

	public int numNodes() {
		return nodeCount;
	}
}
