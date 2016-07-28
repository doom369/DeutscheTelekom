package cc.blynk.dt;

import cc.blynk.dt.model.Data;
import cc.blynk.dt.model.Measurement;
import cc.blynk.dt.model.Unit;
import cc.blynk.dt.util.JsonParser;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Base64;

/**
 * The Blynk Project.
 * Created by Dmitriy Dumanskiy.
 * Created on 28.07.16.
 */
public class MeasurementReader implements Runnable {

    private static final Logger log = LogManager.getLogger(MeasurementReader.class);

    private final String hostUrl;
    private final String base64Hash;
    private final CloseableHttpClient httpclient;

    public MeasurementReader(String hostUrl, String user, String pass) throws UnsupportedEncodingException {
        this.hostUrl = hostUrl;
        this.base64Hash = Base64.getEncoder().encodeToString((user + ":" + pass).getBytes("UTF-8"));
        this.httpclient = HttpClients.createDefault();
    }

    @Override
    public void run() {
        HttpGet getMeasurementRequest = new HttpGet(hostUrl + "/measurement/measurements");
        getMeasurementRequest.setHeader("Authorization", "Basic " + base64Hash);

        try (CloseableHttpResponse response = httpclient.execute(getMeasurementRequest)) {
            if (response.getStatusLine().getStatusCode() != 200) {
                log.error(consumeString(response));
            } else {
                Data data = parse(response);
                for (Measurement measurement : data.measurements) {
                    if (measurement.type.equals("Temperatur")) {
                        Unit unit = measurement.Temperatur.get(0);
                        if (unit != null) {
                            sendDataToBlynk(unit);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Error getting measurements.", e);
        }
    }

    private void sendDataToBlynk(Unit unit) throws IOException {
        //hardcoded auth token and pin

        //put into value display widget
        HttpPut put = new HttpPut("http://blynk-cloud.com/743cecc5eab44fa0a9ef9c208d2e95f6/pin/V0");
        put.setEntity(new StringEntity("[\"" + unit.value + " " + unit.unit + "\"]", ContentType.APPLICATION_JSON));
        try (CloseableHttpResponse response = httpclient.execute(put)) {

        }

        //put into terminal widget
        put = new HttpPut("http://blynk-cloud.com/743cecc5eab44fa0a9ef9c208d2e95f6/pin/V1");
        put.setEntity(new StringEntity("[\"" + unit.value + " " + unit.unit + "\\n" + "\"]", ContentType.APPLICATION_JSON));
        try (CloseableHttpResponse response = httpclient.execute(put)) {

        }

    }

    private Data parse(CloseableHttpResponse response) throws IOException {
        String responseString = consumeString(response);
        return JsonParser.mapper.readValue(responseString, Data.class);
    }

    private String consumeString(CloseableHttpResponse response) throws IOException {
        return EntityUtils.toString(response.getEntity());
    }

}
