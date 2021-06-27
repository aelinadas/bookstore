/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package neu.edu.applicationstats;

import com.timgroup.statsd.NonBlockingStatsDClient;
import com.timgroup.statsd.StatsDClient;

/**
 *
 * @author aelinadas
 */
public class Statistic {
    private StatsDClient statsDClient;

    private Statistic() {
        statsDClient = new NonBlockingStatsDClient("webapp", "127.0.0.1", 8125);
    }

    public StatsDClient getInstance() {
        return statsDClient;
    }
}
