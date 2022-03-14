package io.nwdaf.analytics.api;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.UUID;

import net.minidev.json.JSONArray;
import org.json.JSONObject;

import com.jayway.jsonpath.Configuration;
import com.jayway.jsonpath.JsonPath;

import io.nwdaf.analytics.model.Accuracy;
import io.nwdaf.analytics.model.AnalyticsMetadataIndication;
import io.nwdaf.analytics.model.AnySlice;
import io.nwdaf.analytics.model.AnyUe;
import io.nwdaf.analytics.model.Ecgi;
import io.nwdaf.analytics.model.EventFilter;
import io.nwdaf.analytics.model.EventReportingRequirement;
import io.nwdaf.analytics.model.GNbId;
import io.nwdaf.analytics.model.GlobalRanNodeId;
import io.nwdaf.analytics.model.Gpsi;
import io.nwdaf.analytics.model.GroupId;
import io.nwdaf.analytics.model.NFType;
import io.nwdaf.analytics.model.Ncgi;
import io.nwdaf.analytics.model.NetworkAreaInfo;
import io.nwdaf.analytics.model.NsiIdInfo;
import io.nwdaf.analytics.model.OutputStrategy;
import io.nwdaf.analytics.model.PlmnId;
import io.nwdaf.analytics.model.SamplingRatio;
import io.nwdaf.analytics.model.Snssai;
import io.nwdaf.analytics.model.Supi;
import io.nwdaf.analytics.model.Tai;
import io.nwdaf.analytics.model.TargetUeInformation;
import io.nwdaf.analytics.model.TimeWindow;
import io.nwdaf.analytics.model.UInteger;

public class InputDataHandler {

	public InputDataHandler() {
		
	}
	
