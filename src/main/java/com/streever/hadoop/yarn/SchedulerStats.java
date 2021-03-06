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

import com.streever.hadoop.AbstractStats;
import com.streever.hadoop.hdfs.shell.command.Direction;
import com.streever.hadoop.yarn.parsers.QueueParser;
import com.streever.hadoop.shell.Environment;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by streever on 2016-04-25.
 * <p>
 * Using the Resource Manager JMX, collect the queue stats .
 *
 */
public class SchedulerStats extends AbstractStats {

    private String timestamp = null;

    public SchedulerStats(String name) {
        super(name);
    }

    public SchedulerStats(String name, Environment env, Direction directionContext) {
        super(name, env, directionContext);
    }

    public SchedulerStats(String name, Environment env, Direction directionContext, int directives) {
        super(name, env, directionContext, directives);
    }

    public SchedulerStats(String name, Environment env, Direction directionContext, int directives, boolean directivesBefore, boolean directivesOptional) {
        super(name, env, directionContext, directives, directivesBefore, directivesOptional);
    }

    public SchedulerStats(String name, Environment env) {
        super(name, env);
    }

    @Override
    public void process(CommandLine cmdln) {

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        this.timestamp = df.format(new Date());

        String hostAndPort = configuration.get("yarn.resourcemanager.webapp.address");

        System.out.println("Resource Manager Server URL: " + hostAndPort);

        String rootPath = hostAndPort + "/ws/v1/cluster/scheduler";

        try {

            URL schUrl = new URL("http://" + rootPath);

            URLConnection schConnection = schUrl.openConnection();
            String schJson = IOUtils.toString(schConnection.getInputStream());

            QueueParser queueParser = new QueueParser(schJson);
            Map<String, List<Map<String,Object>>> queueSet = queueParser.getQueues(timestamp);

            Iterator<Map.Entry<String,List<Map<String,Object>>>> qIter = queueSet.entrySet().iterator();

            while (qIter.hasNext()) {
                Map.Entry<String, List<Map<String,Object>>> entry = qIter.next();
                addRecords(entry.getKey(), entry.getValue());
            }

                /*
                1. Get current queue state.

                 */


            Iterator<Map.Entry<String, List<Map<String, Object>>>> rIter = getRecords().entrySet().iterator();
            while (rIter.hasNext()) {
                Map.Entry<String, List<Map<String, Object>>> recordSet = rIter.next();
                print(recordSet.getKey(), recordSet.getValue());
            }

        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    protected void getHelp() {
        StringBuilder sb = new StringBuilder();
        sb.append("Collect Queue Stats from the JMX url.").append("\n");


        System.out.println(sb.toString());
    }


}
