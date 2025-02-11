/*
 * Copyright 2014 hbz NRW (http://www.hbz-nrw.de/)
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
package models;

import java.io.StringWriter;
import java.util.List;
import java.util.Vector;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.wordnik.swagger.core.util.JsonUtil;

/**
 * @author Jan Schnasse, schnasse@hbz-nrw.de
 * 
 */

public class DublinCoreData implements java.io.Serializable {

	List<String> contributor = new Vector<>();
	List<String> coverage = new Vector<>();
	List<String> creator = new Vector<>();
	List<String> date = new Vector<>();
	List<String> description = new Vector<>();
	List<String> format = new Vector<>();
	List<Pair<String, String>> identifier = new Vector<>();
	List<String> language = new Vector<>();
	List<String> publisher = new Vector<>();
	List<String> relation = new Vector<>();
	List<String> rights = new Vector<>();
	List<String> source = new Vector<>();
	List<String> subject = new Vector<>();
	List<String> title = new Vector<>();
	List<String> type = new Vector<>();
	List<String> wglcontributor = new Vector<>();
	List<String> wglSubject = new Vector<>();
	List<String> wglType = new Vector<>();

	/**
	 * @return wgl:wglcontributor
	 */
	public List<String> getWglType() {
		return removeDuplicateEntries(wglType);
	}

	/**
	 * @param cwgl:wglcontributor
	 * @return this
	 */
	public DublinCoreData setWglType(List<String> wglType) {
		this.wglType = wglType;
		return this;
	}

	/**
	 * @param e wgl:wglcontributor
	 * @return this
	 */
	public DublinCoreData addWglType(String e) {
		wglType.add(e);
		return this;
	}

	/**
	 * @return wgl:wglsubject
	 */
	public List<String> getWglSubject() {
		return removeDuplicateEntries(wglSubject);
	}

	/**
	 * @param cwgl:wglsubject
	 * @return this
	 */
	public DublinCoreData setWglSubject(List<String> wglSubject) {
		this.wglSubject = wglSubject;
		return this;
	}

	/**
	 * @param e wgl:wglsubject
	 * @return this
	 */
	public DublinCoreData addWglSubject(String e) {
		wglSubject.add(e);
		return this;
	}

	/**
	 * @return wgl:wglcontributor
	 */
	public List<String> getWglContributor() {
		return wglcontributor;
	}

	/**
	 * @param cwgl:wglcontributor
	 * @return this
	 */
	public DublinCoreData setWglContributor(List<String> wglContributor) {
		this.wglcontributor = wglContributor;
		return this;
	}

	/**
	 * @param e wgl:wglcontributor
	 * @return this
	 */
	public DublinCoreData addWglContributor(String e) {
		wglcontributor.add(e);
		return this;
	}

	/**
	 * @return wgl:wglcontributor
	 */
	public String getFirstWglContributor() {
		List<String> elements = getWglContributor();
		if (elements == null || elements.size() == 0) {
			return "";
		}

		return elements.get(0);
	}

	/**
	 * @return dc:contributer
	 */
	public List<String> getContributor() {
		return contributor;
	}

	/**
	 * @param contributor dc:contributor
	 * @return this
	 */
	public DublinCoreData setContributor(List<String> contributor) {
		this.contributor = contributor;
		return this;
	}

	/**
	 * @param e dc:contributer
	 * @return this
	 */
	public DublinCoreData addContributor(String e) {
		contributor.add(e);
		return this;
	}

	/**
	 * @return dc:contributer
	 */
	public String getFirstContributor() {
		List<String> elements = getContributor();
		if (elements == null || elements.size() == 0) {
			return "";
		}
		return elements.get(0);
	}

	/**
	 * @return dc:coverae
	 */
	public List<String> getCoverage() {
		return coverage;
	}

	/**
	 * @param coverage dc:coverage
	 * @return this
	 */
	public DublinCoreData setCoverage(List<String> coverage) {
		this.coverage = coverage;
		return this;
	}

	/**
	 * @param e dc:coverage
	 * @return this
	 */
	public DublinCoreData addCoverage(String e) {
		coverage.add(e);
		return this;
	}

	/**
	 * @return dc:coverage
	 */
	public String getFirstCoverage() {
		List<String> elements = getCoverage();
		if (elements == null || elements.size() == 0) {
			return "";
		}

		return elements.get(0);
	}

	/**
	 * @return dc:creator
	 */
	public List<String> getCreator() {
		return creator;
	}

	/**
	 * @param creator dc:creator
	 * @return this
	 */
	public DublinCoreData setCreator(List<String> creator) {
		this.creator = creator;
		return this;
	}

	/**
	 * @param e dc:creator
	 * @return this
	 */
	public DublinCoreData addCreator(String e) {
		creator.add(e);
		return this;
	}