	//Get the values of the anaReq field and use them to create the EventReportingRequirement object
	public static EventReportingRequirement eventReportingRequirementExtractor(String anaReq) {
		
		EventReportingRequirement givenEventReportingRequirements = new EventReportingRequirement();
		AnalyticsMetadataIndication givenAnaMetaInd = new AnalyticsMetadataIndication();
		TimeWindow anaMetaIndTimeWindow = new TimeWindow();
		
		JSONObject anaReqJSON = new JSONObject(anaReq);
		
		String json = anaReq;
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
		
		String accuracy = null;									String accuracyQuery = "$.accuracy";
		String startTs = null;  								String startTsQuery = "$.startTs";
		String endTs = null;    								String endTsQuery = "$.endTs";
		String offsetPeriod = null;   							String offsetPeriodQuery = "$.offsetPeriod";
		String sampRatio = null;								String sampRatioQuery = "$.sampRatio";
		String maxObjectNbr = null;								String maxObjectNbrQuery = "$.maxObjectNbr";
		String maxSupiNbr = null;  								String maxSupiNbrQuery = "$.maxSupiNbr";
		String timeAnaNeeded = null;                			String timeAnaNeededQuery = "$.timeAnaNeeded";
		ArrayList<String> anaMeta = new ArrayList<String>();	String anaMetaQuery = "$.anaMeta";
		Object anaMetaInd = null;								String anaMetaIndQuery = "$.anaMetaInd";
			String dataWindow = null;							
				String startTime = null;						String startTimeQuery = "$.anaMetaInd.dataWindow.startTime";
				String stopTime = null;							String stopTimeQuery = "$.anaMetaInd.dataWindow.stopTime";
				ArrayList<String> dataStatProps = new ArrayList<String>(); String dataStatPropsQuery ="$.anaMetaInd.dataStatProps";
			String strategy = null;								String strategyQuery = "$.anaMetaInd.strategy";
			ArrayList<String> aggrNwdafIds = new ArrayList<String>();   String aggrNwdafIdsQuery = "$.anaMetaInd.aggrNwdafIds";
		
		
		if(anaReqJSON.has("accuracy")) {
			accuracy = JsonPath.read(document, accuracyQuery);
			if (accuracy!=null && accuracy!="") {givenEventReportingRequirements.setAccuracy(new Accuracy(accuracy));}
		}
		if(anaReqJSON.has("startTs")) {
			startTs = JsonPath.read(document, startTsQuery);
			if (startTs!=null && startTs!="") {givenEventReportingRequirements.setStartTs(OffsetDateTime.parse(startTs));}
		}
		if(anaReqJSON.has("endTs")) {
			endTs = JsonPath.read(document, endTsQuery);
			if (endTs!=null && endTs!="") {givenEventReportingRequirements.setEndTs(OffsetDateTime.parse(endTs));}
		}
		if(anaReqJSON.has("offsetPeriod")) {
			offsetPeriod = JsonPath.read(document, offsetPeriodQuery);
			if (offsetPeriod!=null && offsetPeriod!=null) {givenEventReportingRequirements.setOffsetPeriod(Integer.parseInt(offsetPeriod));}
		}
		if(anaReqJSON.has("sampRatio")) {
			sampRatio = JsonPath.read(document, sampRatioQuery);
			if (sampRatio!=null && sampRatio!="") {givenEventReportingRequirements.setSampRatio(new SamplingRatio(sampRatio).getSamplingRatio());}
		}
		if(anaReqJSON.has("maxObjectNbr")) {
			maxObjectNbr = JsonPath.read(document, maxObjectNbrQuery);
			if (maxObjectNbr!=null &&  maxObjectNbr!="") {givenEventReportingRequirements.setMaxObjectNbr(new UInteger(maxObjectNbr).getValue());}
		}
		if(anaReqJSON.has("maxSupiNbr")) {
			maxSupiNbr = JsonPath.read(document,maxSupiNbrQuery);
			if (maxSupiNbr!=null &&  maxSupiNbr!="") {givenEventReportingRequirements.setMaxSupiNbr(new UInteger(maxSupiNbr).getValue());}
		}
		if(anaReqJSON.has("timeAnaNeeded")) {
			timeAnaNeeded = JsonPath.read(document, timeAnaNeededQuery);
			if (timeAnaNeeded != null && timeAnaNeeded !="") { givenEventReportingRequirements.setTimeAnaNeeded(OffsetDateTime.parse(timeAnaNeeded));}
		}
		if(anaReqJSON.has("anaMeta")) {
			anaMeta = JsonPath.read(document,anaMetaQuery);
			if (anaMeta!=null) {givenEventReportingRequirements.setAnaMetaFromStringList(anaMeta);}
		}
		if(anaReqJSON.has("anaMetaInd")) {
			anaMetaInd = JsonPath.read(document,anaMetaIndQuery);
			if(anaMetaInd!=null && !anaMetaInd.equals("")) {
				//--------------->Set DataWindow<-----------------
				if(keyExists(anaReqJSON, "dataWindow")){
					if(keyExists(anaReqJSON, "startTime")){
						startTime = JsonPath.read(document,startTimeQuery);
						if (startTime != null && startTime !="") { anaMetaIndTimeWindow.setStartTime(OffsetDateTime.parse(startTime));}
					}
					if(keyExists(anaReqJSON, "stopTime")){
						stopTime = JsonPath.read(document,stopTimeQuery);
						if (stopTime != null && stopTime !="") { anaMetaIndTimeWindow.setStopTime(OffsetDateTime.parse(stopTime));}
					}
					givenAnaMetaInd.setDataWindow(anaMetaIndTimeWindow);
				}
				//--------------->Set Rest Values<-----------------
				if(keyExists(anaReqJSON, "dataStatProps")) {
					dataStatProps = JsonPath.read(document,dataStatPropsQuery);
					if (dataStatProps!=null) {givenAnaMetaInd.setDataStatPropsFromStringList(dataStatProps);}
				}
				if(keyExists(anaReqJSON, "strategy")) {
					strategy = JsonPath.read(document,strategyQuery);
					if (strategy!=null && strategy!=null) {givenAnaMetaInd.setStrategy(new OutputStrategy(strategy));}
				}
				if(keyExists(anaReqJSON, "aggrNwdafIds")) {
					aggrNwdafIds = JsonPath.read(document,aggrNwdafIdsQuery);
					if (aggrNwdafIds!=null) {givenAnaMetaInd.setAggrNwdafIdsFromString(aggrNwdafIds);}
				}				
				givenEventReportingRequirements.setAnaMetaInd(givenAnaMetaInd);
			}
			
		}

		return givenEventReportingRequirements;
	}
	

