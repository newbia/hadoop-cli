/*
 *  Hadoop CLI
 *
 *  (c) 2016-2019 David W. Streever. All rights reserved.
 *
 * This code is provided to you pursuant to your written agreement with David W. Streever, which may be the terms of the
 * Affero General Public License version 3 (AGPLv3), or pursuant to a written agreement with a third party authorized
 * to distribute this code.  If you do not have a written agreement with David W. Streever or with an authorized and
 * properly licensed third party, you do not have any rights to this code.
 *
 * If this code is provided to you under the terms of the AGPLv3:
 * (A) David W. Streever PROVIDES THIS CODE TO YOU WITHOUT WARRANTIES OF ANY KIND;
 * (B) David W. Streever DISCLAIMS ANY AND ALL EXPRESS AND IMPLIED WARRANTIES WITH RESPECT TO THIS CODE, INCLUDING BUT NOT
 *   LIMITED TO IMPLIED WARRANTIES OF TITLE, NON-INFRINGEMENT, MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE;
 * (C) David W. Streever IS NOT LIABLE TO YOU, AND WILL NOT DEFEND, INDEMNIFY, OR HOLD YOU HARMLESS FOR ANY CLAIMS ARISING
 *    FROM OR RELATED TO THE CODE; AND
 *  (D) WITH RESPECT TO YOUR EXERCISE OF ANY RIGHTS GRANTED TO YOU FOR THE CODE, David W. Streever IS NOT LIABLE FOR ANY
 *    DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, PUNITIVE OR CONSEQUENTIAL DAMAGES INCLUDING, BUT NOT LIMITED TO,
 *   DAMAGES RELATED TO LOST REVENUE, LOST PROFITS, LOSS OF INCOME, LOSS OF BUSINESS ADVANTAGE OR UNAVAILABILITY,
 *     OR LOSS OR CORRUPTION OF DATA.
 *
 */

package com.streever.hadoop.yarn;

import com.streever.hadoop.util.RecordConverter;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by streever on 2016-04-25.
 */
public class YarnAppRecordConverter {

    private ObjectMapper mapper = null;
    private RecordConverter recordConverter = null;
    private String timestamp = null;

    public YarnAppRecordConverter() {
        mapper = new ObjectMapper();
        recordConverter = new RecordConverter();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        this.timestamp = df.format(new Date());
    }

    /*
{
  "apps":
  {
    "app":
    [
       {
          "finishedTime" : 1326815598530,
          "amContainerLogs" : "http://host.domain.com:8042/node/containerlogs/container_1326815542473_0001_01_000001",
          "trackingUI" : "History",
          "state" : "FINISHED",
          "user" : "user1",
          "id" : "application_1326815542473_0001",
          "clusterId" : 1326815542473,
          "finalStatus" : "SUCCEEDED",
          "amHostHttpAddress" : "host.domain.com:8042",
          "progress" : 100,
          "name" : "word count",
          "startedTime" : 1326815573334,
          "elapsedTime" : 25196,
          "diagnostics" : "",
          "trackingUrl" : "http://host.domain.com:8088/proxy/application_1326815542473_0001/jobhistory/job/job_1326815542473_1_1",
          "queue" : "default",
          "allocatedMB" : 0,
          "allocatedVCores" : 0,
          "runningContainers" : 0,
          "memorySeconds" : 151730,
          "vcoreSeconds" : 103
       },
       {
          "finishedTime" : 1326815789546,
          "amContainerLogs" : "http://host.domain.com:8042/node/containerlogs/container_1326815542473_0002_01_000001",
          "trackingUI" : "History",
          "state" : "FINISHED",
          "user" : "user1",
          "id" : "application_1326815542473_0002",
          "clusterId" : 1326815542473,
          "finalStatus" : "SUCCEEDED",
          "amHostHttpAddress" : "host.domain.com:8042",
          "progress" : 100,
          "name" : "Sleep job",
          "startedTime" : 1326815641380,
          "elapsedTime" : 148166,
          "diagnostics" : "",
          "trackingUrl" : "http://host.domain.com:8088/proxy/application_1326815542473_0002/jobhistory/job/job_1326815542473_2_2",
          "queue" : "default",
          "allocatedMB" : 0,
          "allocatedVCores" : 0,
          "runningContainers" : 1,
          "memorySeconds" : 640064,
          "vcoreSeconds" : 442
       }
    ]
  }
}
     */
    public List<String> appIdList(String appsJson) throws IOException {
        List<String> rtn = new ArrayList<String>();
        //System.out.println(jobsJson);
        JsonNode apps = mapper.readValue(appsJson, JsonNode.class);

        if (apps != null) {
            JsonNode appsNode = apps.get("apps");
            if (appsNode != null) {

                JsonNode appsArrayNode = appsNode.get("app");

                if (appsArrayNode != null) {

                    if (appsArrayNode.isArray()) {
                        Iterator<JsonNode> appIter = appsArrayNode.getElements();
                        while (appIter.hasNext()) {
                            JsonNode appNode = appIter.next();
                            rtn.add(appNode.get("id").asText());
                        }
                    } else {
                        System.out.println("Nope");
                    }
                }
            }
        }
        return rtn;
    }