	/**
	 * @return dc:creator
	 */
	public String getFirstCreator() {
		List<String> elements = getCreator();
		if (elements == null || elements.size() == 0) {
			return "";
		}

		return elements.get(0);
	}

	/**
	 * @return dc:date
	 */
	public List<String> getDate() {
		return date;
	}

	/**
	 * @param date dc:date
	 * @return this
	 */
	public DublinCoreData setDate(List<String> date) {
		this.date = date;
		return this;
	}

	/**
	 * @param e dc:date
	 * @return this
	 */
	public DublinCoreData addDate(String e) {
		date.add(e);
		return this;
	}

	/**
	 * @return dc:date
	 */
	public String getFirstDate() {
		List<String> elements = getDate();
		if (elements == null || elements.size() == 0) {
			return "";
		}

		return elements.get(0);
	}

	/**
	 * @return dc:description
	 */
	public List<String> getDescription() {
		return description;
	}

	/**
	 * @param description dc:description
	 * @return this
	 */
	public DublinCoreData setDescription(List<String> description) {
		this.description = description;
		return this;
	}

	/**
	 * @param e dc:description
	 * @return this
	 */
	public DublinCoreData addDescription(String e) {
		description.add(e);
		return this;
	}

	/**
	 * @return dc:description
	 */
	public String getFirstDescription() {
		List<String> elements = getDescription();
		if (elements == null || elements.size() == 0) {
			return "";
		}

		return elements.get(0);
	}

	/**
	 * @return dc:format
	 */
	public List<String> getFormat() {
		return format;
	}

	/**
	 * @param format dc:format
	 * @return this
	 */
	public DublinCoreData setFormat(List<String> format) {
		this.format = format;
		return this;
	}

	/**
	 * @param e dc:format
	 * @return this
	 */
	public DublinCoreData addFormat(String e) {
		format.add(e);
		return this;
	}

	/**
	 * @return dc:format
	 */
	public String getFirstFormat() {
		List<String> elements = getFormat();
		if (elements == null || elements.size() == 0) {
			return "";
		}

		return elements.get(0);
	}

	/**
	 * @return dc:identifier
	 */
	public List<Pair<String, String>> getIdentifier() {
		return identifier;
	}

	/**
	 * @param list dc:identifier
	 * @return this
	 */
	public DublinCoreData setIdentifier(List<Pair<String, String>> list) {
		this.identifier = list;
		return this;
	}

	/**
	 * @param e dc:identifier
	 * @return this
	 */
	public DublinCoreData addIdentifier(String e) {
		if (e == null || e.isEmpty())
			return this;
		identifier.add(new Pair<String, String>(e, null));
		return this;
	}

	public DublinCoreData addIdentifier(List<String> list) {
		if (identifier == null) {
			identifier = new Vector<>();
		}
		for (String i : list) {
			addIdentifier(i);
		}
		return this;
	}

	/**
	 * @param value the actual identifier
	 * @param type type of the identifier preferred a qualified:name
	 * @return the modified Data
	 */
	public DublinCoreData addIdentifier(String value, String type) {
		if (value == null || value.isEmpty())
			return this;
		identifier.add(new Pair<String, String>(value, type));
		return this;
	}

	/**
	 * @return dc:identifier
	 */
	public Pair<String, String> getFirstIdentifier() {
		List<Pair<String, String>> elements = getIdentifier();
		if (elements == null || elements.size() == 0) {
			return new Pair<String, String>("", "");
		}

		return elements.get(0);
	}

	/**
	 * @return dc:language
	 */
	public List<String> getLanguage() {
		return language;
	}

	/**
	 * @param language dc:language
	 * @return this
	 */
	public DublinCoreData setLanguage(List<String> language) {
		this.language = language;
		return this;
	}

	/**
	 * @param e dc:language
	 * @return this
	 */
	public DublinCoreData addLanguage(String e) {
		language.add(e);
		return this;
	}

	/**
	 * @return dc:language
	 */
	public String getFirstLanguage() {
		List<String> elements = getLanguage();
		if (elements == null || elements.size() == 0) {
			return "";
		}

		return elements.get(0);
	}

	/**
	 * @return dc:publisher
	 */
	public List<String> getPublisher() {
		return publisher;
	}

	/**
	 * @param publisher dc:publisher
	 * @return this
	 */
	public DublinCoreData setPublisher(List<String> publisher) {
		this.publisher = publisher;
		return this;
	}

	/**
	 * @param e dc:publisher
	 * @return this
	 */
	public DublinCoreData addPublisher(String e) {
		publisher.add(e);
		return this;
	}

	/**
	 * @return dc:publisher
	 */
	public String getFirstPublisher() {
		List<String> elements = getPublisher();
		if (elements == null || elements.size() == 0) {
			return "";
		}

		return elements.get(0);
	}

	/**
	 * @return dc:relation
	 */
	public List<String> getRelation() {
		return relation;
	}