	//Get the values of the event-filter field and use them to create the EventReportingRequirement object
	public static EventFilter eventFilterextractor(String eventFilter) {
		EventFilter givenEventFilter = new EventFilter();
		ArrayList<NsiIdInfo> givenNsiIdInfos = new ArrayList<NsiIdInfo>();
		
		JSONObject eventFiltJSON = new JSONObject(eventFilter);
		
		String json = eventFilter;
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
		
		String anySlice = null;		  String anySliceQuery = "$.anySlice";
		JSONArray nsiIdInfos = new JSONArray();	  String nsiIdInfosQuery = "$.nsiIdInfos";
		LinkedHashMap networkArea = new LinkedHashMap<>();	  String networkAreaQuery = "$.networkArea";
		JSONArray snssais = new JSONArray();	  String snssaisQuery = "$.snssais";
		ArrayList<String> nfInstanceIds = new ArrayList<String>(); String nfInstanceIdsQuery = "$.nfInstanceIds";
		ArrayList<String> nfSetIds = new ArrayList<String>(); String nfSetIdsQuery = "$.nfSetIds";
		ArrayList<String> nfTypes = new ArrayList<String>(); String nfTypesQuery = "$.nfTypes";
	
		if(eventFiltJSON.has("snssais")) {
			snssais = JsonPath.read(document,snssaisQuery);
			if(snssais!=null) {
				ArrayList<Snssai> snssaisSnssai = new ArrayList<Snssai>();
				for(int i=0; i<snssais.size(); i++) {
					Snssai curSnssai = new Snssai((String)((LinkedHashMap) snssais.get(i)).get("sst"), (String)((LinkedHashMap) snssais.get(i)).get("sd"));
					snssaisSnssai.add(curSnssai);
				}
				givenEventFilter.setSnssais(snssaisSnssai);
			}
		}
		
		if(eventFiltJSON.has("nfTypes")) {
			nfTypes = JsonPath.read(document, nfTypesQuery);
			if(nfTypes!=null) {
				ArrayList<NFType> nfTypesNFType = new ArrayList<NFType>();
				for(int i=0; i<nfTypes.size(); i++) {
					String curNFType = nfTypes.get(i);
					nfTypesNFType.add(new NFType(curNFType));
				}
				givenEventFilter.setNfTypes(nfTypesNFType);
			}
		}
				
		if(eventFiltJSON.has("nfSetIds")) {
			nfSetIds = JsonPath.read(document, nfSetIdsQuery);
			if(nfSetIds!=null) {
				givenEventFilter.setNfSetIds(nfSetIds);
			}
		}
		
		
		if(eventFiltJSON.has("networkArea")) {
			networkArea = JsonPath.read(document, networkAreaQuery);
			if (networkArea!=null) {
				NetworkAreaInfo givenNetworkAreaInfo = new NetworkAreaInfo();
				
				ArrayList<Ecgi> ecgis = new ArrayList<Ecgi>();
				JSONArray givenEcgis = new JSONArray();
				if(networkArea.containsKey("ecgis")) {
					givenEcgis = (JSONArray) networkArea.get("ecgis");
				}
				if(givenEcgis!=null) {
					for(int i=0; i<givenEcgis.size(); i++) {
						LinkedHashMap curEcgi = (LinkedHashMap) givenEcgis.get(i);
						Ecgi curEcgiEcgi = new Ecgi();
						LinkedHashMap curPlmnId = new LinkedHashMap<>();
						if(curEcgi.containsKey("plmnId")) {
							curPlmnId = (LinkedHashMap) curEcgi.get("plmnId");
							PlmnId curPlmnIdPlmnId = new PlmnId();
							if(curPlmnId.containsKey("mcc")) { curPlmnIdPlmnId.setMcc((String) curPlmnId.get("mcc")); }
							if(curPlmnId.containsKey("mnc")) { curPlmnIdPlmnId.setMnc((String) curPlmnId.get("mnc")); }							
							curEcgiEcgi.setPlmnId(curPlmnIdPlmnId);
						}
						
						if(curEcgi.containsKey("eutraCellId")) {
							curEcgiEcgi.setEutraCellId((String) curEcgi.get("eutraCellId"));
						}
						
						if(curEcgi.containsKey("nid")) {
							curEcgiEcgi.setNid((String) curEcgi.get("nid"));          
						}						
						ecgis.add(curEcgiEcgi);
					}
					givenNetworkAreaInfo.setEcgis(ecgis);
				}
				
				ArrayList<Ncgi> ncgis = new ArrayList<Ncgi>();
				JSONArray givenNcgis = new JSONArray();
				if(networkArea.containsKey("ncgis")) {
					givenNcgis = (JSONArray) networkArea.get("ncgis");
				}
				if(givenNcgis!=null) {
					for(int i=0; i<givenNcgis.size(); i++) {
						LinkedHashMap curNcgi = (LinkedHashMap) givenNcgis.get(i);
						Ncgi curNcgiNcgi = new Ncgi();
						LinkedHashMap curPlmnId = new LinkedHashMap<>();
						if(curNcgi.containsKey("plmnId")) {
							curPlmnId = (LinkedHashMap) curNcgi.get("plmnId");
							PlmnId curPlmnIdPlmnId = new PlmnId();
							if(curPlmnId.containsKey("mcc")) { curPlmnIdPlmnId.setMcc((String) curPlmnId.get("mcc")); }
							if(curPlmnId.containsKey("mnc")) { curPlmnIdPlmnId.setMnc((String) curPlmnId.get("mnc")); }							
							curNcgiNcgi.setPlmnId(curPlmnIdPlmnId);
						}
						
						if(curNcgi.containsKey("nrCellId")) {
							curNcgiNcgi.setNrCellId((String) curNcgi.get("nrCellId"));
						}
						
						if(curNcgi.containsKey("nid")) {
							curNcgiNcgi.setNid((String) curNcgi.get("nid"));          
						}						
						ncgis.add(curNcgiNcgi);
					}
					givenNetworkAreaInfo.setNcgis(ncgis);
				}
								
				ArrayList<GlobalRanNodeId> gRanNodeIds = new ArrayList<GlobalRanNodeId>();
				JSONArray givenGRanNodeIds = new JSONArray();
				if(networkArea.containsKey("gRanNodeIds")) {
					givenGRanNodeIds = (JSONArray) networkArea.get("gRanNodeIds");
				}
				if(givenGRanNodeIds!=null) {
					for(int i=0; i<givenGRanNodeIds.size(); i++) {
						LinkedHashMap curGRanNodeId = (LinkedHashMap) givenGRanNodeIds.get(i);
						GlobalRanNodeId curGRanNIdGlobalRanNodeId = new GlobalRanNodeId();
						
						LinkedHashMap curPlmnId = new LinkedHashMap<>();
						if(curGRanNodeId.containsKey("plmnId")) {
							curPlmnId = (LinkedHashMap) curGRanNodeId.get("plmnId");
							PlmnId curPlmnIdPlmnId = new PlmnId();
							if(curPlmnId.containsKey("mcc")) { curPlmnIdPlmnId.setMcc((String) curPlmnId.get("mcc")); }
							if(curPlmnId.containsKey("mnc")) { curPlmnIdPlmnId.setMnc((String) curPlmnId.get("mnc")); }							
							curGRanNIdGlobalRanNodeId.setPlmnId(curPlmnIdPlmnId);
						}
						
						if(curGRanNodeId.containsKey("nid")) {
							String nid = (String) curGRanNodeId.get("nid");
							curGRanNIdGlobalRanNodeId.setNid(nid);          
						}	
						
						if(curGRanNodeId.containsKey("n3IwfId")) {
							curGRanNIdGlobalRanNodeId.setN3IwfId((String) curGRanNodeId.get("n3IwfId"));
						}

						if(curGRanNodeId.containsKey("ngeNbId")) {
							curGRanNIdGlobalRanNodeId.setNgeNbId((String) curGRanNodeId.get("ngeNbId"));
						}
						if(curGRanNodeId.containsKey("wagfId")) {
							curGRanNIdGlobalRanNodeId.setWagfId((String) curGRanNodeId.get("wagfId"));
						}
						if(curGRanNodeId.containsKey("tngfId")) {
							curGRanNIdGlobalRanNodeId.setTngfId((String) curGRanNodeId.get("tngfId"));
						}
						if(curGRanNodeId.containsKey("eNbId")) {
							curGRanNIdGlobalRanNodeId.setENbId((String) curGRanNodeId.get("eNbId"));
						}

						if(curGRanNodeId.containsKey("gNbId")) {
							LinkedHashMap curGNbId = (LinkedHashMap) curGRanNodeId.get("gNbId");
							
							GNbId curGNbIdGNbId = new GNbId();
							if(curGNbId.containsKey("bitLength")) {
								Integer bitLength = Integer.parseInt((String) curGNbId.get("bitLength"));
								curGNbIdGNbId.setBitLength(bitLength);
							}
							if(curGNbId.containsKey("gNBValue")) {
								curGNbIdGNbId.setGNBValue((String) curGNbId.get("gNBValue"));
							}
							
							curGRanNIdGlobalRanNodeId.setGNbId(curGNbIdGNbId);
						}
						
						gRanNodeIds.add(curGRanNIdGlobalRanNodeId);
					}
					givenNetworkAreaInfo.setGRanNodeIds(gRanNodeIds);
				}
				
				ArrayList<Tai> tais = new ArrayList<Tai>();
				JSONArray givenTais = new JSONArray();
				if(networkArea.containsKey("tais")) {
					givenTais = (JSONArray) networkArea.get("tais");
				}
				if(givenTais!=null) {
					for(int i=0; i<givenTais.size(); i++) {
						LinkedHashMap curTai = (LinkedHashMap) givenTais.get(i);
						Tai curTaiTai = new Tai();
						
						LinkedHashMap curPlmnId = new LinkedHashMap<>();
						if(curTai.containsKey("plmnId")) {
							curPlmnId = (LinkedHashMap) curTai.get("plmnId");
							PlmnId curPlmnIdPlmnId = new PlmnId();
							if(curPlmnId.containsKey("mcc")) { curPlmnIdPlmnId.setMcc((String) curPlmnId.get("mcc")); }
							if(curPlmnId.containsKey("mnc")) { curPlmnIdPlmnId.setMnc((String) curPlmnId.get("mnc")); }							
							curTaiTai.setPlmnId(curPlmnIdPlmnId);
						}
						
						
						if(curTai.containsKey("tac")) {
							curTaiTai.setTac((String) curTai.get("tac"));
						}
						
						
						if(curTai.containsKey("nid")) {
							curTaiTai.setNid((String) curTai.get("nid"));          
						}		
						
						tais.add(curTaiTai);
					}
					givenNetworkAreaInfo.setTais(tais);
				}
				givenEventFilter.setNetworkArea(givenNetworkAreaInfo);
			}
		}
			
		if(eventFiltJSON.has("nfInstanceIds")) {
			nfInstanceIds = JsonPath.read(document , nfInstanceIdsQuery);
			ArrayList<UUID> nfInstanceIdsUUID = new ArrayList<UUID>();
			if(nfInstanceIds!=null) { 
				for(int i=0; i<nfInstanceIds.size(); i++) {
					String curUUID = nfInstanceIds.get(i);
					nfInstanceIdsUUID.add(UUID.fromString(curUUID));
				}
				givenEventFilter.setNfInstanceIds(nfInstanceIdsUUID);
			};
		}
		
		if(eventFiltJSON.has("anySlice")) {
			anySlice = JsonPath.read(document, anySliceQuery);
			if (anySlice!=null && anySlice!="") {givenEventFilter.setAnySlice(new AnySlice(anySlice).getAnySlice());}
		}
		if(eventFiltJSON.has("nsiIdInfos")) {
			nsiIdInfos = JsonPath.read(document, nsiIdInfosQuery);
			if(nsiIdInfos!=null) {
				for(int i=0; i<nsiIdInfos.size(); i++) {
					
					NsiIdInfo curNsiIdInfo = new NsiIdInfo();
					LinkedHashMap curNsiIdInfoMap = new LinkedHashMap();
					LinkedHashMap curSnssai = new LinkedHashMap();
					JSONArray curNsiIds = new JSONArray();
						
					curNsiIdInfoMap = (LinkedHashMap) nsiIdInfos.get(i);		
					curSnssai.put("sst", ((LinkedHashMap) curNsiIdInfoMap.get("snssai")).get("sst"));
					Integer curSst = Integer.parseInt((String) curSnssai.get("sst"));
					
					if(((LinkedHashMap) curNsiIdInfoMap.get("snssai")).containsKey("sd")) {
						curSnssai.put("sd", ((LinkedHashMap) curNsiIdInfoMap.get("snssai")).get("sd"));
						String curSd = (String) curSnssai.get("sd");
						curNsiIdInfo.setSnssai(new Snssai(curSst, curSd));
					}
					else {
						curNsiIdInfo.setSnssai(new Snssai(curSst));
					}
						
					if(curNsiIdInfoMap.size()>1) {
						curNsiIds = (JSONArray) curNsiIdInfoMap.get("nsiIds");
						List<String> curNsiIdsList = new ArrayList<String>();
						for(int k = 0; k < curNsiIds.size(); k++){
							curNsiIdsList.add((String) curNsiIds.get(k));
						}
						curNsiIdInfo.setNsiIds(curNsiIdsList);
					}
					givenNsiIdInfos.add(curNsiIdInfo);
				}
				givenEventFilter.setNsiIdInfos(givenNsiIdInfos);}
		}

		return givenEventFilter;
	}
	
	
	public static TargetUeInformation tgtUeExtractor(String tgtUe) {
		TargetUeInformation givenTargetUeInformation = new TargetUeInformation();
		
		JSONObject tgtUeJSON = new JSONObject(tgtUe);
		String json = tgtUe;
		Object document = Configuration.defaultConfiguration().jsonProvider().parse(json);
		
		String anyUe = null;		  String anyUeQuery = "$.anyUe";
		JSONArray supis = new JSONArray(); String supisQuery = "$.supis";
		JSONArray gpsis = new JSONArray(); String gpsisQuery = "$.gpsis";
		ArrayList<String> intGroupIds = new ArrayList<String>(); String intGroupIdsQuery = "$.intGroupIds";		
		
		if(tgtUeJSON.has("anyUe")) {
			anyUe = JsonPath.read(document, anyUeQuery);
			if (anyUe!=null && anyUe!="") {givenTargetUeInformation.setAnyUe(new AnyUe(anyUe).getAnyUe());}
		}
		
		if(tgtUeJSON.has("supis")) {
			supis = JsonPath.read(document, supisQuery);
			if(supis!=null) {
				List<String> supisList = new ArrayList<String>();
				for(int i=0; i<supis.size(); i++) {
					String curSupi = (String) supis.get(i);
					supisList.add(new Supi(curSupi).supiValue());
				}
				givenTargetUeInformation.setSupis(supisList);
			}
		}
		
		if(tgtUeJSON.has("gpsis")) {
			gpsis = JsonPath.read(document, gpsisQuery);
			if(gpsis!=null) {
				List<String> gpsisList = new ArrayList<String>();
				for(int i=0; i<gpsis.size(); i++) {
					String curGpsi = (String) gpsis.get(i);
					gpsisList.add(new Gpsi(curGpsi).gpsiValue());
				}
				givenTargetUeInformation.setGpsis(gpsisList);
			}
		}	
		
		if(tgtUeJSON.has("intGroupIds")) {
			intGroupIds = JsonPath.read(document, intGroupIdsQuery);
			if(intGroupIds!=null) {
				List<String> intGroupIdsList = new ArrayList<String>();
				for(int i=0; i<intGroupIds.size(); i++) {
					String curIntGroupIds = (String) intGroupIds.get(i);
					intGroupIdsList.add(new GroupId(curIntGroupIds).groupIdValue());
				}
				givenTargetUeInformation.setIntGroupIds(intGroupIdsList);
			}
		}			
		
		return givenTargetUeInformation;
		

	}
	
	
	
	//Check to see for nested key values
	public static boolean keyExists(JSONObject  object, String searchedKey) {
	    boolean exists = object.has(searchedKey);
	    if(!exists) {      
	        Iterator<?> keys = object.keys();
	        while( keys.hasNext() ) {
	            String key = (String)keys.next();
	            if ( object.get(key) instanceof JSONObject ) {
	                    exists = keyExists((JSONObject) object.get(key), searchedKey);
	            }
	        }
	    }
	    return exists;
	}
	
}