    /*
{
"apps":
{
"app":
[
   {
      "finishedTime" : 1326815598530,
      "amContainerLogs" : "http://host.domain.com:8042/node/containerlogs/container_1326815542473_0001_01_000001",
      "trackingUI" : "History",
      "state" : "FINISHED",
      "user" : "user1",
      "id" : "application_1326815542473_0001",
      "clusterId" : 1326815542473,
      "finalStatus" : "SUCCEEDED",
      "amHostHttpAddress" : "host.domain.com:8042",
      "progress" : 100,
      "name" : "word count",
      "startedTime" : 1326815573334,
      "elapsedTime" : 25196,
      "diagnostics" : "",
      "trackingUrl" : "http://host.domain.com:8088/proxy/application_1326815542473_0001/jobhistory/job/job_1326815542473_1_1",
      "queue" : "default",
      "allocatedMB" : 0,
      "allocatedVCores" : 0,
      "runningContainers" : 0,
      "memorySeconds" : 151730,
      "vcoreSeconds" : 103
   },
   {
      "finishedTime" : 1326815789546,
      "amContainerLogs" : "http://host.domain.com:8042/node/containerlogs/container_1326815542473_0002_01_000001",
      "trackingUI" : "History",
      "state" : "FINISHED",
      "user" : "user1",
      "id" : "application_1326815542473_0002",
      "clusterId" : 1326815542473,
      "finalStatus" : "SUCCEEDED",
      "amHostHttpAddress" : "host.domain.com:8042",
      "progress" : 100,
      "name" : "Sleep job",
      "startedTime" : 1326815641380,
      "elapsedTime" : 148166,
      "diagnostics" : "",
      "trackingUrl" : "http://host.domain.com:8088/proxy/application_1326815542473_0002/jobhistory/job/job_1326815542473_2_2",
      "queue" : "default",
      "allocatedMB" : 0,
      "allocatedVCores" : 0,
      "runningContainers" : 1,
      "memorySeconds" : 640064,
      "vcoreSeconds" : 442
   }
]
}
}
 */
    public List<Map<String, Object>> apps(String appsJson) throws IOException {
        List<Map<String, Object>> rtn = new ArrayList<Map<String, Object>>();
        //System.out.println(jobsJson);
        JsonNode apps = mapper.readValue(appsJson, JsonNode.class);

        if (apps != null) {
            JsonNode appsNode = apps.get("apps");
            if (appsNode != null) {

                JsonNode appsArrayNode = appsNode.get("app");

                if (appsArrayNode != null) {

                    if (appsArrayNode.isArray()) {
                        Iterator<JsonNode> appIter = appsArrayNode.getElements();
                        while (appIter.hasNext()) {
                            JsonNode appNode = appIter.next();

                            Map<String, Object> appMap = new LinkedHashMap<String, Object>();
                            appMap.put("reporting_ts", timestamp);
                            appMap = recordConverter.convert(appMap, appNode, null, null);

                            rtn.add(appMap);
                        }
                    } else {
                        System.out.println("Nope");
                    }
                }
            }
        }
        return rtn;
    }

    /*
   {
   "appAttempts" : {
      "appAttempt" : [
         {
            "nodeId" : "host.domain.com:8041",
            "nodeHttpAddress" : "host.domain.com:8042",
            "startTime" : 1326381444693,
            "id" : 1,
            "logsLink" : "http://host.domain.com:8042/node/containerlogs/container_1326821518301_0005_01_000001/user1",
            "containerId" : "container_1326821518301_0005_01_000001"
         }
      ]
   }
}
     */
    public List<Map<String, Object>> appAttempts(String attemptsJson, String appId) throws IOException {
        List<Map<String, Object>> rtn = new ArrayList<Map<String, Object>>();

        //System.out.println(tasksJson);
        JsonNode jobs = mapper.readValue(attemptsJson, JsonNode.class);

        if (jobs != null) {
            JsonNode attemptsNode = jobs.get("appAttempts");
            if (attemptsNode != null) {
                JsonNode attemptsArrayNode = attemptsNode.get("appAttempt");

                if (attemptsArrayNode != null) {
                    if (attemptsArrayNode.isArray()) {
                        Iterator<JsonNode> attemptsIter = attemptsArrayNode.getElements();
                        while (attemptsIter.hasNext()) {
                            JsonNode attemptNode = attemptsIter.next();
                            Map<String, Object> attemptMap = new LinkedHashMap<String, Object>();
                            attemptMap.put("reporting_ts", timestamp);
                            attemptMap.put("appId", appId);
                            attemptMap = recordConverter.convert(attemptMap, attemptNode, null, null);

                            rtn.add(attemptMap);
                        }
                    } else {
                        System.out.println("Nope");
                    }
                }
            }
        }
        return rtn;
    }

}