	/**
	 * @param relation dc:relation
	 * @return this
	 */
	public DublinCoreData setRelation(List<String> relation) {
		this.relation = relation;
		return this;
	}

	/**
	 * @param e dc:relation
	 * @return this
	 */
	public DublinCoreData addRelation(String e) {
		relation.add(e);
		return this;
	}

	/**
	 * @return dc:relation
	 */
	public String getFirstRelation() {
		List<String> elements = getRelation();
		if (elements == null || elements.size() == 0) {
			return "";
		}

		return elements.get(0);
	}

	/**
	 * @return dc:rights
	 */
	public List<String> getRights() {
		return rights;
	}

	/**
	 * @param rights dc:rights
	 * @return this
	 */
	public DublinCoreData setRights(List<String> rights) {
		this.rights = rights;
		return this;
	}

	/**
	 * @param e dc:rights
	 * @return this
	 */
	public DublinCoreData addRights(String e) {
		rights.add(e);
		return this;
	}

	/**
	 * @return dc:rights
	 */
	public String getFirstRights() {
		List<String> elements = getRights();
		if (elements == null || elements.size() == 0) {
			return "";
		}

		return elements.get(0);
	}

	/**
	 * @return dc:source
	 */
	public List<String> getSource() {
		return source;
	}

	/**
	 * @param source dc:source
	 * @return this
	 */
	public DublinCoreData setSource(List<String> source) {
		this.source = source;
		return this;
	}

	/**
	 * @param e dc:source
	 * @return this
	 */
	public DublinCoreData addSource(String e) {
		source.add(e);
		return this;
	}

	/**
	 * @return dc:source
	 */
	public String getFirstSource() {
		List<String> elements = getSource();
		if (elements == null || elements.size() == 0) {
			return "";
		}

		return elements.get(0);
	}

	/**
	 * @return dc:subject
	 */
	public List<String> getSubject() {
		return removeDuplicateEntries(subject);
	}

	private List<String> removeDuplicateEntries(List<String> list) {
		return list.stream().distinct().collect(Collectors.toList());
	}

	/**
	 * @param subject dc:subject
	 * @return this
	 */
	public DublinCoreData setSubject(List<String> subject) {
		this.subject = subject;
		return this;
	}

	/**
	 * @param e dc:subject
	 * @return this
	 */
	public DublinCoreData addSubject(String e) {
		subject.add(e);
		return this;
	}

	public DublinCoreData addSubjects(List<String> list) {
		subject.addAll(list);
		return this;
	}

	/**
	 * @return dc:subject
	 */
	public String getFirstSubject() {
		List<String> elements = getSubject();
		if (elements == null || elements.size() == 0) {
			return "";
		}

		return elements.get(0);
	}

	/**
	 * @return dc:title
	 */
	public List<String> getTitle() {
		return title;
	}

	/**
	 * @param title dc:title
	 * @return this
	 */
	public DublinCoreData setTitle(List<String> title) {
		this.title = title;
		return this;
	}

	/**
	 * @param e dc:title
	 * @return this
	 */
	public DublinCoreData addTitle(String e) {
		title.add(e);
		return this;
	}

	/**
	 * @return dc:title
	 */
	public String getFirstTitle() {
		List<String> elements = getTitle();
		if (elements == null || elements.size() == 0) {
			return "";
		}

		return elements.get(0);
	}

	/**
	 * @return dc:type
	 */
	public List<String> getType() {
		return type;
	}

	/**
	 * @param type dc:type
	 * @return this
	 */
	public DublinCoreData setType(List<String> type) {
		this.type = type;
		return this;
	}

	/**
	 * @param e dc:type
	 * @return this
	 */
	public DublinCoreData addType(String e) {
		type.add(e);
		return this;
	}

	/**
	 * @return dc:type
	 */
	public String getFirstType() {
		List<String> elements = getType();
		if (elements == null || elements.size() == 0) {
			return "";
		}
		return elements.get(0);
	}

	/**
	 * 
	 */
	public void trim() {
		while (contributor.remove(""))
			;
		while (coverage.remove(""))
			;
		while (creator.remove(""))
			;
		while (date.remove(""))
			;
		while (description.remove(""))
			;
		while (format.remove(""))
			;
		while (identifier.remove(""))
			;
		while (language.remove(""))
			;
		while (publisher.remove(""))
			;
		while (relation.remove(""))
			;
		while (rights.remove(""))
			;
		while (source.remove(""))
			;
		while (subject.remove(""))
			;
		while (title.remove(""))
			;
		while (type.remove(""))
			;
	}

	@Override
	public String toString() {
		ObjectMapper mapper = JsonUtil.mapper();
		StringWriter w = new StringWriter();
		try {
			mapper.writeValue(w, this);
		} catch (Exception e) {
			return super.toString();
		}
		return w.toString();
	}

}
